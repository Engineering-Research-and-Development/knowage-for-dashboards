package it.eng.demeter.pilot5_2UC1;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.util.HtmlUtils;

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
						//dsR.setId(String.valueOf(l)+String.valueOf(j)+String.valueOf(x));
						dsR.setAssetName(jsonArray.getJSONObject(l).get("@id").toString());
						dsR.setEntityName(dsR.getAssetName().replaceAll("\\s+", "_"));
						dsR.setObservationName(measurementsData.getJSONObject(x).get("Measurement").toString());
						dsR.setMeasurementName(dsR.getObservationName().replaceAll("\\s+", "_"));
						dsR.setDate(measurementsData.getJSONObject(x).get("Date").toString().split("T")[0]);
						String time = measurementsData.getJSONObject(x).get("Date").toString().split("T")[1];
						time = time.split(":")[0] + ":" + time.split(":")[1];
						dsR.setTime(time);
						//dsR.setDateTimeStamp(measurementsData.getJSONObject(x).get("Date").toString());
						dsR.setDateTimeStamp(dsR.getDate()+" "+dsR.getTime());
						dsR.setGeoLocationWKT(measurementsData.getJSONObject(x).get("asWKT").toString());
						dsR.setValue(measurementsData.getJSONObject(x).get("Value").toString());
						dsR.setUnit(HtmlUtils.htmlEscapeDecimal(measurementsData.getJSONObject(x).get("uom").toString()));
						dsR.setProvider(measurementsData.getJSONObject(x).get("Provider").toString());
						dsR.setId(dsR.getEntityName()+"_"+dsR.getMeasurementName()+"_"+dsR.getDate()+"_"+dsR.getTime());
						dsRList.add(dsR);
					}
				}
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		// Add an empty element for manage the "no latest measurement found on Sensor Dashboard"
		UC1DatasetRecord emptyDsR = new UC1DatasetRecord();
		emptyDsR.setId("-1");
		emptyDsR.setValue("0");
		emptyDsR.setAssetName("none");
		emptyDsR.setEntityName("none");
		emptyDsR.setObservationName("none");
		emptyDsR.setMeasurementName("none");
		emptyDsR.setDate("");
		emptyDsR.setTime("");
		emptyDsR.setDateTimeStamp("");
		emptyDsR.setGeoLocationWKT("");
		emptyDsR.setUnit("");
		emptyDsR.setProvider("none");
		dsRList.add(emptyDsR);
		
		/*Exporting Records to XML KA format*/
		for(UC1DatasetRecord dsR:dsRList) {
			rows += "<ROW Id=\"" + dsR.getId()
					+ "\" AssetName=\"" + dsR.getAssetName()
					+ "\" EntityName=\"" + dsR.getEntityName()
					+ "\" ObservationName=\"" + dsR.getObservationName()
					+ "\" MeasurementName=\"" + dsR.getMeasurementName()
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
		//rows = rows.replaceAll("°", "&#176;").replaceAll("Â","");
		//System.out.println("XML DATASET: ["+rows+"]");
		return rows;
	}
}
