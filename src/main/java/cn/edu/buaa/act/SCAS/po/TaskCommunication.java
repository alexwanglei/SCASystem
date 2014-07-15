package cn.edu.buaa.act.SCAS.po;

public class TaskCommunication {
	private Variable variable;
	
	private Task srcTask;
	
	private Task dstTask;

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public Task getSrcTask() {
		return srcTask;
	}

	public void setSrcTask(Task srcTask) {
		this.srcTask = srcTask;
	}

	public Task getDstTask() {
		return dstTask;
	}

	public void setDstTask(Task dstTask) {
		this.dstTask = dstTask;
	}
	
	public String toString(){
		final String tab = ";";

		String retValue = "";

		retValue = "ResourceListener ( " +  "variable = " + this.variable.getName() + tab + "srcTask = "
				+ this.srcTask.getName() + tab + "dstTask = " + this.dstTask.getName() 
				+ " )";

		return retValue;
	}
}
