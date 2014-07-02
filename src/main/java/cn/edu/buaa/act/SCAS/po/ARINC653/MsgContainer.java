package cn.edu.buaa.act.SCAS.po.ARINC653;

import org.dom4j.Element;

public abstract class MsgContainer {
	private int id;
	
	private String name;
	
	private int messageSize;
	
	public abstract String createMsgContainer();
	public abstract String genLocalId();
	public abstract String revContainerMsg(IOput i, int n);
	public abstract String sendContainerMsg(IOput o, int n);
	public abstract void genMsgContainerEle(Element parent);
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public int getMessageSize() {
		return messageSize;
	}
}
