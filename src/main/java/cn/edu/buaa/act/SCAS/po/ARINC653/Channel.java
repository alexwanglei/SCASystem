package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

public class Channel {
	int id;
	
	String srcPartition;
	
	String srcPort;

	ArrayList<String> dstPartitions = new ArrayList<String>();
	
	ArrayList<String> dstPorts = new ArrayList<String>();
	
	public Channel(int id, String srcPartition, String srcPort){
		this.id = id;
		this.srcPartition = srcPartition;
		this.srcPort = srcPort;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSrcPartition() {
		return srcPartition;
	}

	public void setSrcPartition(String srcPartition) {
		this.srcPartition = srcPartition;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public ArrayList<String> getDstPartitions() {
		return dstPartitions;
	}

	public void setDstPartitions(ArrayList<String> dstPartitions) {
		this.dstPartitions = dstPartitions;
	}

	public ArrayList<String> getDstPorts() {
		return dstPorts;
	}

	public void setDstPorts(ArrayList<String> dstPorts) {
		this.dstPorts = dstPorts;
	}
}
