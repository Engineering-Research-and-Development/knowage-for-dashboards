package it.eng.demeter.e1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class E1Dataset extends DemeterAbstractJavaClassDataSet /*implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet*/ {

	/*@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String url = parameters.get("URL").toString();
			//String url = "http://localhost:9080/TestBenchmark/test/E1/Test";
			url = url.replaceAll("\'","");
			ds = aimReaderForE1(url);
			//System.out.println(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}*/


	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		//public static String aimReaderForE1(String urlToRead) throws Exception, JSONException {

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
		Map<String, String> smartMeters = new HashMap<String, String>();
		Map<String, E1ObservationData> observations = new HashMap<String, E1ObservationData>();
		Map<String, E1QuantityValue> quantityValues = new HashMap<String, E1QuantityValue>();

		try {

			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString());  
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());

			/*Reading elements*/
			for (int l=0; l<jsonArray.length(); l++){
				String elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (elemento) {
				case "SmartMeter":
					String smartMeterId = jsonArray.getJSONObject(l).get("@id").toString();
					String smartMeterType = jsonArray.getJSONObject(l).getJSONObject("meterType").get("value").toString();
					smartMeters.put(smartMeterId,smartMeterType);
					break;
				case "Observation":
					E1ObservationData ob = new E1ObservationData();
					ob.id = jsonArray.getJSONObject(l).get("@id").toString() +Integer.toString(l);
					ob.smartMeterId = jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("@id").toString();
					JSONArray results = new JSONArray(jsonArray.getJSONObject(l).get("hasResult").toString());
					for (int j=0; j<results.length(); j++) {
						ob.quantityValueId = results.getJSONObject(j).get("@id").toString();
					}
					observations.put(ob.id, ob);
					break;
				case "QuantityValue":
					E1QuantityValue qv = new E1QuantityValue();
					qv.id = jsonArray.getJSONObject(l).get("@id").toString();
					qv.trapId = jsonArray.getJSONObject(l).get("identifier").toString();
					qv.value = jsonArray.getJSONObject(l).get("numericValue").toString();

					final String OLD_FORMAT = "yyyyMMdd";
					final String NEW_FORMAT = "yyyy-MM-dd";

					String oldDateString = qv.id.split("/")[1];
					//String newDateString;

					SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
					Date d = sdf.parse(oldDateString);
					sdf.applyPattern(NEW_FORMAT);
					qv.date = sdf.format(d);
					quantityValues.put(qv.id,qv);
				}
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}

		/*Creating records for dataset*/
		List<E1DatasetRecord> dsRList=new ArrayList<E1DatasetRecord>();
		List<E1DatasetRecord> dsRListTemp=new ArrayList<E1DatasetRecord>();
		smartMeters.forEach((smId, smType) -> {
			observations.forEach((obId, obObj) -> {
				if(obObj.smartMeterId.equals(smId)) {
					E1DatasetRecord dsR = new E1DatasetRecord();
					quantityValues.forEach((qvId, qvObj) -> {
						if(qvObj.id.equals(obObj.quantityValueId)) {
							boolean recordFound = false;
							String measureName = smType;
							for(E1DatasetRecord dsRec:dsRListTemp) {
								if(qvObj.trapId.equals(dsRec.trapId) && qvObj.date.equals(dsRec.date)) {
									recordFound = true;
									switch (measureName) {
									case "Sterile flies":
										dsRec.sterileFlies = qvObj.value;
										break;
									case "Non-sterile flies":
										dsRec.nonSterileFlies = qvObj.value;
										break;
									}
								}
							}
							if (recordFound == false) {
								dsR.trapId = qvObj.trapId;
								dsR.date = qvObj.date;
								switch (measureName) {
								case "Sterile flies":
									dsR.sterileFlies = qvObj.value;
									break;
								case "Non-sterile flies":
									dsR.nonSterileFlies = qvObj.value;
									break;
								}
								dsRListTemp.add(dsR);
							}
						}
					});

				}

			});
		});
		dsRList.addAll(dsRListTemp);
		dsRListTemp.clear();

		/*Exporting Records to XML KA format*/
		for(E1DatasetRecord dsR:dsRList) {
			int s = (int) Double.parseDouble(dsR.sterileFlies); 
			int ns = (int) Double.parseDouble(dsR.nonSterileFlies); 
			String total = Integer.toString(s+ns);

			rows += "<ROW Date=\"" + dsR.date
					+ "\" TrapID=\"" + dsR.trapId
					+ "\" SterileFlies=\"" + Integer.toString(s)
					+ "\" NonSterileFlies=\"" + Integer.toString(ns)
					+ "\" Total=\"" + total
					+ "\"/>";
		}
		rows += "</ROWS>";
		return rows;
	}

}