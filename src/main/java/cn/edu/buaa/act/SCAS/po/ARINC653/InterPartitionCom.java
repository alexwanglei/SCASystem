package cn.edu.buaa.act.SCAS.po.ARINC653;

public class InterPartitionCom {
	
	private String srcPartition;
	private String dstPartition;
	private String mode;
	private String concept;
	
	private String srcPort;
	private String dstPort;
	

	public String getSrcPartition() {
		return srcPartition;
	}
	public void setSrcPartition(String srcPartition) {
		this.srcPartition = srcPartition;
	}
	public String getDstPartition() {
		return dstPartition;
	}
	public void setDstPartition(String dstPartition) {
		this.dstPartition = dstPartition;
	}
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	public String getDstPort() {
		return dstPort;
	}
	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
