package cn.edu.buaa.act.SCAS.po.ARINC653;

import org.dom4j.Element;

public class Buffer extends MsgContainer{
	private int bufferLength;
	
	private String discipline;
	
	public String createMsgContainer(){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_BUFFER (\""+super.getName()+"\", "+super.getMessageSize()+", "+this.bufferLength+", "+this.discipline+", &buffer_"+super.getId()+", &retCode);\n");
		code.append("	if (retCode != NO_ERROR)\n" +
				"		printf (\"CREATE_BUFFER: can't create a buffer (%s)\\n\", codeToStr(retCode));\n" +
				"	else\n" +
				"		printf (\"CREATE_BUFFER: "+super.getName()+" created\\n\");\n");
		return code.toString();
	}
	
	public String genLocalId(){
		return "LOCAL BUFFER_ID_TYPE buffer_"+super.getId()+";\n";
	}

	public String revContainerMsg(IOput i, int n){
		StringBuffer code = new StringBuffer();
		code.append("		RECEIVE_BUFFER(buffer_"+super.getId()+", INFINITE_TIME_VALUE, (MESSAGE_ADDR_TYPE)&"+i.getConceptName().replaceAll(" ", "")+", &lendata"+n+", &retCode);\n" +
		"		CHECK_CODE (\"RECEIVE_BUFFER\", retCode);\n");
		return code.toString();
	}
	
	public String sendContainerMsg(IOput o, int n){
		StringBuffer code = new StringBuffer();
		code.append("		SEND_BUFFER(buffer_"+super.getId()+", (MESSAGE_ADDR_TYPE)&"+o.getConceptName().replaceAll(" ", "")+", lenMsgData"+n+",  INFINITE_TIME_VALUE, &retCode);\n" +
		"		CHECK_CODE (\"SEND_BUFFER\", retCode);\n");
		return code.toString();
	}
	
	public void genMsgContainerEle(Element parent){
		Element bufferEle = parent.addElement("Buffer");
		bufferEle.addAttribute("ID", Integer.toString(super.getId()));
		bufferEle.addAttribute("Name", super.getName());
		bufferEle.addAttribute("messageSize", Integer.toString(super.getMessageSize()));
		bufferEle.addAttribute("BufferLength", Integer.toString(this.bufferLength));
		bufferEle.addAttribute("Discipline", this.discipline);
	}
	
	public void setBufferLength(int bufferLength) {
		this.bufferLength = bufferLength;
	}

	public int getBufferLength() {
		return bufferLength;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	public String getDiscipline() {
		return discipline;
	}
}
