package it.eng.demeter.a3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class A3Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {

	//String url = "";
	//private static final Logger logger = LogManager.getLogger(A3Dataset.class);

	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String profileRole = (String) profile.get("user_roles");
		String ds = null;
		try {
			url = parameters.get("URL").toString();
			if (profileRole.contains("_")) {
				parameters.forEach((key, value) -> {
					if (!((String) key).equalsIgnoreCase("URL")) {
						if (profileRole.split("_")[1].replaceAll("'", "")
								.equalsIgnoreCase(((String) key).split("_")[1])) {
							url = url + "/" + value;
						}
					}
				});
			}
			url = url.replaceAll("\'", "");
			ds = aimReaderForA3(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}*/

	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForA3(String urlToRead) throws Exception, JSONException {
	      
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
	      Map<String, A3FarmData> farms = new HashMap<String, A3FarmData>();
	      Map<String, A3ParcelData> parcelsData = new HashMap<String, A3ParcelData>();
	      Map<String, A3ParcelRecord> parcelsRecord = new HashMap<String, A3ParcelRecord>();
	      Map<String, A3WeatherData> weather = new HashMap<String, A3WeatherData>();
	      Map<String, A3Zone> managementZones = new HashMap<String, A3Zone>();
	      Map<String, A3Zone> polygonZones = new HashMap<String, A3Zone>();
	      Map<String, A3Zone> zones = new HashMap<String, A3Zone>();
	      Map<String, A3Result> results = new HashMap<String, A3Result>();
	      Map<String, A3AgriCrop> agriCrops = new HashMap<String, A3AgriCrop>();
	      
	      try {
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "AgriFarm":
		    		  A3FarmData fd = new A3FarmData();
		    		  fd.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  fd.name = jsonArray.getJSONObject(l).get("name").toString();
		    		  farms.put(fd.id,fd);
		    		  break;
		    	  case "AgriCrop":
		    		  A3AgriCrop agriCrop = new A3AgriCrop();
		    		  agriCrop.id = jsonArray.getJSONObject(l).get("@id").toString();
	    			  agriCrop.crop = jsonArray.getJSONObject(l).get("name").toString();
	    			  agriCrop.wateringFrequency = jsonArray.getJSONObject(l).get("wateringFrequency").toString();
	    			  agriCrops.put(agriCrop.id,agriCrop);
		    		  break;	  	  	  
		    	  case "AgriParcelRecord":
		    		  A3ParcelRecord pr = new A3ParcelRecord();
		    		  pr.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  JSONArray containsZones = new JSONArray(jsonArray.getJSONObject(l).get("containsZone").toString());  
		    		  for (int x=0; x<containsZones.length(); x++) {
		    			  A3Zone zone = new A3Zone();
	    				  zone.id = containsZones.get(x).toString();
	    				  zone.parcelRecordId = jsonArray.getJSONObject(l).get("@id").toString();
	    				  zones.put(zone.id, zone); 
		    		  }
		    		  pr.parcelId = jsonArray.getJSONObject(l).get("hasAgriParcel").toString();
		    		  JSONArray arrayResults = new JSONArray(jsonArray.getJSONObject(l).get("result").toString());
		    		  for (int x=0; x<arrayResults.length(); x++) {
		    			  A3Result result = new A3Result();
	    				  result.value = arrayResults.get(x).toString();
	    				  result.parcelRecordId = jsonArray.getJSONObject(l).get("@id").toString() + "." + x;
	    				  results.put(result.parcelRecordId, result); 
		    		  }
		    		  pr.date = jsonArray.getJSONObject(l).get("observedAt").toString().split("T")[0];
		    		  pr.soilWater = (jsonArray.getJSONObject(l).getDouble("soilMoistureVwc"))*100;
		    		  pr.relativeHumidity = (jsonArray.getJSONObject(l).getDouble("relativeHumidity"))*100;
		    		  pr.windSpeed = (jsonArray.getJSONObject(l).getDouble("windSpeed"));
		    		  pr.rainfall = jsonArray.getJSONObject(l).getDouble("rainfall");
		    		  pr.airTemp = jsonArray.getJSONObject(l).get("airTemperature").toString();
		    		  pr.soilTemp = jsonArray.getJSONObject(l).get("soilTemperature").toString();
		    		  parcelsRecord.put(pr.id, pr);
		    		  break;
		    	  case "AgriParcel":
		    		  A3ParcelData pd = new A3ParcelData();
	    			  pd.id = jsonArray.getJSONObject(l).get("@id").toString();
	    			  pd.farmId = jsonArray.getJSONObject(l).get("belongsTo").toString();
	    			  pd.area = jsonArray.getJSONObject(l).getDouble("area");
	    			  pd.name = jsonArray.getJSONObject(l).get("name").toString();
	    			  pd.cropId = jsonArray.getJSONObject(l).get("hasAgriCrop").toString();
	    			  parcelsData.put(pd.id,pd);
		    		  break;
		    	  case "ManagementZone":
		    		  A3Zone managementZone = new A3Zone();
		    		  managementZone.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  managementZone.code = jsonArray.getJSONObject(l).get("http://foodie-cloud.com/model/foodie#code").toString();
		    		  managementZone.geometryId = jsonArray.getJSONObject(l).get("hasGeometry").toString();
		    		  managementZones.put(managementZone.id, managementZone);
		    		  break;
		    	  case "POLYGON":
		    		  A3Zone polygonZone = new A3Zone();
		    		  polygonZone.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  polygonZone.wkt = jsonArray.getJSONObject(l).get("asWKT").toString();
		    		  polygonZones.put(polygonZone.id, polygonZone);
		    		  break;	  
		    	  case "WeatherObserved":
		    		  A3WeatherData wd = new A3WeatherData();
		    		  wd.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  wd.parcelId = jsonArray.getJSONObject(l).get("http://www.w3.org/ns/sosa/hasFeatureOfInterest").toString();
		    		  wd.date = jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#dateObserved").toString().split("T")[0];
		    		  wd.heat = jsonArray.getJSONObject(l).getDouble("scorchingHeat");
		    		  wd.winter = jsonArray.getJSONObject(l).getDouble("winterHarshness");
		    		  weather.put(wd.id,wd);
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	    	  logger.error(e.getMessage(), e.getCause());
	      }
	      
	      /*Creating records for dataset*/
	      List<A3DatasetRecord> dsRList=new ArrayList<A3DatasetRecord>();
	      farms.forEach((farmId, farmObj) -> {
	    	  parcelsData.forEach((pdId, pdObj) -> {
	    		  if(pdObj.farmId.equals(farmId)) {
	    			  parcelsRecord.forEach((prId, prObj) -> {
	    				  if(prObj.parcelId.equals(pdId)) {
	    					  A3DatasetRecord dsR = new A3DatasetRecord();
	    					  zones.forEach((id, zoneObj) -> {
	    						  if(prObj.id.equals(zoneObj.parcelRecordId)) {
	    							  managementZones.forEach((managementZoneId, managementZoneObj) -> {
	    	    						  if(zoneObj.id.equals(managementZoneId)) {
	    	    							  polygonZones.forEach((polygonZonesId, polygonZoneObj) -> {
	    	    	    						  if(managementZoneObj.geometryId.equals(polygonZonesId)) {	
	    	    	    							  results.forEach((parcelRecordId, resultObj) -> {
	    	    	    	    						  if (prId.equals(parcelRecordId.split("\\.")[0])){
	    	    	    	    							  A3DatasetRecord dsR1 = new A3DatasetRecord();
	    	    	    	    	    					  dsR1.farmName = farmObj.name;
	    	    	    	    	    					  dsR1.parcelArea = Double.toString(pdObj.area);
	    	    	    	    	    					  dsR1.parcelName = pdObj.name;
	    	    	    	    							  dsR1.wkt = polygonZoneObj.wkt;	
	    	    	    	    							  dsR1.code= managementZoneObj.code;
	    	    	    	    							  dsR1.zone = polygonZonesId;
	    	    	    	    	    					  dsR1.parcelDate = prObj.date;
	    	    	    	    	    					  dsR1.parcelSoilWc = Double.toString(prObj.soilWater);
	    	    	    	    	    					  dsR1.parcelAirTemp = prObj.airTemp;
	    	    	    	    	    					  dsR1.parcelSoilTemp = prObj.soilTemp;
	    	    	    	    	    					  dsR1.parcelRelativeHumidity = Double.toString(prObj.relativeHumidity);
	    	    	    	    	    					  dsR1.parcelRainfall = Double.toString(prObj.rainfall);
	    	    	    	    	    					  dsR1.parcelWindSpeed = Double.toString(prObj.windSpeed);
	    	    	    	    	    					  dsR1.parcelResult = resultObj.value;
	    	    	    	    							  dsRList.add(dsR1);
	    	    	    	    						  } 
	    	    	    							  });
	    	    	    						  }
	    	    	    					  });	  
	    	    						  }
	    	    					  });  
	    						  }
	    					  });
	    					  dsR.farmName = farmObj.name;
	    					  dsR.parcelArea = Double.toString(pdObj.area);
	    					  dsR.parcelName = pdObj.name;
	    					  dsR.parcelCrop = pdObj.crop;
                              							
 	    					  dsR.parcelAirTemp = prObj.airTemp;
							  dsR.parcelSoilTemp = prObj.soilTemp;
	    					  dsR.parcelSoilWc = Double.toString(prObj.soilWater);
	    					  dsR.parcelDate = prObj.date;
	    					  dsR.parcelRelativeHumidity = Double.toString(prObj.relativeHumidity);
							
	    					  dsR.parcelRainfall = Double.toString(prObj.rainfall);
	    					  dsR.parcelWindSpeed = Double.toString(prObj.windSpeed);
	    					  agriCrops.forEach((id, cropObj) -> {
	    						  if(pdObj.cropId.equals(id)) {
	    							  dsR.parcelCrop = cropObj.crop;
	    							  dsR.wateringFrequency = cropObj.wateringFrequency;
	    						  }
	    					  }); 
	    					  weather.forEach((wId,wObj) -> {
	    						  if(wObj.parcelId.equals(prObj.parcelId)) {
	    							  dsR.scorchingHeat = Double.toString(wObj.heat);
	    							  dsR.winterHarshness = Double.toString(wObj.winter);
	    							  dsR.observationDate = wObj.date;	    						
	    						  }
	    					  });
	    					  dsRList.add(dsR);
	    				  }
	    			  });
	    		  }  
	    	  });
	      });
	      
	      int len = dsRList.size();
	      for (int i = 0; i < dsRList.size(); i++) {
	          for (int j = i + 1; j < len; j++) {
	              if (dsRList.get(i).parcelResult == dsRList.get(j).parcelResult) {
	               	  dsRList.get(j).parcelResult = "none";
	                  j++;
	              }
	          }
	      }
	      
	      /*Exporting Records to XML KA format*/
	      for(A3DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW Date=\"" + dsR.parcelDate
	    			  + "\" ObservationDate=\"" + dsR.observationDate 
	    			  + "\" Farm=\"" + dsR.farmName
	    			  + "\" Zone=\"" + dsR.zone
	    			  + "\" WKT=\"" + dsR.wkt
	    			  + "\" Code=\"" + dsR.code
	    			  + "\" Parcel=\"" + dsR.parcelName
	    			  + "\" Area=\"" + dsR.parcelArea
	    			  + "\" CropName=\"" + dsR.parcelCrop
	    			  + "\" WateringFrequency=\"" + dsR.wateringFrequency
	    			  + "\" AvgAirTemp=\"" + dsR.parcelAirTemp
	    			  + "\" MaxAirTemp=\"" + dsR.parcelAirTemp
	    			  + "\" MinAirTemp=\"" + dsR.parcelAirTemp
	    			  + "\" MaxSoilTemp=\"" + dsR.parcelSoilTemp
	    			  + "\" Rainfall=\"" + dsR.parcelRainfall
	    			  + "\" SoilWater=\"" + dsR.parcelSoilWc
	    			  + "\" RelativeHumidity=\"" + dsR.parcelRelativeHumidity
	    			  + "\" WindSpeed=\"" + dsR.parcelWindSpeed
	    			  + "\" LeafWetness=\"" + dsR.parcelLeafWetness
	    			  + "\" Result=\"" + dsR.parcelResult
	    			  + "\" ScorchingHeat=\"" + dsR.scorchingHeat
	    			  + "\" WinterHarshness=\"" + dsR.winterHarshness
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	   }
}