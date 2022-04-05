package it.eng.demeter.a1;

import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class A1TrainingData extends DemeterAbstractJavaClassDataSet {
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		A1TrainingDataRecord tdr = new A1TrainingDataRecord();
		
		try {
			/* READING JSON DATA */
			JSONObject jsonObject = new JSONObject(aim.toString());
			
			/* READING METRICS */
			String r2Training = jsonObject.getJSONObject("metrics").get("R²_training").toString();
			String r2Validation = jsonObject.getJSONObject("metrics").get("R²_validation").toString();
			tdr.setR2Training(r2Training);
			tdr.setR2Validation(r2Validation);
			
			/* READING MLFLOW DATA */
			String modelName = jsonObject.getJSONObject("mlflow_data").get("modelname").toString();
			String modelVersion = jsonObject.getJSONObject("mlflow_data").get("modelversion").toString();
			String runId = jsonObject.getJSONObject("mlflow_data").get("run_id").toString();
			tdr.setModelName(modelName);
			tdr.setModelVersion(modelVersion);
			tdr.setRunId(runId);
			
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		/*Exporting Records to XML KA format*/
		rows += "<ROW Header=\"Model name\" Content=\"" + tdr.getModelName() + "\"/>";
		rows += "<ROW Header=\"Model version\" Content=\"" + tdr.getModelVersion() + "\"/>";
		rows += "<ROW Header=\"MLFlow run ID\" Content=\"" + tdr.getRunId() + "\"/>";
		rows += "<ROW Header=\"R&#178; training\" Content=\"" + tdr.getR2Training() + "\"/>";
		rows += "<ROW Header=\"R&#178; validation\" Content=\"" + tdr.getR2Validation() + "\"/>";
		rows += "</ROWS>";
		return rows;
	}

}
