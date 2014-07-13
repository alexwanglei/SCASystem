
package cn.edu.buaa.act.SCAS.po;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

/** 
 * @Description 
 * @author wanglei
 * @date 2014年7月12日 
 */   	
public class Task {
	private int id;
	
	private String name;
	
	private ArrayList<Formula> formulas = new ArrayList<Formula>();
	
	private ArrayList<Variable> inputs = new ArrayList<Variable>();
	
	private ArrayList<Variable> outputs = new ArrayList<Variable>();

	public Task(){
		
	}
	
	public Task(Element e){
		this.id = Integer.parseInt(e.attributeValue("Id"));
		this.name = e.attributeValue("Name");
		//处理formula元素
		Element formulasEle = e.element("Formulas");
		List<Element> formulaEleList = formulasEle.elements("Formula");
		for(Element f : formulaEleList){
			Formula formula = new Formula(f);
			this.formulas.add(formula);
		}
		//处理Input元素
		Element inputsEle = e.element("Inputs");
		List<Element> inputEleList = inputsEle.elements("Variable");
		for(Element v : inputEleList){
			Variable variable = new Variable(v);
			this.inputs.add(variable);
		}
		//处理Output元素
		Element outputsEle = e.element("Outputs");
		List<Element> outputEleList = outputsEle.elements("Variable");
		for(Element v : outputEleList){
			Variable variable = new Variable(v);
			this.outputs.add(variable);
		}
	}
	
	public void findIOput(HashSet<Variable> otherInputs){
		Set<Variable> formulaInputs = new HashSet<Variable>();
		Set<Variable> formulaOutputs = new HashSet<Variable>();
		for(Formula formula:formulas){
			for(Variable i:formula.getVars()){
				formulaInputs.add(i);
			}
			formulaOutputs.add(formula.getResult());
		}
		//任务的输入=任务中公式的全部输入  与  任务中公式的全部输出 的差集
		Set<Variable> result = new HashSet<Variable>();
		result.clear();
		result.addAll(formulaInputs);
		result.removeAll(formulaOutputs);
		for(Variable i : result){
			this.inputs.add(i);
		}
		
		//任务的输出 = （任务中公式的全部输出 与 任务之外的其他公式的全部输入的 交集 ） 并 （任务中公式的全部输出 与任务中公式的全部输入的 差集）
		//任务中公式的全部输出 与任务中公式的全部输入的 差集
		result.clear();
		result.addAll(formulaOutputs);
		result.removeAll(formulaInputs);
		
		//任务中公式的全部输出 与 任务之外的其他公式的全部输入的 交集
		Set<Variable> temp = new HashSet<Variable>();
		temp.clear();
		temp.addAll(formulaOutputs);
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

	public ArrayList<Formula> getFormulas() {
		return formulas;
	}

	public void setFormulas(ArrayList<Formula> formulas) {
		this.formulas = formulas;
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
