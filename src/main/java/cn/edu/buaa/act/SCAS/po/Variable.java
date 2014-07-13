package cn.edu.buaa.act.SCAS.po;

import org.dom4j.Element;

public class Variable {
	private String name;
	
	private String type;
	
	private String unit;

	public Variable(){
		
	}
	
	public Variable(Element e){
		this.name = e.attributeValue("Name");
		this.type = e.attributeValue("Type");
		if(e.attributeValue("Unit")!=null){
			this.unit = e.attributeValue("Unit");
		}
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		else if(o==this){
			return true;
		}
		else if(this.getClass() != o.getClass()){
			return false;
		}
		Variable var = (Variable) o;
		return this.name.equals(var.getName());
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode();
	}
}
