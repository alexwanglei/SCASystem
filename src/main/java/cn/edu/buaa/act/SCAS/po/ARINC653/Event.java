package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

public class Event {
	private int id;
	
	private String name;
	
	private String setEventProcess;
	
	private String resetEventProcess;
	
	private ArrayList<String> waitEventProcesses = new ArrayList<String>();

	public String genLocalId(){
		return "LOCAL EVENT_ID_TYPE event_"+this.id+";\n";
	}
	
	public String createEvent(){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_EVENT (\""+this.getName()+"\", "+", &event_"+this.getId()+", &retCode);\n");
		code.append("	if (retCode != NO_ERROR)\n" +
				"		printf (\"CREATE_EVENT: can't create a event (%s)\\n\", codeToStr(retCode));\n" +
				"	else\n" +
				"		printf (\"CREATE_EVENT: "+this.getName()+" created\\n\");\n");
		return code.toString();
	}
	
	public String setEvent(){
		StringBuffer code = new StringBuffer();
		code.append("		SET_EVENT (event_"+this.getId()+", &retCode);\n"
				+ "        CHECK_CODE (\"SET_EVENT\", retCode);\n");
		return code.toString();
	}
	
	public String resetEvent(){
		StringBuffer code = new StringBuffer();
		code.append("		RESET_EVENT (event_"+this.getId()+", &retCode);\n"
				+ "        CHECK_CODE (\"RESET_EVENT\", retCode);\n");
		return code.toString();
	}
	
	public String waitEvent(){
		StringBuffer code = new StringBuffer();
		code.append("		WAIT_EVENT (event_"+this.getId()+", INFINITE_TIME_VALUE, &retCode);\n"
				+ "        CHECK_CODE (\"WAIT_EVENT\", retCode);\n");
		return code.toString();
	}
	
	public String getWEProcessString(){
		StringBuffer sb = new StringBuffer();
		int i;
		for(i=0; i<this.waitEventProcesses.size()-1;i++){
			sb.append(this.waitEventProcesses.get(i)+",");
		}
		sb.append(this.waitEventProcesses.get(i));
		return sb.toString();		
	}
	
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

	public String getSetEventProcess() {
		return setEventProcess;
	}

	public void setSetEventProcess(String setEventProcess) {
		this.setEventProcess = setEventProcess;
	}

	public String getResetEventProcess() {
		return resetEventProcess;
	}

	public void setResetEventProcess(String resetEventProcess) {
		this.resetEventProcess = resetEventProcess;
	}

	public ArrayList<String> getWaitEventProcesses() {
		return waitEventProcesses;
	}

	public void setWaitEventProcesses(ArrayList<String> waitEventProcesses) {
		this.waitEventProcesses = waitEventProcesses;
	}
}
