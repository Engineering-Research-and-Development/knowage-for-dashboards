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
		Map<String, E1ObservationData> sfObs = new HashMap<String, E1ObservationData>();
		Map<String, E1ObservationData> nsfObs = new HashMap<String, E1ObservationData>();

		try {

			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString());  
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());

			/*Reading elements*/
			for (int l=0; l<jsonArray.length(); l++){
				String elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (elemento) {
				case "Observation":
					E1ObservationData ob = new E1ObservationData();
					String typeOfFly = "";
					String elementID = "";
					ob.setTrapId(jsonArray.getJSONObject(l).getJSONObject("hasFeatureOfInterest").get("identifier").toString());
					ob.setDate(jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0]);
					ob.setType(jsonArray.getJSONObject(l).get("observationType").toString());
					elementID = ob.getDate()+"_"+ob.getTrapId()+"_"+ob.getType();
					JSONArray results = new JSONArray(jsonArray.getJSONObject(l).get("hasResult").toString());
					for (int j=0; j<results.length(); j++) {
						ob.setValue(results.getJSONObject(j).get("numericValue").toString());
						typeOfFly = results.getJSONObject(j).get("description").toString();
					}
					if (typeOfFly.equals("esteril")) {
						sfObs.put(elementID, ob);
					} else {
						nsfObs.put(elementID, ob);
					}
					break;
				}
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}

		/*Creating records for dataset*/
		List<E1DatasetRecord> dsRList=new ArrayList<E1DatasetRecord>();
		sfObs.forEach((id, item) -> {
			E1DatasetRecord dsR = new E1DatasetRecord();
			dsR.setDate(item.getDate());
			dsR.setTrapId(item.getTrapId());
			dsR.setType(item.getType());
			dsR.setSterileFlies(item.getValue());
			// check for non sterile flies value
			if (nsfObs.containsKey(id)) {
				dsR.setNonSterileFlies(nsfObs.get(id).getValue());
			}
			dsRList.add(dsR);
		});

		/*Exporting Records to XML KA format*/
		for(E1DatasetRecord dsR:dsRList) {
			int s = (int) Double.parseDouble(dsR.getSterileFlies()); 
			int ns = (int) Double.parseDouble(dsR.getNonSterileFlies()); 
			String total = Integer.toString(s+ns);

			rows += "<ROW Date=\"" + dsR.getDate()
					+ "\" TrapID=\"" + dsR.getTrapId()
					+ "\" SterileFlies=\"" + Integer.toString(s)
					+ "\" NonSterileFlies=\"" + Integer.toString(ns)
					+ "\" Total=\"" + total
					+ "\" Type=\"" + dsR.getType()
					+ "\"/>";
		}
		rows += "</ROWS>";
		return rows;
	}
}