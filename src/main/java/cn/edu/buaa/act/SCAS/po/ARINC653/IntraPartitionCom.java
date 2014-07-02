package cn.edu.buaa.act.SCAS.po.ARINC653;

public class IntraPartitionCom {
	private String srcTask;
	private String dstTask;
	private String conceptName;
	private String dataType;
	private MsgContainer msgContainer;
	
	
	public void setSrcTask(String srcTask) {
		this.srcTask = srcTask;
	}
	public String getSrcTask() {
		return srcTask;
	}
	public void setDstTask(String dstTask) {
		this.dstTask = dstTask;
	}
	public String getDstTask() {
		return dstTask;
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	public String getConceptName() {
		return conceptName;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setMsgContainer(MsgContainer msgContainer) {
		this.msgContainer = msgContainer;
	}
	public MsgContainer getMsgContainer() {
		return msgContainer;
	}
}
