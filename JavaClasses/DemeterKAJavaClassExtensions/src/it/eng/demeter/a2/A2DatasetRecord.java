package it.eng.demeter.a2;

public class A2DatasetRecord {
	
	private String parcelId = "";
	private String date = "";
	private String doy = "0";
	private String bbch = "0";
	private String gdd = "0";
	private String description = "";
	
	public String getParcelId() {
		return parcelId;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getDoy() {
		return doy;
	}
	
	public String getBbch() {
		return bbch;
	}
	
	public String getGdd() {
		return gdd;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setDoy(String doy) {
		this.doy = doy;
	}
	
	public void setBbch(String bbch) {
		this.bbch = bbch;
	}
	
	public void setGdd(String gdd) {
		this.gdd = gdd;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
		
}