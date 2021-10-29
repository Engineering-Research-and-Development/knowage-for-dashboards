package it.eng.demeter.a2;

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

import org.joda.time.DateTime;

public class A2Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet{
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			String place = parameters.get("PLACE").toString();
			url = url.replaceAll("\'","");
			place = place.replaceAll("\'","");
			url = url+"/"+place;
			ds = aimReaderForA2(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	public static String aimReaderForA2(String urlToRead) throws Exception, JSONException {
	      
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
	      List<String> features = new ArrayList<String>();
	      Map<String, A2ObservationCollection> observationCollections = new HashMap<String, A2ObservationCollection>();
	      Map<String, A2Observation> observations = new HashMap<String, A2Observation>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "geo:Feature":
		    		  String featureId = jsonArray.getJSONObject(l).get("@id").toString();
		    		  features.add(featureId);
		    		  break;
		    	  case "ObservationCollection":
		    		  A2ObservationCollection ob = new A2ObservationCollection();
		    		  ob.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  ob.featureId = jsonArray.getJSONObject(l).get("hasFeatureOfInterest").toString();
		    		  ob.date = jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0];
		    		  JSONArray members = new JSONArray(jsonArray.getJSONObject(l).get("hasMember").toString());
		    		  for (int j=0; j<members.length(); j++) {
		    			  ob.observationId.add(members.get(j).toString());
		    		  }
		    		  observationCollections.put(ob.id, ob);
		    		  break;
		    	  case "Observation":
		    		  A2Observation qv = new A2Observation();
		    		  qv.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  qv.identifier = jsonArray.getJSONObject(l).get("identifier").toString();
		    		  if(qv.identifier.equals("BBCH")) {
		    			  qv.description = jsonArray.getJSONObject(l).get("description").toString();
		    		  }
		    		  qv.value = jsonArray.getJSONObject(l).getJSONObject("hasResult").get("numericValue").toString();
		    		  observations.put(qv.id,qv);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<A2DatasetRecord> dsRList=new ArrayList<A2DatasetRecord>();
	      List<A2DatasetRecord> dsRListTemp=new ArrayList<A2DatasetRecord>();
	      for(String feature:features) {
	    	  observationCollections.forEach((obcId, obcObj) -> {
	    		  if(obcObj.featureId.equals(feature)) {
	    			  A2DatasetRecord dsR = new A2DatasetRecord();
		    		  DateTime dt = new DateTime(obcObj.date);
		    		  String doy = Integer.toString(dt.getDayOfYear());
		    		  dsR.parcelId = feature;
		    		  dsR.doy = doy;
		    		  dsR.date = obcObj.date;
	    			  observations.forEach((obId, obObj) -> {
	    				  for(String observationID:obcObj.observationId) {
	    					  if(obObj.id.equals(observationID)) {
		    					  boolean doyFound = false;
		    					  String measureName = obObj.identifier;
		    	    			  for(A2DatasetRecord dsRec:dsRListTemp) {
		    	    				  if(doy.equals(dsRec.doy)) {
		    	    					  doyFound = true;
		    	    					  switch (measureName) {
		    	    					  case "BBCH":
		    	    						  dsRec.bbch = obObj.value;
		    	    						  dsRec.description = obObj.description;
		    	    						  break;
		    	    					  case "GDD":
		    	    						  dsRec.gdd = obObj.value;
		    	    						  break;
		    	    					  }
		    	    				  }
		    	    			  }
		    	    			  if (doyFound == false) {
		    	    				  switch (measureName) {
			    					  case "BBCH":
			    						  dsR.bbch = obObj.value;
			    						  dsR.description = obObj.description;
			    						  break;
			    					  case "GDD":
			    						  dsR.gdd = obObj.value;
			    						  break;
			    					  }
		    	    				  dsRListTemp.add(dsR);
		    	    			  }
		    				  }
	    				  }
	    			  });
	    		  }
	    	  });
	    	  dsRList.addAll(dsRListTemp);
		      dsRListTemp.clear();
	      }
	      
	      /*Exporting Records to XML KA format*/
	      for(A2DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW DOY=\"" + dsR.doy
	    			  + "\" Parcel=\"" + dsR.parcelId
	    			  + "\" BBCH=\"" + dsR.bbch
	    			  + "\" GDD=\"" + dsR.gdd
	    			  + "\" Description=\"" + dsR.description
	    			  + "\" Date=\"" + dsR.date
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	   }
}
