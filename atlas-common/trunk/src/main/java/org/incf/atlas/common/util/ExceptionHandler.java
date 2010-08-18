package org.incf.atlas.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;

/**
 * The constructor creates a WPS ExceptionReport object. This object has a list
 * of one or more ExceptionTypes. Exception cannot be used because it's a
 * reserved word. Each ExceptionType has a list of one or more ExceptionText's.
 * 
 * @author davidlittle
 */
public class ExceptionHandler {
	
	private ExceptionReportDocument exceptionReportDoc;
	private ExceptionReport exceptionReport;
	
	/**
	 * Creates a WPS ExceptionReport, specifying the version and the language.
	 * 
	 * @param version the ExceptionReport version
	 * @param language the ExceptionReport language
	 */
	public ExceptionHandler(String version, String language) {
		exceptionReportDoc = ExceptionReportDocument.Factory.newInstance();
		exceptionReport = exceptionReportDoc.addNewExceptionReport();

		exceptionReport.setVersion(version);
		
		// lang attribute is optional
		if (language != null) {
			exceptionReport.setLang(language);
		}
	}
	
	/**
	 * Creates an ExceptionType and adds it to the ExceptionReport.
	 * 
	 * @param exceptionCode the WPS exception code
	 * @param locator an optional locator can be used to to state where the
	 *     problem occurred
	 * @param exceptionText an explanation of the exception expressed as an
	 *     array of strings
	 */
	public void addExceptionToReport(ExceptionCode exceptionCode, 
			String locator, String[] exceptionText) {
				
		// handle one new exception
		ExceptionType exception = exceptionReport.addNewException();
		exception.setExceptionCode(exceptionCode.toString());
		
		// locator attribute is optional
		if (locator != null) {
			exception.setLocator(locator);
		}
		
		// ExceptionText element(s) is optional
		if (exceptionText != null && exceptionText.length !=0) {
			exception.setExceptionTextArray(exceptionText);
		}
	}
	
//	public ExceptionReport getExceptionReport() {
//		return exceptionReport;
//	}
	
	/**
	 * Generate the ExceptionReport as an XML string.
	 * 
	 * @return the ExceptionReport as an XML string
	 */
	public String getXMLExceptionReport() {
		Map<String, String> suggestedPrefixes = new HashMap<String, String>();
		suggestedPrefixes.put("http://www.opengis.net/ows/2.0", "ows");
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSaveNamespacesFirst();
		opts.setSaveAggressiveNamespaces();
		opts.setUseDefaultNamespace();
		return exceptionReportDoc.xmlText(opts);
	}
	
//	public Representation getDomExceptionReport() {
//		return XMLUtilities.getDomRepresentation(exceptionReport, 
//				NAMESPACE_URI, CONTEXT_PATH);
//	}
	
//	public String toString() {
//		StringBuilder buf = new StringBuilder();
//		List<ExceptionType> exceptions = exceptionReport.getException();
//		for (ExceptionType exception : exceptions) {
//			buf.append("  Exception code: ");
//			buf.append(exception.getExceptionCode()).append('\n');
//			List<String> lines = exception.getExceptionText();
//			for (String line : lines) {
//				buf.append("    ").append(line).append('\n');
//			}
//		}
//		return buf.toString();
//	}
	
	// testing only
	public static void main(String[] args) /*throws JAXBException*/ {
		ExceptionHandler eh = new ExceptionHandler("1.0.0", "en-US");
		eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "Line 1a.", "Line 1b." });
		eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "Line 2a.", "Line 2b." });
		System.out.println(eh.getXMLExceptionReport());
	}

}
