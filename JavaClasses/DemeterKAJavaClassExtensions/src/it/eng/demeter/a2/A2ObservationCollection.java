package it.eng.demeter.a2;

import java.util.ArrayList;

public class A2ObservationCollection {
	
	private String id = "";
	private String featureId = "";
	private String date = "";
	private ArrayList<String> observationId = new ArrayList<String>();
	
	public String getId() {
		return id;
	}
	
	public String getFeatureId() {
		return featureId;
	}
	
	public String getDate() {
		return date;
	}
	
	public ArrayList<String> getObservationId() {
		return observationId;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setObservationId(ArrayList<String> observationId) {
		this.observationId = observationId;
	}
	
	public void addObservationId(String observationId) {
		this.observationId.add(observationId);
	}
		
}
