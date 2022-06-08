package it.eng.demeter.d1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class D1Dataset extends DemeterAbstractJavaClassDataSet {
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		
		List<D1ObservationCollection> obsCollection = new ArrayList<D1ObservationCollection>();
		Map<String, D1Observation> obs = new HashMap<String, D1Observation>();
		
		try {

			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString()); 
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
			for (int l=0; l<jsonArray.length(); l++){		
				String elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (elemento) {
				case "ObservationCollection":
					D1ObservationCollection obC = new D1ObservationCollection();
					obC.setId(jsonArray.getJSONObject(l).get("@id").toString());
					obC.setMachineId(jsonArray.getJSONObject(l).get("madeBySensor").toString());
					obC.setEvaluationDate(jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0]);
					obC.setStartDate(jsonArray.getJSONObject(l).getJSONObject("phenomenonTime").get("hasBeginning").toString().split("T")[0]);
					obC.setEndDate(jsonArray.getJSONObject(l).getJSONObject("phenomenonTime").get("hasEnd").toString().split("T")[0]);
					String timeStart = jsonArray.getJSONObject(l).getJSONObject("phenomenonTime").get("hasBeginning").toString().split("T")[1];
					String timeEnd = jsonArray.getJSONObject(l).getJSONObject("phenomenonTime").get("hasEnd").toString().split("T")[1];
					obC.setTimeStart(timeStart.split(":")[0]+":"+timeStart.split(":")[1]);
					obC.setTimeEnd(timeEnd.split(":")[0]+":"+timeEnd.split(":")[1]);
					JSONArray arrayObsId = new JSONArray(jsonArray.getJSONObject(l).get("hasMember").toString());
					ArrayList<String> obsIdList = new ArrayList<String>();
					for (int j=0; j<arrayObsId.length(); j++) {
						obsIdList.add(arrayObsId.get(j).toString());
					}
					obC.setObservationList(obsIdList);
					obsCollection.add(obC);
					break;
				case "Observation":
					D1Observation ob = new D1Observation();
					ob.setId(jsonArray.getJSONObject(l).get("@id").toString());
					ob.setName(jsonArray.getJSONObject(l).get("observedProperty").toString());
					JSONArray results = new JSONArray(jsonArray.getJSONObject(l).get("hasSimpleResult").toString());
					for (int j=0; j<results.length(); j++) {
						ob.setStatus(results.get(0).toString());
						ob.setError(results.get(1).toString());
						ob.setErrorDetail(results.get(2).toString());
						ob.setNote(results.get(3).toString());
					}
					obs.put(ob.getId(), ob);
					break;
				}
			}
			
			/*Creating records for dataset*/
			List<D1DatasetRecord> dsRList = new ArrayList<D1DatasetRecord>();
			for(D1ObservationCollection obC:obsCollection) {
				// Create one record for Date filter table
				D1DatasetRecord dsRforDate = new D1DatasetRecord();
				dsRforDate.setFilterForDateTable("true");
				dsRforDate.setMachineId(obC.getMachineId());
				dsRforDate.setUniqueId(obC.getId());
				dsRforDate.setDateEvaluation(obC.getEvaluationDate());
				dsRforDate.setDateStart(obC.getStartDate());
				dsRforDate.setDateEnd(obC.getEndDate());
				dsRforDate.setTimeStart(obC.getTimeStart());
				dsRforDate.setTimeEnd(obC.getTimeEnd());
				dsRList.add(dsRforDate);
				// Check for observations
				for(String obId:obC.getObservationList()) {
					if (obs.containsKey(obId)) {
						D1Observation obItem = obs.get(obId);
						D1DatasetRecord dsR = new D1DatasetRecord();
						dsR.setFilterForDateTable("false");
						dsR.setUniqueId(obC.getId());
						dsR.setMachineId(obC.getMachineId());
						dsR.setDateEvaluation(obC.getEvaluationDate());
						dsR.setDateStart(obC.getStartDate());
						dsR.setDateEnd(obC.getEndDate());
						dsR.setTimeStart(obC.getTimeStart());
						dsR.setTimeEnd(obC.getTimeEnd());
						dsR.setSensor(obItem.getName());
						dsR.setStatus(obItem.getStatus());
						dsR.setError(obItem.getError());
						dsR.setErrorDetail(obItem.getErrorDetail());
						dsR.setNote(obItem.getNote());
						dsRList.add(dsR);
					}
				}
			}
			
			for(D1DatasetRecord dsR:dsRList) {
				rows += "<ROW machineId=\"" + dsR.getMachineId()
						+ "\" uniqueId=\"" + dsR.getUniqueId()
						+ "\" evaluationDate=\"" + dsR.getDateEvaluation()
						+ "\" startDate=\"" + dsR.getDateStart()
						+ "\" endDate=\"" + dsR.getDateEnd()
						+ "\" timeStart=\"" + dsR.getTimeStart()
						+ "\" timeEnd=\"" + dsR.getTimeEnd()
						+ "\" sensor=\"" + dsR.getSensor()
						+ "\" status=\"" + dsR.getStatus()
						+ "\" error=\"" + dsR.getError()
						+ "\" errorDetail=\"" + dsR.getErrorDetail()
						+ "\" note=\"" + dsR.getNote()
						+ "\" filterForDateTable=\"" + dsR.getFilterForDateTable()
						+ "\"/>";
			}
			rows += "</ROWS>";

		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		return rows;
	}
}
