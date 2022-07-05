package it.eng.demeter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.demeter.a1.A1Dataset;
import it.eng.demeter.a1.A1TrainingData;
import it.eng.demeter.a2.A2Dataset;
import it.eng.demeter.a3.A3Dataset;
import it.eng.demeter.a5.A5Dataset;
import it.eng.demeter.b1.B1Dataset;
import it.eng.demeter.c1.C1Dataset;
import it.eng.demeter.c2.C2Dataset;
import it.eng.demeter.d1.D1DSS2Dataset;
import it.eng.demeter.d1.D1Dataset;
import it.eng.demeter.d2.D2Dataset;
import it.eng.demeter.d3.D3Dataset;
import it.eng.demeter.d3.D3FieldData;
import it.eng.demeter.e1.E1Dataset;
import it.eng.demeter.e2.E2Dataset;
import it.eng.demeter.f1.F1Dataset;
import it.eng.demeter.f2.F2Dataset;
import it.eng.demeter.g1.G1Dataset;
import it.eng.demeter.g2.G2Dataset;
import it.eng.demeter.h2.H2Dataset;
import it.eng.demeter.i1.I1Dataset;
import it.eng.demeter.i2.I2Dataset;
import it.eng.demeter.i3.I3Dataset;
import it.eng.demeter.pilot5_2UC1.UC1Dataset;
import it.eng.demeter.pilot5_2UC2.UC2Dataset;
import it.eng.demeter.test.TestG1Metrics;

public class MainForTestDatasetOutput {

	static protected Logger logger = Logger.getLogger(DemeterAbstractJavaClassDataSet.class);
	public static void main(String[] args) throws JSONException, Exception {
		Map<String, String> profile = new HashMap<String, String>();
		Map<String, String> parameters = new HashMap<String, String>();
		
		D3Dataset d3 = new D3Dataset();
		G1Dataset g1 = new G1Dataset();
		B1Dataset b1 = new B1Dataset();
		D1Dataset d1 = new D1Dataset();
		D2Dataset d2 = new D2Dataset();
		C1Dataset c1 = new C1Dataset();
		E2Dataset e2 = new E2Dataset();
		I2Dataset i2 = new I2Dataset();
		F1Dataset f1 = new F1Dataset();
		A2Dataset a2 = new A2Dataset();
		E1Dataset e1 = new E1Dataset();
		G2Dataset g2 = new G2Dataset();
		H2Dataset h2 = new H2Dataset();
		F2Dataset f2 = new F2Dataset();
		I1Dataset i1 = new I1Dataset();
		C2Dataset c2 = new C2Dataset();
		I3Dataset i3 = new I3Dataset();
		A5Dataset a5 = new A5Dataset();
		A3Dataset a3 = new A3Dataset();
		D3FieldData d3fd = new D3FieldData();
		A1TrainingData a1td = new A1TrainingData();
		A1Dataset a1 = new A1Dataset();
		D1DSS2Dataset d1dss2 = new D1DSS2Dataset();
		UC2Dataset uc2ds = new UC2Dataset();
		UC1Dataset uc1ds = new UC1Dataset();
		TestG1Metrics g1test = new TestG1Metrics();
		
		try { //https://sirvoz-pre.tragsatec.es/api/GetData/e500df44-b471-473c-aaf5-0ab454104302
			  String Url = "https://margarita.etsist.upm.es:9150/getObservationsBySensor/latest/?limit=1&measurements=&entityNames=";
		      FileWriter myWriter = new FileWriter("C:\\Users\\luidicorra\\Desktop\\Test\\filename.xml");
		      // UNLOCK THIS LINE AND CHANGE THE DATASET CLASS TO TEST
		      // REMEMBER TO COPY THE METHOD AT THE END OF THIS PAGE INSIDE BEING TESTED.
		      //myWriter.write(uc1ds.debugTest(getAim(Url,"GET")));
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }

	}
	
	public static StringBuilder getAim(String urlToRead, String methodHttp) {
		StringBuilder aim = new StringBuilder();
		try {
			if(!methodHttp.equals("GET")) {
				URL url = new URL(urlToRead);
				Map headerParameters = new HashMap<>();
				headerParameters.put("Accept", "application/json");
				headerParameters.put("Content-Type", "application/json");
				JSONObject jsonBody = new JSONObject();
				//BODY FOR D3 FIELDS
				jsonBody.put("users", "5752f9e3-8d88-4219-804d-73aad595b765");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(methodHttp);
				for (Object headerCurr : headerParameters.keySet()) {
					con.setRequestProperty((String)headerCurr, (String)headerParameters.get(headerCurr));
				}
				
				con.setDoOutput(true);
				try(OutputStream os = con.getOutputStream()) {
				    byte[] input = jsonBody.toString().getBytes("UTF-8");
				    os.write(input, 0, input.length);
				    os.close();
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					aim.append(line);
				}
				rd.close();
			} else {
				Map headerParameters = new HashMap<>();
				//headerParameters.put("HTTP_KNOWAGE", "Knowage");
				URL url = new URL(urlToRead);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				for (Object headerCurr : headerParameters.keySet()) {
					con.setRequestProperty((String)headerCurr, (String)headerParameters.get(headerCurr));
				}
				con.setRequestMethod("GET");
				BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					aim.append(line);
				}
				rd.close();
			}		
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
			return aim;
		}
	
	//COPY THIS METHOD INSIDE A COMPONENT DATASET CLASS AND CALL IT INTO THIS MAIN FOR TESTS
	/*public String debugTest(StringBuilder aim) throws JSONException, Exception {
		return aimTranslator(aim);
	  }*/

}
