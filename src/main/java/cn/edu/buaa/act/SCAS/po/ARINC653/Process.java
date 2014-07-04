package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

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
		code.append("	START ("+this.id+", &retCode);\n");
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
			code.append("	int lenMsgData"+ sn + "= sizeof("+o.getConceptName()+");\n");
		}
		
		//判断任务是否为周期任务
		if(this.period!=0){
			code.append(" FOREVER\n" +
					"	{\n" +
					"		PERIODIC_WAIT (&retCode);\n" +
					"		CHECK_CODE (\"PERIODIC_WAIT\", retCode);\n");	
		}
		//生成任务接受队列或采样消息
		for(IOput i : this.inputs){
			int n = i.getId();
			if(i.getType()!=null&&(i.getType().equals("queue")||i.getType().equals("sample")))
			{
				code.append(i.getPort().revPortMsg(i, n));
			}
			else if(i.getType()!=null&&(i.getType().equals("blackboard")||i.getType().equals("buffer")))
			{
				code.append(i.getMsgContainer().revContainerMsg(i, n));
			}
	
			//打印接受的数据
			code.append("		printf(\""+this.name+" "+i.getType()+" receive %d\\n\","+i.getConceptName().replaceAll(" ", "")+");\n");
		}
		
		//生成任务发送队列和采样消息
		for(IOput o: this.outputs)
		{		
			int n = o.getId();
			if(o.getType()!=null&&(o.getType().equals("queue")||o.getType().equals("sample")))
			{
				code.append(o.getPort().sendPortMsg(o, n));
			}
			else if(o.getType()!=null&&(o.getType().equals("blackboard")||o.getType().equals("buffer")))
			{
				code.append(o.getMsgContainer().sendContainerMsg(o, n));
			}

			
			//打印发送的数据
			code.append("		printf(\""+this.name+" "+o.getType()+" send %d\\n\","+o.getConceptName().replaceAll(" ", "")+"++);\n");
		}
		
		//判断任务是否为周期任务
		if(this.period!=0){
			code.append("	}\n}");
		}
		code.append("}\n");
		return code.toString();
	}

	
}
