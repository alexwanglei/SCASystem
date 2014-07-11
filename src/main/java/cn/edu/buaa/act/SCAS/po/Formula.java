package cn.edu.buaa.act.SCAS.po;

import java.util.ArrayList;

public class Formula {
	private int id;
	
	private String name;
	
	private ArrayList<Variable> vars = new ArrayList<Variable>();
	
	private Variable result;
	
	private String description;

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

	public ArrayList<Variable> getVars() {
		return vars;
	}

	public void setVars(ArrayList<Variable> vars) {
		this.vars = vars;
	}

	public Variable getResult() {
		return result;
	}

	public void setResult(Variable result) {
		this.result = result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString(){
		final String tab = ";";

		String retValue = "";

		retValue = "ResourceListener ( " +  "id = " + this.id + tab + "name = "
				+ this.name 
				+ " )";

		return retValue;
	}
}
