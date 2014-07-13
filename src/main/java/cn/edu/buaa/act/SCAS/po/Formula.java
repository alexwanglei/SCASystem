package cn.edu.buaa.act.SCAS.po;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class Formula {
	private int id;
	
	private String name;
	
	private ArrayList<Variable> vars = new ArrayList<Variable>();
	
	private Variable result;
	
	private String description;

	public Formula(){
		
	}
	
	public Formula(Element e){
		this.id = Integer.parseInt(e.attributeValue("Id"));
		this.name = e.attributeValue("Name");
		List<Element> variableList = e.elements("Variable");
		for(Element var: variableList){
			Variable variable = new Variable(var);
			this.vars.add(variable);
		}
		Element resultEle = e.element("Result");
		Variable result = new Variable(resultEle);
		this.result = result;
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
