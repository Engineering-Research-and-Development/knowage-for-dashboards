package it.eng.demeter.d1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.util.HtmlUtils;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class D1DSS2Dataset extends DemeterAbstractJavaClassDataSet {
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		
		List<D1DSS2ObservationCollection> obsCollection = new ArrayList<D1DSS2ObservationCollection>();
		Map<String, D1DSS2Observation> obs = new HashMap<String, D1DSS2Observation>();
		
		try {
			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString()); 
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
			for (int l=0; l<jsonArray.length(); l++){		
				String elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (elemento) {
				case "ObservationCollection":
					D1DSS2ObservationCollection obC = new D1DSS2ObservationCollection();
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
					D1DSS2Observation ob = new D1DSS2Observation();
					ob.setId(jsonArray.getJSONObject(l).get("@id").toString());
					ob.setName(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("observedProperty").toString()));
					ob.setValue(jsonArray.getJSONObject(l).get("hasSimpleResult").toString());
					if(jsonArray.getJSONObject(l).has("hasDescription")) {
						ob.setType(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("hasDescription").toString()));
					}
					if(jsonArray.getJSONObject(l).has("hasName")) {
						ob.setChartName(jsonArray.getJSONObject(l).get("hasName").toString());
					}
					/*if (jsonArray.getJSONObject(l).get("hasSimpleResult") instanceof JSONArray)
				    {
						JSONArray results = new JSONArray(jsonArray.getJSONObject(l).get("hasSimpleResult").toString());
						for (int j=0; j<results.length(); j++) {
							ob.setValue(results.get(0).toString());
							ob.setUnit(results.get(1).toString());
						}
				    } else {
				    	ob.setValue(jsonArray.getJSONObject(l).get("hasSimpleResult").toString());
				    }
					
					if(this.isContain(ob.getName(), "on Road")) {
						ob.setType("On Road");
					} else if(this.isContain(ob.getName(), "off Road")) {
						ob.setType("Off Road");
					}*/
					/*if(ob.getName().contains("on Road")) {
						ob.setType("On Road");
					} else if(ob.getName().contains("off Road")) {
						ob.setType("Off Road");
					}*/
					obs.put(ob.getId(), ob);
					break;
				}
			}
			
			/*Creating records for dataset*/
			List<D1DSS2DatasetRecord> dsRList = new ArrayList<D1DSS2DatasetRecord>();
			for(D1DSS2ObservationCollection obC:obsCollection) {
				// Create one record for Date filter table
				D1DSS2DatasetRecord dsRforDate = new D1DSS2DatasetRecord();
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
						D1DSS2Observation obItem = obs.get(obId);
						D1DSS2DatasetRecord dsR = new D1DSS2DatasetRecord();
						dsR.setFilterForDateTable("false");
						dsR.setUniqueId(obC.getId());
						dsR.setMachineId(obC.getMachineId());
						dsR.setDateEvaluation(obC.getEvaluationDate());
						dsR.setDateStart(obC.getStartDate());
						dsR.setDateEnd(obC.getEndDate());
						dsR.setTimeStart(obC.getTimeStart());
						dsR.setTimeEnd(obC.getTimeEnd());
						dsR.setMeasure(obItem.getName());
						dsR.setValue(obItem.getValue());
						dsR.setUnit(obItem.getUnit());
						dsR.setType(obItem.getType());
						dsR.setChartName(obItem.getChartName());
						dsRList.add(dsR);
					}
				}
			}
			
			for(D1DSS2DatasetRecord dsR:dsRList) {
				rows += "<ROW machineId=\"" + dsR.getMachineId()
						+ "\" uniqueId=\"" + dsR.getUniqueId()
						+ "\" evaluationDate=\"" + dsR.getDateEvaluation()
						+ "\" startDate=\"" + dsR.getDateStart()
						+ "\" endDate=\"" + dsR.getDateEnd()
						+ "\" timeStart=\"" + dsR.getTimeStart()
						+ "\" timeEnd=\"" + dsR.getTimeEnd()
						+ "\" measure=\"" + dsR.getMeasure()
						+ "\" value=\"" + dsR.getValue()
						+ "\" unit=\"" + dsR.getUnit()
						+ "\" type=\"" + dsR.getType()
						+ "\" filterForDateTable=\"" + dsR.getFilterForDateTable()
						+ "\" chartName=\"" + dsR.getChartName()
						+ "\"/>";
			}
					
			rows += "</ROWS>";
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		return rows;
	}
	
	/*private boolean isContain(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
   }*/
	
}
