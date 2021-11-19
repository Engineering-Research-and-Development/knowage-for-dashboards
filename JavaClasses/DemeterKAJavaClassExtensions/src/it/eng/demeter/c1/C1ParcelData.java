package it.eng.demeter.c1;

public class C1ParcelData {
	
	private String id = "";
	private String farmId = "";
	private String name = "";
	private double area = 0;
	private String cropId = "";
	private String crop = "";
	
	public String getId() {
		return id;
	}
	public String getFarmId() {
		return farmId;
	}
	public String getName() {
		return name;
	}
	public double getArea() {
		return area;
	}
	public String getCropId() {
		return cropId;
	}
	public String getCrop() {
		return crop;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public void setCropId(String cropId) {
		this.cropId = cropId;
	}
	public void setCrop(String crop) {
		this.crop = crop;
	}
	
}
