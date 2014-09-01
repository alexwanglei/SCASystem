package cn.edu.buaa.act.SCAS.po;

public class News {
	private int id;
	
	private String title;
	
	private String slug;
	
	private String text;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toString(){
		final String tab = ";";

		String retValue = "";

		retValue = "News ( " +  "id = " + this.id + tab + "title = "
				+ this.title + tab + "slug = " + this.slug + tab + "text = "
				+ this.text
				+ " )";

		return retValue;
	}
	
}
