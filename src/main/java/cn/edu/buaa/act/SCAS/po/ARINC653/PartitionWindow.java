package cn.edu.buaa.act.SCAS.po.ARINC653;

public class PartitionWindow {
	private int id;
	private String partName;
	private double duration;
	private double releasePoint;
	
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

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getReleasePoint() {
		return releasePoint;
	}

	public void setReleasePoint(double releasePoint) {
		this.releasePoint = releasePoint;
	}
}
