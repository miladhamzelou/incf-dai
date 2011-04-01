package org.incf.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monitor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	protected static final String CONFIG_FILE = "/config.xml";
	protected static final int SERVER_LENGTH = "incf-dev-local".length();
	protected static final int HUB_LENGTH = "central".length();
	protected static final int PROCESS_LENGTH = "GetTransformationChain".length();
	
	protected static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	protected static final DateFormat tf = new SimpleDateFormat("HH:mm:ss");
	
	private XMLConfiguration config;
	private String server;
	private String currentHub;
	
	// set up list for any failures
	List<Failure> failures = new ArrayList<Failure>();
	
	private final PrintStream out = System.out;
	
	public Monitor() {
		config = new XMLConfiguration();
		try {
			InputStream is = getClass().getResourceAsStream(CONFIG_FILE);
			config.load(is);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Master method. Runs all tests
	 */
	@SuppressWarnings("unchecked")
	private void runTests() {
		
		logger.info("Starting monitor run.");
		
		// report header
		Date now = new Date();
		String horizLine = "-------------------------------------------------------"
				+ "------------------------------------------------%n";
		out.printf("INCF Monitor: date: %s, start: %s%n", df.format(now), tf.format(now));
		out.printf(horizLine);
		out.printf("   Time   HTTP Pass/%n");
		out.printf("   msec   Code Fail  %s %s %s Data Inputs%n", 
				padRight("Server", SERVER_LENGTH), 
				padRight("Hub", HUB_LENGTH), 
				padRight("Process", PROCESS_LENGTH));
		out.printf(horizLine);

		// read config file that itemizes tests
		List<String> serverList = config.getList("servers.server");
		
		// read config file that itemizes tests
		List<HierarchicalConfiguration> testList = config.configurationsAt(
				"tests.test");
		
		// iterate set of servers and run tests for each one
		for(Iterator<String> servers = serverList.iterator(); 
				servers.hasNext(); ) {
		    server = servers.next();

		    out.printf("%n%s%n", server);

			// iterate set of tests and run each one
			for(Iterator<HierarchicalConfiguration> test = testList.iterator(); 
					test.hasNext(); ) {
			    HierarchicalConfiguration node = test.next();
			    String uri = node.getString("uri");
				if (uri.equals("Not applicable") || uri.equals("Planned")) {
					continue;
				}
			    String resolvedUri = uri.replace("@SERVER@", server);
			    String responsePattern = node.getString("responsePattern");
			    
				URL url = null;
				try {
					url = new URL(resolvedUri);
				} catch (MalformedURLException e) {
					throw new RuntimeException("Malformed URL", e);
				}
				
				// run test
			    TestResult testResult = runTest(url, responsePattern);
			    
				// get process, data inputs, and other reporting info
				String process = null;
				String dataInputs = null;
				Map<String, String> kvMap = buildQueryKVMap(url);
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
				if (dataInputs == null) {
					dataInputs = "";
				}
				String path = url.getPath();
				String hub = path.split("/")[1];
				
				// blank line for new hub
				if (!hub.equals(currentHub)) {
					out.println();
					currentHub = hub;
				}
				
				// report detail; add line for this test
				out.print(String.format("%8.3f  %d  %s  %s %s %s %s%n",
						testResult.elapsedTime, testResult.statusCode, 
						(testResult.pass ? "pass" : "FAIL"),
						padRight(server.split("\\.")[0], SERVER_LENGTH),
						padRight(hub, HUB_LENGTH), 
						padRight(process, PROCESS_LENGTH), dataInputs));
				
				// accrue any failure
				if (!testResult.pass) {
					failures.add(new Failure(server, path, process));
				}
			}
		}
		
		// report footer
		out.printf(horizLine);
		if (failures.size() > 0) {
			out.printf("Failures (server, path, process):%n");
			for (Failure failure : failures) {
				out.printf("  %s %s %s%n", 
						padRight(failure.server.split("\\.")[0], SERVER_LENGTH),
						padRight(failure.path.split("/")[1], HUB_LENGTH),
						padRight(failure.process, PROCESS_LENGTH));
			}
		} else {
			out.printf("No failures%n");
		}
		out.printf(horizLine);
		out.printf("Finish: %s%n", tf.format(new Date()));
		
		// report if any failures
		if (failures.size() > 0) {
			if (config.getBoolean("reporting.sendemail")) {
				List<String> tosList = config.getList("reporting.email.tos.to");
				String[] tos = tosList.toArray(new String[0]);
				try {
					
					// TODO send what
					sendEmail(tos, "INCF Hubs Status - Failure", "");
				} catch (EmailException e) {
					logger.error("Email exception", e);
				}
			}
		}
		
		if (config.getBoolean("debug")) {
//			System.out.println(buf.toString());
		}
		
		logger.info("Finishing monitor run.");
		
	}
	
	/**
	 * Run a single test with its URL and expected response pattern.
	 * 
	 * @param url
	 * @param responsePattern
	 */
	public static TestResult runTest(URL url, String responsePattern) {
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
			response = readAsString(responseStream);
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
		
		// scan response and determine pass/fail
		boolean pass = false;
		if (statusCode == HttpURLConnection.HTTP_OK 
				&& response.contains(responsePattern)) {
			pass = true;
		}
		
		double elapsedTime = (double) (System.currentTimeMillis() - startTime) 
				/ 1000;
		
		return new TestResult(statusCode, pass, elapsedTime, exception);
	}
	
	protected static String padRight(String string, int width) {
		int length = string.length();
		if (length >= width) {
			return string.substring(0, width);
		}
		StringBuilder buf = new StringBuilder(string);
		for (int i = 0; i < width - length; i++) {
			buf.append(' ');
		}
		return buf.toString();
	}

	private void sendEmail(String[] tos, String subject, String body) 
			throws EmailException {
//		Email email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		email.setHostName(config.getString("reporting.email.host"));
		email.setFrom(config.getString("reporting.email.from"));
		for (String to : tos) {
			email.addTo(to);
		}
		email.setSubject(subject);
//		email.setMsg(body);
		email.setHtmlMsg("<html><body><pre>" + body + "</pre></body></html>");
		email.send();	
	}

    private static String readAsString(InputStream in)
    		throws java.io.IOException{
    	StringBuffer fileData = new StringBuffer(1000);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	char[] buf = new char[1024];
    	int numRead = 0;
    	while((numRead = reader.read(buf)) != -1){
    		fileData.append(buf, 0, numRead);
    	}
    	reader.close();
    	return fileData.toString();
    }
    
    public static class TestResult {
    	protected int statusCode;
    	protected boolean pass;
    	protected double elapsedTime;
    	protected Exception exception;
    	protected TestResult(int statusCode, boolean pass, double elapsedTime,
    			Exception exception) {
    		this.statusCode = statusCode;
    		this.pass = pass;
    		this.elapsedTime = elapsedTime;
    		this.exception = exception;
    	}
    }
    
    private class Failure {
    	private String server;
    	private String path;
    	private String process;
    	private Failure(String server, String path, String process) {
    		this.server = server;
    		this.path = path;
    		this.process = process;
    	}
    }
    
    protected static Map<String, String> buildQueryKVMap(URL url) {
    	Map<String, String> kvMap = new HashMap<String, String>();
		String query = url.getQuery();
		String[] kvPairs = query.split("&");
		for (int i = 0; i < kvPairs.length; i++) {
			String kvPair = kvPairs[i];
			int firstEqualsSymbol = kvPair.indexOf('=');
			String key = kvPair.substring(0, firstEqualsSymbol).toLowerCase();
			String value = kvPair.substring(firstEqualsSymbol + 1);
			kvMap.put(key, value);
		}
    	return kvMap;
    }

	public static void main(String[] args) {
		Monitor app = new Monitor();
		app.runTests();
		
		// test
//		System.out.println('|' + app.leftJustify("ABC", 3) + '|');
//		System.out.println('|' + app.leftJustify("ABC", 5) + '|');
//		System.out.println('|' + app.leftJustify("ABC", 2) + '|');
	}

}
