package it.eng.demeter.a5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class A5Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/A5/Test";
			url = url.replaceAll("\'","");
			ds = aimReaderForA5(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}*/
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForA5(String urlToRead) throws Exception, JSONException {
	      
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
	      Map<String, A5PlotData> plots = new HashMap<String, A5PlotData>();
	      Map<String, A5CropData> crops = new HashMap<String, A5CropData>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Plot":
		    		  A5PlotData p = new A5PlotData();
		    		  p.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  p.fieldId = jsonArray.getJSONObject(l).get("identifier").toString();
		    		  p.wkt = jsonArray.getJSONObject(l).getJSONObject("hasGeometry").get("asWKT").toString();
		    		  p.area = jsonArray.getJSONObject(l).get("area").toString();
		    		  p.description = jsonArray.getJSONObject(l).get("description").toString();
		    		  p.category = jsonArray.getJSONObject(l).get("category").toString();
		    		  p.cropStatus = jsonArray.getJSONObject(l).get("cropStatus").toString();
		    		  p.lastPlantedAt = jsonArray.getJSONObject(l).get("lastPlantedAt").toString().split("T")[0];
		    		  p.numberOfHivesNeeded = jsonArray.getJSONObject(l).get("numberOfHivesNeeded").toString();
		    		  p.cropId = jsonArray.getJSONObject(l).getJSONObject("hasAgriCrop").getJSONObject("cropSpecies").get("@id").toString();
		    		  plots.put(p.id, p);
		    		  break;
		    	  case "CropType":
		    		  A5CropData cd = new A5CropData();
		    		  cd.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  cd.code = jsonArray.getJSONObject(l).get("code").toString();
		    		  cd.name = jsonArray.getJSONObject(l).get("name").toString();
		    		  cd.family = jsonArray.getJSONObject(l).get("family").toString();
		    		  cd.description = jsonArray.getJSONObject(l).get("description").toString();
		    		  cd.species = jsonArray.getJSONObject(l).get("species").toString();
		    		  crops.put(cd.id, cd);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  e.printStackTrace();
	    	  logger.error(e.getMessage(), e.getCause());
	      }
	      
	      /*Creating records for dataset*/
	      List<A5DatasetRecord> dsRList=new ArrayList<A5DatasetRecord>();
	      plots.forEach((plotId, plotObj) -> {
	    	  A5DatasetRecord dsR = new A5DatasetRecord();
	    	  dsR.fieldId = plotObj.fieldId;
	    	  dsR.wkt = plotObj.wkt;
	    	  dsR.area = plotObj.area;
	    	  dsR.fieldDescription = plotObj.description;
	    	  dsR.category = plotObj.category;
	    	  dsR.cropStatus = plotObj.cropStatus;
	    	  dsR.lastPlantedAt = plotObj.lastPlantedAt;
	    	  dsR.numberOfHivesNeeded = plotObj.numberOfHivesNeeded;
	    	  crops.forEach((cropId, cropObj) -> {
	    		  if(plotObj.cropId.equals(cropId)) {
	    			  dsR.cropCode = cropObj.code;
	    			  dsR.cropDescription = cropObj.description;
	    			  dsR.cropName = cropObj.name;
	    			  dsR.cropFamily = cropObj.family;
	    			  dsR.cropSpecies = cropObj.species;
	    		  }
	    	  });
	    	  dsRList.add(dsR);
	      });
	      
	      /*Exporting Records to XML KA format*/
	      for(A5DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW FieldID=\"" + dsR.fieldId
	    			  + "\" WKT=\"" + dsR.wkt
	    			  + "\" Area=\"" + dsR.area
	    			  + "\" FieldDescription=\"" + dsR.fieldDescription
	    			  + "\" Category=\"" + dsR.category
	    			  + "\" CropStatus=\"" + dsR.cropStatus
	    			  + "\" LastPlantedAt=\"" + dsR.lastPlantedAt
	    			  + "\" HivesRequired=\"" + dsR.numberOfHivesNeeded
	    			  + "\" CropCode=\"" + dsR.cropCode
	    			  + "\" CropName=\"" + dsR.cropName
	    			  + "\" CropFamily=\"" + dsR.cropFamily
	    			  + "\" CropDescription=\"" + dsR.cropDescription
	    			  + "\" CropSpecies=\"" + dsR.cropSpecies
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      
	      return rows;
	}
}
