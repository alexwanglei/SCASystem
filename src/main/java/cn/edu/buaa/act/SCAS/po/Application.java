package cn.edu.buaa.act.SCAS.po;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Application {
	private int id;
	
	private String name;
	
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	private ArrayList<Variable> inputs = new ArrayList<Variable>();
	
	private ArrayList<Variable> outputs = new ArrayList<Variable>();

	public void findIOput(HashSet<Variable> otherInputs){
		Set<Variable> taskInputs = new HashSet<Variable>();
		Set<Variable> taskOutputs = new HashSet<Variable>();
		for(Task task:tasks){
			for(Variable i : task.getInputs()){
				taskInputs.add(i);
			}
			for(Variable o : task.getOutputs()){
				taskOutputs.add(o);
			}
		}
		//分区的输入=分区中任务的全部输入  与  分区中任务的全部输出 的差集
		Set<Variable> result = new HashSet<Variable>();
		result.clear();
		result.addAll(taskInputs);
		result.removeAll(taskOutputs);
		for(Variable i : result){
			this.inputs.add(i);
		}
		
		//分区的输出 = （分区中任务的全部输出 与 分区之外的其他任务的全部输入的 交集 ） 并 （分区中任务的全部输出 与分区中任务的全部输入的 差集）
		//分区中任务的全部输出 与分区中任务的全部输入的 差集
		result.clear();
		result.addAll(taskOutputs);
		result.removeAll(taskInputs);
		
		//分区中任务的全部输出 与 分区之外的其他任务的全部输入的 交集
		Set<Variable> temp = new HashSet<Variable>();
		temp.clear();
		temp.addAll(taskOutputs);
		temp.retainAll(otherInputs);
		
		result.addAll(temp);
		
		for(Variable o : result){
			this.outputs.add(o);
		}
		
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

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public ArrayList<Variable> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<Variable> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<Variable> getOutputs() {
		return outputs;
	}

	public void setOutputs(ArrayList<Variable> outputs) {
		this.outputs = outputs;
	}
	
	
}
