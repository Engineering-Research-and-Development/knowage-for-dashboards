package it.eng.demeter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TEST_DataSetUtil implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet{
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		Set setKeys = parameters.keySet();
		Set setKeys2 = profile.keySet();
		String a = "Hello";
		int b = 10;
		for(Object keyCurr: setKeys) {
			System.out.println(keyCurr);
		}
		for(Object keyCurr2: setKeys2) {
			System.out.println(keyCurr2);
		}
		String ds = "<ROWS><ROW value1="+parameters.get("param1")+" value2=\""+profile.get("user_id")+"\"/><ROW value1=\"value1\" value2=\"value2\"/></ROWS>";
		//String ds = "<ROWS><ROW Saluto=Hello Nome=Luca/><ROW Saluto=Ciao Nome=Maria/></ROWS>";
		//String ds = "<ROWS><ROW Saluto=\""+a+"\" Nome=\"Luca\" Eta=\"10\" /><ROW Saluto=\"Ciao\" Nome=\"Maria\" Eta=\"20\"/></ROWS>";
		//String ds = "<ROWS><ROW Index=\"102\" Date=\"2018-04-12\" Pedometer=\"609\" Cow=\"729\" MID=\"186\" Lactations=\"3\" DailyProduction=\"38.37\" AverageDailyProduction=\"30.13\" DdailyFat=\"3.42\" DailyProteins=\"3.03\" DailyFatProteins=\"1.13\" Conductivity1=\"9.2\" Conductivity2=\"11.9\" Conductivity3=\"10.9\" Activity1=\"253\" Activity2=\"148\" Activity3=\"174\" TotalDailyLying=\"705\" ActualLameness=\"Healthy\" PredictedLameness=\"Healthy\" ActualKetosis=\"Healthy\" PredictedKetosis=\"Healthy\" ActualMastitis=\"Sick\" PredictedMastitis=\"Sick\"/></ROWS>";
		return ds;
	}

}
