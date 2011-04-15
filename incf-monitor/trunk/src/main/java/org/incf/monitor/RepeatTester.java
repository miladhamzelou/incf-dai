package org.incf.monitor;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepeatTester {

	private static Logger LOG = LoggerFactory.getLogger(RepeatTester.class);
	
	private static final String CONFIG_FILE = "/config.xml";
	
	private XMLConfiguration config;

	private final PrintStream out = System.out;
	
	public RepeatTester() {
		config = new XMLConfiguration();
		try {
			InputStream is = getClass().getResourceAsStream(CONFIG_FILE);
			config.load(is);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void runTests() {
		
		// using server id, get server address 
		String searchId = config.getString("repeatTest.serverId");
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
		
		// using test id, get test uri and response pattern
		String testId = config.getString("repeatTest.testId");
		List<String> testIds = (List<String>) config.getList("tests.test[@id]");
		String testUri = null;
		String responsePattern = null;
		for (int i = 0; i < testIds.size(); i++) {
			String targetId = testIds.get(i);
			if (targetId.equals(testId)) {
				testUri = config.getString("tests.test(" + i + ").uri");
				responsePattern = config.getString(
						"tests.test(" + i + ").responsePattern");
				break;
			}
		}
		
		// report header
		Date now = new Date();
		String horizLine = "-------------------------------------------------------"
				+ "------------------------------------------------%n";
		out.printf("INCF Monitor: date: %s, start: %s%n", 
				Monitor.df.format(now), Monitor.tf.format(now));
		out.printf(horizLine);
		out.printf("  Test    Time   HTTP Pass/%n");
		out.printf("   Nbr    msec   Code Fail  %s %s %s Data Inputs%n", 
				Monitor.padRight("Server", Monitor.SERVER_LENGTH), 
				Monitor.padRight("Hub", Monitor.HUB_LENGTH), 
				Monitor.padRight("Process", Monitor.PROCESS_LENGTH));
		out.printf(horizLine);

		if (testUri.equals("Not applicable")) {
			out.printf("Test %s is not implemented.%n", testId);
			out.printf(horizLine);
			return;
		}
		if (testUri.equals("Planned")) {
			out.printf("Test %s is not yet implemented.%n", testId);
			out.printf(horizLine);
			return;
		}
		
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
		
		int repetitions = config.getInt("repeatTest.repititions");
		int passes = 0;
		int failures = 0;
		int firstFailure = -1;
		for (int i = 1; i <= repetitions; i++) {
			Monitor.TestResult testResult = Monitor.runTest(url, responsePattern);
			
			if (!testResult.pass) {
				firstFailure = i;
			}
			
			// report detail; add line for this test
			out.print(String.format("%6d %8.3f  %d  %s  %s %s %s %s%n",
					i, testResult.elapsedTime, testResult.statusCode, 
					(testResult.pass ? "pass" : "FAIL"),
					Monitor.padRight(server.split("\\.")[0], Monitor.SERVER_LENGTH),
					Monitor.padRight(hub, Monitor.HUB_LENGTH), 
					Monitor.padRight(process, Monitor.PROCESS_LENGTH), dataInputs));
		}
		
		// report footer
		out.printf(horizLine);
		out.printf("Tested server: %s, Hub: %s, Process: %s%n", 
				server.split("\\.")[0],
				path.split("/")[1],
				process);
		out.printf("Repetitions: %d, Passes: %d, Failures: %d, First failure: %d%n",
				repetitions, passes, failures, firstFailure);
		out.printf("Finish: %s%n", Monitor.tf.format(new Date()));
	}
	
	public static void main(String[] args) {
		RepeatTester app = new RepeatTester();
		app.runTests();
	}

}
