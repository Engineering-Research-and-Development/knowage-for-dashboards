package it.eng.demeter.d1;

public class D1DSS2DatasetRecord {
	
	private String machineId = "";
	private String dateEvaluation = "";
	private String dateStart = "";
	private String dateEnd = "";
	private String timeStart = "";
	private String timeEnd = "";
	private String measure = "";
	private String value = "0";
	private String unit = "-";
	private String uniqueId = "";
	private String filterForDateTable = ""; //true or false
	private String type = ""; // refer to property "hasDescription"
	private String chartName = ""; // refer to property "hasName"

	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
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
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
}
