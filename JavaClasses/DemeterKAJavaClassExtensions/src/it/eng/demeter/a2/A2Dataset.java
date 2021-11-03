package it.eng.demeter.a2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class A2Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
	/*@Override
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
	}*/
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForA2(String urlToRead) throws Exception, JSONException {
	      
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
		    		  ob.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  ob.setFeatureId(jsonArray.getJSONObject(l).get("hasFeatureOfInterest").toString());
		    		  ob.setDate(jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0]);
		    		  JSONArray members = new JSONArray(jsonArray.getJSONObject(l).get("hasMember").toString());
		    		  for (int j=0; j<members.length(); j++) {
		    			  ob.addObservationId(members.get(j).toString());
		    		  }
		    		  observationCollections.put(ob.getId(), ob);
		    		  break;
		    	  case "Observation":
		    		  A2Observation qv = new A2Observation();
		    		  qv.setId(jsonArray.getJSONObject(l).get("@id").toString());
		    		  qv.setIdentifier(jsonArray.getJSONObject(l).get("identifier").toString());
		    		  if(qv.getIdentifier().equals("BBCH")) {
		    			  qv.setDescription(jsonArray.getJSONObject(l).get("description").toString());
		    		  }
		    		  qv.setValue(jsonArray.getJSONObject(l).getJSONObject("hasResult").get("numericValue").toString());
		    		  observations.put(qv.getId(),qv);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  logger.error(e.getMessage(), e.getCause());
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<A2DatasetRecord> dsRList=new ArrayList<A2DatasetRecord>();
	      List<A2DatasetRecord> dsRListTemp=new ArrayList<A2DatasetRecord>();
	      for(String feature:features) {
	    	  observationCollections.forEach((obcId, obcObj) -> {
	    		  if(obcObj.getFeatureId().equals(feature)) {
	    			  A2DatasetRecord dsR = new A2DatasetRecord();
		    		  DateTime dt = new DateTime(obcObj.getDate());
		    		  String doy = Integer.toString(dt.getDayOfYear());
		    		  dsR.setParcelId(feature);
		    		  dsR.setDoy(doy);
		    		  dsR.setDate(obcObj.getDate());
	    			  observations.forEach((obId, obObj) -> {
	    				  for(String observationID:obcObj.getObservationId()) {
	    					  if(obObj.getId().equals(observationID)) {
		    					  boolean doyFound = false;
		    					  String measureName = obObj.getIdentifier();
		    	    			  for(A2DatasetRecord dsRec:dsRListTemp) {
		    	    				  if(doy.equals(dsRec.getDoy())) {
		    	    					  doyFound = true;
		    	    					  switch (measureName) {
		    	    					  case "BBCH":
		    	    						  dsRec.setBbch(obObj.getValue());
		    	    						  dsRec.setDescription(obObj.getDescription());
		    	    						  break;
		    	    					  case "GDD":
		    	    						  dsRec.setGdd(obObj.getValue());
		    	    						  break;
		    	    					  }
		    	    				  }
		    	    			  }
		    	    			  if (doyFound == false) {
		    	    				  switch (measureName) {
			    					  case "BBCH":
			    						  dsR.setBbch(obObj.getValue());
			    						  dsR.setDescription(obObj.getDescription());
			    						  break;
			    					  case "GDD":
			    						  dsR.setGdd(obObj.getValue());
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
	    	  rows += "<ROW DOY=\"" + dsR.getDoy()
	    			  + "\" Parcel=\"" + dsR.getParcelId()
	    			  + "\" BBCH=\"" + dsR.getBbch()
	    			  + "\" GDD=\"" + dsR.getGdd()
	    			  + "\" Description=\"" + dsR.getDescription()
	    			  + "\" Date=\"" + dsR.getDate()
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	   }
	
}
