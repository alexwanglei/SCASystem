package cn.edu.buaa.act.SCAS.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.buaa.act.SCAS.po.Application;
import cn.edu.buaa.act.SCAS.po.DirectAccessPort;
import cn.edu.buaa.act.SCAS.po.Formula;
import cn.edu.buaa.act.SCAS.po.Task;
import cn.edu.buaa.act.SCAS.po.TaskCommunication;
import cn.edu.buaa.act.SCAS.po.Variable;

/** 
 * @Description 
 * @author wanglei
 * @date 2014年7月12日 
 */ 
  	
@Service
public class ModelGenerationService {
	
	private static Logger logger = LoggerFactory.getLogger(ModelGenerationService.class);
	
	private String rootPath = this.getClass().getResource("/File").getPath();
	
	public boolean gnerateSAModel(List<Application> applications, String ac, String ec, String filepath) throws JSONException, IOException{
		File modelFolder = new File(rootPath+"/"+filepath.substring(0,filepath.indexOf('/')) + "/SAModel");
		logger.info(modelFolder.getPath());
		if(!modelFolder.exists())
			modelFolder.mkdir();
		
		HashMap<String,String> variableTypeMap = new HashMap<String,String>();
		HashSet<String> ecVariableSet = new HashSet<String>();
		JSONArray acJsonArray = new JSONArray(ac);
		for(int i=0; i<acJsonArray.length(); i++){
			variableTypeMap.put(acJsonArray.getJSONObject(i).getString("variable"), acJsonArray.getJSONObject(i).getString("type"));
		}
		JSONArray ecJsonArray = new JSONArray(ec);
		for(int i=0; i<ecJsonArray.length(); i++){
			variableTypeMap.put(ecJsonArray.getJSONObject(i).getString("variable"), ecJsonArray.getJSONObject(i).getString("type"));
			ecVariableSet.add(ecJsonArray.getJSONObject(i).getString("variable"));
		}
		

		//直连端口数组
		ArrayList<DirectAccessPort> daPortList = new ArrayList<DirectAccessPort>();
		
		
		
		
		//生成分区模型
		for(Application app : applications){
			
			File partitionModelFile = new File(modelFolder.getPath()+"/"+app.getName()+".amp");
//			logger.info(partitionModelFile.getPath());
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("SAPartition");
			root.addAttribute("Id", Integer.toString(app.getId()));
			root.addAttribute("Name", app.getName());
			//生成任务
			Element saTaskEle = root.addElement("SATasks");
			saTaskEle.addAttribute("xmlns:xi", "http://www.w3.org/2003/XInclude");
			for(Task task : app.getTasks()){
				Element includeEle = saTaskEle.addElement("include");
				includeEle.addAttribute("href", task.getName()+".amt");
			}
			//生成分区内通信
			Element intraComEle = root.addElement("IntraCommunications");
			int mcId = 1;
			for(TaskCommunication tc : app.getTaskCommunications()){
				Element commEle = intraComEle.addElement("Communication");
				commEle.addAttribute("SrcTask", tc.getSrcTask().getName());
				commEle.addAttribute("DstTask", tc.getDstTask().getName());
				commEle.addAttribute("MessageContainerId", Integer.toString(mcId++));
				commEle.addText(tc.getVariable().getName());
			}
			//生成消息容器
			Element mcsEle = root.addElement("MessageContainers");

			mcId = 1;
			for(TaskCommunication tc : app.getTaskCommunications()){
				Element mcEle = mcsEle.addElement("MessageContainer");
				mcEle.addAttribute("Id", Integer.toString(mcId));
				if(tc.getType().equals("buffer")){
					Element bufferEle = mcEle.addElement("Buffer");
					String bufferName = "buffer_"+mcId++;
					bufferEle.addAttribute("Name", bufferName);
					bufferEle.addAttribute("MessageSize", "50");
					bufferEle.addAttribute("BufferLength", "10");
					bufferEle.addAttribute("Discipline", "FIFO");
					app.getIOMcNameMap().put(tc.getVariable(), bufferName);
				}
				else if(tc.getType().equals("blackboard")){
					Element blackboardEle = mcEle.addElement("Blackboard");
					String blackboardName = "blackboard_"+mcId++;
					blackboardEle.addAttribute("Name", blackboardName);
					blackboardEle.addAttribute("MessageSize", "100");
					app.getIOMcNameMap().put(tc.getVariable(), blackboardName);
				}
			}
			//生成信号量
			root.addElement("Semaphores");
			
			//生成事件
			root.addElement("Events");
			
			//生成应用端口
			ArrayList<DirectAccessPort> appDaPortList = new ArrayList<DirectAccessPort>();
			Element portsEle = root.addElement("ApplicationPorts");
			int portId = 1;
			int daPortId = 1;
			for(Variable var : app.getInputs()){
				String type = variableTypeMap.get(var.getName());
				if(type !=null){
					if(type.equals("sample")){
						Element samplePortEle = portsEle.addElement("SamplePort");
						samplePortEle.addAttribute("Id", Integer.toString(portId));
						String samplePortName = "samplePort_"+portId++ +"d";
						samplePortEle.addAttribute("Name", samplePortName);
						samplePortEle.addAttribute("Direction", "DESTINATION");
						samplePortEle.addAttribute("MessageSize", "8");
						samplePortEle.addAttribute("RefreshPeriod", "4.0");
						app.getPortNameMap().put(var, samplePortName);
						//创建对应的直连端口
						if(ecVariableSet.contains(var.getName())){
							appDaPortList.add(new DirectAccessPort(daPortId, "directAccessPort_"+daPortId++,"SOURCE","sample",app.getName(),samplePortName,var.getName()));
						}
					}
					else if(type.equals("queue")){
						Element queuePortEle = portsEle.addElement("QueuePort");
						queuePortEle.addAttribute("Id", Integer.toString(portId));
						String queuePortName = "queuePort_"+portId++ +"d";
						queuePortEle.addAttribute("Name", queuePortName);
						queuePortEle.addAttribute("Direction", "DESTINATION");
						queuePortEle.addAttribute("MessageSize", "120");
						queuePortEle.addAttribute("QueueLength", "5");
						queuePortEle.addAttribute("Protocol", "NOT_APPLICABLE");
						queuePortEle.addAttribute("Discipline", "FIFO");
						app.getPortNameMap().put(var, queuePortName);
						//创建对应的直连端口
						if(ecVariableSet.contains(var.getName())){
							appDaPortList.add(new DirectAccessPort(daPortId, "directAccessPort_"+daPortId++,"SOURCE","queue",app.getName(),queuePortName,var.getName()));
						}
					}
				}
				else{
					logger.info(var.toString() + "类型找不到");
				}
			}
			for(Variable var : app.getOutputs()){
				String type = variableTypeMap.get(var.getName());
				if(type !=null){
					if(type.equals("sample")){
						Element samplePortEle = portsEle.addElement("SamplePort");
						samplePortEle.addAttribute("Id", Integer.toString(portId));
						String samplePortName = "samplePort_"+portId++ +"s";
						samplePortEle.addAttribute("Name", samplePortName);
						samplePortEle.addAttribute("Direction", "SOURCE");
						samplePortEle.addAttribute("MessageSize", "8");
						samplePortEle.addAttribute("RefreshPeriod", "4.0");
						app.getPortNameMap().put(var, samplePortName);
						//创建对应的直连端口
						if(ecVariableSet.contains(var.getName())){
							appDaPortList.add(new DirectAccessPort(daPortId, "directAccessPort_"+daPortId++,"DESTINATION","sample",app.getName(),samplePortName,var.getName()));
						}
					}
					else if(type.equals("queue")){
						Element queuePortEle = portsEle.addElement("QueuePort");
						queuePortEle.addAttribute("Id", Integer.toString(portId));
						String queuePortName = "queuePort_"+portId++ + "s";
						queuePortEle.addAttribute("Name", queuePortName);
						queuePortEle.addAttribute("Direction", "SOURCE");
						queuePortEle.addAttribute("MessageSize", "120");
						queuePortEle.addAttribute("QueueLength", "100");
						queuePortEle.addAttribute("Protocol", "NOT_APPLICABLE");
						queuePortEle.addAttribute("Discipline", "FIFO");
						app.getPortNameMap().put(var, queuePortName);
						//创建对应的直连端口
						if(ecVariableSet.contains(var.getName())){
							appDaPortList.add(new DirectAccessPort(daPortId, "directAccessPort_"+daPortId++,"DESTINATION","queue",app.getName(),queuePortName,var.getName()));
						}
					}
				}
				else{
					logger.info(var.toString() + "类型找不到");
				}
			}
			
			//生成分区直连端口
			Element partPortsEle = root.addElement("PartitionPorts");
			for(DirectAccessPort daPort : appDaPortList){
				if(daPort.getType().equals("sample")){
					Element samplePortEle = partPortsEle.addElement("SamplePort");
					samplePortEle.addAttribute("Id", Integer.toString(daPort.getId()));
					samplePortEle.addAttribute("Name", daPort.getName());
					samplePortEle.addAttribute("Direction", daPort.getDirection());
					samplePortEle.addAttribute("MessageSize", "8");
					samplePortEle.addAttribute("RefreshPeriod", "4.0");
				}
				else if(daPort.getType().equals("queue")){
					Element queuePortEle = partPortsEle.addElement("QueuePort");
					queuePortEle.addAttribute("Id", Integer.toString(daPort.getId()));
					queuePortEle.addAttribute("Name", daPort.getName());
					queuePortEle.addAttribute("Direction", daPort.getDirection());
					queuePortEle.addAttribute("MessageSize", "120");
					queuePortEle.addAttribute("QueueLength", "5");
					queuePortEle.addAttribute("Protocol", "NOT_APPLICABLE");
					queuePortEle.addAttribute("Discipline", "FIFO");
				}
			}
			daPortList.addAll(appDaPortList);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(partitionModelFile));
			XMLWriter out = null;
			OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        out = new XMLWriter(bw, format);
	        out.write(doc);
	        bw.close();
		}
		
		//生成任务模型
		for(Application app : applications){
			for(Task task : app.getTasks()){
				File taskModelFile = new File(modelFolder.getPath()+"/"+task.getName()+".amt");
				logger.info(taskModelFile.getPath());
				Document doc = DocumentHelper.createDocument();
				Element saTaskEle = doc.addElement("SATask");
				saTaskEle.addAttribute("Id", Integer.toString(task.getId()));
				saTaskEle.addAttribute("Name", task.getName());
				Element stackEle = saTaskEle.addElement("Stack");
				stackEle.addText("400");
				Element priortyEle = saTaskEle.addElement("Priorty");
				priortyEle.addText("200");
				Element periodEle = saTaskEle.addElement("Period");
				periodEle.addText("1");
				Element timeCapacityEle = saTaskEle.addElement("TimeCapacity");
				timeCapacityEle.addText("1");
				Element deadlineEle = saTaskEle.addElement("Deadline");
				deadlineEle.addText("SOFT");
				Element taskInputsEle = saTaskEle.addElement("TaskInputs");
				int ioId = 1;
				for(Variable var : task.getInputs()){
					Element ioEle = taskInputsEle.addElement("IO");
					ioEle.addAttribute("Id", Integer.toString(ioId++));
					ioEle.addAttribute("ConceptName", var.getName());
					ioEle.addAttribute("DataType", var.getType());
					
					if(app.getIOMcNameMap().get(var)!=null){
						ioEle.addAttribute("Type", "IntraPartition");
						ioEle.addAttribute("Connect", app.getIOMcNameMap().get(var));
					}
					else if(app.getPortNameMap().get(var)!=null){
						ioEle.addAttribute("Type", "InterPartition");
						ioEle.addAttribute("Connect", app.getPortNameMap().get(var));
					}
				}
				Element taskOutputsEle = saTaskEle.addElement("TaskOutputs");
				for(Variable var : task.getOutputs()){
					if(app.getIOMcNameMap().get(var)!=null){
						Element ioEle = taskOutputsEle.addElement("IO");
						ioEle.addAttribute("Id", Integer.toString(ioId++));
						ioEle.addAttribute("ConceptName", var.getName());
						ioEle.addAttribute("DataType", var.getType());
						ioEle.addAttribute("Type", "IntraPartition");
						ioEle.addAttribute("Connect", app.getIOMcNameMap().get(var));
					}
					if(app.getPortNameMap().get(var)!=null){
						Element ioEle = taskOutputsEle.addElement("IO");
						ioEle.addAttribute("Id", Integer.toString(ioId++));
						ioEle.addAttribute("ConceptName", var.getName());
						ioEle.addAttribute("DataType", var.getType());
						ioEle.addAttribute("Type", "InterPartition");
						ioEle.addAttribute("Connect", app.getPortNameMap().get(var));
					}
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(taskModelFile));
				XMLWriter out = null;
				OutputFormat format = OutputFormat.createPrettyPrint();
		        format.setEncoding("UTF-8");
		        out = new XMLWriter(bw, format);
		        out.write(doc);
		        bw.close();
			}
		}
		
		//生成模块模型
		File moduleModelFile = new File(modelFolder.getPath()+"/module.amm");
		Document mdoc = DocumentHelper.createDocument();
		Element mroot = mdoc.addElement("SAModule");
		mroot.addAttribute("Id", "1");
		mroot.addAttribute("Name", "module");
		Element saPartitionsEle = mroot.addElement("SAPartitions");
		for(Application app : applications){
			Element includeEle = saPartitionsEle.addElement("include");
			includeEle.addAttribute("href", app.getName()+".amp");
		}
		//生成分区间通信
		Element interComsEle = mroot.addElement("InterCommunications");
		for(int i=0; i<acJsonArray.length(); i++){
			Element comEle = interComsEle.addElement("Communication");
			String srcApp = acJsonArray.getJSONObject(i).getString("srcApp");
			String dstApp = acJsonArray.getJSONObject(i).getString("dstApp");
			String variableName = acJsonArray.getJSONObject(i).getString("variable");
			Variable variable = new Variable();
			variable.setName(variableName);
			comEle.addAttribute("SrcPart", srcApp);
			comEle.addAttribute("DstPart", dstApp);
		
			for(Application app : applications){
				if(app.getName().equals(srcApp)){
					comEle.addAttribute("SrcPort", app.getPortNameMap().get(variable));
				}
				if(app.getName().equals(dstApp)){
					comEle.addAttribute("DstPort", app.getPortNameMap().get(variable));
				}
			}
			comEle.addAttribute("Mode", acJsonArray.getJSONObject(i).getString("type"));
			comEle.addText(variableName);
		}
		//生成分区直连端口和应用端口的连接
		Element daComsEle = mroot.addElement("DaCommunications");
		for(DirectAccessPort daPort :daPortList){
			Element comEle = daComsEle.addElement("Communication");
			comEle.addAttribute("SrcPart", daPort.getPartitionName());
			comEle.addAttribute("DstPart", daPort.getPartitionName());
			if(daPort.getDirection().equals("SOURCE")){
				comEle.addAttribute("SrcPort", daPort.getName());
				comEle.addAttribute("DstPort", daPort.getAppPortName());
			}
			else{
				comEle.addAttribute("SrcPort", daPort.getAppPortName());
				comEle.addAttribute("DstPort", daPort.getName());
			}
			comEle.addAttribute("Mode", daPort.getType());
			comEle.addText(daPort.getVariableName());
		}
		
		Element scheduleEle = mroot.addElement("Schedule");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(moduleModelFile));
		XMLWriter out = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        out = new XMLWriter(bw, format);
        out.write(mdoc);
        bw.close();
        
		return true;
	}
	
	
	public boolean generateTaskFile(List<Task> tasks, String filepath){
//		logger.info(filepath);
		File taskFile = new File(rootPath+"/"+filepath.substring(0,filepath.indexOf('.')) + ".task");
		logger.info(taskFile.getPath());
		
//		for(Task task : tasks){
//			System.out.println(task.getName()+"输入:");
//			for(Variable i:task.getInputs()){
//				System.out.println(i.getName());
//			}
//			System.out.println(task.getName()+"输出:");
//			for(Variable o:task.getOutputs()){
//				System.out.println(o.getName());
//			}
//		}
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Tasks");
		for(Task task : tasks){
			Element taskEle = root.addElement("Task");
			taskEle.addAttribute("Id", Integer.toString(task.getId()));
			taskEle.addAttribute("Name", task.getName());
			//formulas 元素
			Element formulasEle = taskEle.addElement("Formulas");
			for(Formula f : task.getFormulas()){
				Element formulaEle = formulasEle.addElement("Formula");
				formulaEle.addAttribute("Id", Integer.toString(f.getId()));
				formulaEle.addAttribute("Name", f.getName());
				for(Variable v : f.getVars()){
					Element varEle = formulaEle.addElement("Variable");
					varEle.addAttribute("Name", v.getName());
					varEle.addAttribute("Type", v.getType());
					if(v.getUnit()!=null){
						varEle.addAttribute("Unit", v.getUnit());
					}
				}
				Element resultEle = formulaEle.addElement("Result");
				resultEle.addAttribute("Name", f.getResult().getName());
				resultEle.addAttribute("Type", f.getResult().getType());
				if(f.getResult().getUnit()!=null){
					resultEle.addAttribute("Unit", f.getResult().getUnit());
				}
			}
			
			//Inputs元素
			Element inputEle = taskEle.addElement("Inputs");
			for(Variable i : task.getInputs()){
				Element varEle = inputEle.addElement("Variable");
				varEle.addAttribute("Name", i.getName());
				varEle.addAttribute("Type", i.getType());
				if(i.getUnit()!=null){
					varEle.addAttribute("Unit", i.getUnit());
				}
			}
			
			//Outputs元素
			Element outputEle = taskEle.addElement("Outputs");
			for(Variable o : task.getOutputs()){
				Element varEle = outputEle.addElement("Variable");
				varEle.addAttribute("Name", o.getName());
				varEle.addAttribute("Type", o.getType());
				if(o.getUnit()!=null){
					varEle.addAttribute("Unit", o.getUnit());
				}
			}
		}
		
//		logger.info(doc.asXML());
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(taskFile));
			XMLWriter out = null;
			OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        out = new XMLWriter(bw, format);
	        out.write(doc);
	        bw.close();
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean generateApplicationFile(List<Application> applications, String filepath){
		
		File partitionFile = new File(rootPath+"/"+filepath.substring(0,filepath.indexOf('.')) + ".application");
		logger.info(partitionFile.getPath());
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Applications");
		for(Application app : applications){
			Element PartitionEle = root.addElement("Application");
			PartitionEle.addAttribute("Id", Integer.toString(app.getId()));
			PartitionEle.addAttribute("Name", app.getName());
			Element TasksEle = PartitionEle.addElement("Tasks");
			for(Task t: app.getTasks()){
				Element taskEle = TasksEle.addElement("Task");
				taskEle.addAttribute("Id", Integer.toString(t.getId()));
			}
			
			//Inputs元素
			Element inputEle = PartitionEle.addElement("Inputs");
			for(Variable i : app.getInputs()){
				Element varEle = inputEle.addElement("Variable");
				varEle.addAttribute("Name", i.getName());
				varEle.addAttribute("Type", i.getType());
				if(i.getUnit()!=null){
					varEle.addAttribute("Unit", i.getUnit());
				}
			}
			
			//Outputs元素
			Element outputEle = PartitionEle.addElement("Outputs");
			for(Variable o : app.getOutputs()){
				Element varEle = outputEle.addElement("Variable");
				varEle.addAttribute("Name", o.getName());
				varEle.addAttribute("Type", o.getType());
				if(o.getUnit()!=null){
					varEle.addAttribute("Unit", o.getUnit());
				}
			}
		}
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(partitionFile));
			XMLWriter out = null;
			OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        out = new XMLWriter(bw, format);
	        out.write(doc);
	        bw.close();
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
