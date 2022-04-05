package it.eng.demeter.a1;

public class A1TrainingDataRecord {
	private String r2Training = "";
	private String r2Validation = "";
	private String modelName = "";
	private String modelVersion = "";
	private String runId = "";
	
	public String getR2Training() {
		return r2Training;
	}
	public void setR2Training(String r2Training) {
		this.r2Training = r2Training;
	}
	public String getR2Validation() {
		return r2Validation;
	}
	public void setR2Validation(String r2Validation) {
		this.r2Validation = r2Validation;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
	public String getRunId() {
		return runId;
	}
	public void setRunId(String runId) {
		this.runId = runId;
	}
}
