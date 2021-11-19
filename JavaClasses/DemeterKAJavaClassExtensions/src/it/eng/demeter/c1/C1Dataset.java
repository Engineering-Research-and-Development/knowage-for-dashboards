package it.eng.demeter.c1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class C1Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
	private String url = null;
	
	/*String url = "";
	private static final Logger logger = LogManager.getLogger(C1Dataset.class);
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String profileRole = (String) profile.get("user_roles");
		String ds = null;
		
		try {
			url = parameters.get("URL").toString();
			if (profileRole.contains("_")){
				parameters.forEach((key, value) -> {
					if (!((String) key).equalsIgnoreCase("URL")){	
						if (profileRole.split("_")[1].replaceAll("'","").equalsIgnoreCase(((String) key).split("_")[1])){
							url = url + "/"+value;
						}
					}
				});
			}
			url = url.replaceAll("\'","");
			ds = aimReaderForC1(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}*/
	
	@Override
	public String getValues(Map profile, Map parameters) {
		String profileRole = (String) profile.get("user_roles");
		try {
			url = parameters.get(URL).toString();
			logger.info("base url ->" + url + "<-");
			System.out.println("base url ->" + url + "<-");
			
			if (profileRole.contains("_")) {
				parameters.forEach((key, value) -> {
					if (!((String) key).equalsIgnoreCase(URL)) {
						if (profileRole.split("_")[1].replaceAll("'", "")
								.equalsIgnoreCase(((String) key).split("_")[1])) {
							url = url + "/" + value;
						}
					}
				});
			}
			url = url.replaceAll("\'", "");
			
			logger.info("final url ->" + url + "<-");
			System.out.println("final url ->" + url + "<-");
			
			Object removedUrl = parameters.remove(URL);
			
			logger.info("removed url ->" + removedUrl + "<-");
			System.out.println("removed url ->" + removedUrl + "<-");
			
			parameters.put(URL, url);
			
			logger.info("putted url ->" + url + "<-");
			System.out.println("putted url ->" + url + "<-");
		} catch (Exception e) {
			e.printStackTrace();
	    	logger.error(e.getMessage(), e.getCause());
		}
		
		return super.getValues(profile, parameters);
	}
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForC1(String urlToRead) throws Exception, JSONException {
	      
		  /*Requesting AIM*/
		  /*StringBuilder aim = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         aim.append(line);
	      }
	      rd.close();*/
	      
	      String rows = "";
	      rows = "<ROWS>";
	      Map<String, C1FarmData> farms = new HashMap<String, C1FarmData>();
	      Map<String, C1ParcelData> parcelsData = new HashMap<String, C1ParcelData>();
	      Map<String, C1ParcelRecord> parcelRecords = new HashMap<String, C1ParcelRecord>();
	      Map<String, C1WeatherForecast> weather = new HashMap<String, C1WeatherForecast>();
	      Map<String, C1Zone> zones = new HashMap<String, C1Zone>();
	      Map<String, C1Zone> managementZones = new HashMap<String, C1Zone>();
	      Map<String, C1Zone> polygonZones = new HashMap<String, C1Zone>();
	      Map<String, C1AgriCrop> agriCrops = new HashMap<String, C1AgriCrop>();
	      Map<String, C1Observation> observations = new HashMap<String, C1Observation>();
	      Map<String, C1ParcelOperation> parcelOperations = new HashMap<String, C1ParcelOperation>();
	      Map<String, C1ParcelOperation> productTypes = new HashMap<String, C1ParcelOperation>();
	      
	      try {
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "AgriFarm":
		    		  C1FarmData fd = new C1FarmData();
		    		  fd.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  fd.setName(jsonArray.getJSONObject(l).get("name").toString());
		    		  farms.put(fd.getId(),fd);
		    		  break;
		    	  case "AgriCrop":
		    		  C1AgriCrop agriCrop = new C1AgriCrop();
		    		  agriCrop.setId(jsonArray.getJSONObject(l).get("@id").toString());
	    			  agriCrop.setCrop(jsonArray.getJSONObject(l).get("name").toString());
	    			  agriCrop.setWateringFrequency(jsonArray.getJSONObject(l).get("wateringFrequency").toString());
	    			  agriCrops.put(agriCrop.getId(),agriCrop);
		    		  break;	  	  
		    	  case "AgriParcelRecord":
		    		  C1ParcelRecord pr = new C1ParcelRecord();
		    		  pr.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  JSONArray containsZones = new JSONArray(jsonArray.getJSONObject(l).get("http://foodie-cloud.com/model/foodie#containsZone").toString());  
		    		  for (int x=0; x<containsZones.length(); x++) {
		    			  C1Zone zone = new C1Zone();
	    				  zone.setId(containsZones.get(x).toString());
	    				  zone.setParcelRecordId(jsonArray.getJSONObject(l).get("@id").toString());
	    				  zones.put(zone.getId(), zone); 
		    		  }
		    		  pr.setDate(jsonArray.getJSONObject(l).get("dateCreated").toString().split("T")[0]);
		    		  pr.setParcelId(jsonArray.getJSONObject(l).get("hasAgriParcel").toString()); 
		    		  pr.setNitrogenLevel((jsonArray.getJSONObject(l).getDouble("leafNitrogenLevel"))*100);
		    		  pr.setTreatmentDescription(jsonArray.getJSONObject(l).getString("http://foodie-cloud.com/model/foodie#treatmentDescription"));
		    		  pr.setTotalAffectedArea(jsonArray.getJSONObject(l).getDouble("totalAffectedArea"));
		    		  parcelRecords.put(pr.getId(), pr);
		    		  break;
		    	  case "AgriParcel":
		    		  C1ParcelData pd = new C1ParcelData();
	    			  pd.setId(jsonArray.getJSONObject(l).get("@id").toString());
	    			  pd.setFarmId(jsonArray.getJSONObject(l).get("belongsTo").toString());
	    			  pd.setArea(jsonArray.getJSONObject(l).getDouble("area"));
	    			  pd.setName(jsonArray.getJSONObject(l).get("name").toString());
	    			  pd.setCropId(jsonArray.getJSONObject(l).get("hasAgriCrop").toString());
	    			  parcelsData.put(pd.getId(),pd);
		    		  break;	  
		    	  case "ManagementZone":
		    		  C1Zone managementZone = new C1Zone();
		    		  managementZone.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  managementZone.setCode(jsonArray.getJSONObject(l).get("http://foodie-cloud.com/model/foodie#code").toString());
		    		  managementZone.setGeometryId(jsonArray.getJSONObject(l).get("hasGeometry").toString());
		    		  managementZones.put(managementZone.getId(), managementZone);
		    		  break;
		    	  case "POLYGON":
		    		  C1Zone polygonZone = new C1Zone();
		    		  polygonZone.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  polygonZone.setWkt(jsonArray.getJSONObject(l).get("asWKT").toString());
		    		  polygonZones.put(polygonZone.getId(), polygonZone);
		    		  break;
		    	  case "Observation":
		    		  C1Observation observation = new C1Observation();
		    		  observation.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  observation.setDate(jsonArray.getJSONObject(l).get("http://www.w3.org/ns/sosa/resultTime").toString());
		    		  observation.setWeatherForecastId(jsonArray.getJSONObject(l).get("http://www.w3.org/ns/sosa/hasFeatureOfInterest").toString());
		    		  observation.setResult(jsonArray.getJSONObject(l).get("hasSimpleResult").toString());
		    		  observations.put(observation.getWeatherForecastId(), observation);
		    		  break;
		    	  case "AgriParcelOperation":
		    		  C1ParcelOperation po = new C1ParcelOperation();
		    		  po.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  po.setParcelId(jsonArray.getJSONObject(l).get("hasAgriParcel").toString());
		    		  po.setFertiliserTypeId(jsonArray.getJSONObject(l).get("hasAgriProductType").toString());
					  po.setQuantity(jsonArray.getJSONObject(l).get("quantity").toString());
		    		  parcelOperations.put(po.getId(), po);
		    		  break;	
		    	  case "AgriProductType":
		    		  C1ParcelOperation agriProductType = new C1ParcelOperation();
		    		  agriProductType.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  agriProductType.setFertiliserType(jsonArray.getJSONObject(l).get("name").toString());
		    		  productTypes.put(agriProductType.getId(), agriProductType);
		    		  break;		  
		    	  case "WeatherForecast":
		    		  C1WeatherForecast wf = new C1WeatherForecast();
		    		  wf.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  wf.setParcelId(jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#hasAgriParcel").toString());
		    		  wf.setDateValidFrom(jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#dateIssued").toString().split("T")[0]);
		    		  wf.setDateValidThrough(jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#dateIssued").toString().split("T")[0]);
		    		  wf.setWeatherType(jsonArray.getJSONObject(l).get("weatherType").toString());
		    		  wf.setTemperature(jsonArray.getJSONObject(l).getDouble("temperature"));
		    		  wf.setWindSpeed(jsonArray.getJSONObject(l).getDouble("windSpeed"));
		    		  wf.setPrecipitation((jsonArray.getJSONObject(l).getDouble("precipitationProbability"))*100);
		    		  weather.put(wf.getId(),wf);
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	    	  logger.error(e.getMessage(), e.getCause());
	      }
	      
	      /*Creating records for dataset*/
	      ArrayList<C1DatasetRecord> dsRList=new ArrayList<C1DatasetRecord>();
	      farms.forEach((farmId, farmObj) -> {
	    	  parcelsData.forEach((pdId, pdObj) -> {
	    		  if(pdObj.getFarmId().equals(farmId)) {
	    			  parcelRecords.forEach((prId, prObj) -> {
	    				  if(prObj.getParcelId().equals(pdId)) {
	    					  C1DatasetRecord dsR = new C1DatasetRecord();
	    					  zones.forEach((id, zoneObj) -> {
	    						  if(prObj.getId().equals(zoneObj.getParcelRecordId())) {
	    							  managementZones.forEach((managementZoneId, managementZoneObj) -> {
	    	    						  if(zoneObj.getId().equals(managementZoneId)) {
	    	    							  polygonZones.forEach((polygonZonesId, polygonZoneObj) -> {
	    	    	    						  if(managementZoneObj.getGeometryId().equals(polygonZonesId)) {
	    	    	    							  C1DatasetRecord dsR1 = new C1DatasetRecord();
	    	    	    							  dsR1.setFarmName(farmObj.getName());
	    	    	    	    					  dsR1.setParcelArea(Double.toString(pdObj.getArea()));
	    	    	    	    					  dsR1.setParcelName(pdObj.getName());
	    	    	    							  dsR1.setWkt(polygonZoneObj.getWkt());
	    	    	    							  dsR1.setCode(managementZoneObj.getCode());
	    	    	    							  dsR1.setZone(polygonZonesId);
	    	    	    							  dsR1.setTreatmentDescription(prObj.getTreatmentDescription());
	    	    	    							  dsR1.setNitrogenLevel(Double.toString(prObj.getNitrogenLevel()));
	    	    	    	    					  dsR1.setTotalAffectedArea(Double.toString(prObj.getTotalAffectedArea()));
	    	    	    	    					  dsR1.setParcelDate(prObj.getDate());
	    	    	    							  dsRList.add(dsR1);
	    	    	    						  }
	    	    	    					  });	  
	    	    						  }
	    	    					  });  
	    						  }
	    					  });
	    					  
	    					  dsR.setFarmName(farmObj.getName());
	    					  dsR.setParcelArea(Double.toString(pdObj.getArea()));
	    					  dsR.setParcelName(pdObj.getName());
	    					  dsR.setParcelDate(prObj.getDate());
	    					  agriCrops.forEach((id, cropObj) -> {
	    						  if(pdObj.getCropId().equals(id)) {
	    							  dsR.setParcelCrop(cropObj.getCrop());
	    							  dsR.setWateringFrequency(cropObj.getWateringFrequency());
	    						  }
	    					  }); 
	    					  parcelOperations.forEach((parcelOperationId, parcelOperationObj) -> {
	    						  if(pdObj.getId().equals(parcelOperationObj.getParcelId())) {
	    							  productTypes.forEach((productTypeId, productTypeObj) -> {
	    								  if(parcelOperationObj.getFertiliserTypeId().equals(productTypeId)) {
	    									  dsR.setFertiliserName(productTypeObj.getFertiliserType());
	    									  dsR.setFertiliserQty(parcelOperationObj.getQuantity());
	    								  }
	    							  });
	    						  }
	    					  }); 
	    					  dsRList.add(dsR);
	    					  
	    					  weather.forEach((wId,wObj) -> {
	    						  if(wObj.getParcelId().equals(pdId)) {
	    							  C1DatasetRecord dsR2 = new C1DatasetRecord();
	    							  dsR2.setFarmName(farmObj.getName());
	    							  dsR2.setParcelName(pdObj.getName());
	    							  dsR2.setParcelDate(prObj.getDate());
	    							  dsR2.setWeatherDateValidFrom(wObj.getDateValidFrom());
	    							  dsR2.setWeatherDateValidThrough(wObj.getDateValidThrough());
	    							  dsR2.setWeatherType(wObj.getWeatherType());
	    							  dsR2.setWeatherTemperature(Double.toString(wObj.getTemperature()));
	    							  dsR2.setWeatherWindSpeed(Double.toString(wObj.getWindSpeed()));
	    							  dsR2.setWeatherPrecipitation(Double.toString(wObj.getPrecipitation()));
	    							  observations.forEach((observationId, observationObj) -> {
	    	    						  if(wObj.getId().equals(observationObj.getWeatherForecastId())) {
	    	    							  dsR2.setSprayingCondition(observationObj.getResult());  
	    	    							  dsRList.add(dsR2);
	    	    						  }
	    	    					  });
	    						  }
	    					  });
	    				  }
	    			  });
	    		  }
	    		  
	    	  });
	    	  
	      });
	      
	      /*Exporting Records to XML KA format*/
	      for(C1DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW PreviousFertilizationDate=\"" + dsR.getParcelDate()
	    			  + "\" Farm=\"" + dsR.getFarmName()
	    			  + "\" Zone=\"" + dsR.getZone()
	    			  + "\" WKT=\"" + dsR.getWkt()
	    			  + "\" Code=\"" + dsR.getCode()
	    			  + "\" Parcel=\"" + dsR.getParcelName()
	    			  + "\" Area=\"" + dsR.getParcelArea()
	    			  + "\" CropName=\"" + dsR.getParcelCrop()
	    			  + "\" Irrigation=\"" + dsR.getParcelIrrigation()
	    			  + "\" TreatmentDescription=\"" + dsR.getTreatmentDescription()
	    			  + "\" FertilizerType=\"" + dsR.getFertiliserName()
	    			  + "\" FertilizerQuantity=\"" + dsR.getFertiliserQty()
	    			  + "\" WateringFrequency=\"" + dsR.getWateringFrequency()
	    			  + "\" SprayingCondition=\"" + dsR.getSprayingCondition()
	    			  + "\" NitrogenLevel=\"" + dsR.getNitrogenLevel()
	    			  + "\" TotalAffectedArea=\"" + dsR.getTotalAffectedArea()
	    			  + "\" WeatherDateValidFrom=\"" + dsR.getWeatherDateValidFrom()
	    			  + "\" WeatherDateValidThrough=\"" + dsR.getWeatherDateValidThrough()
	    			  + "\" WeatherType=\"" + dsR.getWeatherType()
	    			  + "\" WeatherTemperature=\"" + dsR.getWeatherTemperature()
	    			  + "\" WeatherWindSpeed=\"" + dsR.getWeatherWindSpeed()
	    			  + "\" WeatherPrecipitation=\"" + dsR.getWeatherPrecipitation()
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      
	      return rows;
	   }
}