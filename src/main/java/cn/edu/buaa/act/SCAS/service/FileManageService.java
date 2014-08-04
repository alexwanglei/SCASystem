package cn.edu.buaa.act.SCAS.service;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;






































import cn.edu.buaa.act.SCAS.po.Application;
import cn.edu.buaa.act.SCAS.po.Formula;
import cn.edu.buaa.act.SCAS.po.Task;
import cn.edu.buaa.act.SCAS.po.Variable;
import cn.edu.buaa.act.SCAS.po.ARINC653.Blackboard;
import cn.edu.buaa.act.SCAS.po.ARINC653.Buffer;
import cn.edu.buaa.act.SCAS.po.ARINC653.Channel;
import cn.edu.buaa.act.SCAS.po.ARINC653.IOput;
import cn.edu.buaa.act.SCAS.po.ARINC653.InterPartitionCom;
import cn.edu.buaa.act.SCAS.po.ARINC653.IntraPartitionCom;
import cn.edu.buaa.act.SCAS.po.ARINC653.Module;
import cn.edu.buaa.act.SCAS.po.ARINC653.Partition;
import cn.edu.buaa.act.SCAS.po.ARINC653.PartitionWindow;
import cn.edu.buaa.act.SCAS.po.ARINC653.Port;
import cn.edu.buaa.act.SCAS.po.ARINC653.Process;
import cn.edu.buaa.act.SCAS.po.ARINC653.MsgContainer;
import cn.edu.buaa.act.SCAS.po.ARINC653.QueuePort;
import cn.edu.buaa.act.SCAS.po.ARINC653.SamplePort;

@Service
public class FileManageService {
	
	
	
	private static Logger logger = LoggerFactory.getLogger(FileManageService.class);
	
	private String rootPath = this.getClass().getResource("/File").getPath();
	
	public String getDirectory() throws JSONException{

//		logger.info("存储工程文件的根目录："+rootPath);
		File rootDirectory = new File(rootPath);
		JSONObject jsonDir = new JSONObject();
		getJsonDir(rootDirectory, jsonDir);
//		logger.info("工程文件目录json："+jsonDir);
		return jsonDir.getJSONArray("children").toString();
	}
	
	private void getJsonDir(File dir, JSONObject jsonObject) throws JSONException {
		jsonObject.put("text", dir.getName());

		if (dir.isFile()) {
			return;
		} else {
			jsonObject.put("state", "closed");
			File files[] = dir.listFiles();
			if (files.length == 0) {
			} else {
				JSONArray ja = new JSONArray();
				for (int i = 0; i < files.length; i++) {
					JSONObject childJo = new JSONObject();
					getJsonDir(files[i], childJo);
					ja.put(childJo);
				}
				jsonObject.put("children", ja);
			}
		}
	}
	
	public List<Formula> getFormula(String filename) throws DocumentException{
		List<Formula> formulas = new ArrayList<Formula>();
		File file = new File(rootPath+"/"+filename);
	//	logger.info(file.getPath());
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();

		List<Element> formulaList = root.elements("Formula");

		for(Element e: formulaList){
			Formula formula = new Formula(e);
			formulas.add(formula);
		}
		return formulas;
	}
	
	public List<Task> getTask(String filename) throws DocumentException{
		List<Task> tasks = new ArrayList<Task>();
		File file = new File(rootPath+"/"+filename);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();
		
		List<Element> taskEleList = root.elements("Task");
		for(Element e : taskEleList){
			Task task = new Task(e);
			tasks.add(task);
		}
		return tasks;
	}
	
	public List<Application> getApplication(String filename) throws DocumentException{
		List<Application> applications = new ArrayList<Application>();
		File file = new File(rootPath+"/"+filename);
		String taskFilename = filename.replace(".application", ".task");
		List<Task> tasks = getTask(taskFilename);
		HashMap<String,Task> taskMap = new HashMap<String,Task>();
		for(Task t : tasks){
			taskMap.put(Integer.toString(t.getId()), t);
		}
		
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();
		
		List<Element> appEleList = root.elements("Application");
		for(Element e : appEleList){
			Application application = new Application();
			application.setId(Integer.parseInt(e.attributeValue("Id")));
			application.setName(e.attributeValue("Name"));
			List<Element> taskEleList = e.element("Tasks").elements("Task");
			for(Element taskEle : taskEleList){
				application.getTasks().add(taskMap.get(taskEle.attributeValue("Id")));
			}
			
			List<Element> inputEleList = e.element("Inputs").elements("Variable");
			for(Element ve : inputEleList){
				application.getInputs().add(new Variable(ve));
			}
			List<Element> outputEleList = e.element("Outputs").elements("Variable");
			for(Element ve : outputEleList){
				application.getOutputs().add(new Variable(ve));
			}
			application.findTaskComm();
			applications.add(application);
		}
		
		
		
		return applications;
		
	}
	
	public String getXmlString(String filename) throws DocumentException{
		File file = new File(rootPath+"/"+filename);
		//	logger.info(file.getPath());
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();
		String xml = root.asXML();
		xml = "\""+xml.replace("\"","\\\"" )+"\"";
		xml = xml.replace("\n", "\\n");
		xml = xml.replace("\t","\\t");
		return xml;
	}
	
	
	
	
	public Module getModule(String filename) throws DocumentException{
		Module module = new Module();
		File file = new File(rootPath+"/"+filename);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();
		
		String xml = root.asXML();
		xml = "\""+xml.replace("\"","\\\"" )+"\"";
		xml = xml.replace("\n", "\\n");
		xml = xml.replace("\t","\\t");
		
		module.setXmlModule(xml);
		
		module.setId(Integer.parseInt(root.attributeValue("Id")));
		module.setName(root.attributeValue("Name"));
		

		List<Element> includeList = root.element("SAPartitions").elements("include");
		
		for(Element include : includeList){
			String pfilename = filename.substring(0,filename.lastIndexOf('/')+1)+include.attributeValue("href");
			logger.info(pfilename);
			module.getPartitions().add(getPartition(pfilename));
		}
		
		List<Element> interCommList = root.element("InterCommunications").elements("Communication");
		if(interCommList != null){
			for(Element interCom : interCommList){
				InterPartitionCom interPartitionCom = new InterPartitionCom();
				interPartitionCom.setSrcPartition(interCom.attributeValue("SrcPart"));
				interPartitionCom.setDstPartition(interCom.attributeValue("DstPart"));
				interPartitionCom.setSrcPort(interCom.attributeValue("SrcPort"));
				interPartitionCom.setDstPort(interCom.attributeValue("DstPort"));
				interPartitionCom.setMode(interCom.attributeValue("Mode"));
				interPartitionCom.setConcept(interCom.getText());
				module.getInterCom().add(interPartitionCom);
			}
		}
		
		List<Element> daCommList = root.element("DaCommunications").elements("Communication");
		if(daCommList != null){
			for(Element daCom : daCommList){
				InterPartitionCom daPartitionCom = new InterPartitionCom();
				daPartitionCom.setSrcPartition(daCom.attributeValue("SrcPart"));
				daPartitionCom.setDstPartition(daCom.attributeValue("DstPart"));
				daPartitionCom.setSrcPort(daCom.attributeValue("SrcPort"));
				daPartitionCom.setDstPort(daCom.attributeValue("DstPort"));
				daPartitionCom.setMode(daCom.attributeValue("Mode"));
				daPartitionCom.setConcept(daCom.getText());
				module.getDaCom().add(daPartitionCom);
			}
		}
		
		//生成schedule成员变量
		List<Element> scheduleList = root.element("Schedule").elements("window");
		if(scheduleList != null){
			for(Element e : scheduleList){
				PartitionWindow partitionWindow = new PartitionWindow();
				partitionWindow.setId(Integer.parseInt(e.attributeValue("Id")));
				partitionWindow.setPartName(e.attributeValue("PartitonNameRef"));
				partitionWindow.setDuration(Double.parseDouble(e.attributeValue("Duration")));
				partitionWindow.setReleasePoint(Double.parseDouble(e.attributeValue("ReleasePoint")));
				module.getSchedule().add(partitionWindow);
			}
		}
		
		//生成connection
		//1:分区之间的端口连接
		//2:分区直连端口和应用端口的连接
		
		HashMap<String,String> srcMap = new HashMap<String,String>();
		for(InterPartitionCom ipc : module.getInterCom()){
			srcMap.put(ipc.getSrcPartition()+ipc.getSrcPort(), ipc.getSrcPort());
		}
		for(InterPartitionCom dac : module.getDaCom()){
			srcMap.put(dac.getSrcPartition()+dac.getSrcPort(), dac.getSrcPort());
		}
		int cid=1;
		Iterator iter= srcMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String,String> entry = (Entry)iter.next();
			Channel c = new Channel(cid++,  entry.getKey().substring(0, entry.getKey().indexOf(entry.getValue())),entry.getValue());
			module.getChannels().add(c);
		}
		
		for(Channel c: module.getChannels()){
			for(InterPartitionCom ipc : module.getInterCom()){
				if(ipc.getSrcPort().equals(c.getSrcPort())&&ipc.getSrcPartition().equals(c.getSrcPartition())){
					c.getDstPartitions().add(ipc.getDstPartition());
					c.getDstPorts().add(ipc.getDstPort());
				}
			}
			for(InterPartitionCom dac : module.getDaCom()){
				if(dac.getSrcPort().equals(c.getSrcPort())&&dac.getSrcPartition().equals(c.getSrcPartition())){
					c.getDstPartitions().add(dac.getDstPartition());
					c.getDstPorts().add(dac.getDstPort());
				}
			}
		}	
		
		return module;
	}
	
	
	
	public Partition getPartition(String filename) throws DocumentException{
		Partition partition = new Partition();
		File file = new File(rootPath+"/"+filename);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();
		
		String xml = root.asXML();
		xml = "\""+xml.replace("\"","\\\"" )+"\"";
		xml = xml.replace("\n", "\\n");
		xml = xml.replace("\t","\\t");
		
		partition.setXmlPartition(xml);
		
		partition.setId(Integer.parseInt(root.attributeValue("Id")));
		partition.setName(root.attributeValue("Name"));
		
		
		List<Element> msgContainerList = root.element("MessageContainers").elements("MessageContainer");
		HashMap<String,MsgContainer> msgContainerMap = new HashMap<String,MsgContainer>();
		//保存消息容器和名字的map
		HashMap<String,MsgContainer> msgContainerNameMap = new HashMap<String,MsgContainer>();
		for(Element messageContainer : msgContainerList){
			String key = messageContainer.attributeValue("Id");
			for(Element e: (List<Element>)messageContainer.elements()){
				if(e.getName().equals("Buffer")){
					Buffer buffer = new Buffer();
					buffer.setId(Integer.parseInt(key));
					buffer.setName(e.attributeValue("Name"));
					if(e.attributeValue("MessageSize")!=""){
						buffer.setMessageSize(Integer.parseInt(e.attributeValue("MessageSize")));
					}
					if(e.attributeValue("BufferLength")!=""){
						buffer.setBufferLength(Integer.parseInt(e.attributeValue("BufferLength")));
					}
					if(e.attributeValue("Discipline")!=""){
						buffer.setDiscipline(e.attributeValue("Discipline"));
					}
					msgContainerMap.put(key, buffer);
					msgContainerNameMap.put(buffer.getName(), buffer);
				}
				else if(e.getName().equals("Blackboard")){
					Blackboard blackboard = new Blackboard();
					blackboard.setId(Integer.parseInt(key));
					blackboard.setName(e.attributeValue("Name"));
					if(e.attributeValue("MessageSize")!=""){
						blackboard.setMessageSize(Integer.parseInt(e.attributeValue("MessageSize")));
					}
					
					msgContainerMap.put(key, blackboard);
					msgContainerNameMap.put(blackboard.getName(), blackboard);
				}
			}
		}
		
		
		List<Element> intraCommList = root.element("IntraCommunications").elements("Communication");
		for(Element e: intraCommList){
			IntraPartitionCom intraPartitionCom = new IntraPartitionCom();
			intraPartitionCom.setSrcTask(e.attributeValue("SrcTask"));
			intraPartitionCom.setDstTask(e.attributeValue("DstTask"));
			intraPartitionCom.setMsgContainer(msgContainerMap.get(e.attributeValue("MessageContainerId")));
			intraPartitionCom.setConceptName(e.getText());
			partition.getIntraComs().add(intraPartitionCom);
		}
		
		//处理应用端口元素
		HashMap<String,Port> portMap = new HashMap<String,Port>();
		List<Element> portList = root.element("ApplicationPorts").elements();
		for(Element e: portList){
			if(e.getName().equals("SamplePort")){
				SamplePort samplePort = new SamplePort();
				samplePort.setId(Integer.parseInt(e.attributeValue("Id")));
				samplePort.setName(e.attributeValue("Name"));
				samplePort.setDirection(e.attributeValue("Direction"));
				if(e.attributeValue("MessageSize")!=""){
					samplePort.setMessageSize(Integer.parseInt(e.attributeValue("MessageSize")));
				}
				if(e.attributeValue("RefreshPeriod")!=""){
					samplePort.setRefreshPeriod(Double.parseDouble(e.attributeValue("RefreshPeriod")));
				}
				partition.getPorts().add(samplePort);
				portMap.put(samplePort.getName(), samplePort);
			}
			else if(e.getName().equals("QueuePort")){
				QueuePort queuePort = new QueuePort();
				queuePort.setId(Integer.parseInt(e.attributeValue("Id")));
				queuePort.setName(e.attributeValue("Name"));
				queuePort.setDirection(e.attributeValue("Direction"));
				if(e.attributeValue("MessageSize")!=""){
					queuePort.setMessageSize(Integer.parseInt(e.attributeValue("MessageSize")));
				}
				if(e.attributeValue("MessageSize")!=""){
					queuePort.setQueueLength(Integer.parseInt(e.attributeValue("QueueLength")));
				}
				queuePort.setProtocol(e.attributeValue("Protocol"));
				queuePort.setDiscipline(e.attributeValue("Discipline"));
				partition.getPorts().add(queuePort);
				portMap.put(queuePort.getName(), queuePort);
			}
		}
		//处理分区直连端口元素
		List<Element> daPortList = root.element("PartitionPorts").elements();
		for(Element e: daPortList){
			if(e.getName().equals("SamplePort")){
				SamplePort samplePort = new SamplePort();
				samplePort.setId(Integer.parseInt(e.attributeValue("Id")));
				samplePort.setName(e.attributeValue("Name"));
				samplePort.setDirection(e.attributeValue("Direction"));
				if(e.attributeValue("MessageSize")!=""){
					samplePort.setMessageSize(Integer.parseInt(e.attributeValue("MessageSize")));
				}
				if(e.attributeValue("RefreshPeriod")!=""){
					samplePort.setRefreshPeriod(Double.parseDouble(e.attributeValue("RefreshPeriod")));
				}
				partition.getDaPorts().add(samplePort);
			}
			else if(e.getName().equals("QueuePort")){
				QueuePort queuePort = new QueuePort();
				queuePort.setId(Integer.parseInt(e.attributeValue("Id")));
				queuePort.setName(e.attributeValue("Name"));
				queuePort.setDirection(e.attributeValue("Direction"));
				if(e.attributeValue("MessageSize")!=""){
					queuePort.setMessageSize(Integer.parseInt(e.attributeValue("MessageSize")));
				}
				if(e.attributeValue("MessageSize")!=""){
					queuePort.setQueueLength(Integer.parseInt(e.attributeValue("QueueLength")));
				}
				queuePort.setProtocol(e.attributeValue("Protocol"));
				queuePort.setDiscipline(e.attributeValue("Discipline"));
				partition.getDaPorts().add(queuePort);
			}
		}
		
		//处理任务模型
		List<Element> includeList = root.element("SATasks").elements("include");
		for(Element include : includeList){
			String tfilename = filename.substring(0,filename.lastIndexOf('/')+1)+include.attributeValue("href");
			logger.info(tfilename);
			Process process = getProcess(tfilename);
			for(IOput in : process.getInputs()){
				if(in.getType().equals("IntraPartition")){
					in.setMsgContainer(msgContainerNameMap.get(in.getConnect()));
				}
				else if(in.getType().equals("InterPartition")){
					in.setPort(portMap.get(in.getConnect()));
				}
			}
			
			for(IOput out : process.getOutputs()){
				if(out.getType().equals("IntraPartition")){
					out.setMsgContainer(msgContainerNameMap.get(out.getConnect()));
				}
				else if(out.getType().equals("InterPartition")){
					out.setPort(portMap.get(out.getConnect()));
				}
			}
			partition.getProcesses().add(process);
		}
		
		
		
		return partition;
	}
	
	public Process getProcess(String filename) throws DocumentException{
		Process process = new Process();
		File file = new File(rootPath+"/"+filename);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();

		Element stack = root.element("Stack");

		Element priorty = root.element("Priorty");

		Element period = root.element("Period");

		Element timeCapacity = root.element("TimeCapacity");
		Element deadline = root.element("Deadline");
		
		String xml = root.asXML();
		xml = "\""+xml.replace("\"","\\\"" )+"\"";
		xml = xml.replace("\n", "\\n");
		xml = xml.replace("\t","\\t");
		
		process.setXmlProcess(xml);
//		System.out.println(process.getXmlProcess());
		process.setId(Integer.parseInt(root.attributeValue("Id")));
		process.setName(root.attributeValue("Name"));

		if(stack.getText() != ""){
			process.setStack(Integer.parseInt(stack.getText()));
		}
//		System.out.println("run:"+priorty.getText());
		if(priorty.getText() != ""){
			process.setPriorty(Integer.parseInt(priorty.getText()));
		}
		if(period.getText() != ""){
			process.setPeriod(Double.parseDouble(period.getText()));
		}
		if(timeCapacity.getText() != ""){
			process.setCapacity(Double.parseDouble(timeCapacity.getText()));
		}
		if(deadline.getText() != ""){
			process.setDeadline(deadline.getText());
		}
		//解析进程的输入
		List<Element> inputList = root.element("TaskInputs").elements("IO");
		for(Element e: inputList){
			IOput input = new IOput();
			input.setId(Integer.parseInt(e.attributeValue("Id")));
			input.setConceptName(e.attributeValue("ConceptName"));
			input.setDataType(e.attributeValue("DataType"));
			input.setType(e.attributeValue("Type"));
			input.setConnect(e.attributeValue("Connect"));
			process.getInputs().add(input);
		}
		//解析进程的输出
		List<Element> outputList = root.element("TaskOutputs").elements("IO");
		for(Element e: outputList){
			IOput output = new IOput();
			output.setId(Integer.parseInt(e.attributeValue("Id")));
			output.setConceptName(e.attributeValue("ConceptName"));
			output.setDataType(e.attributeValue("DataType"));
			output.setType(e.attributeValue("Type"));
			output.setConnect(e.attributeValue("Connect"));
			process.getOutputs().add(output);
		}
		
		return process;
		
	}
	
	public boolean saveProcessToXml(Process process, String filename){
		File processXmlFile = new File(rootPath+"/"+filename);
		Document doc = DocumentHelper.createDocument();
		Element saTaskEle = doc.addElement("SATask");
		saTaskEle.addAttribute("Id", Integer.toString(process.getId()));
		saTaskEle.addAttribute("Name", process.getName());
		Element stackEle = saTaskEle.addElement("Stack");
		stackEle.addText(Integer.toString(process.getStack()));
		Element priortyEle = saTaskEle.addElement("Priorty");
		priortyEle.addText(Integer.toString(process.getPriorty()));
		Element periodEle = saTaskEle.addElement("Period");
		periodEle.addText(Double.toString(process.getPeriod()));
		Element timeCapacityEle = saTaskEle.addElement("TimeCapacity");
		timeCapacityEle.addText(Double.toString(process.getCapacity()));
		Element deadlineEle = saTaskEle.addElement("Deadline");
		deadlineEle.addText(process.getDeadline());
		
		Element taskInputsEle = saTaskEle.addElement("TaskInputs");

		for(IOput in : process.getInputs()){
			Element ioEle = taskInputsEle.addElement("IO");
			ioEle.addAttribute("Id", Integer.toString(in.getId()));
			ioEle.addAttribute("ConceptName", in.getConceptName());
			ioEle.addAttribute("Datatype", in.getDataType());
			ioEle.addAttribute("Type", in.getType());
			ioEle.addAttribute("Connect", in.getConnect());
		
		}
		Element taskOutputsEle = saTaskEle.addElement("TaskOutputs");
		for(IOput out : process.getOutputs()){
			Element ioEle = taskOutputsEle.addElement("IO");
			ioEle.addAttribute("Id", Integer.toString(out.getId()));
			ioEle.addAttribute("ConceptName", out.getConceptName());
			ioEle.addAttribute("DataType", out.getDataType());
			ioEle.addAttribute("Type", out.getType());
			ioEle.addAttribute("Connect", out.getConnect());
		}
		
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(processXmlFile));
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
	
	public boolean modifyPartitionToXml(HttpServletRequest request,String filename){
		

		String[] bbMessageSize = request.getParameterValues("bbMessageSize");
		
		String[] bfMessageSize = request.getParameterValues("bfMessageSize");
		String[] bfLength = request.getParameterValues("bfLength");
		String[] bfDiscipline = request.getParameterValues("bfDiscipline");
		
		String[] appSpMessageSize = request.getParameterValues("appSpMessageSize");
		String[] appSpRefreshPeriod = request.getParameterValues("appSpRefreshPeriod");
		
		String[] appQpMessageSize = request.getParameterValues("appQpMessageSize");
		String[] appQpQueueLength = request.getParameterValues("appQpQueueLength"); 
		String[] appQpProtocol = request.getParameterValues("appQpProtocol"); 
		String[] appQpDiscipline = request.getParameterValues("appQpDiscipline");
		
		String[] daSpMessageSize = request.getParameterValues("daSpMessageSize");
		String[] daSpRefreshPeriod = request.getParameterValues("daSpRefreshPeriod");
		
		String[] daQpMessageSize = request.getParameterValues("daQpMessageSize");
		String[] daQpQueueLength = request.getParameterValues("daQpQueueLength"); 
		String[] daQpProtocol = request.getParameterValues("daQpProtocol"); 
		String[] daQpDiscipline = request.getParameterValues("daQpDiscipline");
		
		File partitionModelFile = new File(rootPath+"/"+filename);
		
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(partitionModelFile);
			
			//补充黑板的属性
			List<Attribute> bbMsAttribute = document.selectNodes("/SAPartition/MessageContainers/MessageContainer/Blackboard/@MessageSize");
			for(int i=0; i<bbMsAttribute.size(); i++){
				bbMsAttribute.get(i).setValue(bbMessageSize[i]);
			}
			
			//补充缓冲区的属性
			List<Element> bufferEles = document.selectNodes("//Buffer");
			Element buffer;
			for(int i=0; i<bufferEles.size(); i++){
				buffer = bufferEles.get(i);
				Attribute messageSize = buffer.attribute("MessageSize");
				messageSize.setValue(bfMessageSize[i]);
				Attribute bufferLength = buffer.attribute("BufferLength");
				bufferLength.setValue(bfLength[i]);
				Attribute discipline = buffer.attribute("Discipline");
				discipline.setValue(bfDiscipline[i]);
			}
			
			//补充采样端口的属性
			List<Element> sampleEles = document.selectNodes("//ApplicationPorts/SamplePort");
			logger.info("采样端口数："+sampleEles.size());
			Element sample;
			for(int i=0; i<sampleEles.size(); i++){
				sample = sampleEles.get(i);
//				logger.info(sample.attributeValue("Id"));
				Attribute messageSize = sample.attribute("MessageSize");
				messageSize.setValue(appSpMessageSize[i]);
				Attribute refreshPeriod = sample.attribute("RefreshPeriod");
				refreshPeriod.setValue(appSpRefreshPeriod[i]);
				//logger.info(sample.asXML());
			}
			
			//补充队列端口属性
			List<Element> queueEles = document.selectNodes("//ApplicationPorts/QueuePort");
			Element queue;
			for(int i=0; i<queueEles.size(); i++){
				queue = queueEles.get(i);
				Attribute messageSize = queue.attribute("MessageSize");
				messageSize.setValue(appQpMessageSize[i]);
				Attribute refreshPeriod = queue.attribute("QueueLength");
				refreshPeriod.setValue(appQpQueueLength[i]);
				Attribute protocol = queue.attribute("Protocol");
				protocol.setValue(appQpProtocol[i]);
				Attribute discipline = queue.attribute("Discipline");
				discipline.setValue(appQpDiscipline[i]);
			}
			
			//补充直连采样端口信息
			Element daSample;
			List<Element> daSampleEles = document.selectNodes("//PartitionPorts/SamplePort");
			for(int i=0; i<daSampleEles.size(); i++){
				daSample = daSampleEles.get(i);
//				logger.info(daSample.attributeValue("Id"));
				Attribute messageSize = daSample.attribute("MessageSize");
				messageSize.setValue(daSpMessageSize[i]);
				Attribute refreshPeriod = daSample.attribute("RefreshPeriod");
				refreshPeriod.setValue(daSpRefreshPeriod[i]);
			}
			
			//补充直连队列端口属性
			Element daQueue;
			List<Element> daQueueEles = document.selectNodes("//PartitionPorts/QueuePort");
			for(int i=0; i<daQueueEles.size(); i++){
				daQueue = daQueueEles.get(i);
				Attribute messageSize = daQueue.attribute("MessageSize");
				messageSize.setValue(daQpMessageSize[i]);
				Attribute refreshPeriod = daQueue.attribute("QueueLength");
				refreshPeriod.setValue(daQpQueueLength[i]);
				Attribute protocol = daQueue.attribute("Protocol");
				protocol.setValue(daQpProtocol[i]);
				Attribute discipline = daQueue.attribute("Discipline");
				discipline.setValue(daQpDiscipline[i]);
			}
			
//			logger.info(document.asXML());
			BufferedWriter bw = new BufferedWriter(new FileWriter(partitionModelFile));
			XMLWriter out = null;
			OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        out = new XMLWriter(bw, format);
	        out.write(document);
	        bw.close();
	        return true;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveByXml(String xml, String filename){
		FileWriter writer;
		try {
			writer = new FileWriter(rootPath+"/"+filename);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write(xml);
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
//	public boolean saveModuleByXml(String moduleXml, String filename){
//		FileWriter writer;
//		try {
//			writer = new FileWriter(rootPath+"/"+filename);
//			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//			writer.write(moduleXml);
//			writer.close();
//			return true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}
//	
//	public boolean saveTaskByXml(String taskXml, String filename){
//		FileWriter writer;
//		try {
//			writer = new FileWriter(rootPath+"/"+filename);
//			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//			writer.write(taskXml);
//			writer.close();
//			return true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public String getCCode(String filename) throws IOException{
		File file = new File(rootPath+"/"+filename);
		Long fileLength = file.length();
		byte[] filecontent = new byte[fileLength.intValue()];
		FileInputStream in = new FileInputStream(file);
		in.read(filecontent);
		in.close();
		String code = new String(filecontent, "utf-8");
		code = code.replace("\\", "\\\\");
		code = "\""+code.replace("\"","\\\"" )+"\"";
		code = code.replace("\n", "\\n");
		code = code.replace("\t","\\t");
		logger.info(code);
		
		return code;
		
	}
	
	public String getConf(String filename) throws IOException, DocumentException{
		File file = new File(rootPath+"/"+filename);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(file);
		Element root = doc.getRootElement();
		
		String xml = root.asXML();
		xml = "\""+xml.replace("\"","\\\"" )+"\"";
		xml = xml.replace("\n", "\\n");
		xml = xml.replace("\t","\\t");
		
		return xml;
		
	}
}
