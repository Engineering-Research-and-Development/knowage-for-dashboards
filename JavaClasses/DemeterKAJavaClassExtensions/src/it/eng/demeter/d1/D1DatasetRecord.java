package it.eng.demeter.d1;

public class D1DatasetRecord {
	
	private String machineId = "";
	private String dateEvaluation = "";
	private String dateStart = "";
	private String dateEnd = "";
	private String timeStart = "";
	private String timeEnd = "";
	private String sensor = "";
	private String status = "0";
	private String error = "-";
	private String errorDetail = "-";
	private String note = "-";
	private String uniqueId = "";
	private String filterForDateTable = ""; //true or false
	
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getFilterForDateTable() {
		return filterForDateTable;
	}
	public void setFilterForDateTable(String filterForDateTable) {
		this.filterForDateTable = filterForDateTable;
	}
	public String getDateEvaluation() {
		return dateEvaluation;
	}
	public void setDateEvaluation(String dateEvaluation) {
		this.dateEvaluation = dateEvaluation;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
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
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

}
