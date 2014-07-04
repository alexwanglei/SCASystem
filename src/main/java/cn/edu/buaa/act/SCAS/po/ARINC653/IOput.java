package cn.edu.buaa.act.SCAS.po.ARINC653;

public class IOput {
	private int id;
	private String conceptName;
	
	private Port port=null;
	private MsgContainer msgContainer=null;

	private String type;
	private String dataType;
	
	private String connect;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}




	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Port getPort() {
		return port;
	}

	public void setPort(Port port) {
		this.port = port;
	}

	public MsgContainer getMsgContainer() {
		return msgContainer;
	}

	public void setMsgContainer(MsgContainer msgContainer) {
		this.msgContainer = msgContainer;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}
}
