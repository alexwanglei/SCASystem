package cn.edu.buaa.act.SCAS.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.buaa.act.SCAS.po.Application;
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
	
	public boolean gnerateSAModel(List<Application> applications, String ac, String ec, String filepath){
		File modelFolder = new File(rootPath+"/"+filepath.substring(0,filepath.indexOf('/')) + "/SAModel");
		logger.info(modelFolder.getPath());
		if(!modelFolder.exists())
			modelFolder.mkdir();
		
		//生成分区模型
		for(Application app : applications){
			File partitionModelFile = new File(modelFolder.getPath()+"/"+app.getName()+".amp");
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("SAPartition");
			root.addAttribute("Id", Integer.toString(app.getId()));
			root.addAttribute("Name", app.getName());
			Element saTaskEle = root.addElement("SATasks");
			saTaskEle.addAttribute("xmlns:xi", "http://www.w3.org/2003/XInclude");
			for(Task task : app.getTasks()){
				Element includeEle = saTaskEle.addElement("xi:include");
				includeEle.addAttribute("href", task.getName());
			}
			
			Element intraComEle = root.addElement("IntraCommunications");
			int mc = 1;
			for(TaskCommunication tc : app.getTaskCommunications()){
				Element commEle = intraComEle.addElement("Communication");
				commEle.addAttribute("SrcTask", tc.getSrcTask().getName());
				commEle.addAttribute("DstTask", tc.getDstTask().getName());
				commEle.addAttribute("MessageContainerNameRef", "mc"+mc++);
				commEle.addText(tc.getVariable().getName());
			}
			
			Element mcsEle = root.addElement("MessageContainers");
			//HashMap<String, TaskCommunciaton> = new H
			mc = 1;
			for(TaskCommunication tc : app.getTaskCommunications()){
				Element mcEle = mcsEle.addElement("MessageContainer");
				mcEle.addAttribute("Name", "mc"+mc);
				if(tc.getType().equals("buffer")){
					Element bufferEle = mcEle.addElement("Buffer");
					bufferEle.addAttribute("Id", Integer.toString(mc));
					String bufferName = "b"+mc++;
					bufferEle.addAttribute("Name", bufferName);
					bufferEle.addAttribute("MessageSize", "");
					bufferEle.addAttribute("BufferLength", "");
					bufferEle.addAttribute("Discipline", "");
				}
				else if(tc.getType().equals("blackboard")){
					Element blackboardEle = mcEle.addElement("Blackboard");
					blackboardEle.addAttribute("Id", Integer.toString(mc));
					String blackboardName = "b"+mc++;
					blackboardEle.addAttribute("Name", blackboardName);
					blackboardEle.addAttribute("MessageSize", "");
				}
			}
			
			Element portsEle = root.addElement("Ports");
			int port = 1;
			for(Variable var : app.getInputs()){
				if(var.getType().equals("sample"))
				Element portEle = portsEle.addElement("");
			}
			
		}
		
		//生成任务模型
		for(Application app : applications){
			for(Task task : app.getTasks()){
				File taskModelFile = new File(modelFolder.getPath()+"/"+task.getName()+".amt");
				
			}
		}
		
		
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
