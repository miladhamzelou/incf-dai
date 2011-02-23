package org.incf.monitor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private static final String NEWLINE = "\n";
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat tf = new SimpleDateFormat("HH:mm:ss");
	
	private XMLConfiguration config;
	private StringBuilder buf;
	private boolean anyFailure;
	
	public Monitor() {
		config = new XMLConfiguration();
		try {
			InputStream is = getClass().getResourceAsStream(CONFIG_FILE);
			config.load(is);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buf = new StringBuilder();
	}
	
	/**
	 * Master method. Runs all tests
	 */
	@SuppressWarnings("unchecked")
	private void runTests() {
		
		logger.info("Starting monitor run.");
		
		// compose report header
		Date now = new Date();
		String header0 = "INCF Monitor: date: " + df.format(now) 
			+ ", start: " + tf.format(now) + ", finish ";
		String rule = "-------------------------------------------------------"
			+ "------------------------------------------------";
		String header1 = "   Time   HTTP Pass/";
		String header2 = "   msec   Code Fail  Path           Query";
		buf.append(rule).append(NEWLINE);
		buf.append(header1).append(NEWLINE);
		buf.append(header2).append(NEWLINE);
		buf.append(rule).append(NEWLINE);

		// read config file that itemizes tests
		List<String> hostList = config.getList("hosts.host");
		
		// read config file that itemizes tests
		List<HierarchicalConfiguration> testList = config.configurationsAt(
				"tests.test");
		
		// iterate set of hosts and run tests for each one
		for(Iterator<String> hosts = hostList.iterator(); 
				hosts.hasNext(); ) {
		    String host = hosts.next();
		    
		    System.out.println("host: " + host);
		    
			buf.append(String.format("%s%n", host));

			// iterate set of tests and run each one
			for(Iterator<HierarchicalConfiguration> test = testList.iterator(); 
					test.hasNext(); ) {
			    HierarchicalConfiguration node = test.next();
			    String uri = node.getString("uri");
			    String responsePattern = node.getString("responsePattern");
			    
			    String resolvedUri = uri.replace("@HOST@", host);
			    
			    runTest(resolvedUri, responsePattern);
			}
		}
		
		// compose report footer
		header0 += tf.format(new Date()) + NEWLINE;
		buf.insert(0, header0);
		buf.append(rule);
		
		// report if any failures
		if (anyFailure) {
			if (config.getBoolean("reporting.sendemail")) {
				List<String> tosList = config.getList("reporting.email.tos.to");
				String[] tos = tosList.toArray(new String[0]);
				try {
					sendEmail(tos, "INCF Hubs Status - Failure", buf.toString());
				} catch (EmailException e) {
					logger.error("Email exception", e);
				}
			}
		}
		
		if (config.getBoolean("debug")) {
			System.out.println(buf.toString());
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
		
		conn.disconnect();
		
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

		// add line for this test to report
		buf.append(String.format("%8.3f  %d  %s  %-14s %s%n",
				elapsedTime, statusCode, result, leftJustify(url.getPath(), 14), 
				url.getQuery()));
	}
	
	private String leftJustify(String string, int width) {
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

	public static void main(String[] args) {
		Monitor app = new Monitor();
		app.runTests();
		
		// test
//		System.out.println('|' + app.leftJustify("ABC", 3) + '|');
//		System.out.println('|' + app.leftJustify("ABC", 5) + '|');
//		System.out.println('|' + app.leftJustify("ABC", 2) + '|');
	}

}
