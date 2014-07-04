package cn.edu.buaa.act.SCAS.po.ARINC653;

import org.dom4j.Element;

public class QueuePort extends Port{
	private int queueLength;
	private String protocol;
	private String discipline;
	
	public String createPort(){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_QUEUING_PORT (\""+super.getName() + "\", "+super.getMessageSize()+", "+this.queueLength+", "+super.getDirection()+", "+this.discipline+", &queuePort_"+super.getId()+", &retCode );\n");
		code.append("	if(retCode != NO_ERROR)\n" +
				"		printf(\"CREATE_QUEUING_PORT: can't create "+super.getName()+" (%s)\\n\", codeToStr(retCode));\n");
		return code.toString();
	}
	public String genLocalID(){
		return "LOCAL QUEUING_PORT_ID_TYPE queuePort_"+super.getId()+";\n";
	}
	public String revPortMsg(IOput i, int n){
		StringBuffer code = new StringBuffer();
		code.append("		RECEIVE_QUEUING_MESSAGE(queuePort_"+super.getId()+", INFINITE_TIME_VALUE, (MESSAGE_ADDR_TYPE)&"+i.getConceptName().replaceAll(" ", "")+", &lendata"+n+", &retCode);\n" +
		"		CHECK_CODE(\"READ_QUEUING_MESSAGE\", retCode);\n");
		return code.toString();
	}
	
	public String sendPortMsg(IOput o, int n){
		StringBuffer code = new StringBuffer();
		code.append("		SEND_QUEUING_MESSAGE(queuePort_"+super.getId()+", (MESSAGE_ADDR_TYPE)&"+o.getConceptName().replaceAll(" ", "")+", lenMsgData"+n+", INFINITE_TIME_VALUE, &retCode);\n" +
		"		CHECK_CODE (\"WRITE_QUEUING_MESSAGE\", retCode);\n");
		return code.toString();
	}
	
	public void genPortXML(Element portsEle){
		Element queuingPortEle = portsEle.addElement("QueuingPort");
		queuingPortEle.addAttribute("Name", super.getName());
		queuingPortEle.addAttribute("Direction", super.getDirection());
		queuingPortEle.addAttribute("MessageSize", Integer.toString(super.getMessageSize()));
		queuingPortEle.addAttribute("QueueLength", Integer.toString(this.queueLength));
		queuingPortEle.addAttribute("Protocol", this.protocol);
	}
	
	public void genPortEle(Element parent){
		Element queuePortEle = parent.addElement("QueuePort");
		queuePortEle.addAttribute("ID", Integer.toString(super.getId()));
		queuePortEle.addAttribute("Name",super.getName());
		queuePortEle.addAttribute("Direction", super.getDirection());
		queuePortEle.addAttribute("MessageSize", Integer.toString(super.getMessageSize()));
		queuePortEle.addAttribute("QueueLength", Integer.toString(this.queueLength));
		queuePortEle.addAttribute("Protocol", this.protocol);
		queuePortEle.addAttribute("discipline", this.discipline);
	}

	public int getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
}
