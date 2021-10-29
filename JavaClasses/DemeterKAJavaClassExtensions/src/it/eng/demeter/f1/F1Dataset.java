package it.eng.demeter.f1;

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

public class F1Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet {
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}
	
	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/F1/Test";
			url = url.replaceAll("\'","");
			ds = aimReaderForF1(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForF1(String urlToRead) throws Exception, JSONException {
		  
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
	      List<String> animalIDs = new ArrayList<String>();
	      Map<String, List<String>> milkYieldPredictionsCollection = new HashMap<String, List<String>>();
	      Map<String, List<String>> lactationObservationsCollection = new HashMap<String, List<String>>();
	      Map<String, F1Observation> observationsCollection = new HashMap<String, F1Observation>();
	      Map<String, F1DailyYieldPrediction> dailyYieldPredictionCollection = new HashMap<String, F1DailyYieldPrediction>();
	      Map<String, F1QuantityValue> quantityValueCollection = new HashMap<String, F1QuantityValue>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (elemento) {
		    	  case "Animal":
		    		  String animalID = jsonArray.getJSONObject(l).get("@id").toString();
		    		  animalIDs.add(animalID);
		    		  break;
		    	  case "MilkYieldPredictionsCollection":
		    		  List<String> predictionsResultsIDs = new ArrayList<String>();
		    		  String predictionsRelatedAnimalID = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
		    		  JSONArray predictionsResults = new JSONArray(jsonArray.getJSONObject(l).get("milkYieldPrediction").toString());
		    		  for (int j=0; j<predictionsResults.length(); j++) {
		    			  predictionsResultsIDs.add(predictionsResults.getJSONObject(j).get("@id").toString());
		    		  }
		    		  milkYieldPredictionsCollection.put(predictionsRelatedAnimalID, predictionsResultsIDs);
		    		  break;
		    	  case "LactationObservationsCollection":
		    		  List<String> observationsResultsIDs = new ArrayList<String>();
		    		  String observationsRelatedAnimalID = jsonArray.getJSONObject(l).get("hasFeatureOfInterest").toString();
		    		  JSONArray observationsResults = new JSONArray(jsonArray.getJSONObject(l).get("milkingEvent").toString());
		    		  for (int j=0; j<observationsResults.length(); j++) {
		    			  observationsResultsIDs.add(observationsResults.getString(j));
		    		  }
		    		  lactationObservationsCollection.put(observationsRelatedAnimalID, observationsResultsIDs);
		    		  break;
		    	  case "Observation":
		    		  String obsProperty = jsonArray.getJSONObject(l).get("observedProperty").toString().split("#")[1];
		    		  if (obsProperty.equalsIgnoreCase("milkingWeight")) {
		    			  F1Observation observation = new F1Observation();
		    			  observation.id = jsonArray.getJSONObject(l).get("@id").toString();
		    			  observation.propertyName = obsProperty;
		    			  observation.quantityValueId = jsonArray.getJSONObject(l).get("hasResult").toString();
		    			  observation.date = jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0];
		    			  observationsCollection.put(observation.id, observation);
		    		  } else {
		    			 break;
		    		  }
		    		  break;
		    	  case "DailyYieldPrediction":
		    		  String predProperty = jsonArray.getJSONObject(l).get("predictedProperty").toString().split("#")[1];
		    		  if (predProperty.equalsIgnoreCase("milkingWeight")) {
		    			  F1DailyYieldPrediction prediction = new F1DailyYieldPrediction();
		    			  prediction.id = jsonArray.getJSONObject(l).get("@id").toString();
		    			  prediction.propertyName = predProperty;
		    			  prediction.quantityValueId = jsonArray.getJSONObject(l).getJSONObject("hasResult").get("@id").toString();
		    			  prediction.date = jsonArray.getJSONObject(l).get("predictionTime").toString().split("T")[0];
		    			  dailyYieldPredictionCollection.put(prediction.id, prediction);
		    		  } else {
		    			 break;
		    		  }
		    		  break;
		    	  case "QuantityValue":
		    		  F1QuantityValue quantityValue = new F1QuantityValue();
		    		  quantityValue.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  quantityValue.numericValue = jsonArray.getJSONObject(l).get("numericValue").toString();
		    		  if (jsonArray.getJSONObject(l).has("lowerConfidenceValue")) {
		    			  quantityValue.lowerConfidenceValue = jsonArray.getJSONObject(l).get("lowerConfidenceValue").toString();
		    		  }
		    		  if (jsonArray.getJSONObject(l).has("upperConfidenceValue")) {
		    			  quantityValue.upperConfidenceValue = jsonArray.getJSONObject(l).get("upperConfidenceValue").toString();
		    		  }
		    		  quantityValueCollection.put(quantityValue.id, quantityValue);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<F1DatasetRecord> dsRList = new ArrayList<F1DatasetRecord>();
	      
	      for(String aID:animalIDs) {
	    	  // Reading Observations for the current Animal ID
	    	  lactationObservationsCollection.forEach((obsColAnimalID, obsResultIDs) -> {
	    		  if (obsColAnimalID.equals(aID)) {
	    			  for (String resultID:obsResultIDs) {
		    			  observationsCollection.forEach((obsID, obsObj) -> {
		    				  if (obsID.equals(resultID)) {
		    					  quantityValueCollection.forEach((qvID, qvObj) -> {
		    						  if (qvID.equals(obsObj.quantityValueId)) {
		    							  F1DatasetRecord dsR = new F1DatasetRecord();
		    							  dsR.animalId = aID.split(":")[3];
		    							  dsR.date = obsObj.date;
		    							  dsR.milk = qvObj.numericValue;
		    							  dsRList.add(dsR);
		    						  }
		    					  });
		    				  }
		    			  });
	    			  }
	    		  }
	    	  });
	    	  
	    	  // Reading Predictions for the current Animal ID
	    	  milkYieldPredictionsCollection.forEach((predColAnimalID, predResultIDs) -> {
	    		  if (predColAnimalID.equals(aID)) {
	    			  for (String resultID:predResultIDs) {
		    			  dailyYieldPredictionCollection.forEach((predID, predObj) -> {
		    				  if (predID.equals(resultID)) {
		    					  quantityValueCollection.forEach((qvID, qvObj) -> {
		    						  if (qvID.equals(predObj.quantityValueId)) {
		    							  F1DatasetRecord dsR = new F1DatasetRecord();
		    							  dsR.animalId = aID.split(":")[3];
		    							  dsR.date = predObj.date;
		    							  dsR.milk = qvObj.numericValue;
		    							  dsR.lower = qvObj.lowerConfidenceValue;
		    							  dsR.upper = qvObj.upperConfidenceValue;
		    							  dsRList.add(dsR);
		    						  }
		    					  });
		    				  }
		    			  });
	    			  }
	    		  }
	    	  });	  
	      }
	      
	      /*Exporting Records to XML KA format*/
	      for(F1DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW AnimalID=\"" + dsR.animalId
	    			  + "\" Date=\"" + dsR.date
	    			  + "\" Milk=\"" + dsR.milk
	    			  + "\" Upper=\"" + dsR.upper
	    			  + "\" Lower=\"" + dsR.lower
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      return rows;
	   }

}
