package it.eng.demeter.test;

import org.json.JSONException;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class TestG1Dataset extends DemeterAbstractJavaClassDataSet {

	@Override
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		rows += "<ROW Index=\"" +"1"+ "\" Date=\"" +"2021-05-05"+ "\" Pedometer=\"" +"261000086"+ "\" Cow=\"" +"2864"+ "\" MID=\"" +"39"+ "\" Lactations=\"" +"3"+ "\" DailyProduction=\"" +"44.2"+ "\" AverageDailyProduction=\"" +"44.2"+ "\" DailyFat=\"" +"3.5"+ "\" DailyProteins=\"" +"3.1"+ "\" DailyFatProteins=\"" +"1.12"+ "\" Conductivity1=\"" +"10.5"+ "\" Conductivity2=\"" +"10.8"+ "\" Conductivity3=\"" +"9.4"+ "\" Activity1=\"" +"102"+ "\" Activity2=\"" +"110"+ "\" Activity3=\"" +"130"+ "\" TotalDailyLying=\"" +"724"+ "\" AverageRuminationTime1=\"" +"163"+ "\" AverageRuminationTime2=\"" +"167"+ "\" AverageRuminationTime3=\"" +"207"+ "\" AverageIngestionTime1=\"" +"138"+ "\" AverageIngestionTime2=\"" +"143"+ "\" AverageIngestionTime3=\"" +"109"+ "\" ActualLameness=\"" +"Healthy"+ "\" PredictedLameness=\"" +"Healthy"+ "\" ActualKetosis=\"" +"Healthy"+ "\" PredictedKetosis=\"" +"Healthy"+ "\" ActualMastitis=\"" +"Healthy"+ "\" PredictedMastitis=\"" +"Healthy"+ "\" ActualHeatStress=\"" +"Stressed"+ "\" PredictedHeatStress=\"" +"Stressed"+"\"/>";
		rows += "<ROW Index=\"" +"2"+ "\" Date=\"" +"2021-05-05"+ "\" Pedometer=\"" +"261000123"+ "\" Cow=\"" +"3044"+ "\" MID=\"" +"50"+ "\" Lactations=\"" +"3"+ "\" DailyProduction=\"" +"56.8"+ "\" AverageDailyProduction=\"" +"56.8"+ "\" DailyFat=\"" +"3.1"+ "\" DailyProteins=\"" +"3.01"+ "\" DailyFatProteins=\"" +"1.04"+ "\" Conductivity1=\"" +"10.2"+ "\" Conductivity2=\"" +"9.8"+ "\" Conductivity3=\"" +"9.9"+ "\" Activity1=\"" +"110"+ "\" Activity2=\"" +"111"+ "\" Activity3=\"" +"138"+ "\" TotalDailyLying=\"" +"795"+ "\" AverageRuminationTime1=\"" +"118"+ "\" AverageRuminationTime2=\"" +"100"+ "\" AverageRuminationTime3=\"" +"142"+ "\" AverageIngestionTime1=\"" +"48"+ "\" AverageIngestionTime2=\"" +"57"+ "\" AverageIngestionTime3=\"" +"58"+ "\" ActualLameness=\"" +"Healthy"+ "\" PredictedLameness=\"" +"Sick"+ "\" ActualKetosis=\"" +"Healthy"+ "\" PredictedKetosis=\"" +"Sick"+ "\" ActualMastitis=\"" +"Healthy"+ "\" PredictedMastitis=\"" +"Sick"+ "\" ActualHeatStress=\"" +"Healthy"+ "\" PredictedHeatStress=\"" +"Stressed"+"\"/>";
		rows += "</ROWS>";
		return rows;
	}
	
	public String debugTest(StringBuilder aim) throws JSONException, Exception {
		return aimTranslator(aim);
	  }
}
