package it.eng.demeter.d3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class D3Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {

	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			String plot = parameters.get("PLOT").toString();
			//String url = "https://int.watchitgrow.be/proxy/es_gateway/es5-gateway-keycloak/aim/fields/urn:ngsi-ld:Plot:3e71606b-586b-a5b0-544708d6d8bd/taskmap";
			//String plot = "Plot1b";
			url = url.replaceAll("\'","");
			plot = plot.replaceAll("\'","");
			url = url+"/"+plot+"/taskmap";
			ds = aimReaderForD3(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}*/

	@Override
	public String getValues(Map profile, Map parameters) {
		logger.info("start D3Dataset.getValues(...)");
		
		String url = parameters.get("URL").toString();
		String plot = parameters.get("PLOT").toString();
		url = url.replaceAll("\'","");
		plot = plot.replaceAll("\'","");
		url = url + "/" + plot + "/taskmap";

		concrete_url = url;			
		logger.info("concrete_url->" + concrete_url + "<-");
		
		return super.getValues(profile, parameters);
	}


	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForD3(String urlToRead) throws Exception, JSONException {

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
		Map<String, D3TreatmentData> treatments = new HashMap<String, D3TreatmentData>();
		Map<String, D3PlotData> plots = new HashMap<String, D3PlotData>();

		try {
			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString());  
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());

			/*Reading elements*/
			for (int l=0; l<jsonArray.length(); l++){
				String Elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (Elemento) {
				case "Treatment":
					D3TreatmentData td = new D3TreatmentData();
					td.id = jsonArray.getJSONObject(l).get("@id").toString();
					td.name = jsonArray.getJSONObject(l).get("name").toString();
					td.interventionZoneId = jsonArray.getJSONObject(l).getJSONObject("interventionZone").get("@id").toString();
					td.areaDoseMax = jsonArray.getJSONObject(l).getJSONObject("areaDose").getJSONObject("maximumDose").getInt("numericValue");
					td.areaDoseMin = jsonArray.getJSONObject(l).getJSONObject("areaDose").getJSONObject("minimumDose").getInt("numericValue");
					td.description = jsonArray.getJSONObject(l).get("treatmentDescription").toString();
					td.quantity = jsonArray.getJSONObject(l).getJSONObject("quantity").getInt("numericValue");
					treatments.put(td.id,td);
					break;
				case "Plot":  
					JSONArray zones = new JSONArray(jsonArray.getJSONObject(l).get("containsZone").toString());
					for (int j=0; j<zones.length(); j++) {
						D3PlotData pd = new D3PlotData();
						pd.code = jsonArray.getJSONObject(l).get("code").toString();
						pd.id = jsonArray.getJSONObject(l).get("identifier").toString();
						pd.description = jsonArray.getJSONObject(l).get("description").toString();
						pd.treatmentId = zones.getJSONObject(j).get("@id").toString();
						pd.wkt = zones.getJSONObject(j).getJSONObject("hasGeometry").get("asWKT").toString();
						plots.put(pd.treatmentId, pd);
					}
					break;
				}
			}

		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}

		/*Creating records for dataset*/
		List<D3DatasetRecord> dsRList=new ArrayList<D3DatasetRecord>();
		treatments.forEach((treatmentId, treatmentObj) -> {
			plots.forEach((plotId, plotObj) -> {
				if(treatmentObj.interventionZoneId.equals(plotObj.treatmentId)) {
					D3DatasetRecord dsR = new D3DatasetRecord();
					dsR.treatmentName = treatmentObj.name;
					dsR.treatmentDescription = treatmentObj.description;
					dsR.treatmentMaxDose = Integer.toString(treatmentObj.areaDoseMax);
					dsR.treatmentMinDose = Integer.toString(treatmentObj.areaDoseMin);
					dsR.treatmentQuantity = Integer.toString(treatmentObj.quantity);
					dsR.plotCode = plotObj.code;
					dsR.plotDescription = plotObj.description;
					dsR.plotWKT = plotObj.wkt;
					dsRList.add(dsR);
				}
			});
		});

		/*Exporting Records to XML KA format*/
		for(D3DatasetRecord dsR:dsRList) {
			rows += "<ROW TreatmentName=\"" + dsR.treatmentName
					+ "\" TreatmentDescription=\"" + dsR.treatmentDescription
					+ "\" TreatmentMaxDose=\"" + dsR.treatmentMaxDose
					+ "\" TreatmentMinDose=\"" + dsR.treatmentMinDose
					+ "\" TreatmentQuantity=\"" + dsR.treatmentQuantity
					+ "\" PlotCode=\"" + dsR.plotCode
					+ "\" PlotDescription=\"" + dsR.plotDescription
					+ "\" WKT=\"" + dsR.plotWKT
					+ "\"/>";
		}
		rows += "</ROWS>";
		//treatments.forEach((key, value) -> {System.out.println(key + ":" + value);});
		//plots.forEach((key, value) -> System.out.println(key + ":" + value));
		return rows;
	}

}