package it.eng.demeter.a3;

public class A3Zone {
	
	private String id = "";
	private String code = "";
	private String parcelRecordId ="";
	private String wkt = "";
	private String geometryId = "";
	
	public String getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getParcelRecordId() {
		return parcelRecordId;
	}
	public String getWkt() {
		return wkt;
	}
	public String getGeometryId() {
		return geometryId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setParcelRecordId(String parcelRecordId) {
		this.parcelRecordId = parcelRecordId;
	}
	public void setWkt(String wkt) {
		this.wkt = wkt;
	}
	public void setGeometryId(String geometryId) {
		this.geometryId = geometryId;
	}
	
}
