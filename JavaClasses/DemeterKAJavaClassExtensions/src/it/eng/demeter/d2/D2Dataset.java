package it.eng.demeter.d2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class D2Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/D2/Test";
			url = url.replaceAll("\'","");
			ds = aimReaderForD2(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}*/
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//private static String aimReaderForD2(String urlToRead) throws Exception, JSONException {
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
	    List<String> vehicles = new ArrayList<String>();
	    List<String> behaviours = new ArrayList<String>();
	    List<D2MachineTrajectoryData> trajectories = new ArrayList<D2MachineTrajectoryData>();
	      
	    try {
	    	/*Convert AIM to JSON*/
		    JSONObject jsonObject = new JSONObject(aim.toString());  
		    JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		    
		    /*Reading elements*/
	        for (int l=0; l<jsonArray.length(); l++){
	        	String elemento = jsonArray.getJSONObject(l).get("@type").toString();
	        	switch (elemento) {
	        	case "Vehicle":
	        		String vehicleId = jsonArray.getJSONObject(l).get("identifier").toString();
	        		vehicles.add(vehicleId);
	        		break;
	        	case "DriverBehaviour":
	        		String behaviour = jsonArray.getJSONObject(l).get("driverBehaviourValue").toString();
	        		behaviours.add(behaviour);
	        		break;
	        	case "VehicleTrajectory":
	        		D2MachineTrajectoryData mtd = new D2MachineTrajectoryData();
	        		mtd.duration = jsonArray.getJSONObject(l).get("trajectoryDuration").toString();
	        		mtd.distance = jsonArray.getJSONObject(l).get("trajectoryDistance").toString();
	        		mtd.avgSpeed = jsonArray.getJSONObject(l).get("trajectoryAverageSpeed").toString();
	        		trajectories.add(mtd);
	        		break;
	        	}
	        }
	    }
	    catch (JSONException e) {
	    	logger.error(e.getMessage(), e.getCause());
	    	e.printStackTrace();
	    }
	    
	    /*Creating records for dataset*/
	    List<D2DatasetRecord> dsRList = new ArrayList<D2DatasetRecord>();
	    for(String v:vehicles) {
	    	D2DatasetRecord dsR = new D2DatasetRecord();
	    	dsR.vehicleId = v;
	    	for(String b:behaviours) {
	    		dsR.behaviour = b;
	    	}
	    	for(D2MachineTrajectoryData trajectoryObj:trajectories) {
	    			dsR.duration = trajectoryObj.duration;
	    			dsR.distance = trajectoryObj.distance;
	    			dsR.avgSpeed = trajectoryObj.avgSpeed;
	    	}
	    	dsRList.add(dsR);
	    }
	    
	    /*Exporting Records to XML KA format*/
	    for(D2DatasetRecord dsR:dsRList) {
	    	rows += "<ROW VehicleID=\"" + dsR.vehicleId
	    			  + "\" DriverBehaviour=\"" + dsR.behaviour
	    			  + "\" Distance=\"" + dsR.distance
	    			  + "\" Duration=\"" + dsR.duration
	    			  + "\" AvgSpeed=\"" + dsR.avgSpeed
	    			  + "\"/>";
	    }
		rows += "</ROWS>";

	    return rows;		
	}
	
	
	// IF AIM EXPOSE MULTIPLE VEHICLES USE THIS (MODIFY THE AIM READING IF NECESSARY)
//	public static String aimReaderForD2(String urlToRead) throws Exception, JSONException {
//		//Requesting AIM
//		StringBuilder aim = new StringBuilder();
//	    URL url = new URL(urlToRead);
//	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	    conn.setRequestMethod("GET");
//	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//	    String line;
//	    while ((line = rd.readLine()) != null) {
//	       aim.append(line);
//	    }
//	    rd.close();
//	      
//	    String rows = "";
//	    rows = "<ROWS>";
//	    Map<String, String> vehicles = new HashMap<String, String>();
//	    Map<String, String> behaviours = new HashMap<String, String>();
//	    Map<String, D2MachineTrajectoryData> trajectories = new HashMap<String, D2MachineTrajectoryData>();
//	      
//	    try {
//	    	//Convert AIM to JSON
//		    JSONObject jsonObject = new JSONObject(aim.toString());  
//		    JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
//		    
//		    //Reading elements
//	        for (int l=0; l<jsonArray.length(); l++){
//	        	String elemento = jsonArray.getJSONObject(l).get("@type").toString();
//	        	switch (elemento) {
//	        	case "Vehicle":
//	        		String elementId = jsonArray.getJSONObject(l).get("@id").toString();
//	        		String vehicleId = jsonArray.getJSONObject(l).get("identifier").toString();
//	        		vehicles.put(elementId,vehicleId);
//	        		break;
//	        	case "DriverBehaviour":
//	        		String hasFeatureId = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
//	        		String behaviour = jsonArray.getJSONObject(l).get("driverBehaviourValue").toString();
//	        		behaviours.put(hasFeatureId,behaviour);
//	        		break;
//	        	case "VehicleTrajectory":
//	        		D2MachineTrajectoryData mtd = new D2MachineTrajectoryData();
//	        		mtd.id = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
//	        		mtd.duration = jsonArray.getJSONObject(l).get("trajectoryDuration").toString();
//	        		mtd.distance = jsonArray.getJSONObject(l).get("trajectoryDistance").toString();
//	        		mtd.avgSpeed = jsonArray.getJSONObject(l).get("trajectoryAverageSpeed").toString();
//	        		trajectories.put(mtd.id, mtd);
//	        		break;
//	        	}
//	        }
//	    }
//	    catch (JSONException e) {
//	    	e.printStackTrace();
//	    }
//	    
//	    //Creating records for dataset
//	    List<D2DatasetRecord> dsRList = new ArrayList<D2DatasetRecord>();
//	    vehicles.forEach((vehicleKey, vehicleId) -> {
//	    	D2DatasetRecord dsR = new D2DatasetRecord();
//	    	dsR.vehicleId = vehicleId;
//	    	behaviours.forEach((behaviourId,behaviourValue) -> {
//	    		if(behaviourId.equals(vehicleKey)) {
//	    			dsR.behaviour = behaviourValue;
//	    		}
//	    	});
//	    	trajectories.forEach((trajectoryId,trajectoryObj) -> {
//	    		if(trajectoryObj.id.equals(vehicleKey)) {
//	    			dsR.duration = trajectoryObj.duration;
//	    			dsR.distance = trajectoryObj.distance;
//	    			dsR.avgSpeed = trajectoryObj.avgSpeed;
//	    		}
//	    	});
//	    	dsRList.add(dsR);
//	    }); 
//	    
//	    //Exporting Records to XML KA format
//	    for(D2DatasetRecord dsR:dsRList) {
//	    	rows += "<ROW VehicleID=\"" + dsR.vehicleId
//	    			  + "\" DriverBehaviour=\"" + dsR.behaviour
//	    			  + "\" Distance=\"" + dsR.distance
//	    			  + "\" Duration=\"" + dsR.duration
//	    			  + "\" AvgSpeed=\"" + dsR.avgSpeed
//	    			  + "\"/>";
//	    }
//		rows += "</ROWS>";
//		/*vehicles.forEach((key, value) -> {System.out.println(key + ":" + value);});
//		behaviours.forEach((key, value) -> System.out.println(key + ":" + value));
//		trajectories.forEach((key, value) -> System.out.println(key + ":" + value));
//		*/
//		
//	    return rows;
//		
//	}

}