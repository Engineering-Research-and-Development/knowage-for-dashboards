package it.eng.demeter.d1;

import java.util.ArrayList;

public class D1ObservationCollection {
	
	private String id = "";
	private String machineId = "";
	private String evaluationDate = "";
	private String startDate = "";
	private String endDate = "";
	private String timeStart = "";
	private String timeEnd = "";
	private ArrayList<String> observationList = new ArrayList<String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getEvaluationDate() {
		return evaluationDate;
	}
	public void setEvaluationDate(String evaluationDate) {
		this.evaluationDate = evaluationDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public ArrayList<String> getObservationList() {
		return observationList;
	}
	public void setObservationList(ArrayList<String> observationList) {
		this.observationList = observationList;
	}
	
	

}
