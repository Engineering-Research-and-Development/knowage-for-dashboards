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

	private String url = null;

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
		    		  fd.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  fd.setName(jsonArray.getJSONObject(l).get("name").toString());
		    		  farms.put(fd.getId(),fd);
		    		  break;
		    	  case "AgriCrop":
		    		  A3AgriCrop agriCrop = new A3AgriCrop();
		    		  agriCrop.setId(jsonArray.getJSONObject(l).get("@id").toString());
	    			  agriCrop.setCrop(jsonArray.getJSONObject(l).get("name").toString());
	    			  agriCrop.setWateringFrequency(jsonArray.getJSONObject(l).get("wateringFrequency").toString());
	    			  agriCrops.put(agriCrop.getId(),agriCrop);
		    		  break;	  	  	  
		    	  case "AgriParcelRecord":
		    		  A3ParcelRecord pr = new A3ParcelRecord();
		    		  pr.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  JSONArray containsZones = new JSONArray(jsonArray.getJSONObject(l).get("containsZone").toString());  
		    		  for (int x=0; x<containsZones.length(); x++) {
		    			  A3Zone zone = new A3Zone();
	    				  zone.setId(containsZones.get(x).toString());
	    				  zone.setParcelRecordId(jsonArray.getJSONObject(l).get("@id").toString());
	    				  zones.put(zone.getId(), zone); 
		    		  }
		    		  pr.setParcelId(jsonArray.getJSONObject(l).get("hasAgriParcel").toString());
		    		  JSONArray arrayResults = new JSONArray(jsonArray.getJSONObject(l).get("result").toString());
		    		  for (int x=0; x<arrayResults.length(); x++) {
		    			  A3Result result = new A3Result();
	    				  result.setValue(arrayResults.get(x).toString());
	    				  result.setParcelRecordId(jsonArray.getJSONObject(l).get("@id").toString() + "." + x);
	    				  results.put(result.getParcelRecordId(), result); 
		    		  }
		    		  pr.setDate(jsonArray.getJSONObject(l).get("observedAt").toString().split("T")[0]);
		    		  pr.setSoilWater((jsonArray.getJSONObject(l).getDouble("soilMoistureVwc"))*100);
		    		  pr.setRelativeHumidity((jsonArray.getJSONObject(l).getDouble("relativeHumidity"))*100);
		    		  pr.setWindSpeed((jsonArray.getJSONObject(l).getDouble("windSpeed")));
		    		  pr.setRainfall(jsonArray.getJSONObject(l).getDouble("rainfall"));
		    		  pr.setAirTemp(jsonArray.getJSONObject(l).get("airTemperature").toString());
		    		  pr.setSoilTemp(jsonArray.getJSONObject(l).get("soilTemperature").toString());
		    		  parcelsRecord.put(pr.getId(), pr);
		    		  break;
		    	  case "AgriParcel":
		    		  A3ParcelData pd = new A3ParcelData();
	    			  pd.setId(jsonArray.getJSONObject(l).get("@id").toString());
	    			  pd.setFarmId(jsonArray.getJSONObject(l).get("belongsTo").toString());
	    			  pd.setArea(jsonArray.getJSONObject(l).getDouble("area"));
	    			  pd.setName(jsonArray.getJSONObject(l).get("name").toString());
	    			  pd.setCropId(jsonArray.getJSONObject(l).get("hasAgriCrop").toString());
	    			  parcelsData.put(pd.getId(),pd);
		    		  break;
		    	  case "ManagementZone":
		    		  A3Zone managementZone = new A3Zone();
		    		  managementZone.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  managementZone.setCode(jsonArray.getJSONObject(l).get("http://foodie-cloud.com/model/foodie#code").toString());
		    		  managementZone.setGeometryId(jsonArray.getJSONObject(l).get("hasGeometry").toString());
		    		  managementZones.put(managementZone.getId(), managementZone);
		    		  break;
		    	  case "POLYGON":
		    		  A3Zone polygonZone = new A3Zone();
		    		  polygonZone.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  polygonZone.setWkt(jsonArray.getJSONObject(l).get("asWKT").toString());
		    		  polygonZones.put(polygonZone.getId(), polygonZone);
		    		  break;	  
		    	  case "WeatherObserved":
		    		  A3WeatherData wd = new A3WeatherData();
		    		  wd.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  wd.setParcelId(jsonArray.getJSONObject(l).get("http://www.w3.org/ns/sosa/hasFeatureOfInterest").toString());
		    		  wd.setDate(jsonArray.getJSONObject(l).get("https://uri.fiware.org/ns/data-models#dateObserved").toString().split("T")[0]);
		    		  wd.setHeat(jsonArray.getJSONObject(l).getDouble("scorchingHeat"));
		    		  wd.setWinter(jsonArray.getJSONObject(l).getDouble("winterHarshness"));
		    		  weather.put(wd.getId(),wd);
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
	    		  if(pdObj.getFarmId().equals(farmId)) {
	    			  parcelsRecord.forEach((prId, prObj) -> {
	    				  if(prObj.getParcelId().equals(pdId)) {
	    					  A3DatasetRecord dsR = new A3DatasetRecord();
	    					  zones.forEach((id, zoneObj) -> {
	    						  if(prObj.getId().equals(zoneObj.getParcelRecordId())) {
	    							  managementZones.forEach((managementZoneId, managementZoneObj) -> {
	    	    						  if(zoneObj.getId().equals(managementZoneId)) {
	    	    							  polygonZones.forEach((polygonZonesId, polygonZoneObj) -> {
	    	    	    						  if(managementZoneObj.getGeometryId().equals(polygonZonesId)) {	
	    	    	    							  results.forEach((parcelRecordId, resultObj) -> {
	    	    	    	    						  if (prId.equals(parcelRecordId.split("\\.")[0])){
	    	    	    	    							  A3DatasetRecord dsR1 = new A3DatasetRecord();
	    	    	    	    	    					  dsR1.setFarmName(farmObj.getName());
	    	    	    	    	    					  dsR1.setParcelArea(Double.toString(pdObj.getArea()));
	    	    	    	    	    					  dsR1.setParcelName(pdObj.getName());
	    	    	    	    							  dsR1.setWkt(polygonZoneObj.getWkt());	
	    	    	    	    							  dsR1.setCode(managementZoneObj.getCode());
	    	    	    	    							  dsR1.setZone(polygonZonesId);
	    	    	    	    	    					  dsR1.setParcelDate(prObj.getDate());
	    	    	    	    	    					  dsR1.setParcelSoilWc(Double.toString(prObj.getSoilWater()));
	    	    	    	    	    					  dsR1.setParcelAirTemp(prObj.getAirTemp());
	    	    	    	    	    					  dsR1.setParcelSoilTemp(prObj.getSoilTemp());
	    	    	    	    	    					  dsR1.setParcelRelativeHumidity(Double.toString(prObj.getRelativeHumidity()));
	    	    	    	    	    					  dsR1.setParcelRainfall(Double.toString(prObj.getRainfall()));
	    	    	    	    	    					  dsR1.setParcelWindSpeed(Double.toString(prObj.getWindSpeed()));
	    	    	    	    	    					  dsR1.setParcelResult(resultObj.getValue());
	    	    	    	    							  dsRList.add(dsR1);
	    	    	    	    						  } 
	    	    	    							  });
	    	    	    						  }
	    	    	    					  });	  
	    	    						  }
	    	    					  });  
	    						  }
	    					  });
	    					  dsR.setFarmName(farmObj.getName());
	    					  dsR.setParcelArea(Double.toString(pdObj.getArea()));
	    					  dsR.setParcelName(pdObj.getName());
	    					  dsR.setParcelCrop(pdObj.getCrop());
                              							
 	    					  dsR.setParcelAirTemp(prObj.getAirTemp());
							  dsR.setParcelSoilTemp(prObj.getSoilTemp());
	    					  dsR.setParcelSoilWc(Double.toString(prObj.getSoilWater()));
	    					  dsR.setParcelDate(prObj.getDate());
	    					  dsR.setParcelRelativeHumidity(Double.toString(prObj.getRelativeHumidity()));
							      
	    					  dsR.setParcelRainfall(Double.toString(prObj.getRainfall()));
	    					  dsR.setParcelWindSpeed(Double.toString(prObj.getWindSpeed()));
	    					  agriCrops.forEach((id, cropObj) -> {
	    						  if(pdObj.getCropId().equals(id)) {
	    							  dsR.setParcelCrop(cropObj.getCrop());
	    							  dsR.setWateringFrequency(cropObj.getWateringFrequency());
	    						  }
	    					  }); 
	    					  weather.forEach((wId,wObj) -> {
	    						  if(wObj.getParcelId().equals(prObj.getParcelId())) {
	    							  dsR.setScorchingHeat(Double.toString(wObj.getHeat()));
	    							  dsR.setWinterHarshness(Double.toString(wObj.getWinter()));
	    							  dsR.setObservationDate(wObj.getDate());	    						
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
	              if (dsRList.get(i).getParcelResult() == dsRList.get(j).getParcelResult()) {
	               	  dsRList.get(j).setParcelResult("none");
	                  j++;
	              }
	          }
	      }
	      
	      /*Exporting Records to XML KA format*/
	      for(A3DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW Date=\"" + dsR.getParcelDate()
	    			  + "\" ObservationDate=\"" + dsR.getObservationDate() 
	    			  + "\" Farm=\"" + dsR.getFarmName()
	    			  + "\" Zone=\"" + dsR.getZone()
	    			  + "\" WKT=\"" + dsR.getWkt()
	    			  + "\" Code=\"" + dsR.getCode()
	    			  + "\" Parcel=\"" + dsR.getParcelName()
	    			  + "\" Area=\"" + dsR.getParcelArea()
	    			  + "\" CropName=\"" + dsR.getParcelCrop()
	    			  + "\" WateringFrequency=\"" + dsR.getWateringFrequency()
	    			  // start parameters and labels are different
	    			  + "\" AvgAirTemp=\"" + dsR.getParcelAirTemp()
	    			  + "\" MaxAirTemp=\"" + dsR.getParcelAirTemp()
	    			  + "\" MinAirTemp=\"" + dsR.getParcelAirTemp()
	    			  + "\" MaxSoilTemp=\"" + dsR.getParcelSoilTemp()
	    			  // end parameters and labels are different	    			  
	    			  + "\" Rainfall=\"" + dsR.getParcelRainfall()
	    			  + "\" SoilWater=\"" + dsR.getParcelSoilWc()
	    			  + "\" RelativeHumidity=\"" + dsR.getParcelRelativeHumidity()
	    			  + "\" WindSpeed=\"" + dsR.getParcelWindSpeed()
	    			  + "\" LeafWetness=\"" + dsR.getParcelLeafWetness()
	    			  + "\" Result=\"" + dsR.getParcelResult()
	    			  + "\" ScorchingHeat=\"" + dsR.getScorchingHeat()
	    			  + "\" WinterHarshness=\"" + dsR.getWinterHarshness()
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	   }
}