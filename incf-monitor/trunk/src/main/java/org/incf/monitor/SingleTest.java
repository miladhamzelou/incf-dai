package org.incf.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleTest {

	private static Logger LOG = LoggerFactory.getLogger(SingleTest.class);
	
	private static final String CONFIG_FILE = "/config.xml";
	
	private XMLConfiguration config;

	private final PrintStream out = System.out;
	
	public SingleTest() {
		config = new XMLConfiguration();
		try {
			InputStream is = getClass().getResourceAsStream(CONFIG_FILE);
			config.load(is);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test() {
		
		// using server id, get server address 
		String searchId = config.getString("singleTest.serverId");
		List<String> serverIds = (List<String>) config.getList(
				"servers.server[@id]");
		String server = null;
		for (int i = 0; i < serverIds.size(); i++) {
			String targetId = serverIds.get(i);
			if (targetId.equals(searchId)) {
				server = config.getString("servers.server(" + i + ")");
				break;
			}
		}

		// get test uri
		String testUri = config.getString("singleTest.uri");

		// insert server host name into uri
	    String resolvedUri = testUri.replace("@SERVER@", server);
		URL url = null;
		try {
			url = new URL(resolvedUri);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL", e);
		}
		
		// get process, data inputs, and other reporting info
		String process = null;
		String dataInputs = "";
		Map<String, String> kvMap = Monitor.buildQueryKVMap(url);
		String request = kvMap.get("request");
		if (request.equalsIgnoreCase("GetCapabilities")) {
			process = "GetCapabilities";
		} else if (request.equalsIgnoreCase("DescribeProcess")) {
			process = "DescribeProcess";
			dataInputs = kvMap.get("identifier");
		} else if (request.equalsIgnoreCase("Execute")) {
			process = kvMap.get("identifier");
			dataInputs = kvMap.get("datainputs");
		}
		String path = url.getPath();
		String hub = path.split("/")[1];		
		
		String response = "";				// avoid npe later
		HttpURLConnection conn = null;
		InputStream responseStream = null;
		int statusCode = -1;
		Exception exception = null;
		long startTime = System.currentTimeMillis();
		
		// execute test's url
		try {
			conn = (HttpURLConnection) url.openConnection();
			statusCode = conn.getResponseCode();
			responseStream = (InputStream) conn.getContent();
			response = Monitor.readAsString(responseStream);
		} catch (Exception e) {
			exception = e;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (responseStream != null) {
				try {
					responseStream.close();
				} catch (IOException ignore) {
				}
			}
		}
		
		out.printf("Status code: %s%n", String.valueOf(statusCode));
		out.printf("Response:%n%s%n", response);
	}
	
	public static void main(String[] args) {
		SingleTest app = new SingleTest();
		app.test();
	}

}
