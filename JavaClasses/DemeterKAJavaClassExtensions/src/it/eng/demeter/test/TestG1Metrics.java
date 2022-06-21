package it.eng.demeter.test;

import org.json.JSONException;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class TestG1Metrics extends DemeterAbstractJavaClassDataSet {

	@Override
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		rows += "<ROW LAMENESS_TRUE_POSITIVE_RATE=\"" +"50.00"+ "\" LAMENESS_FALSE_POSITIVE_RATE=\"" +"50.00"+ "\" LAMENESS_PRECISION=\"" +"50.00"+ "\" LAMENESS_ACCURACY=\"" +"50.00"+ "\" MASTITIS_TRUE_POSITIVE_RATE=\"" +"50.00"+ "\" MASTITIS_FALSE_POSITIVE_RATE=\"" +"50.00"+ "\" MASTITIS_PRECISION=\"" +"50.00"+ "\" MASTITIS_ACCURACY=\"" +"50.00"+ "\" KETOSIS_TRUE_POSITIVE_RATE=\"" +"50.00"+ "\" KETOSIS_FALSE_POSITIVE_RATE=\"" +"50.00"+ "\" KETOSIS_PRECISION=\"" +"50.00"+ "\" KETOSIS_ACCURACY=\"" +"50.00"+ "\" HEATSTRESS_TRUE_POSITIVE_RATE=\"" +"50.00"+ "\" HEATSTRESS_FALSE_POSITIVE_RATE=\"" +"50.00"+ "\" HEATSTRESS_PRECISION=\"" +"50.00"+ "\" HEATSTRESS_ACCURACY=\"" +"50.00"+ "\" />";	
		rows += "</ROWS>";
		return rows;
	}
	
	public String debugTest(StringBuilder aim) throws JSONException, Exception {
		return aimTranslator(aim);
	  }
}
