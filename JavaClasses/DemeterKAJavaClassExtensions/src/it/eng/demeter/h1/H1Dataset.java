package it.eng.demeter.h1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;


public class H1Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {
	
	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/H1/Test";
			url = url.replaceAll("\'","");
			ds = milkQualityReverseTranslator(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}*/
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String milkQualityReverseTranslator(String urlToRead) throws Exception, JSONException {
	      
	      /*Chiamo il servizio di acquisizione AIM*/
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
	      
	      /*Conversione della stringa AIM in JSON*/
	      JSONObject jsonObject = new JSONObject(aim.toString());
	      JSONArray CSVjsonArray = new JSONArray(jsonObject.get("@graph").toString());
	      
	      /*Posizioni dei campi all'interno dell'AIM*/
	      int datePos = 1;
	      int timePos = 1;
	      int productNamePos = 0;
	      int acidityPos = 2;
	      int caseinPos = 3;
	      int densityPos= 4;
	      int fatPos = 5;
	      int freezingPointPos = 7;
	      int lactosePos = 8;
	      int proteinPos = 6;
	      int snfPos = 9;
	      int ureaPos = 10;
	      int remarkPos = 0;
	      int actualQualityPos = 11;
	      int predictedQualityPos = 12;
	      
	      String actualQualityValue = "";
	      String predictedQualityValue = "";
	      
	      String rows = "";
	      rows = "<ROWS>";
	      
	      /*Ogni elemento del grafo � costitituito da 13 sezioni
		  Le metriche, che non vanno considerate in questa lettura, sono presenti solo nel primo elemento
		  e quindi il contatore "l" partir� da 1*/
	      
	      for (int l = 1; l < CSVjsonArray.length(); l+=13){
			  try {
				  if (CSVjsonArray.getJSONObject(actualQualityPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("qualityValue-High"))  {
					  actualQualityValue = "High";
				  }
				  if (CSVjsonArray.getJSONObject(actualQualityPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("qualityValue-Medium"))  {
					  actualQualityValue = "Medium";
				  }
				  if (CSVjsonArray.getJSONObject(actualQualityPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("qualityValue-Low"))  {
					  actualQualityValue = "Low";
				  }
				  if (CSVjsonArray.getJSONObject(predictedQualityPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("qualityValue-High"))  {
					  predictedQualityValue = "High";
				  }
				  if (CSVjsonArray.getJSONObject(predictedQualityPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("qualityValue-Medium"))  {
					  predictedQualityValue = "Medium";
				  }
				  if (CSVjsonArray.getJSONObject(predictedQualityPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("qualityValue-Low"))  {
					  predictedQualityValue = "Low";
				  }
				 
		          rows += "<ROW Date=\"" + CSVjsonArray.getJSONObject(datePos+l).get("resultTime") 
		        		     + "\" Time=\"" + CSVjsonArray.getJSONObject(timePos+l).get("resultTime") 
		        		     + "\" ProductName=\"" + CSVjsonArray.getJSONObject(productNamePos+l).get("productName") 
		        		     + "\" AciditySH=\"" + CSVjsonArray.getJSONObject(acidityPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		     + "\" Casein=\"" + CSVjsonArray.getJSONObject(caseinPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		     + "\" Density=\"" + CSVjsonArray.getJSONObject(densityPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		     + "\" Fat=\"" + CSVjsonArray.getJSONObject(fatPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		     + "\" FreezingPointMC=\"" + CSVjsonArray.getJSONObject(freezingPointPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		     + "\" Lactose=\"" + CSVjsonArray.getJSONObject(lactosePos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		     + "\" Protein=\"" + CSVjsonArray.getJSONObject(proteinPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		     + "\" SNF=\"" + CSVjsonArray.getJSONObject(snfPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		     + "\" Urea=\"" + CSVjsonArray.getJSONObject(ureaPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		     + "\" Remark=\"" + CSVjsonArray.getJSONObject(remarkPos+l).get("productType")
		        		     + "\" ActualQuality=\"" + actualQualityValue
		        		     + "\" PredictedQuality=\"" + predictedQualityValue
		        		     + "\" />";
		          
			  } catch (JSONException e) {};
	   	  }
	      rows += "</ROWS>";
	     
	      return rows;
	   }
}