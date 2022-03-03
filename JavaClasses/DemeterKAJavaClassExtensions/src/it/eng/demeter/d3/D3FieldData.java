package it.eng.demeter.d3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class D3FieldData  extends DemeterAbstractJavaClassDataSet {
	
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		List<D3FieldDataRecord> fdrList = new ArrayList<D3FieldDataRecord>();
		try {
			JSONObject jsonObject = new JSONObject(aim.toString());
			Iterator<String> keys = jsonObject.keys();
			while(keys.hasNext()) {
				String keyString = keys.next();
				D3FieldDataRecord fdr = new D3FieldDataRecord();
				JSONObject keyValue = new JSONObject(jsonObject.get(keyString).toString());
				fdr.setFieldID(keyString);
				fdr.setFieldName(keyValue.getString("fieldName"));
				fdr.setCropType(keyValue.getString("cropType"));
				fdr.setVariety(keyValue.getString("variety"));
				fdr.setCountry(keyValue.getString("country"));
				fdr.setProvince(keyValue.getString("province"));
				fdr.setCommunity(keyValue.getString("community"));
				fdr.setCompany(keyValue.getString("company"));
				fdr.setPostalCode(keyValue.get("postalCode").toString());
				fdr.setYear(keyValue.get("year").toString());
				fdr.setValiddate(keyValue.getString("validdate"));
				fdr.setStartdate(keyValue.getString("startdate"));
				fdr.setEnddate(keyValue.getString("enddate"));
				fdrList.add(fdr);
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		/*Exporting Records to XML KA format*/
		for(D3FieldDataRecord fdr:fdrList) {
			rows += "<ROW FieldID=\"" + fdr.getFieldID()
					+ "\" FieldName=\"" + fdr.getFieldName()
					+ "\" CropType=\"" + fdr.getCropType()
					+ "\" Variety=\"" + fdr.getVariety()
					+ "\" Country=\"" + fdr.getCountry()
					+ "\" Province=\"" + fdr.getProvince()
					+ "\" Community=\"" + fdr.getCommunity()
					+ "\" Company=\"" + fdr.getCompany()
					+ "\" PostalCode=\"" + fdr.getPostalCode()
					+ "\" Year=\"" + fdr.getYear()
					+ "\" ValidDate=\"" + fdr.getValiddate()
					+ "\" StartDate=\"" + fdr.getStartdate()
					+ "\" EndDate=\"" + fdr.getEnddate()
					+ "\"/>";
		}
		rows += "</ROWS>";
		return rows;
	}

}
