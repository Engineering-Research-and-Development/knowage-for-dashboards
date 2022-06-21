package it.eng.demeter.pilot5_2UC1;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class UC1Dataset extends DemeterAbstractJavaClassDataSet {

	@Override
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		
		/*Creating records for dataset*/
		List<UC1DatasetRecord> dsRList = new ArrayList<UC1DatasetRecord>();
		
		try {
			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString());  
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
			
			/*Reading elements*/
			for (int l=0; l<jsonArray.length(); l++){
				JSONArray observations = new JSONArray(jsonArray.getJSONObject(l).get("measurements").toString());
				for (int j=0; j<observations.length(); j++){
					JSONArray measurementsData = new JSONArray(observations.getJSONObject(j).get("containsPlot").toString());
					for (int x=0; x<measurementsData.length(); x++){
						UC1DatasetRecord dsR = new UC1DatasetRecord();
						dsR.setAssetName(jsonArray.getJSONObject(l).get("@id").toString());
						dsR.setObservationName(measurementsData.getJSONObject(x).get("Measurement").toString());
						dsR.setDate(measurementsData.getJSONObject(x).get("Date").toString().split("T")[0]);
						String time = measurementsData.getJSONObject(x).get("Date").toString().split("T")[1];
						time = time.split(":")[0] + ":" + time.split(":")[1];
						dsR.setTime(time);
						dsR.setDateTimeStamp(measurementsData.getJSONObject(x).get("Date").toString());
						dsR.setGeoLocationWKT(measurementsData.getJSONObject(x).get("asWKT").toString());
						dsR.setValue(measurementsData.getJSONObject(x).get("Value").toString());
						dsR.setUnit(measurementsData.getJSONObject(x).get("uom").toString());
						dsR.setProvider(measurementsData.getJSONObject(x).get("Provider").toString());
						dsRList.add(dsR);
					}
				}
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		/*Exporting Records to XML KA format*/
		for(UC1DatasetRecord dsR:dsRList) {
			rows += "<ROW AssetName=\"" + dsR.getAssetName()
					+ "\" ObservationName=\"" + dsR.getObservationName()
					+ "\" Date=\"" + dsR.getDate()
					+ "\" Time=\"" + dsR.getTime()
					+ "\" DateTimeStamp=\"" + dsR.getDateTimeStamp()
					+ "\" GeoLocationWKT=\"" + dsR.getGeoLocationWKT()
					+ "\" Value=\"" + dsR.getValue()
					+ "\" Unit=\"" + dsR.getUnit()
					+ "\" Provider=\"" + dsR.getProvider()
					+ "\"/>";
		}
		
		rows += "</ROWS>";
		rows = rows.replaceAll("°", "&#176;");
		return rows;
	}
	
	public String debugTest(StringBuilder aim) throws JSONException, Exception {
		return aimTranslator(aim);
	  }
}
