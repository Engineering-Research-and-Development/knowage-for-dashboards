package it.eng.demeter.c1;

public class C1WeatherForecast {
	
	private String id = "";
	private String parcelId = "";
	private String dateValidFrom = "";
	private String dateValidThrough = "";
	private String weatherType = "";
	private double temperature = 0;
	private double windSpeed = 0;
	private double precipitation = 0;
	
	public String getId() {
		return id;
	}
	public String getParcelId() {
		return parcelId;
	}
	public String getDateValidFrom() {
		return dateValidFrom;
	}
	public String getDateValidThrough() {
		return dateValidThrough;
	}
	public String getWeatherType() {
		return weatherType;
	}
	public double getTemperature() {
		return temperature;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public double getPrecipitation() {
		return precipitation;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	public void setDateValidFrom(String dateValidFrom) {
		this.dateValidFrom = dateValidFrom;
	}
	public void setDateValidThrough(String dateValidThrough) {
		this.dateValidThrough = dateValidThrough;
	}
	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}
	
}
