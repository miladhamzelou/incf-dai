package org.incf.monitor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monitor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String CONFIG_FILE = "/config.xml";
	private static final int SERVER_LENGTH = "incf-dev-local".length();
	private static final int PATH_LENGTH = "/central/atlas".length();
	private static final int HUB_LENGTH = "central".length();
	private static final int PROCESS_LENGTH = "GetTransformationChain".length();
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat tf = new SimpleDateFormat("HH:mm:ss");
	
	private XMLConfiguration config;
	private String server;
	private String currentHub;
	private boolean anyFailure;
	
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
			    String responsePattern = node.getString("responsePattern");
			    
			    String resolvedUri = uri.replace("@SERVER@", server);
			    
			    runTest(resolvedUri, responsePattern);
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
		if (anyFailure) {
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
	 * Run a single test with its URI and expected response pattern.
	 * 
	 * @param uri
	 * @param responsePattern
	 */
	private void runTest(String uri, String responsePattern) {
		if (uri.equals("Not applicable") || uri.equals("Planned")) {
			return;
		}
		
		URL url = null;
		String s = null;
		HttpURLConnection conn = null;
		int statusCode = -1;
		long startTime = 0;
		
		// execute test's uri
		try {
			url = new URL(uri);
			startTime = System.currentTimeMillis();
			conn = (HttpURLConnection) url.openConnection();
			statusCode = conn.getResponseCode();
			InputStream is = (InputStream) conn.getContent();
			s = readAsString(is);
		} catch (Exception e) {
			logger.error("Exception: ", e);
		}
		
		if (conn != null) {
			conn.disconnect();
		}
		
		// scan response and determine pass/fail
		String result = null;
		if (statusCode == HttpURLConnection.HTTP_OK 
				&& s.contains(responsePattern)) {
			result = "Pass";
		} else {
			result = "FAIL";
			anyFailure = true;
		}
		
		double elapsedTime = (double) (System.currentTimeMillis() - startTime) 
				/ 1000;

		// TODO extract Identifier value unless GC or DP
		String process = null;
		String dataInputs = "";
		String query = url.getQuery();
		String[] kvPairs = query.split("&");
		if (kvPairs[1].contains("GetCapabilities")) {
			process = "GetCapabilities";
		} else if (kvPairs[2].contains("DescribeProcess")) {
			process = "DescribeProcess";
			int firstEqualsSymbol = kvPairs[3].indexOf('=');
			dataInputs =  kvPairs[3].substring(firstEqualsSymbol + 1);
		} else if (kvPairs[2].contains("Execute")) {
			process = kvPairs[3].split("=")[1];
			if (kvPairs.length > 4) {
				int firstEqualsSymbol = kvPairs[4].indexOf('=');
				dataInputs =  kvPairs[4].substring(firstEqualsSymbol + 1);
			}
		}
		
		//
		String path = url.getPath();
		String hub = path.split("/")[1];
		
		// blank line for new hub
		if (!hub.equals(currentHub)) {
			out.println();
			currentHub = hub;
		}
		
		// add line for this test to report
		out.print(String.format("%8.3f  %d  %s  %s %s %s %s%n",
				elapsedTime, statusCode, result, 
				padRight(server.split("\\.")[0], SERVER_LENGTH),
				padRight(hub, HUB_LENGTH), 
				padRight(process, PROCESS_LENGTH), dataInputs));
		
		// accrue any failure
		if (result.equals("FAIL")) {
			failures.add(new Failure(server, path, process));
		}
	}
	
	private String padRight(String string, int width) {
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

	public static void main(String[] args) {
		Monitor app = new Monitor();
		app.runTests();
		
		// test
//		System.out.println('|' + app.leftJustify("ABC", 3) + '|');
//		System.out.println('|' + app.leftJustify("ABC", 5) + '|');
//		System.out.println('|' + app.leftJustify("ABC", 2) + '|');
	}

}
