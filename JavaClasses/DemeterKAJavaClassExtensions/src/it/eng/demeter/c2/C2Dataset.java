package it.eng.demeter.c2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class C2Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet{
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/C2/Test";
			url = url.replaceAll("\'","");
			String dsContent = parameters.get("DATASET_CONTENT").toString();
			//String dsContent = "parcel";
			dsContent = dsContent.replaceAll("\'","");
			switch (dsContent.toUpperCase()) {
			case "PARCEL":
				ds = aimReaderForC2_Parcel(url);
				break;
			case "ZONE":
				ds = aimReaderForC2_Zone(url);
				break;
			case "WEATHER":
				ds = aimReaderForC2_Weather(url);
				break;
			}
			
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;

	}
	
	public static String aimReaderForC2_Parcel(String urlToRead) throws Exception, JSONException {
		 /*Requesting AIM*/
		  StringBuilder aim = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         aim.append(line);
	      }
	      rd.close();
	      
	      String rows = "";
	      rows = "<ROWS>";
	      List<C2ParcelDatasetRecord> dsRList=new ArrayList<C2ParcelDatasetRecord>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Farm":
		    		  JSONArray parcels = new JSONArray(jsonArray.getJSONObject(l).get("containsPlot").toString());
		    		  for (int j=0; j<parcels.length(); j++) {
		    			  C2ParcelDatasetRecord dsR = new C2ParcelDatasetRecord();
		    			  dsR.parcelId = Integer.toString(j+1);
		    			  dsR.name = parcels.getJSONObject(j).get("name").toString();
		    			  dsR.soil = parcels.getJSONObject(j).getJSONObject("soilProperty").get("name").toString();
		    			  dsR.area = parcels.getJSONObject(j).get("area").toString();
		    			  dsR.crop = parcels.getJSONObject(j).getJSONObject("crop").getJSONObject("cropSpecies").get("name").toString();
		    			  dsR.cropStatus = parcels.getJSONObject(j).getJSONObject("crop").get("cropStatus").toString();
		    			  dsR.plantedDate = parcels.getJSONObject(j).getJSONObject("crop").get("lastPlantedAt").toString().split("T")[0];
		    			  dsR.beginningSowing = parcels.getJSONObject(j).getJSONObject("recommendedSowingPeriod").getJSONObject("hasBeginning").get("inXSDDateTimeStamp").toString().split("T")[0];
		    			  dsR.endingSowing = parcels.getJSONObject(j).getJSONObject("recommendedSowingPeriod").getJSONObject("hasEnd").get("inXSDDateTimeStamp").toString().split("T")[0];
		    			  dsRList.add(dsR);
		    		  }
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      /*Exporting Records to XML KA format*/
	      for(C2ParcelDatasetRecord dsR:dsRList) {
	    	  rows += "<ROW ParcelID=\"" + dsR.parcelId
	    			  + "\" ParcelName=\"" + dsR.name
	    			  + "\" SoilType=\"" + dsR.soil
	    			  + "\" Area=\"" + dsR.area
	    			  + "\" Crop=\"" + dsR.crop
	    			  + "\" CropStatus=\"" + dsR.cropStatus
	    			  + "\" LastPlantedAt=\"" + dsR.plantedDate
	    			  + "\" BeginningSowing=\"" + dsR.beginningSowing
	    			  + "\" EndingSowing=\"" + dsR.endingSowing
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	
	}
	
	public static String aimReaderForC2_Zone(String urlToRead) throws Exception, JSONException {
		/*Requesting AIM*/
		  StringBuilder aim = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         aim.append(line);
	      }
	      rd.close();
	      
	      String rows = "";
	      rows = "<ROWS>";
	      
	      Map<String, C2Zone> zones = new HashMap<String, C2Zone>();
	      Map<String, C2Intervention> interventions = new HashMap<String, C2Intervention>();
	      
	      try {
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
				  String Elemento = jsonArray.getJSONObject(l).get("@type").toString(); 
				 
				  switch (Elemento) {
				   case "Farm":
		    		  JSONArray parcels = new JSONArray(jsonArray.getJSONObject(l).get("containsPlot").toString());
		    		  for (int j=0; j<parcels.length(); j++) {
		    			  
		    			  JSONArray containsZones = new JSONArray(parcels.getJSONObject(j).get("containsZone").toString());
		    			  JSONArray totalSeeds = new JSONArray(parcels.getJSONObject(j).get("totalSeeds").toString());
		    			  
		    			  for (int x=0; x<containsZones.length(); x++) {
		    				  C2Zone zone = new C2Zone();
		    				  zone.parcelName = parcels.getJSONObject(j).get("name").toString();
		    				  zone.id = containsZones.getJSONObject(x).get("@id").toString();
		    				  zone.wkt = containsZones.getJSONObject(x).getJSONObject("hasGeometry").get("asWKT").toString();
		    				  
		    				  /*Acquisizione quantità per classe e totale*/
		    				  
		    				  for (int y=0; y<totalSeeds.length(); y++) {
			    				  try {
			    					  if (totalSeeds.getJSONObject(y).get("ndviClass").toString().equalsIgnoreCase("1")) {
			    						  zone.class1 = totalSeeds.getJSONObject(y).get("numericValue").toString();
			    					  }
			    					  if (totalSeeds.getJSONObject(y).get("ndviClass").toString().equalsIgnoreCase("2")) {
			    						  zone.class2 = totalSeeds.getJSONObject(y).get("numericValue").toString();
			    					  }
			    					  if (totalSeeds.getJSONObject(y).get("ndviClass").toString().equalsIgnoreCase("3")) {
			    						  zone.class3 = totalSeeds.getJSONObject(y).get("numericValue").toString();
			    					  }  
			    				  }
			    				  catch (JSONException e) {
			    					  zone.total = totalSeeds.getJSONObject(y).get("numericValue").toString();
			    			      }  
			    			  }
			    			  zones.put(zone.id, zone);
		    			  }
		    		  }  
	    		  break;
	    	      case "Intervention":
					  C2Intervention intervention = new C2Intervention(); 
					  intervention.id = jsonArray.getJSONObject(l).get("@id").toString(); 
					  intervention.zoneClass = jsonArray.getJSONObject(l).get("ndviClass").toString();
					  intervention.zoneId = jsonArray.getJSONObject(l).get("interventionZone").toString();
					  intervention.density = jsonArray.getJSONObject(l).get("densityValue").toString();
					  interventions.put(intervention.id, intervention);	     
		    	  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<C2ZoneDatasetRecord> dsRList=new ArrayList<C2ZoneDatasetRecord>();
	      zones.forEach((zId, zObj) -> {
	    	  interventions.forEach((interId, interObj) -> {
	    		  if(interObj.zoneId.equals(zId)) {
	    			  C2ZoneDatasetRecord dsR = new C2ZoneDatasetRecord();
	    			  dsR.parcelName = zObj.parcelName;
	    			  dsR.zoneClass = interObj.zoneClass;
	    			  dsR.density = interObj.density;
	    			  dsR.wkt = zObj.wkt;
	    			  
	    			  /*Acquisizione quantità per classe e totale*/
	    			  dsR.class1 = zObj.class1;
					  dsR.class2 = zObj.class2;
					  dsR.class3 = zObj.class3;
					  dsR.total = zObj.total; 
					  
	    			  dsRList.add(dsR);
	    		  }
	    	  });
	      });
	      
	      /*Exporting Records to XML KA format*/
	      for(C2ZoneDatasetRecord dsR:dsRList) {
	    	  rows += "<ROW ParcelName=\"" + dsR.parcelName
	    			  + "\" ZoneClass=\"" + dsR.zoneClass
	    			  + "\" ZoneClassAsMeasure=\"" + dsR.zoneClass
	    			  + "\" Density=\"" + dsR.density
	    			  
	    			  /*Acquisizione quantità per classe e totale*/
	    			  + "\" Class1=\"" + dsR.class1
	    			  + "\" Class2=\"" + dsR.class2
	    			  + "\" Class3=\"" + dsR.class3
	    			  + "\" Total=\"" + dsR.total
	    			  
	    			  + "\" WKT=\"" + dsR.wkt
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;	
	}
	
	public static String aimReaderForC2_Weather(String urlToRead) throws Exception, JSONException {
		/*Requesting AIM*/
		  StringBuilder aim = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         aim.append(line);
	      }
	      rd.close();
	      
	      String rows = "";
	      rows = "<ROWS>";
	      List<C2WeatherDatasetRecord> dsRList=new ArrayList<C2WeatherDatasetRecord>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Observation":
		    		  C2WeatherDatasetRecord dsR = new C2WeatherDatasetRecord();
	    			  dsR.date = jsonArray.getJSONObject(l).get("phenomenonTime").toString().split("T")[0];
					/*
					 * dsR.precipitation =
					 * jsonArray.getJSONObject(l).get("precipitation").toString(); dsR.temperature =
					 * jsonArray.getJSONObject(l).get("airTemperature").toString(); dsR.windSpeed =
					 * jsonArray.getJSONObject(l).get("windSpeed").toString();
					 */
	    			  
	    			  dsR.precipitation = jsonArray.getJSONObject(l).getJSONObject("hasResult").get("precipitation").toString();
	    			  dsR.temperature = jsonArray.getJSONObject(l).getJSONObject("hasResult").get("airTemperature").toString();
	    			  dsR.windSpeed = jsonArray.getJSONObject(l).getJSONObject("hasResult").get("windSpeed").toString();
	    			  
	    			  dsRList.add(dsR);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      /*Exporting Records to XML KA format*/
	      for(C2WeatherDatasetRecord dsR:dsRList) {
	    	  rows += "<ROW Date=\"" + dsR.date
	    			  + "\" Precipitation=\"" + dsR.precipitation
	    			  + "\" Temperature=\"" + dsR.temperature
	    			  + "\" WindSpeed=\"" + dsR.windSpeed
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	}
}