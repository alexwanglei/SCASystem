package cn.edu.buaa.act.SCAS.po.ARINC653;

import org.dom4j.Element;

public abstract class Port {
	private int id;
	
	private String name;
	
	private String direction;
	
	private int messageSize;
	
	
	
public abstract String createPort();
	
	public abstract String genLocalID();
	
	public abstract String revPortMsg(IOput i, int n);
	
	public abstract String sendPortMsg(IOput i, int n);
	//生成配置文件中端口xml元素的方法
	public abstract void genPortXML(Element portsEle);
	//生成SA模型文件中端口xml元素的方法
	public abstract void genPortEle(Element parent);
	
	
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}
}
