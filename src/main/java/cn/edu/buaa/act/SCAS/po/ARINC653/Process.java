package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

public class Process {
	
	private int id;
	
	private String name;
	
	private int stack;
	
	private int priorty;
	
	private double period;
	
	private double capacity;
	
	private String deadline;

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

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack;
	}

	public int getPriorty() {
		return priorty;
	}

	public void setPriorty(int priorty) {
		this.priorty = priorty;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
//	private ArrayList<IOput> inputs = new ArrayList<IOput>();
//	
//	private ArrayList<IOput> outputs = new ArrayList<IOput>();
}
