package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

public class Module {
	private int id;
	
	private String name;
	
	private ArrayList<Partition> partitions = new ArrayList<Partition>();
	
	private ArrayList<InterPartitionCom> interCom = new ArrayList<InterPartitionCom>();
	
	private ArrayList<Channel> channels = new ArrayList<Channel>();
	
	private ArrayList<PartitionWindow> schedule = new ArrayList<PartitionWindow>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(ArrayList<Partition> partitions) {
		this.partitions = partitions;
	}

	public ArrayList<InterPartitionCom> getInterCom() {
		return interCom;
	}

	public void setInterCom(ArrayList<InterPartitionCom> interCom) {
		this.interCom = interCom;
	}

	public ArrayList<Channel> getChannels() {
		return channels;
	}

	public void setChannels(ArrayList<Channel> channels) {
		this.channels = channels;
	}

	public ArrayList<PartitionWindow> getSchedule() {
		return schedule;
	}

	public void setSchedule(ArrayList<PartitionWindow> schedule) {
		this.schedule = schedule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
