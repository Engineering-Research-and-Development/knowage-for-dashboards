package it.eng.demeter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet;

public abstract class DemeterAbstractJavaClassDataSet implements IJavaClassDataSet {

	static protected Logger logger = Logger.getLogger(DemeterAbstractJavaClassDataSet.class);
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String urlToRead = parameters.get("URL").toString();
			String http_method = "GET";
			if(parameters.containsKey("HTTP_METHOD")) {
				http_method = (String)parameters.get("HTTP_METHOD");
				http_method = http_method.replaceAll("'", "");
			}

			JSONObject jsonBody = new JSONObject();
			Map headerParameters = new HashMap<>();

			if(!http_method.equals("GET")) {

				Set<String> parametersKeySet = parameters.keySet();
				for (String keyCurr : parametersKeySet) {
					if(keyCurr.startsWith("BODY_")) {
						String bodyParameter = (String)parameters.get(keyCurr);
					
						bodyParameter.replaceAll("'", "");
						
						jsonBody.put(keyCurr.substring(5), bodyParameter);
					} else if(keyCurr.startsWith("HEADER_")) {
						String headerParameter = (String)parameters.get(keyCurr);
						
						headerParameters.put(keyCurr.substring(7), headerParameter.replaceAll("'", ""));
					} 
				}
			}

			urlToRead = urlToRead.replaceAll("\'","");

			logger.info("->" + http_method + "<-");
			System.out.println("->" + http_method + "<-");
			logger.info("headers-> " + convertWithStream(headerParameters));
			System.out.println("headers-> " + convertWithStream(headerParameters));
			logger.info("body-> " + jsonBody);
			System.out.println("body-> " + jsonBody);			
			
			/*Chiamo il servizio di acquisizione AIM*/
			StringBuilder aim = new StringBuilder();
			URL url = new URL(urlToRead);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(http_method);
			
			if(!http_method.equals("GET")) {
				
				for (Object headerCurr : headerParameters.keySet()) {
					con.setRequestProperty((String)headerCurr, (String)headerParameters.get(headerCurr));
				}
				
				con.setDoOutput(true);
				try(OutputStream os = con.getOutputStream()) {
				    byte[] input = jsonBody.toString().getBytes("UTF-8");
				    os.write(input, 0, input.length);
				    os.close();
				}
			}
				
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				aim.append(line);
			}
			rd.close();

			ds = aimTranslator(aim);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}

		return ds;
	}

	protected String convertWithStream(Map<String, String> map) {
	    String mapAsString = map.keySet().stream()
	      .map(key -> key + "=" + map.get(key))
	      .collect(Collectors.joining(", ", "{", "}"));
	    return mapAsString;
	}
	
	protected abstract String aimTranslator(StringBuilder aim) throws Exception, JSONException;
	
}