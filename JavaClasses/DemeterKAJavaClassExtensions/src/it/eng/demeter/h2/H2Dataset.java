package it.eng.demeter.h2;

import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class H2Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet{
	
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
			//String url_in = "http://localhost:9080/TestBenchmark/test/H2/TestIN";
			//String url_out = "http://localhost:9080/TestBenchmark/test/H2/TestOUT";
			url_in = url_in.replaceAll("\'","");
			url_out = url_out.replaceAll("\'","");
			ds = aimReaderForH2(url_in,url_out);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForH2(String url_inToRead,String url_outToRead) throws Exception, JSONException {
	      
		  String producer = "";
		  String poultryType = "";
		  String certificates = "";
		  String condition = "";
		  String outCondition = "";
		  
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
		    	  case "Flock":
		    		  certificates = jsonArray.getJSONObject(l).get("certificates").toString();
		    		  break;
		    	  case "StressOrPressure":
		    		  producer  = jsonArray.getJSONObject(l).getJSONObject("placeOfProduction").get("name").toString();
		    		  poultryType = jsonArray.getJSONObject(l).get("poultryType").toString();
		    		  break;
		    	  case "Transport":
		    		  condition  = jsonArray.getJSONObject(l).get("transportCondition").toString();
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      // READ OUTPUT
		  //Requesting AIM
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
	    	  
		      //Convert AIM to JSON
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      //Reading elements
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Transport":
		    		  outCondition = jsonArray.getJSONObject(l).get("transportCondition").toString();
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      
	      /*Exporting Records to XML KA format*/
    	  rows += "<ROW Producer=\"" + producer
    			  + "\" PoultryType=\"" + poultryType
    			  + "\" Certificates=\"" + certificates
    			  + "\" TransportCondition=\"" + condition
    			  + "\" OutputCondition=\"" + outCondition
    			  + "\"/>";
	      rows += "</ROWS>";
	      return rows;
	   }

}
