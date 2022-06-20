package it.eng.demeter.pilot5_2UC2;

public class UC2Observation {
	private String id = "";
	private String order = "";
	private String hasFeatureOfInterest = "";
	private String hasSimpleResult = "";
	private String resultTime = "";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getHasFeatureOfInterest() {
		return hasFeatureOfInterest;
	}
	public void setHasFeatureOfInterest(String hasFeatureOfInterest) {
		this.hasFeatureOfInterest = hasFeatureOfInterest;
	}
	public String getHasSimpleResult() {
		return hasSimpleResult;
	}
	public void setHasSimpleResult(String hasSimpleResult) {
		this.hasSimpleResult = hasSimpleResult;
	}
	public String getResultTime() {
		return resultTime;
	}
	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}
	
}
