package cn.edu.buaa.act.SCAS.po.ARINC653;

public class Semaphore {
	private int id;
	
	private String name;
	
	private int currentValue;
	
	private int maxValue;
	
	private String queuingDiscipline;

	
	public String genLocalId(){
		return "LOCAL SEMAPHORE_ID_TYPE semaphore_"+this.id+";\n";
	}
	
	public String createSemaphore(){
		StringBuffer code = new StringBuffer();
		code.append("	CREATE_SEMAPHORE (\""+this.getName()+"\", "+this.currentValue+", "+this.maxValue+", "+this.queuingDiscipline+", &semaphore_"+this.getId()+", &retCode);\n");
		code.append("	if (retCode != NO_ERROR)\n" +
				"		printf (\"CREATE_SEMAPHORE: can't create a semaphore (%s)\\n\", codeToStr(retCode));\n" +
				"	else\n" +
				"		printf (\"CREATE_SEMAPHORE: "+this.getName()+" created\\n\");\n");
		return code.toString();
	}
	
	public String waitSemaphore(){
		StringBuffer code = new StringBuffer();
		code.append("		WAIT_SEMAPHORE (semaphore_"+this.getId()+", INFINITE_TIME_VALUE, &retCode);\n"
				+ "        CHECK_CODE (\"WAIT_SEMAPHORE\", retCode);\n");
		return code.toString();
	}
	
	public String signalSemaphore(){
		StringBuffer code = new StringBuffer();
		code.append("		SIGNAL_SEMAPHORE (semaphore_"+this.getId()+", &retCode);\n"
				+ "        CHECK_CODE (\"SIGNAL_SEMAPHORE\", retCode);\n");
		return code.toString();
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

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public String getQueuingDiscipline() {
		return queuingDiscipline;
	}

	public void setQueuingDiscipline(String queuingDiscipline) {
		this.queuingDiscipline = queuingDiscipline;
	}


	
}
