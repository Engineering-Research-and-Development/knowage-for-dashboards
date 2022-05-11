package it.eng.demeter.e1;

public class E1DatasetRecord {
	String trapId = "";
	String type = "";
	String date = "";
	String sterileFlies = "0";
	String nonSterileFlies = "0";
	
	public String getTrapId() {
		return trapId;
	}
	public void setTrapId(String trapId) {
		this.trapId = trapId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSterileFlies() {
		return sterileFlies;
	}
	public void setSterileFlies(String sterileFlies) {
		this.sterileFlies = sterileFlies;
	}
	public String getNonSterileFlies() {
		return nonSterileFlies;
	}
	public void setNonSterileFlies(String nonSterileFlies) {
		this.nonSterileFlies = nonSterileFlies;
	}
}
