package cn.edu.buaa.act.SCAS.po.ARINC653;

public class IOput {
	private int id;
	private String name;
	
	private Port port;
	private MsgContainer msgContainer;

	private String type;
	private String dataType;
	
	public IOput(int id , String name,String dataType,  String type){
		this.id = id;
		this.name = name;

		this.type = type;
		this.dataType = dataType;
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
}
