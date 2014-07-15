package cn.edu.buaa.act.SCAS.po;

public class AppCommunication {
	
	private Variable variable;
	
	private Application srcApp;
	
	private Application dstApp;

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public Application getSrcApp() {
		return srcApp;
	}

	public void setSrcApp(Application srcApp) {
		this.srcApp = srcApp;
	}

	public Application getDstApp() {
		return dstApp;
	}

	public void setDstApp(Application dstApp) {
		this.dstApp = dstApp;
	}
	
	public String toString(){
		final String tab = ";";

		String retValue = "";

		retValue = "ResourceListener ( " +  "variable = " + this.variable.getName() + tab + "srcApp = "
				+ this.srcApp.getName() + tab + "dstApp = " + this.dstApp.getName() 
				+ " )";

		return retValue;
	}
}
