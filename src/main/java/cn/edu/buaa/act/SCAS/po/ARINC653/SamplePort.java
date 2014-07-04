package cn.edu.buaa.act.SCAS.po.ARINC653;

import org.dom4j.Element;

public class SamplePort extends Port{
	private double refreshPeriod;
	
	public String createPort(){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_SAMPLING_PORT (\""+super.getName() + "\", "+ super.getMessageSize()+ ", "+super.getDirection()+", (SYSTEM_TIME_TYPE)"+(long)(this.refreshPeriod*(long)1000000000)+"ll, &samplePort_"+super.getId() +", &retCode);\n");
		code.append("	if(retCode != NO_ERROR)\n" +
				"		printf(\"CREATE_SAMPLING_PORT: can't create "+super.getName()+" (%s)\\n\", codeToStr(retCode));\n");
		return code.toString();
	}
	public String genLocalID(){
		return "LOCAL SAMPLING_PORT_ID_TYPE samplePort_"+super.getId()+";\n";
	}
	public String revPortMsg(IOput i, int n){
		StringBuffer code = new StringBuffer();
		int num = i.getId();
		code.append("		READ_SAMPLING_MESSAGE(samplePort_"+super.getId()+", (MESSAGE_ADDR_TYPE)&"+i.getConceptName().replaceAll(" ", "")+", &lendata"+n+", &validity"+num+", &retCode);\n" +
		"		CHECK_CODE(\"READ_SAMPLING_MESSAGE\",retCode);\n");
		return code.toString();
	}
	
	public String sendPortMsg(IOput o, int n){
		StringBuffer code = new StringBuffer();
		code.append("		 WRITE_SAMPLING_MESSAGE(samplePort_"+super.getId()+", (MESSAGE_ADDR_TYPE)&"+o.getConceptName().replaceAll(" ", "")+", lenMsgData"+n+", &retCode);\n" +
		"		CHECK_CODE (\"WRITE_SAMPLING_MESSAGE\", retCode);\n");
		return code.toString();
	}
	
	public void genPortXML(Element portsEle){
		Element samplingPortEle = portsEle.addElement("SamplingPort");
		samplingPortEle.addAttribute("Name", super.getName());
		samplingPortEle.addAttribute("Direction", super.getDirection());
		samplingPortEle.addAttribute("MessageSize", Integer.toString(super.getMessageSize()));
		samplingPortEle.addAttribute("RefreshRate", Double.toString(this.refreshPeriod));
	}
	
	public void genPortEle(Element parent){
		Element samplePortEle = parent.addElement("SamplePort");
		samplePortEle.addAttribute("ID", Integer.toString(super.getId()));
		samplePortEle.addAttribute("Name",super.getName());
		samplePortEle.addAttribute("Direction", super.getDirection());
		samplePortEle.addAttribute("MessageSize", Integer.toString(super.getMessageSize()));
		samplePortEle.addAttribute("RefreshPeriod", Double.toString(this.refreshPeriod));

	}
	
	public double getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(double refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}
}
