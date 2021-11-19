package it.eng.demeter.c1;

public class C1Observation {
	
	private String id = "";
	private String date = "";
	private String weatherForecastId = "";
	private String result = "";
	
	public String getId() {
		return id;
	}
	public String getDate() {
		return date;
	}
	public String getWeatherForecastId() {
		return weatherForecastId;
	}
	public String getResult() {
		return result;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setWeatherForecastId(String weatherForecastId) {
		this.weatherForecastId = weatherForecastId;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
