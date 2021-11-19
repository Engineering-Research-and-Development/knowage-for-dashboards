package it.eng.demeter.a3;

public class A3ParcelRecord {
	
	private String id = "";
	private String parcelId = "";
	private double soilWater = 0;
	private double relativeHumidity = 0;
	private double leafWetness = 0;
	private double windSpeed = 0;
	private String result = "";
	private String date = "";
	private String irrigation = "";
	private String airTemp = "";
	private double maxAirTemp = 0;
	private double minAirTemp = 0;
	private double maxSoilTemp = 0;
	private String soilTemp = "";
	private double avgAirTemp = 0;
	private double rainfall = 0;
	
	public String getId() {
		return id;
	}
	public String getParcelId() {
		return parcelId;
	}
	public double getSoilWater() {
		return soilWater;
	}
	public double getRelativeHumidity() {
		return relativeHumidity;
	}
	public double getLeafWetness() {
		return leafWetness;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public String getResult() {
		return result;
	}
	public String getDate() {
		return date;
	}
	public String getIrrigation() {
		return irrigation;
	}
	public String getAirTemp() {
		return airTemp;
	}
	public double getMaxAirTemp() {
		return maxAirTemp;
	}
	public double getMinAirTemp() {
		return minAirTemp;
	}
	public double getMaxSoilTemp() {
		return maxSoilTemp;
	}
	public String getSoilTemp() {
		return soilTemp;
	}
	public double getAvgAirTemp() {
		return avgAirTemp;
	}
	public double getRainfall() {
		return rainfall;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	public void setSoilWater(double soilWater) {
		this.soilWater = soilWater;
	}
	public void setRelativeHumidity(double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public void setLeafWetness(double leafWetness) {
		this.leafWetness = leafWetness;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setIrrigation(String irrigation) {
		this.irrigation = irrigation;
	}
	public void setAirTemp(String airTemp) {
		this.airTemp = airTemp;
	}
	public void setMaxAirTemp(double maxAirTemp) {
		this.maxAirTemp = maxAirTemp;
	}
	public void setMinAirTemp(double minAirTemp) {
		this.minAirTemp = minAirTemp;
	}
	public void setMaxSoilTemp(double maxSoilTemp) {
		this.maxSoilTemp = maxSoilTemp;
	}
	public void setSoilTemp(String soilTemp) {
		this.soilTemp = soilTemp;
	}
	public void setAvgAirTemp(double avgAirTemp) {
		this.avgAirTemp = avgAirTemp;
	}
	public void setRainfall(double rainfall) {
		this.rainfall = rainfall;
	}
	
}
