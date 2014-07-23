package cn.edu.buaa.act.SCAS.po;

public class DirectAccessPort {
	private int id;
	
	private String name;
	
	private String direction;
	
	private String type;
	
	private String partitionName;
	
	private String appPortName;
	
	private String variableName;
	
	public DirectAccessPort(int id, String name, String direction, String type, String partitionName, String appPortName, String variableName){
		this.name = name;
		this.direction = direction;
		this.type = type;
		this.partitionName = partitionName;
		this.appPortName = appPortName;
		this.variableName= variableName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppPortName() {
		return appPortName;
	}

	public void setAppPortName(String appPortName) {
		this.appPortName = appPortName;
	}

	public String getPartitionName() {
		return partitionName;
	}

	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	
}
