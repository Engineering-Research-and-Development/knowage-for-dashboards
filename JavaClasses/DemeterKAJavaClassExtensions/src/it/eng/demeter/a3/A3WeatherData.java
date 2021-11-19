package it.eng.demeter.a3;

public class A3WeatherData {
	
	private String id = "";
	private String parcelId = "";
	private String date = "";
	private double heat = 0;
	private double winter = 0;
	
	public String getId() {
		return id;
	}
	public String getParcelId() {
		return parcelId;
	}
	public String getDate() {
		return date;
	}
	public double getHeat() {
		return heat;
	}
	public double getWinter() {
		return winter;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setHeat(double heat) {
		this.heat = heat;
	}
	public void setWinter(double winter) {
		this.winter = winter;
	}
	
}
