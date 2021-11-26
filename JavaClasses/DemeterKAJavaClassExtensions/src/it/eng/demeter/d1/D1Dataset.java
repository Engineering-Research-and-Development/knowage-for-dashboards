package it.eng.demeter.d1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class D1Dataset extends DemeterAbstractJavaClassDataSet {
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		
		/*Creating records for dataset*/
		List<D1DatasetRecord> dsRList=new ArrayList<D1DatasetRecord>();
		
		try {

			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString()); 
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());
			for (int l=0; l<jsonArray.length(); l++){		
				System.out.println("ELEMENTO :"+l);
				Iterator<String> keyIt = jsonArray.getJSONObject(l).keys();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					JSONObject resultsObject = jsonArray.getJSONObject(l).getJSONObject(key).getJSONObject("results");
					Iterator<String> resultKeyIt = resultsObject.keys();
					while (resultKeyIt.hasNext()) {
						String resultKey = resultKeyIt.next();
						D1DatasetRecord dsR = new D1DatasetRecord();
						dsR.setMachineId(jsonArray.getJSONObject(l).getJSONObject(key).get("MGDL_ID").toString());
						dsR.setDateEvaluation(key.split("_")[0].split("T")[0]);
						dsR.setDateStart(key.split("_")[1].split("T")[0]);
						dsR.setDateEnd(key.split("_")[2].split("T")[0]);
						dsR.setTimeStart(key.split("_")[1].split("T")[1].replaceAll("Z",""));
						dsR.setTimeEnd(key.split("_")[2].split("T")[1].replaceAll("Z",""));
						//Remove Seconds from Times
						dsR.setTimeStart(dsR.getTimeStart().split(":")[0]+":"+dsR.getTimeStart().split(":")[1]);
						dsR.setTimeEnd(dsR.getTimeEnd().split(":")[0]+":"+dsR.getTimeEnd().split(":")[1]);
						dsR.setSensor(resultKey);
						JSONArray arrayValues = new JSONArray(resultsObject.get(resultKey).toString());
						switch (arrayValues.get(0).toString()) {
						case "green":
							dsR.setStatus("0");
							break;
						case "yellow":
							dsR.setStatus("1");
						case "red":
							dsR.setStatus("2");
						}
						dsR.setError(arrayValues.get(1).toString());
						dsR.setErrorDetail(arrayValues.get(2).toString());
						dsR.setNote(arrayValues.get(3).toString());
						
						System.out.println("Evaluation: "+dsR.getDateEvaluation());
						System.out.println("Start: "+dsR.getDateStart());
						System.out.println("Time Start: "+dsR.getTimeStart());
						System.out.println("End: "+dsR.getDateEnd());
						System.out.println("Time End: "+dsR.getTimeEnd());		
						System.out.println("ID: "+dsR.getMachineId());
						System.out.println("Sensor: "+dsR.getSensor());
						dsRList.add(dsR);
					}
				}
				
			}
			
			for(D1DatasetRecord dsR:dsRList) {
				rows += "<ROW machineId=\"" + dsR.getMachineId()
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
						+ "\"/>";
			}
			rows += "</ROWS>";

			/*Reading elements*/
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		return rows;
	}
}
