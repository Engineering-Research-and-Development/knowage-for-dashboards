package it.eng.demeter.g1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class G1Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {

	//static private Logger logger = Logger.getLogger(G1Dataset.class);
	
	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String urlToRead = parameters.get("URL").toString();
			String http_method = "GET";
			if(parameters.containsKey("HTTP_METHOD")) {
				http_method = (String)parameters.get("HTTP_METHOD");
				http_method = http_method.replaceAll("'", "");
			}

			JSONObject jsonBody = new JSONObject();
			Map headerParameters = new HashMap<>();

			if(!http_method.equals("GET")) {

				Set<String> parametersKeySet = parameters.keySet();
				for (String keyCurr : parametersKeySet) {
					if(keyCurr.startsWith("BODY_")) {
						String bodyParameter = (String)parameters.get(keyCurr);
					
						bodyParameter.replaceAll("'", "");
						
						jsonBody.put(keyCurr.substring(5), bodyParameter);
					} else if(keyCurr.startsWith("HEADER_")) {
						String headerParameter = (String)parameters.get(keyCurr);
						
						headerParameters.put(keyCurr.substring(7), headerParameter);
					} 
				}
			}

			urlToRead = urlToRead.replaceAll("\'","");

			logger.info("->" + http_method + "<-");
			System.out.println("->" + http_method + "<-");
			logger.info("headers-> " + convertWithStream(headerParameters));
			System.out.println("headers-> " + convertWithStream(headerParameters));
			logger.info("body-> " + jsonBody);
			System.out.println("body-> " + jsonBody);			
			
			//Chiamo il servizio di acquisizione AIM
			StringBuilder aim = new StringBuilder();
			URL url = new URL(urlToRead);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(http_method);
			
			if(!http_method.equals("GET")) {
				
				for (Object headerCurr : headerParameters.keySet()) {
					con.setRequestProperty((String)headerCurr, (String)headerParameters.get(headerCurr));
				}
				
				con.setDoOutput(true);
				try(OutputStream os = con.getOutputStream()) {
				    byte[] input = jsonBody.toString().getBytes("UTF-8");
				    os.write(input, 0, input.length);
				    os.close();
				}
			
			}
				
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				aim.append(line);
			}
			rd.close();

			ds = animalWelfareReverseTranslator(aim);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}*/

	public String aimTranslator(StringBuilder aim) throws Exception, JSONException {
	//public static String animalWelfareReverseTranslator(StringBuilder aim) throws Exception, JSONException {

		/*Conversione stringa AIM in JSON*/
		JSONObject jsonObject = new JSONObject(aim.toString());  
		JSONArray CSVjsonArray = new JSONArray(jsonObject.get("@graph").toString());

		/*Posizioni dei campi all'interno dell'AIM*/
	      int cowPos = 0;
	      int indexPos = 1;
	      int datePos = 2;
	      int pedometerPos = 7;
	      int midPos = 8;
	      int lactationsPos = 9;
	      int dailyProdPos = 10;
	      int averageDailyProdPos= 11;
	      int dailyFatPos = 12;
	      int dailyProteinsPos = 13;
	      int dailyFatProteinsPos = 14;
	      int tdlPos = 15;
	      int cond1Pos = 24;
	      int cond2Pos = 25;
	      int cond3Pos = 26;
	      int act1Pos = 27;
	      int act2Pos = 28;
	      int act3Pos = 29;
	      int rum1Pos = 30;
	      int rum2Pos = 31;
	      int rum3Pos = 32;
	      int ing1Pos = 33;
	      int ing2Pos = 34;
	      int ing3Pos = 35;
	      int actualLamenessPos = 16;
	      int predictedLamenessPos = 17;
	      int actualKetosisPos = 18;
	      int predictedKetosisPos = 19;
	      int actualMastitisPos = 20;
	      int predictedMastitisPos = 21;
	      int actualHeatStressPos = 22;
	      int predictedHeatStressPos = 23;
	      
	      String actualLamenessValue = "";
	      String predictedLamenessValue = "";
	      String actualKetosisValue = "";
	      String predictedKetosisValue = "";
	      String actualMastitisValue = "";
	      String predictedMastitisValue = "";
	      String actualHeatStressValue = "";
	      String predictedHeatStressValue = "";
	      
	      String rows = "";
	      rows = "<ROWS>";
	      
	      /*Ogni elemento del grafo è costitituito da 36 sezioni, ad eccezione della prima predizione, a cui sono associate le metriche, che non vanno considerate*/
	      for (int l=1; l<CSVjsonArray.length(); l+=36){
	    	  try {
				  if (CSVjsonArray.getJSONObject(actualLamenessPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  actualLamenessValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(actualLamenessPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Sick"))  {
					  actualLamenessValue = "Sick";
				  }
				  if (CSVjsonArray.getJSONObject(predictedLamenessPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  predictedLamenessValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(predictedLamenessPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Sick"))  {
					  predictedLamenessValue = "Sick";
				  }
			
				  if (CSVjsonArray.getJSONObject(actualKetosisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  actualKetosisValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(actualKetosisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Sick"))  {
					  actualKetosisValue = "Sick";
				  }
				  if (CSVjsonArray.getJSONObject(predictedKetosisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  predictedKetosisValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(predictedKetosisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Sick"))  {
					  predictedKetosisValue = "Sick";
				  }
				  
				  if (CSVjsonArray.getJSONObject(actualMastitisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  actualMastitisValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(actualMastitisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Sick"))  {
					  actualMastitisValue = "Sick";
				  }
				  if (CSVjsonArray.getJSONObject(predictedMastitisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  predictedMastitisValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(predictedMastitisPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Sick"))  {
					  predictedMastitisValue = "Sick";
				  }
				  
				  if (CSVjsonArray.getJSONObject(actualHeatStressPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  actualHeatStressValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(actualHeatStressPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Stressed"))  {
					  actualHeatStressValue = "Stressed";
				  }
				  if (CSVjsonArray.getJSONObject(predictedHeatStressPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Healthy"))  {
					  predictedHeatStressValue = "Healthy";
				  }
				  if (CSVjsonArray.getJSONObject(predictedHeatStressPos+l).get("hasResult").toString().split("#")[1].equalsIgnoreCase("healthStatus-Stressed"))  {
					  predictedHeatStressValue = "Stressed";
				  }
				  
				  rows += "<ROW Index=\"" + CSVjsonArray.getJSONObject(indexPos+l).get("indentifier") 
		        		   + "\" Date=\"" + CSVjsonArray.getJSONObject(datePos+l).get("resultTime") 
		        		   + "\" Pedometer=\"" + CSVjsonArray.getJSONObject(pedometerPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Cow=\"" + CSVjsonArray.getJSONObject(cowPos+l).get("livestockNumber") 
		        		   + "\" MID=\"" + CSVjsonArray.getJSONObject(midPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Lactations=\"" + CSVjsonArray.getJSONObject(lactationsPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" DailyProduction=\"" + CSVjsonArray.getJSONObject(dailyProdPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" AverageDailyProduction=\"" + CSVjsonArray.getJSONObject(averageDailyProdPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" DailyFat=\"" + CSVjsonArray.getJSONObject(dailyFatPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" DailyProteins=\"" + CSVjsonArray.getJSONObject(dailyProteinsPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" DailyFatProteins=\"" + CSVjsonArray.getJSONObject(dailyFatProteinsPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Conductivity1=\"" + CSVjsonArray.getJSONObject(cond1Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Conductivity2=\"" + CSVjsonArray.getJSONObject(cond2Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Conductivity3=\"" + CSVjsonArray.getJSONObject(cond3Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Activity1=\"" + CSVjsonArray.getJSONObject(act1Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Activity2=\"" + CSVjsonArray.getJSONObject(act2Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" Activity3=\"" + CSVjsonArray.getJSONObject(act3Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" AverageRuminationTime1=\"" + CSVjsonArray.getJSONObject(rum1Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" AverageRuminationTime2=\"" + CSVjsonArray.getJSONObject(rum2Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" AverageRuminationTime3=\"" + CSVjsonArray.getJSONObject(rum3Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" AverageIngestionTime1=\"" + CSVjsonArray.getJSONObject(ing1Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" AverageIngestionTime2=\"" + CSVjsonArray.getJSONObject(ing2Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" AverageIngestionTime3=\"" + CSVjsonArray.getJSONObject(ing3Pos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue")
		        		   + "\" TotalDailyLying=\"" + CSVjsonArray.getJSONObject(tdlPos+l).getJSONArray("hasResult").getJSONObject(0).get("numericValue") 
		        		   + "\" ActualLameness=\"" + actualLamenessValue 
		        		   + "\" PredictedLameness=\"" + predictedLamenessValue 
		        		   + "\" ActualKetosis=\"" + actualKetosisValue 
		        		   + "\" PredictedKetosis=\"" + predictedKetosisValue 
		        		   + "\" ActualMastitis=\"" + actualMastitisValue 
		        		   + "\" PredictedMastitis=\"" + predictedMastitisValue
		        		   + "\" ActualHeatStress=\"" + actualHeatStressValue 
		        		   + "\" PredictedHeatStress=\"" + predictedHeatStressValue
		        		   + "\"/>";
				  
	    	  } catch (JSONException e) {};
	   	  }
	      rows += "</ROWS>";
	      
	      return rows;
	}
	
}