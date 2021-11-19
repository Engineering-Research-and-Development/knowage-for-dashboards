package it.eng.demeter.c1;

public class C1ParcelRecord {
	
	private String id = "";
	private String date = "";
	private String parcelId = "";
	private double nitrogenLevel = 0;
	private double totalAffectedArea = 0;
	private String treatmentDescription = "";
	
	public String getId() {
		return id;
	}
	public String getDate() {
		return date;
	}
	public String getParcelId() {
		return parcelId;
	}
	public double getNitrogenLevel() {
		return nitrogenLevel;
	}
	public double getTotalAffectedArea() {
		return totalAffectedArea;
	}
	public String getTreatmentDescription() {
		return treatmentDescription;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	public void setNitrogenLevel(double nitrogenLevel) {
		this.nitrogenLevel = nitrogenLevel;
	}
	public void setTotalAffectedArea(double totalAffectedArea) {
		this.totalAffectedArea = totalAffectedArea;
	}
	public void setTreatmentDescription(String treatmentDescription) {
		this.treatmentDescription = treatmentDescription;
	}
	
}
