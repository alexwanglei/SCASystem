package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

public class Partition {
	private int id;
	
	private String name;
	
	private String xmlPartition;
	
	private ArrayList<Process> processes = new ArrayList<Process>();
	
	private ArrayList<Port> ports = new ArrayList<Port>();
	
	private ArrayList<Port> daPorts = new ArrayList<Port>();
	
	private ArrayList<IntraPartitionCom> intraComs = new ArrayList<IntraPartitionCom>();
	
	private int MemorySizeBss = 0x1000;
	private int MemorySizeData= 0x1000;
	private int MemorySizePersistentBss = 0x1000;
	private int MemorySizePersistentData = 0x1000;
	private int MemorySizeRoData = 0x1000;
	private int MemorySizeText =0x4000;
	

	
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
	public ArrayList<Process> getProcesses() {
		return processes;
	}
	public void setProcesses(ArrayList<Process> processes) {
		this.processes = processes;
	}
	public ArrayList<Port> getPorts() {
		return ports;
	}
	public void setPorts(ArrayList<Port> ports) {
		this.ports = ports;
	}
	public ArrayList<IntraPartitionCom> getIntraComs() {
		return intraComs;
	}
	
	public String initPartition(){
		StringBuffer code = new StringBuffer("void Init(void)\n" +
				"{\n" +
				"	retCode = -1;\n");
		//创建分区间队列
		for(Port p : this.ports)
		{
			code.append(p.createPort());
		}

		//创建分区内黑板和缓冲区
		for(IntraPartitionCom ipc : this.intraComs)
		{
			code.append(ipc.getMsgContainer().createMsgContainer());
		}
		//创建进程
		int i =0;
		for(Process p : this.processes)
		{	
			code.append(p.CreateProcess(i));
			i++;
		}
		//设置分区模式为normal
		code.append("	SET_PARTITION_MODE (NORMAL, &retCode);\n" +
				"	CHECK_CODE (\"SET_PARTITION_MODE\", retCode);\n" +
				"	return;\n" +
				"}\n");
		return code.toString();
	}
	
	//生成进程表
	public String defProcessTable()
	{
		StringBuffer code = new StringBuffer("PROCESS_ATTRIBUTE_TYPE processTable[]=\n{\n");
		for(Process p: this.processes)
		{
			if(p.getPeriod()>0)
			{
				code.append("	{\"t"+p.getName()+"\", "+p.getName()+", "+p.getStack()+", "+p.getPriorty()+", "+(long)p.getPeriod()*(long)1000000000+"ll, "+(long)p.getCapacity()*(long)1000000000+"ll, "+p.getDeadline()+"},\n");
			}
			else
			{
				code.append("	{\"t"+p.getName()+"\", "+p.getName()+", "+p.getStack()+", "+p.getPriorty()+", 0, 0 "+ p.getDeadline()+"},\n");
			}
		}
		code.append("	{\"\" , NULL, 0, 0, 0, 0, SOFT}\n};\n");
		return code.toString();
	}
	//生成局部变量
	public String defLocals()
	{
		//返回码
		StringBuffer code = new StringBuffer("LOCAL RETURN_CODE_TYPE retCode;\n");
		//端口ID
		for(Port p : this.ports)
		{
			//System.out.println(p.getName());
			//System.out.println(p.genLocalID());
			code.append(p.genLocalID());
		}

		//黑板 缓冲区
		for(IntraPartitionCom ipc : this.intraComs)
		{
			code.append(ipc.getMsgContainer().genLocalId());
		}
		//进程ID
		for(Process p: this.processes)
		{
			code.append("LOCAL PROCESS_ID_TYPE process_"+p.getId()+";\n");
		}
		return code.toString();
	}
	
	//生成进程函数
	public String defTaskFunc(){
		StringBuffer code = new StringBuffer();
		for(Process p : this.processes){
			code.append(p.CreateProcessFunc());
		}
		return code.toString();
	}
	public void setIntraComs(ArrayList<IntraPartitionCom> intraComs) {
		this.intraComs = intraComs;
	}
	public String getXmlPartition() {
		return xmlPartition;
	}
	public void setXmlPartition(String xmlPartition) {
		this.xmlPartition = xmlPartition;
	}
	public int getMemorySizeBss() {
		return MemorySizeBss;
	}
	public void setMemorySizeBss(int memorySizeBss) {
		MemorySizeBss = memorySizeBss;
	}
	public int getMemorySizeData() {
		return MemorySizeData;
	}
	public void setMemorySizeData(int memorySizeData) {
		MemorySizeData = memorySizeData;
	}
	public int getMemorySizePersistentBss() {
		return MemorySizePersistentBss;
	}
	public void setMemorySizePersistentBss(int memorySizePersistentBss) {
		MemorySizePersistentBss = memorySizePersistentBss;
	}
	public int getMemorySizePersistentData() {
		return MemorySizePersistentData;
	}
	public void setMemorySizePersistentData(int memorySizePersistentData) {
		MemorySizePersistentData = memorySizePersistentData;
	}
	public int getMemorySizeRoData() {
		return MemorySizeRoData;
	}
	public void setMemorySizeRoData(int memorySizeRoData) {
		MemorySizeRoData = memorySizeRoData;
	}
	public int getMemorySizeText() {
		return MemorySizeText;
	}
	public void setMemorySizeText(int memorySizeText) {
		MemorySizeText = memorySizeText;
	}
	public ArrayList<Port> getDaPorts() {
		return daPorts;
	}
	public void setDaPorts(ArrayList<Port> daPorts) {
		this.daPorts = daPorts;
	}
}
