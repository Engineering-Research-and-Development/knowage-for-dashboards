package it.eng.demeter.g2;

import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class G2Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet{
	
	static protected Logger logger = Logger.getLogger(G2Dataset.class);
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url_in = parameters.get("URL_INPUT").toString();
			String url_out = parameters.get("URL_OUTPUT").toString();
			//String url_in = "http://localhost:9080/TestBenchmark/test/G2/TestIN";
			//String url_out = "http://localhost:9080/TestBenchmark/test/G2/TestOUT";
			url_in = url_in.replaceAll("\'","");
			url_out = url_out.replaceAll("\'","");
			ds = aimReaderForG2(url_in,url_out);
			//System.out.println(ds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForG2(String url_inToRead,String url_outToRead) throws Exception, JSONException {
	      
		  String airTemp = "";
		  String humidity = "";
		  String airFlow = "";
		  String light = "";
		  String co2 = "";
		  String power = "";
		  String animalSpecies = "";
		  String avgAge = "";
		  String stressLevel = "";
		  String instructions = "";
		  
		  // READ INPUT
		  /*Requesting AIM*/
		  StringBuilder aim = new StringBuilder();
	      URL url = new URL(url_inToRead);
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
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Device":
		    		  airTemp = jsonArray.getJSONObject(l).get("airTemperature").toString();
		    		  humidity = jsonArray.getJSONObject(l).get("airHumidity").toString();
		    		  airFlow = jsonArray.getJSONObject(l).get("airFlow").toString();
		    		  light = jsonArray.getJSONObject(l).get("lightIntensity").toString();
		    		  co2 = jsonArray.getJSONObject(l).get("airCO2").toString();
		    		  power = jsonArray.getJSONObject(l).get("powerLoses").toString();
		    		  break;
		    	  case "AnimalGroup":
		    		  animalSpecies = jsonArray.getJSONObject(l).get("animalSpecies").toString();
		    		  avgAge = jsonArray.getJSONObject(l).get("flockAverageAge").toString();
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  logger.error(e.getMessage(), e.getCause());
	    	  e.printStackTrace();
	      }
	      
	      // READ OUTPUT
		  /*Requesting AIM*/
		  aim = new StringBuilder();
	      url = new URL(url_outToRead);
	      conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      line = "";
	      while ((line = rd.readLine()) != null) {
	         aim.append(line);
	      }
	      rd.close();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "StressOrPressure":
		    		  stressLevel = jsonArray.getJSONObject(l).get("stressLevel").toString();
		    		  instructions = jsonArray.getJSONObject(l).get("safetyInstructions").toString();
		    		  break;
		    	  case "AnimalGroup":
		    		  //animalSpecies = jsonArray.getJSONObject(l).get("animalSpecies").toString();
		    		  //avgAge = jsonArray.getJSONObject(l).get("flockAverageAge").toString();
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  logger.error(e.getMessage(), e.getCause());
	    	  e.printStackTrace();
	      }
	      
	      
	      /*Exporting Records to XML KA format*/
    	  rows += "<ROW AirTemperature=\"" + airTemp
    			  + "\" AirFlow=\"" + airFlow
    			  + "\" Humidity=\"" + humidity
    			  + "\" LightIntensity=\"" + light
    			  + "\" CO2=\"" + co2
    			  + "\" PowerLoses=\"" + power
    			  + "\" AnimalSpecies=\"" + animalSpecies
    			  + "\" AverageAge=\"" + avgAge
    			  + "\" SafetyInstructions=\"" + instructions
    			  + "\" StressLevel=\"" + stressLevel
    			  + "\"/>";
	      rows += "</ROWS>";
	      return rows;
	   }
}