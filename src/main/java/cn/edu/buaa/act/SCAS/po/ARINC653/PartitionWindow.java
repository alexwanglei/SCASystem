package cn.edu.buaa.act.SCAS.po.ARINC653;

public class PartitionWindow {
	private int id;
	private String partName;
	private String duration;
	private String releasePoint;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getReleasePoint() {
		return releasePoint;
	}

	public void setReleasePoint(String releasePoint) {
		this.releasePoint = releasePoint;
	}
}
