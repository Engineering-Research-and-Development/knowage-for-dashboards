package it.eng.demeter.i3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class I3Dataset implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet {
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}
	
	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String indicatorParam = parameters.get("INDICATOR").toString();
			//String indicatorParam = "Average Irrigation";
			//String url = "http://localhost:9080/TestBenchmark/test/I3/Test";
			//indicatorParam = indicatorParam.replaceAll("\'","");
			url = url.replaceAll("\'","");
			//ds = aimReaderForI3(url,indicatorParam);
			ds = aimReaderForI3(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}
	
	public static String aimReaderForI3(String urlToRead) throws Exception, JSONException {
		  
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
	      Map<String, I3Indicator> indicators = new HashMap<String, I3Indicator>();
	      List<I3IndicatorValue> indicatorsValues = new ArrayList<I3IndicatorValue>();
	      
	      try {
	    	  
		      /*Convert AIM to JSON*/
		      JSONObject jsonObject = new JSONObject(aim.toString());  
		      JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
		      
		      /*Reading elements*/
		      for (int l=0; l<jsonArray.length(); l++){
		    	  String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
		    	  switch (Elemento) {
		    	  case "Farm":
		    		  String farmId = jsonArray.getJSONObject(l).get("@id").toString();
		    		  String desc = jsonArray.getJSONObject(l).get("description").toString();
		    		  desc = desc.replaceAll("<", "&lt;");
		    		  farms.put(farmId, desc);
		    		  break;
		    	  case "KpiIndicator":
		    		  I3Indicator ind = new I3Indicator();
		    		  ind.id = jsonArray.getJSONObject(l).get("@id").toString();
		    		  ind.indicatorName = jsonArray.getJSONObject(l).get("schema.name").toString();
		    		  ind.indicatorName = ind.indicatorName.replaceAll("&", "&amp;");
		    		  ind.sectorName = jsonArray.getJSONObject(l).getJSONObject("sector").get("@id").toString().split("#sectorScheme-")[1];
		    		  indicators.put(ind.id, ind);
		    		  break;
		    	  case "KpiIndicatorValue":
		    		  I3IndicatorValue indV = new I3IndicatorValue();
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
	    	  e.printStackTrace();
	      }
	      
	      /*Creating records for dataset*/
	      List<I3DatasetRecord> dsRList=new ArrayList<I3DatasetRecord>();
	      farms.forEach((farmId, desc) -> {
	    	  indicators.forEach((indId, indObj) -> {
	    		  for(I3IndicatorValue indV:indicatorsValues) {
		    		  if((indV.farmId.equals(farmId) && indV.indicatorId.equals(indId))) {
		    			  I3DatasetRecord dsR = new I3DatasetRecord();
		    			  dsR.description = desc;
		    			  dsR.sector = indObj.sectorName;
		    			  dsR.indicator = indObj.indicatorName;
		    			  dsR.value = indV.value;
		    			  dsR.valueRef = indV.valueRef;
		    			  dsR.year = indV.year;
		    			  dsR.unit = indV.unit;
		    			  if(!dsR.value.equals("null")) {
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
		    			  } else {
		    				  dsR.value = "";
		    				  dsR.result = "-1";
		    				  dsR.progress = "0";
		    			  }
		    			  
		    			  dsRList.add(dsR);
		    		  }
	    		  }
	    		  
	    	  });
	    	  
	      });
	      
	      /*Exporting Records to XML KA format*/
	      for(I3DatasetRecord dsR:dsRList) {
	    	  rows += "<ROW Description=\"" + dsR.description
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
	      //farms.forEach((key, value) -> {System.out.println(key + " : " + value);});
	      //indicators.forEach((key, value) -> System.out.println(key + ":" + value));
	      //for(I3IndicatorValue indV:indicatorsValues) {System.out.println(indV.indicatorId +":"+indV.value.toString());}
	      return rows;
	   }

}
