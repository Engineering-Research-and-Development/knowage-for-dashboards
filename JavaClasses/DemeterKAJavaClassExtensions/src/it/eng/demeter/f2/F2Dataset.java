package it.eng.demeter.f2;

import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class F2Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet {
	
	static protected Logger logger = Logger.getLogger(F2Dataset.class);
	
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
			//String url_in = "http://localhost:9080/TestBenchmark/test/F2/TestIN";
			//String url_out = "http://localhost:9080/TestBenchmark/test/F2/TestOUT";
			url_in = url_in.replaceAll("\'","");
			url_out = url_out.replaceAll("\'","");
			ds = aimReaderForF2(url_in,url_out);
			//System.out.println(ds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForF2(String url_inToRead,String url_outToRead) throws Exception, JSONException {
	      
		  String foodType = "";
		  double silosVolume = 0;
		  double silosFoodDensity = 0;
		  String silosProgress = "";
		  String quality = "";
		  
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
		    	  case "Silos":
		    		  foodType = jsonArray.getJSONObject(l).get("silosFoodType").toString();
		    		  silosVolume = jsonArray.getJSONObject(l).getDouble("silosVolume");
		    		  silosFoodDensity = jsonArray.getJSONObject(l).getDouble("silosFoodDensity");
		    		  double prog = (silosFoodDensity * 100)/silosVolume;
	    			  DecimalFormat df2 = new DecimalFormat("0.00");
	    			  silosProgress = df2.format(prog);
	    			  silosProgress = silosProgress.replace(",",".");
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
		    	  case "AnimalFeeding":
		    		  quality = jsonArray.getJSONObject(l).get("animalFeedingQuality").toString();
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  logger.error(e.getMessage(), e.getCause());
	    	  e.printStackTrace();
	      }
	      
	      
	      /*Exporting Records to XML KA format*/
    	  rows += "<ROW FoodType=\"" + foodType
    			  + "\" FeedingQuality=\"" + quality
    			  + "\" SilosProgress=\"" + silosProgress
    			  + "\"/>";
	      rows += "</ROWS>";
	      return rows;
	   }

}
