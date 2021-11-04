package it.eng.demeter.e2;

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

public class E2Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet {
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}
	
	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/E2/Test";
			url = url.replaceAll("\'","");
			ds = aimReaderForE2(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForE2(String urlToRead) throws Exception, JSONException {
	      
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
	      Map<String, E2Observation> obsData = new HashMap<String, E2Observation>();
	      Map<String, E2ObservationQuantityValue> obsQtyValues = new HashMap<String, E2ObservationQuantityValue>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Observation":
		    		  E2Observation od = new E2Observation();
		    		  od.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  od.date = jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0];
		    		  JSONArray obsHasResultsjsonArray = new JSONArray(jsonArray.getJSONObject(l).get("hasResult").toString());
		    		  for (int j=0; j<obsHasResultsjsonArray.length(); j++) {
		    			  od.obsQuantityId = obsHasResultsjsonArray.getJSONObject(j).get("@id").toString();
		    		  }
		    		  obsData.put(od.id, od);
		    		  break;
		    	  case "QuantityValue":
		    		  E2ObservationQuantityValue qv = new E2ObservationQuantityValue();
		    		  qv.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  qv.stageDesc = jsonArray.getJSONObject(l).get("description").toString();
		    		  qv.value = jsonArray.getJSONObject(l).get("numericValue").toString();
		    		  obsQtyValues.put(qv.id, qv);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<E2DatasetRecord> dsRList=new ArrayList<E2DatasetRecord>();
	      obsData.forEach((obsId, obsObj) -> {
	    	  obsQtyValues.forEach((qvId, qvObj) -> {
	    		  if(qvId.equals(obsObj.obsQuantityId)) {
	    			  E2DatasetRecord dsR = new E2DatasetRecord();
	    			  dsR.date = obsObj.date;
	    			  dsR.stageDescription = qvObj.stageDesc;
	    			  dsR.value = qvObj.value;
	    			  dsRList.add(dsR);
	    		  }
	    		  
	    	  });
	    	  
	      });
	      
	      /*Exporting Records to XML KA format*/
	      for(E2DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW Date=\"" + dsR.date
	    			  + "\" Stage=\"" + dsR.stageDescription
	    			  + "\" Value=\"" + dsR.value
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      //obsData.forEach((key, value) -> {System.out.println(key + ":" + value);});
	      //obsQtyValues.forEach((key, value) -> System.out.println(key + ":" + value));
	      return rows;
	   }

}
