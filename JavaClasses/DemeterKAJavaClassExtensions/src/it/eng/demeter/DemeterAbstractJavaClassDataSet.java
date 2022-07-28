package it.eng.demeter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
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
	private static final String NUMERIC_BODY = "NUMERIC_BODY_";
	private static final String HTTP_METHOD = "HTTP_METHOD";
	protected static final String URL = "URL";
	private static final String URL_PARAMETER = "URL_PARAMETER_";
	private static final String PROXY_HOST = "PROXY_HOST";
	private static final String PROXY_PORT = "PROXY_PORT";
	
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
			System.out.println("USER ID: "+profile.get("user_id").toString());
			String urlToRead = parameters.get(URL).toString();
			String http_method = "GET";
			if(parameters.containsKey(HTTP_METHOD)) {
				http_method = (String)parameters.get(HTTP_METHOD);
				http_method = http_method.replaceAll("\'", "");
			}

			JSONObject jsonBody = new JSONObject();
			Map headerParameters = new HashMap<>();
			Map urlParameters = new HashMap<>();

			if(!http_method.equals("GET")) {

				Set<String> parametersKeySet = parameters.keySet();
				for (String keyCurr : parametersKeySet) {
					if(keyCurr.startsWith(BODY)) {
						String bodyParameter = (String)parameters.get(keyCurr);
						bodyParameter = bodyParameter.replaceAll("\'", "");
						jsonBody.put(keyCurr.substring(5), bodyParameter);
					} else if(keyCurr.startsWith(NUMERIC_BODY)) {
						String bodyParameter = (String)parameters.get(keyCurr);
						if (bodyParameter.startsWith("\'")) {
							bodyParameter = bodyParameter.replaceAll("\'", "");
						}
						if(isNumericInt(bodyParameter)) {
							jsonBody.put(keyCurr.substring(13), Integer.parseInt(bodyParameter));
						} else if(isNumericDouble(bodyParameter)) {
							jsonBody.put(keyCurr.substring(13), Double.parseDouble(bodyParameter));
						}
					} else if(keyCurr.startsWith(HEADER)) {
						String headerParameter = (String)parameters.get(keyCurr);
						headerParameters.put(keyCurr.substring(7), headerParameter.replaceAll("\'", ""));
					} else if(keyCurr.startsWith(URL_PARAMETER)) {
						if (!(parameters.get(keyCurr) == null)) {
							String urlParameter = keyCurr.toLowerCase().substring(14);
							logger.info("setting URL parameter ->" + urlParameter + "<-");
							System.out.println("setting URL parameter ->" + urlParameter + "<-");
							
							urlParameter = snakeToCamel(urlParameter);
							logger.info("final URL parameter ->" + urlParameter + "<-");
							System.out.println("final URL parameter ->" + urlParameter + "<-");
							
							String urlParamValue = (String)parameters.get(keyCurr);
							urlParamValue = urlParamValue.replaceAll("\'", "");//.replaceAll("\\s+", "_");
							logger.info("URL parameter value ->" + urlParamValue + "<-");
							System.out.println("URL parameter value ->" + urlParamValue + "<-");
							
							urlParameters.put(urlParameter, urlParamValue);
						}
					}
				}
			} else {
				/* add only headers and url parameters if found */
				Set<String> parametersKeySet = parameters.keySet();
				for (String keyCurr : parametersKeySet) {
					if(keyCurr.startsWith(HEADER)) {
						String headerParameter = (String)parameters.get(keyCurr);
						headerParameters.put(keyCurr.substring(7), headerParameter.replaceAll("\'", ""));
					} else if(keyCurr.startsWith(URL_PARAMETER)) {
						if (!(parameters.get(keyCurr) == null)) {
							String urlParameter = keyCurr.toLowerCase().substring(14);
							logger.info("setting URL parameter ->" + urlParameter + "<-");
							System.out.println("setting URL parameter ->" + urlParameter + "<-");
							
							urlParameter = snakeToCamel(urlParameter);
							logger.info("final URL parameter ->" + urlParameter + "<-");
							System.out.println("final URL parameter ->" + urlParameter + "<-");
							
							String urlParamValue = (String)parameters.get(keyCurr);
							urlParamValue = urlParamValue.replaceAll("\'", "");//.replaceAll("\\s+", "_");
							logger.info("URL parameter value ->" + urlParamValue + "<-");
							System.out.println("URL parameter value ->" + urlParamValue + "<-");
							
							urlParameters.put(urlParameter, urlParamValue);
						}
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
			
			if(urlParameters.size() > 0) {
				urlToRead += "/?";
				Set<String> urlParametersKeySet = urlParameters.keySet();
				for (String keyCurr : urlParametersKeySet) {
					urlToRead += keyCurr + "=" + urlParameters.get(keyCurr)+"&";
				}
			}

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
			
			String proxyHost = (String)parameters.get(PROXY_HOST);
			String proxyPort = (String)parameters.get(PROXY_PORT);
			HttpURLConnection con = null;
			if(proxyHost != null && proxyPort != null && !proxyHost.isEmpty() && !proxyPort.isEmpty()) {
				proxyHost = proxyHost.replaceAll("\'", "");
				proxyPort = proxyPort.replaceAll("\'", "");
				logger.info("PROXY HOST ->"+proxyHost);
				logger.info("PROXY PORT ->"+proxyPort);
				System.out.println("PROXY HOST ->"+proxyHost);
				System.out.println("PROXY PORT ->"+proxyPort);	
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
				con = (HttpURLConnection) url.openConnection(proxy);
			} else {
				System.out.println("PROXY NOT CONFIGURED, OPENING NORMAL CONNECTION");
				con = (HttpURLConnection) url.openConnection();
			}
			
			con.setRequestMethod(http_method);
			
			for (Object headerCurr : headerParameters.keySet()) {
				con.setRequestProperty((String)headerCurr, (String)headerParameters.get(headerCurr));
			}
			
			if(!http_method.equals("GET")) {

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
	
	protected static String snakeToCamel(String str) {
        // Lower first letter of string
        str = str.substring(0, 1).toLowerCase()
              + str.substring(1);
  
        // Run a loop till string contains underscore
        while (str.contains("_")) {
  
            // Replace the first occurrence
            // of letter that present after
            // the underscore, to capitalize
            // form of next letter of underscore
            str = str
                      .replaceFirst(
                          "_[a-z]",
                          String.valueOf(
                              Character.toUpperCase(
                                  str.charAt(
                                      str.indexOf("_") + 1))));
        }
        
        return str;
    }

}
