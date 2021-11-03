package it.eng.demeter.i2;

import java.util.ArrayList;
import java.util.HashMap;
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

public class I2DatasetTest implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet {
	
	static protected Logger logger = Logger.getLogger(I2DatasetTest.class);
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}
	
	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			String indicatorParam = parameters.get("INDICATOR").toString();
			//String indicatorParam = "Average Irrigation";
			//String url = "http://localhost:9080/TestBenchmark/test/I2/Test";
			indicatorParam = indicatorParam.replaceAll("\'","");
			url = url.replaceAll("\'","");
			ds = aimReaderForI2(url,indicatorParam);
			//System.out.println(ds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForI2(String urlToRead, String indicatorParam) throws Exception, JSONException {
		  
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
	      Map<String, String> farms = new HashMap<String, String>();
	      Map<String, I2Indicator> indicators = new HashMap<String, I2Indicator>();
	      List<I2IndicatorValue> indicatorsValues = new ArrayList<I2IndicatorValue>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Farm":
		    		  String FarmId = jsonArray.getJSONObject(l).get("@id").toString();
		    		  String FarmDesc = jsonArray.getJSONObject(l).get("description").toString();
		    		  farms.put(FarmId, FarmDesc);
		    		  break;
		    	  case "KpiIndicator":
		    		  I2Indicator ind = new I2Indicator();
		    		  ind.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  ind.indicatorName = jsonArray.getJSONObject(l).get("schema.name").toString();
		    		  ind.sectorName = jsonArray.getJSONObject(l).getJSONObject("sector").get("@id").toString().split("#sectorScheme-")[1];
		    		  indicators.put(ind.id, ind);
		    		  break;
		    	  case "KpiIndicatorValue":
		    		  I2IndicatorValue indV = new I2IndicatorValue();
		    		  indV.farmId = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
		    		  indV.indicatorId = jsonArray.getJSONObject(l).getJSONObject("observedProperty").get("@id").toString();
		    		  JSONArray HasResultsjsonArray = new JSONArray(jsonArray.getJSONObject(l).get("hasResult").toString());
		    		  for (int j=0; j<HasResultsjsonArray.length(); j++) {
		    			  indV.value = HasResultsjsonArray.getJSONObject(j).get("numericValue").toString();
		    			  String[] words = HasResultsjsonArray.getJSONObject(j).getJSONObject("unit").get("@id").toString().split(":");
		    			  if (words.length > 1) {
		    				  indV.unit = words[1];
		    			  } else {
		    				  indV.unit = HasResultsjsonArray.getJSONObject(j).getJSONObject("unit").get("@id").toString();
		    			  }
		    		  }
		    		   
		    		  indV.valueRef = jsonArray.getJSONObject(l).get("referenceValue").toString();
		    		  indV.year = jsonArray.getJSONObject(l).get("resultTime").toString().split("-")[0];
		    		  indicatorsValues.add(indV);
		    		  break;
		    	  }
		   	  }
	      } catch (JSONException e) {
	    	  logger.error(e.getMessage(), e.getCause());
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<I2DatasetRecord> dsRList=new ArrayList<I2DatasetRecord>();
	      farms.forEach((farmId, farmName) -> {
	    	  indicators.forEach((indId, indObj) -> {
	    		  for(I2IndicatorValue indV:indicatorsValues) {
		    		  if((indV.farmId.equals(farmId) && indV.indicatorId.equals(indId))) {
		    			  I2DatasetRecord dsR = new I2DatasetRecord();
		    			  dsR.farm = farmName;
		    			  dsR.sector = indObj.sectorName;
		    			  dsR.indicator = indObj.indicatorName;
		    			  dsR.value = indV.value;
		    			  dsR.valueRef = indV.valueRef;
		    			  dsR.year = indV.year;
		    			  dsR.unit = indV.unit;
		    			  if ((!dsR.valueRef.equals("0")) && (!dsR.valueRef.equals("null"))) {
		    				  double res = (Double.parseDouble(dsR.value)/Double.parseDouble(dsR.valueRef));
		    				  double prog = ((Double.parseDouble(dsR.value)*100)/Double.parseDouble(dsR.valueRef));
			    			  dsR.result = Double.toString(res);
			    			  DecimalFormat df2 = new DecimalFormat("0.00");
			    			  dsR.progress = df2.format(prog);
			    			  dsR.progress = dsR.progress.replaceAll(",",".");
		    			  } else {
		    				  dsR.result = dsR.value;
		    				  dsR.progress = "0";
		    			  }
		    			  
		    			  dsRList.add(dsR);
		    		  }
	    		  }
	    		  
	    	  });
	    	  
	      });
	      
	      /*Exporting Records to XML KA format*/
	      for(I2DatasetRecord dsR:dsRList) {
	    	  if (!indicatorParam.equals("All")) {
	    		  if (!dsR.indicator.equals(indicatorParam)) {
	    			  continue;
	    		  } 
	    	  } 
	    	  rows += "<ROW Farm=\"" + dsR.farm
	    			  + "\" Sector=\"" + dsR.sector
	    			  + "\" IndicatorName=\"" + dsR.indicator
	    			  + "\" Unit=\"" + dsR.unit
	    			  + "\" Value=\"" + dsR.value
	    			  + "\" ReferenceValue=\"" + dsR.valueRef
	    			  + "\" Year=\"" + dsR.year
	    			  + "\" Result=\"" + dsR.result
	    			  + "\" Progress=\"" + dsR.progress
	    			  + "\"/>";
	      }
	      rows += "</ROWS>";
	      //farms.forEach((key, value) -> {System.out.println(key + ":" + value);});
	      //indicators.forEach((key, value) -> System.out.println(key + ":" + value));
	      //for(I2IndicatorValue indV:indicatorsValues) {System.out.println(indV.indicatorId +":"+indV.value.toString());}
	      return rows;
	   }

}
