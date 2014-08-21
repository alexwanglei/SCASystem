package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.edu.buaa.act.SCAS.po.Variable;

public class Process {
	
	private int id;
	
	private String name;
	
	private int stack;
	
	private int priorty;
	
	private double period;
	
	private double capacity;
	
	private String deadline;
	
	private String xmlProcess;
	
	
	private ArrayList<IOput> inputs = new  ArrayList<IOput>();
	private ArrayList<IOput> outputs = new  ArrayList<IOput>();
	
	private ArrayList<Semaphore> semaphores = new ArrayList<Semaphore>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack;
	}

	public int getPriorty() {
		return priorty;
	}

	public void setPriorty(int priorty) {
		this.priorty = priorty;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getXmlProcess() {
		return xmlProcess;
	}

	public void setXmlProcess(String xmlProcess) {
		this.xmlProcess = xmlProcess;
	}

	public ArrayList<IOput> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<IOput> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<IOput> getOutputs() {
		return outputs;
	}

	public void setOutputs(ArrayList<IOput> outputs) {
		this.outputs = outputs;
	}
	
	public String CreateProcess(int sn){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_PROCESS(&processTable["+sn+"], &process_"+this.id+", &retCode);\n");
		code.append("	if(retCode != NO_ERROR)\n" +
				"		printf(\"CREATE_PROCESS: can't create process "+this.name + " %s)\\n\",codeToStr(retCode));\n");
		return code.toString();
	}
	
	public String StartProcess(){
		StringBuffer code = new StringBuffer();
		code.append("	START (process_"+this.id+", &retCode);\n");
		code.append("	if(retCode != NO_ERROR)\n" +
				"		printf(\"START: can't start process "+this.name + " %s)\\n\",codeToStr(retCode));\n");
		return code.toString();
	}
	
	public String CreateProcessFunc(){
		StringBuffer code = new StringBuffer("void "+this.name+"(void)\n{\n");
		//接受队列，采样，黑板，缓冲区的变量，变量长度，有效参数
		for(IOput i: this.inputs){
			code.append("	"+i.getDataType()+" "+i.getConceptName().replaceAll(" ", "")+";\n");
			int sn = i.getId();
			code.append("	MESSAGE_SIZE_TYPE lendata"+sn+";\n");
			if(i.getType()!=null&&i.getType().equals("Sample"))
			{
				int num = i.getId();
				code.append("	VALIDITY_TYPE validity"+num+ "=0;\n");
			}
		}
		
		//发送队列、采样、黑板、缓冲区的变量，变量长度
		for(IOput o : this.outputs){
			code.append("	"+o.getDataType()+ " "+o.getConceptName().replaceAll(" ", "")+"=0;\n");
			int sn = o.getId();
			code.append("	int lenMsgData"+ sn + "= sizeof("+o.getConceptName().replaceAll(" ", "")+");\n");
		}
		
		//判断任务是否为周期任务
		if(this.period!=0){
			code.append("    FOREVER\n" +
					"	{\n" +
					"		PERIODIC_WAIT (&retCode);\n" +
					"		CHECK_CODE (\"PERIODIC_WAIT\", retCode);\n");	
		}
		//生成任务接受队列或采样消息
		for(IOput i : this.inputs){
			int n = i.getId();
			if(i.getType()!=null&&(i.getType().equals("InterPartition")))
			{
				code.append(i.getPort().revPortMsg(i, n));
			}
			else if(i.getType()!=null&&(i.getType().equals("IntraPartition")))
			{
				code.append(i.getMsgContainer().revContainerMsg(i, n));
			}
	
			//打印接受的数据
			//根据数据类型调整打印格式%d, %f
			String conceptName = i.getConceptName().replaceAll(" ", "");
			if(i.getType()!=null&&(i.getType().equals("InterPartition")))
			{
				code.append("		printf(\""+this.name+" receive "+conceptName+"=%f from the "+i.getPort().getName()+"\\n\","+conceptName+");\n");
				
			}
			else if(i.getType()!=null&&(i.getType().equals("IntraPartition")))
			{
				if(i.getMsgContainer() instanceof Buffer)
				{
					code.append("		printf(\""+this.name+" receive "+conceptName+"=%f from the "+i.getMsgContainer().getName()+"\\n\","+conceptName+");\n");
				}
				else if(i.getMsgContainer() instanceof Blackboard)
				{
					code.append("		printf(\""+this.name+" read "+conceptName+"=%f from the "+i.getMsgContainer().getName()+"\\n\","+conceptName+");\n");
				}
			}
		}
		
		//生成等待信号量
		for(Semaphore sem : this.semaphores){
			code.append(sem.waitSemaphore());
			code.append("		/*\n"
					+ "			critical zone\n"
					+ "			access the critical resource\n"
					+ "		*/\n");
			code.append(sem.signalSemaphore());
		}
		
		
		
		//生成任务发送队列和采样消息
		for(IOput o: this.outputs)
		{		
			int n = o.getId();
			if(o.getType()!=null&&(o.getType().equals("InterPartition")))
			{
				code.append(o.getPort().sendPortMsg(o, n));
			}
			else if(o.getType()!=null&&(o.getType().equals("IntraPartition")))
			{
				code.append(o.getMsgContainer().sendContainerMsg(o, n));
			}

			
			//打印发送的数据
			//根据数据类型调整打印格式%d, %f
			String conceptName = o.getConceptName().replaceAll(" ", "");
			if(o.getType()!=null&&(o.getType().equals("InterPartition")))
			{
				code.append("		printf(\""+this.name+" send "+conceptName+"=%f to the "+o.getPort().getName()+"\\n\","+conceptName+");\n");
				
			}
			else if(o.getType()!=null&&(o.getType().equals("IntraPartition")))
			{
				if(o.getMsgContainer() instanceof Buffer)
				{
					code.append("		printf(\""+this.name+" send "+conceptName+"=%f to the "+o.getMsgContainer().getName()+"\\n\","+conceptName+");\n");
				}
				else if(o.getMsgContainer() instanceof Blackboard)
				{
					code.append("		printf(\""+this.name+" display "+conceptName+"=%f on the "+o.getMsgContainer().getName()+"\\n\","+conceptName+");\n");
				}
			}
		}
		
		//判断任务是否为周期任务
		if(this.period!=0){
			code.append("	}\n");
		}
		code.append("}\n");
		return code.toString();
	}
	
	public String toXml(){
		Document doc = DocumentHelper.createDocument();
		Element saTaskEle = doc.addElement("SATask");
		saTaskEle.addAttribute("Id", Integer.toString(this.id));
		saTaskEle.addAttribute("Name", this.name);
		Element stackEle = saTaskEle.addElement("Stack");
		stackEle.addText(Integer.toString(this.stack));
		Element priortyEle = saTaskEle.addElement("Priorty");
		priortyEle.addText(Double.toString(this.priorty));
		Element periodEle = saTaskEle.addElement("Period");
		periodEle.addText(Double.toString(this.period));
		Element timeCapacityEle = saTaskEle.addElement("TimeCapacity");
		timeCapacityEle.addText(Double.toString(this.capacity));
		Element deadlineEle = saTaskEle.addElement("Deadline");
		deadlineEle.addText(this.deadline);
		
		Element taskInputsEle = saTaskEle.addElement("TaskInputs");

		for(IOput in : this.inputs){
			Element ioEle = taskInputsEle.addElement("IO");
			ioEle.addAttribute("Id", Integer.toString(in.getId()));
			ioEle.addAttribute("ConceptName", in.getConceptName());
			ioEle.addAttribute("Datatype", in.getDataType());
			ioEle.addAttribute("Type", in.getType());
			ioEle.addAttribute("Connect", in.getConnect());
		
		}
		Element taskOutputsEle = saTaskEle.addElement("TaskOutputs");
		for(IOput out : this.outputs){
			Element ioEle = taskOutputsEle.addElement("IO");
			ioEle.addAttribute("Id", Integer.toString(out.getId()));
			ioEle.addAttribute("ConceptName", out.getConceptName());
			ioEle.addAttribute("DataType", out.getDataType());
			ioEle.addAttribute("Type", out.getType());
			ioEle.addAttribute("Connect", out.getConnect());
			
		}
		
		return doc.asXML();
	}

	public ArrayList<Semaphore> getSemaphores() {
		return semaphores;
	}

	public void setSemaphores(ArrayList<Semaphore> semaphores) {
		this.semaphores = semaphores;
	}
	
	
}
