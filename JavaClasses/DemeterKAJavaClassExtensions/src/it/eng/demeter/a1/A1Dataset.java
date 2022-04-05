package it.eng.demeter.a1;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class A1Dataset extends DemeterAbstractJavaClassDataSet {
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
	    rows = "<ROWS>";
	    List<A1DatasetRecord> dsRList=new ArrayList<A1DatasetRecord>();
	    try {
			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString());  
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());

			/*Reading elements*/
			for (int l=0; l<jsonArray.length(); l++){
				String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (Elemento) {
				case "Plot":
					DecimalFormat df = new DecimalFormat("0.00");
					A1DatasetRecord dr = new A1DatasetRecord();
					double area = jsonArray.getJSONObject(l).getDouble("area");
					String fieldId = jsonArray.getJSONObject(l).get("code").toString();
					double realProduction = jsonArray.getJSONObject(l).getJSONObject("crop").getJSONObject("production").getJSONObject("productionAmount").getDouble("propertyHasValue");
					double predictedProduction = jsonArray.getJSONObject(l).getJSONObject("crop").getJSONObject("productionForecast").getJSONObject("productionAmount").getDouble("numericValue");
					String predictionDate = jsonArray.getJSONObject(l).getJSONObject("crop").getJSONObject("productionForecast").get("forecastDate").toString();
					double avgPredictedProduction = predictedProduction / area;
					String modelName = jsonArray.getJSONObject(l).getJSONObject("crop").getJSONObject("productionForecast").getJSONObject("productionModel").get("@name").toString();
					String wkt = jsonArray.getJSONObject(l).getJSONObject("crop").getJSONObject("cropArea").get("asWKT").toString();
					
					dr.setArea(df.format(area).replaceAll(",","."));
					dr.setFieldId(fieldId);
					dr.setRealProduction(df.format(realProduction).replaceAll(",","."));
					dr.setPredictedProduction(df.format(predictedProduction).replaceAll(",","."));
					dr.setPredictionDate(predictionDate);
					dr.setAvgPredictedProduction(df.format(avgPredictedProduction).replaceAll(",","."));
					dr.setModelName(modelName);
					dr.setWkt(wkt);
					
					dsRList.add(dr);
				}
			}
	    } catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
	    
	    /*Exporting Records to XML KA format*/
		for(A1DatasetRecord dsR:dsRList) {
			rows += "<ROW Area=\"" + dsR.getArea()
					+ "\" FieldID=\"" + dsR.getFieldId()
					+ "\" RealProduction=\"" + dsR.getRealProduction()
					+ "\" PredictedProduction=\"" + dsR.getPredictedProduction()
					+ "\" PredictionDate=\"" + dsR.getPredictionDate()
					+ "\" AvgPredictedProduction=\"" + dsR.getAvgPredictedProduction()
					+ "\" ModelName=\"" + dsR.getModelName()
					+ "\" WKT=\"" + dsR.getWkt()
					+ "\"/>";
		}
		
	    rows += "</ROWS>";
		return rows;
	}
}
