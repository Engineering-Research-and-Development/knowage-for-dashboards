package it.eng.demeter.b1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class B1Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
	//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
	//private static LocalDateTime now = LocalDateTime.now();
	
	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/B1/Test";
			url = url.replaceAll("\'","");
			ds = aimReaderForB1(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}*/
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForB1(String urlToRead) throws Exception, JSONException {
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
	    Map<String, B1ParcelData> parcels = new HashMap<String, B1ParcelData>();
	    Map<String, B1ForecastingData> forecasts = new HashMap<String, B1ForecastingData>();
	    Map<String, String> measures = new HashMap<String, String>();
	    Map<String, String> images = new HashMap<String, String>();
	    Map<String, B1ObservationGroupData> obsGroups = new HashMap<String, B1ObservationGroupData>();
	    Map<String, B1ObservationData> obs = new HashMap<String, B1ObservationData>();
	      
	    try {
	    	/*Convert AIM to JSON*/
		    JSONObject jsonObject = new JSONObject(aim.toString());  
		    JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		    
		    /*Reading elements*/
	        for (int l=0; l<jsonArray.length(); l++){
	        	String elemento = jsonArray.getJSONObject(l).get("@type").toString();
	        	switch (elemento) {
	        	case "AgriParcel":
		    		  B1ParcelData pd = new B1ParcelData();
	    			  pd.id = jsonArray.getJSONObject(l).get("@id").toString();
	    			  pd.plot = jsonArray.getJSONObject(l).get("name").toString();
		        	  parcels.put(pd.id,pd);
		    		  break;	  
	        	case "Forecasting":
	        		B1ForecastingData fd = new B1ForecastingData();
	        		fd.id = jsonArray.getJSONObject(l).get("@id").toString();
	        		fd.parcelID = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
	        		fd.date = jsonArray.getJSONObject(l).get("dateObserved").toString().split("T")[0];
	        		JSONArray measuresIdArray = new JSONArray(jsonArray.getJSONObject(l).get("hasResult").toString());
	        		for (int j=0; j<measuresIdArray.length(); j++) {
	        			fd.measuresID.add(measuresIdArray.getJSONObject(j).get("@id").toString());
	        		}
	        		forecasts.put(fd.id, fd);
	        		break;
	        	case "QuantityValue":
	        		String measureId = jsonArray.getJSONObject(l).get("@id").toString();
	        		String measureValue = jsonArray.getJSONObject(l).get("numericValue").toString();
	        		measures.put(measureId, measureValue);
	        		break;
	        	case "Image":
	        		String imgId = jsonArray.getJSONObject(l).get("@id").toString();
	        		String imgUrl = jsonArray.getJSONObject(l).get("url").toString();
	        		images.put(imgId, imgUrl);
	        		break;
	        	case "ObservationCollection":
	        		B1ObservationGroupData obsGD = new B1ObservationGroupData();
	        		obsGD.id = jsonArray.getJSONObject(l).get("@id").toString();
	        		obsGD.propertyID = jsonArray.getJSONObject(l).getJSONObject("observedProperty").get("@id").toString();
	        		obsGD.parcelID = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
	        		JSONArray obsIdArray = new JSONArray(jsonArray.getJSONObject(l).get("hasMember").toString());
	        		for (int j=0; j<obsIdArray.length(); j++) {
	        			obsGD.observationElementID.add(obsIdArray.getJSONObject(j).get("@id").toString());
	        		}
	        		obsGroups.put(obsGD.id, obsGD);
	        		break;
	        	case "Observation":
	        		B1ObservationData obsD = new B1ObservationData();
	        		obsD.id = jsonArray.getJSONObject(l).get("@id").toString();
	        		JSONArray obsResultArray = new JSONArray(jsonArray.getJSONObject(l).get("hasResult").toString());
	        		for (int j=0; j<obsResultArray.length(); j++) {
	        			obsD.value = obsResultArray.getJSONObject(j).get("numericValue").toString();
	        		}
	        		obsD.date = jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0];
	        		obs.put(obsD.id, obsD);
	        		break;
	        	}
	        }
	    }
	    catch (JSONException e) {
	    	e.printStackTrace();
	    	logger.error(e.getMessage(), e.getCause());
	    }
	    
	    /*Creating records for dataset*/
	    List<B1DatasetRecord> dsRList = new ArrayList<B1DatasetRecord>();
	    List<B1DatasetRecord> dsRListTemp = new ArrayList<B1DatasetRecord>();
	    parcels.forEach((parcelKey, parcel) -> {
	    	forecasts.forEach((foId,foObj) -> {
	    		if(foObj.parcelID.equals(parcelKey)) {
	    			B1DatasetRecord dsR = new B1DatasetRecord();
	    			dsR.parcelID = parcelKey;
	    			dsR.predictionDate = foObj.date;
	    			dsR.plot = parcel.plot;
	    			measures.forEach((mId,mVal) -> {
	    				if(foObj.measuresID.contains(mId)) {
	    					String measureName = mId.split("Forecasting:")[1];
	    					switch (measureName) {
	    					case "irrigationPrediction":
	    						dsR.irrigationWaterEstimation = mVal;
	    						break;
	    					case "et0":
	    						dsR.estimatedEto = mVal;
	    						break;
	    					case "rainwaterPrediction":
	    						dsR.rainwaterForecast = mVal;
	    						break;
	    					case "avgSoilMoisturePrediction":
	    						dsR.estimatedAvgSoilMoisture = mVal;
	    						break;
	    					case "avgSoilMoistureProbe":
	    						dsR.probeAvgSoilMoisture = mVal;
	    						break;
	    					}
	    				}
	    			});
	    			images.forEach((imgId,imgVal) -> {
	    				if(foObj.measuresID.contains(imgId)) {
	    					String imageName = imgId.split("Forecasting:img:")[1];
	    					switch (imageName) {
	    					case "soilMoistureImg":
	    						dsR.soilMoistureEstimation = imgVal;
	    						break;
	    					case "anomaliesImg":
	    						dsR.soilMoistureLocalColorRange = imgVal;
	    						break;
	    					case "soilMoistureImgLCR":
	    						dsR.cropAnomaliesDetection = imgVal;
	    						break;	
	    					case "anomaliesImgLCR":
	    						dsR.cropAnomaliesLocalColorRange = imgVal;
	    						break;
	    					}
	    				}
	    			});
	    			dsRListTemp.add(dsR);
	    		}
	    	});
	    	obsGroups.forEach((obGId,obGObj) -> {
	    		if (obGObj.parcelID.equals(parcelKey)) {
		    		obs.forEach((obId,obObj) -> {
		    			if(obGObj.observationElementID.contains(obId)) {
		    				boolean dateFound = false;
		    				for (B1DatasetRecord dsRec : dsRListTemp){
		    					if (dsRec.parcelID.equals(parcelKey)) {
		    						if (dsRec.obsDate.equals(obObj.date)) {
		    							dateFound = true;
		    							switch (obGObj.id.split("ObservationCollection.")[1]) {
		    							case "1":
		    								dsRec.irrigation = obObj.value;
		    								break;
		    							case "2":
		    								dsRec.irrigationPrediction = obObj.value;
		    								break;	
		    							case "3":
		    								dsRec.et0 = obObj.value;
		    								break;
		    							case "4":
		    								dsRec.et0Prediction = obObj.value;
		    								break;
		    							case "5":
		    								dsRec.rainwater = obObj.value;
		    								break;
		    							case "6":
		    								dsRec.avgSoilMoisturePrediction = obObj.value;
		    								break;
		    							case "7":
		    								dsRec.avgSoilMoistureProbe = obObj.value;
		    								break;	
		    							}
		    							break;
		    						}
		    					} 
		    				}
		    				if (dateFound == false) {
	    						B1DatasetRecord dsR = new B1DatasetRecord();
    							dsR.parcelID = parcelKey;
    							dsR.obsDate = obObj.date;
    							switch (obGObj.id.split("ObservationCollection.")[1]) {
    							case "1":
    								dsR.irrigation = obObj.value;
    								break;
    							case "2":
    								dsR.irrigationPrediction = obObj.value;
    								break;	
    							case "3":
    								dsR.et0 = obObj.value;
    								break;
    							case "4":
    								dsR.et0Prediction = obObj.value;
    								break;
    							case "5":
    								dsR.rainwater = obObj.value;
    								break;
    							case "6":
    								dsR.avgSoilMoisturePrediction = obObj.value;
    								break;
    							case "7":
    								dsR.avgSoilMoistureProbe = obObj.value;
    								break;	
    							}
    							
    							dsRListTemp.add(dsR);
	    					}
		    			}
		    		});
	    		}
	    	});
	    	dsRList.addAll(dsRListTemp);
	    	dsRListTemp.clear();
	    });
	    
	    LocalDateTime now = LocalDateTime.now();
	    /*Exporting Records to XML KA format*/
	    for(B1DatasetRecord dsR:dsRList) {
	    	rows += "<ROW ParcelID=\"" + dsR.parcelID.split("AgriParcel:")[1]
	    			  + "\" Plot=\"" + dsR.plot
	    			  + "\" CurrentDate=\"" + now.toString().split("T")[0]
	    			  + "\" PredictionDate=\"" + dsR.predictionDate
	    			  + "\" IrrigationWaterEstimation=\"" + dsR.irrigationWaterEstimation
	    			  + "\" EstimatedCropWaterNeeds=\"" + dsR.estimatedCropWaterNeeds
	    			  + "\" EstimatedEto=\"" + dsR.estimatedEto
	    			  + "\" RainwaterForecast=\"" + dsR.rainwaterForecast
	    			  + "\" EstimatedAvgSoilMoisture=\"" + dsR.estimatedAvgSoilMoisture
	    			  + "\" ProbeAvgSoilMoisture=\"" + dsR.probeAvgSoilMoisture
	    			  + "\" SoilMoistureEstimation=\"" + dsR.soilMoistureEstimation
	    			  + "\" SoilMoistureLocalColorRange=\"" + dsR.soilMoistureLocalColorRange
	    			  + "\" CropAnomaliesDetection=\"" + dsR.cropAnomaliesDetection
	    			  + "\" CropAnomaliesLocalColorRange=\"" + dsR.cropAnomaliesLocalColorRange
	    			  + "\" ObservationDate=\"" + dsR.obsDate
	    			  + "\" Irrigation=\"" + dsR.irrigation
	    			  + "\" IrrigationPrediction=\"" + dsR.irrigationPrediction
	    			  + "\" Et0=\"" + dsR.et0
	    			  + "\" Et0Prediction=\"" + dsR.et0Prediction
	    			  + "\" Rainwater=\"" + dsR.rainwater
	    			  + "\" AvgSoilMoisturePrediction=\"" + dsR.avgSoilMoisturePrediction  
	    			  + "\" AvgSoilMoistureProbe=\"" + dsR.avgSoilMoistureProbe
	    			  + "\"/>";
	    }
		rows += "</ROWS>";
		
	    return rows;
	}
}