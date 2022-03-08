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

	private static final String HEADER = "HEADER_";
	private static final String BODY = "BODY_";
	private static final String HTTP_METHOD = "HTTP_METHOD";
	protected static final String URL = "URL";

	protected String concrete_url = null;

	static protected Logger logger = Logger.getLogger(DemeterAbstractJavaClassDataSet.class);

	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = null;
		try {
			String urlToRead = parameters.get(URL).toString();
			String http_method = "GET";
			if(parameters.containsKey(HTTP_METHOD)) {
				http_method = (String)parameters.get(HTTP_METHOD);
				http_method = http_method.replaceAll("\'", "");
			}

			JSONObject jsonBody = new JSONObject();
			Map headerParameters = new HashMap<>();

			if(!http_method.equals("GET")) {

				Set<String> parametersKeySet = parameters.keySet();
				for (String keyCurr : parametersKeySet) {
					if(keyCurr.startsWith(BODY)) {
						String bodyParameter = (String)parameters.get(keyCurr);

						if(bodyParameter.startsWith("\'") && bodyParameter.endsWith("\'")) {		
							bodyParameter = bodyParameter.replaceAll("\'", "");
							jsonBody.put(keyCurr.substring(5), bodyParameter);
						} else {

							if(isNumericInt(bodyParameter)) { 
								jsonBody.put(keyCurr.substring(5), Integer.parseInt(bodyParameter));
							} else if(isNumericDouble(bodyParameter)) {
								jsonBody.put(keyCurr.substring(5), Double.parseDouble(bodyParameter));
							}
						}

					} else if(keyCurr.startsWith(HEADER)) {
						String headerParameter = (String)parameters.get(keyCurr);
						headerParameters.put(keyCurr.substring(7), headerParameter.replaceAll("\'", ""));
					} 
				}
			}

			/* add to allow forcing the URL */
			if(concrete_url != null && !concrete_url.isEmpty()) {
				logger.info("Using concrete_url->" + concrete_url);
				urlToRead = concrete_url;
			}
			/**/


			urlToRead = urlToRead.replaceAll("\'","");

			logger.info("->" + http_method + "<-");
			System.out.println("->" + http_method + "<-");
			logger.info("->" + urlToRead + "<-");
			System.out.println("->" + urlToRead + "<-");
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

	protected static boolean isNumericInt(String string) {
		int intValue;
		if(string == null || string.equals("")) {
			return false;
		}
		try {
			intValue = Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	protected static boolean isNumericDouble(String string) {
		double doubleValue;
		if(string == null || string.equals("")) {
			return false;
		}
		try {
			doubleValue = Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

}
