package cn.edu.buaa.act.SCAS.po.ARINC653;

import org.dom4j.Element;

public class Blackboard extends MsgContainer{
	
	public String createMsgContainer(){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_BLACKBOARD (\""+super.getName()+"\", "+super.getMessageSize()+", &blackboard_"+super.getId()+", &retCode);\n");
		code.append("	if (retCode != NO_ERROR)\n" +
				"		printf (\"CREATE_BLACKBOARD: can't create a blackboard (%s)\\n\", codeToStr(retCode));\n" +
				"	else\n" +
				"		printf (\"CREATE_BLACKBOARD: "+super.getName()+" created\\n\");\n");
		return code.toString();
	}
	
	public String genLocalId(){
		return "LOCAL BLACKBOARD_ID_TYPE blackboard_"+super.getId()+";\n";
	}
	
	public String revContainerMsg(IOput i, int n){
		StringBuffer code = new StringBuffer();
		code.append("		READ_BLACKBOARD(blackboard_"+super.getId()+", INFINITE_TIME_VALUE, (MESSAGE_ADDR_TYPE)&"+i.getConceptName().replaceAll(" ", "")+", &lendata"+n+", &retCode);\n" +
		"		CHECK_CODE (\"READ_BLACKBOARD\", retCode);\n");
		return code.toString();
	}
	
	public String sendContainerMsg(IOput o, int n){
		StringBuffer code = new StringBuffer();
		code.append("		DISPLAY_BLACKBOARD(blackboard_"+super.getId()+", (MESSAGE_ADDR_TYPE)&"+o.getConceptName().replaceAll(" ", "")+", lenMsgData"+n+", &retCode);\n" +
		"		CHECK_CODE (\"DISPLAY_BLACKBOARD\", retCode);\n");
		return code.toString();
	}
	
	public void genMsgContainerEle(Element parent){
		Element backboardEle = parent.addElement("Blackboard");
		backboardEle.addAttribute("ID", Integer.toString(super.getId()));
		backboardEle.addAttribute("Name", super.getName());
		backboardEle.addAttribute("messageSize", Integer.toString(super.getMessageSize()));
	}
}
