package it.eng.demeter.c1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class C1Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
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
		    		  fd.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  fd.name = jsonArray.getJSONObject(l).get("name").toString();
		    		  farms.put(fd.id,fd);
		    		  break;
		    	  case "AgriCrop":
		    		  C1AgriCrop agriCrop = new C1AgriCrop();
		    		  agriCrop.id = jsonArray.getJSONObject(l).get("@id").toString();
	    			  agriCrop.crop = jsonArray.getJSONObject(l).get("name").toString();
	    			  agriCrop.wateringFrequency = jsonArray.getJSONObject(l).get("wateringFrequency").toString();
	    			  agriCrops.put(agriCrop.id,agriCrop);
		    		  break;	  	  
		    	  case "AgriParcelRecord":
		    		  C1ParcelRecord pr = new C1ParcelRecord();
		    		  pr.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  JSONArray containsZones = new JSONArray(jsonArray.getJSONObject(l).get("http://foodie-cloud.com/model/foodie#containsZone").toString());  
		    		  for (int x=0; x<containsZones.length(); x++) {
		    			  C1Zone zone = new C1Zone();
	    				  zone.id = containsZones.get(x).toString();
	    				  zone.parcelRecordId = jsonArray.getJSONObject(l).get("@id").toString();
	    				  zones.put(zone.id, zone); 
		    		  }
		    		  pr.date = jsonArray.getJSONObject(l).get("dateCreated").toString().split("T")[0];
		    		  pr.parcelId = jsonArray.getJSONObject(l).get("hasAgriParcel").toString(); 
		    		  pr.nitrogenLevel = (jsonArray.getJSONObject(l).getDouble("leafNitrogenLevel"))*100;
		    		  pr.treatmentDescription = jsonArray.getJSONObject(l).getString("http://foodie-cloud.com/model/foodie#treatmentDescription");
		    		  pr.totalAffectedArea = jsonArray.getJSONObject(l).getDouble("totalAffectedArea");
		    		  parcelRecords.put(pr.id, pr);
		    		  break;
		    	  case "AgriParcel":
		    		  C1ParcelData pd = new C1ParcelData();
	    			  pd.id = jsonArray.getJSONObject(l).get("@id").toString();
	    			  pd.farmId = jsonArray.getJSONObject(l).get("belongsTo").toString();
	    			  pd.area = jsonArray.getJSONObject(l).getDouble("area");
	    			  pd.name = jsonArray.getJSONObject(l).get("name").toString();
	    			  pd.cropId = jsonArray.getJSONObject(l).get("hasAgriCrop").toString();
	    			  parcelsData.put(pd.id,pd);
		    		  break;	  
		    	  case "ManagementZone":
		    		  C1Zone managementZone = new C1Zone();
		    		  managementZone.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  managementZone.code = jsonArray.getJSONObject(l).get("http://foodie-cloud.com/model/foodie#code").toString();
		    		  managementZone.geometryId = jsonArray.getJSONObject(l).get("hasGeometry").toString();
		    		  managementZones.put(managementZone.id, managementZone);
		    		  break;
		    	  case "POLYGON":
		    		  C1Zone polygonZone = new C1Zone();
		    		  polygonZone.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  polygonZone.wkt = jsonArray.getJSONObject(l).get("asWKT").toString();
		    		  polygonZones.put(polygonZone.id, polygonZone);
		    		  break;
		    	  case "Observation":
		    		  C1Observation observation = new C1Observation();
		    		  observation.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  observation.date = jsonArray.getJSONObject(l).get("http://www.w3.org/ns/sosa/resultTime").toString();
		    		  observation.weatherForecastId = jsonArray.getJSONObject(l).get("http://www.w3.org/ns/sosa/hasFeatureOfInterest").toString();
		    		  observation.result = jsonArray.getJSONObject(l).get("hasSimpleResult").toString();
		    		  observations.put(observation.weatherForecastId, observation);
		    		  break;
		    	  case "AgriParcelOperation":
		    		  C1ParcelOperation po = new C1ParcelOperation();
		    		  po.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  po.parcelId = jsonArray.getJSONObject(l).get("hasAgriParcel").toString();
		    		  po.fertiliserTypeId = jsonArray.getJSONObject(l).get("hasAgriProductType").toString();
					  po.quantity = jsonArray.getJSONObject(l).get("quantity").toString();
		    		  parcelOperations.put(po.id, po);
		    		  break;	
		    	  case "AgriProductType":
		    		  C1ParcelOperation agriProductType = new C1ParcelOperation();
		    		  agriProductType.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  agriProductType.fertiliserType = jsonArray.getJSONObject(l).get("name").toString();
		    		  productTypes.put(agriProductType.id, agriProductType);
		    		  break;		  
		    	  case "WeatherForecast":
		    		  C1WeatherForecast wf = new C1WeatherForecast();
		    		  wf.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  wf.parcelId = jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#hasAgriParcel").toString();
		    		  wf.dateValidFrom = jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#dateIssued").toString().split("T")[0];
		    		  wf.dateValidThrough = jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#dateIssued").toString().split("T")[0];
		    		  wf.weatherType = jsonArray.getJSONObject(l).get("weatherType").toString();
		    		  wf.temperature = jsonArray.getJSONObject(l).getDouble("temperature");
		    		  wf.windSpeed = jsonArray.getJSONObject(l).getDouble("windSpeed");
		    		  wf.precipitation = (jsonArray.getJSONObject(l).getDouble("precipitationProbability"))*100;
		    		  weather.put(wf.id,wf);
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
	    		  if(pdObj.farmId.equals(farmId)) {
	    			  parcelRecords.forEach((prId, prObj) -> {
	    				  if(prObj.parcelId.equals(pdId)) {
	    					  C1DatasetRecord dsR = new C1DatasetRecord();
	    					  zones.forEach((id, zoneObj) -> {
	    						  if(prObj.id.equals(zoneObj.parcelRecordId)) {
	    							  managementZones.forEach((managementZoneId, managementZoneObj) -> {
	    	    						  if(zoneObj.id.equals(managementZoneId)) {
	    	    							  polygonZones.forEach((polygonZonesId, polygonZoneObj) -> {
	    	    	    						  if(managementZoneObj.geometryId.equals(polygonZonesId)) {
	    	    	    							  C1DatasetRecord dsR1 = new C1DatasetRecord();
	    	    	    							  dsR1.farmName = farmObj.name;
	    	    	    	    					  dsR1.parcelArea = Double.toString(pdObj.area);
	    	    	    	    					  dsR1.parcelName = pdObj.name;
	    	    	    							  dsR1.wkt = polygonZoneObj.wkt;
	    	    	    							  dsR1.code= managementZoneObj.code;
	    	    	    							  dsR1.zone = polygonZonesId;
	    	    	    							  dsR1.treatmentDescription = prObj.treatmentDescription;
	    	    	    							  dsR1.nitrogenLevel = Double.toString(prObj.nitrogenLevel);
	    	    	    	    					  dsR1.totalAffectedArea = Double.toString(prObj.totalAffectedArea);
	    	    	    	    					  dsR1.parcelDate = prObj.date;
	    	    	    							  dsRList.add(dsR1);
	    	    	    						  }
	    	    	    					  });	  
	    	    						  }
	    	    					  });  
	    						  }
	    					  });
	    					  
	    					  dsR.farmName = farmObj.name;
	    					  dsR.parcelArea = Double.toString(pdObj.area);
	    					  dsR.parcelName = pdObj.name;
	    					  dsR.parcelDate = prObj.date;
	    					  agriCrops.forEach((id, cropObj) -> {
	    						  if(pdObj.cropId.equals(id)) {
	    							  dsR.parcelCrop = cropObj.crop;
	    							  dsR.wateringFrequency = cropObj.wateringFrequency;
	    						  }
	    					  }); 
	    					  parcelOperations.forEach((parcelOperationId, parcelOperationObj) -> {
	    						  if(pdObj.id.equals(parcelOperationObj.parcelId)) {
	    							  productTypes.forEach((productTypeId, productTypeObj) -> {
	    								  if(parcelOperationObj.fertiliserTypeId.equals(productTypeId)) {
	    									  dsR.fertiliserName = productTypeObj.fertiliserType;
	    									  dsR.fertiliserQty = parcelOperationObj.quantity;
	    								  }
	    							  });
	    						  }
	    					  }); 
	    					  dsRList.add(dsR);
	    					  
	    					  weather.forEach((wId,wObj) -> {
	    						  if(wObj.parcelId.equals(pdId)) {
	    							  C1DatasetRecord dsR2 = new C1DatasetRecord();
	    							  dsR2.farmName = farmObj.name;
	    							  dsR2.parcelName = pdObj.name;
	    							  dsR2.parcelDate = prObj.date;
	    							  dsR2.weatherDateValidFrom = wObj.dateValidFrom;
	    							  dsR2.weatherDateValidThrough = wObj.dateValidThrough;
	    							  dsR2.weatherType = wObj.weatherType;
	    							  dsR2.weatherTemperature = Double.toString(wObj.temperature);
	    							  dsR2.weatherWindSpeed = Double.toString(wObj.windSpeed);
	    							  dsR2.weatherPrecipitation = Double.toString(wObj.precipitation);
	    							  observations.forEach((observationId, observationObj) -> {
	    	    						  if(wObj.id.equals(observationObj.weatherForecastId)) {
	    	    							  dsR2.sprayingCondition = observationObj.result;  
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
	    	  rows += "<ROW PreviousFertilizationDate=\"" + dsR.parcelDate
	    			  + "\" Farm=\"" + dsR.farmName
	    			  + "\" Zone=\"" + dsR.zone
	    			  + "\" WKT=\"" + dsR.wkt
	    			  + "\" Code=\"" + dsR.code
	    			  + "\" Parcel=\"" + dsR.parcelName
	    			  + "\" Area=\"" + dsR.parcelArea
	    			  + "\" CropName=\"" + dsR.parcelCrop
	    			  + "\" Irrigation=\"" + dsR.parcelIrrigation
	    			  + "\" TreatmentDescription=\"" + dsR.treatmentDescription
	    			  + "\" FertilizerType=\"" + dsR.fertiliserName
	    			  + "\" FertilizerQuantity=\"" + dsR.fertiliserQty
	    			  + "\" WateringFrequency=\"" + dsR.wateringFrequency
	    			  + "\" SprayingCondition=\"" + dsR.sprayingCondition
	    			  + "\" NitrogenLevel=\"" + dsR.nitrogenLevel
	    			  + "\" TotalAffectedArea=\"" + dsR.totalAffectedArea
	    			  + "\" WeatherDateValidFrom=\"" + dsR.weatherDateValidFrom
	    			  + "\" WeatherDateValidThrough=\"" + dsR.weatherDateValidThrough
	    			  + "\" WeatherType=\"" + dsR.weatherType
	    			  + "\" WeatherTemperature=\"" + dsR.weatherTemperature
	    			  + "\" WeatherWindSpeed=\"" + dsR.weatherWindSpeed
	    			  + "\" WeatherPrecipitation=\"" + dsR.weatherPrecipitation
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      
	      return rows;
	   }
}
