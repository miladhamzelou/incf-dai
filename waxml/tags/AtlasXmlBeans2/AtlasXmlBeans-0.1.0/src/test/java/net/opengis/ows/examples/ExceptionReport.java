package net.opengis.ows.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import net.opengis.ows.x20.ExceptionDocument;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;

import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.utilities.Utilities;
import org.junit.Test;

public class ExceptionReport {

	public String asXml() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ExceptionReportDocument document = completeResponse();

		ArrayList errorList = new ArrayList();
		Utilities.validateXml(opt, document, errorList);

		return document.xmlText(opt);
	}

	@Test
	public void validFullResponse() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ExceptionReportDocument co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml = Utilities.validateXml(opt, co, errorList);
		assertTrue(errorList.toString(), validXml);

	}

	public ExceptionReportDocument completeResponse() {
		ExceptionReportDocument document = ExceptionReportDocument.Factory
				.newInstance();

		ExceptionReportDocument.ExceptionReport exceptionReport = 
				document.addNewExceptionReport();
		
		exceptionReport.setVersion("1.0.0");
		exceptionReport.setLang("en-US");
		
		ExceptionType exception1 = exceptionReport.addNewException();
		exception1.setExceptionCode("NotApplicableCode");
		exception1.setLocator("locator");
		exception1.setExceptionTextArray(new String[] { "Line 1a.", "Line 1b." });
		
		ExceptionType exception2 = exceptionReport.addNewException();
		exception2.setExceptionCode("NotApplicableCode");
		exception2.setLocator("locator");
		exception2.setExceptionTextArray(new String[] { "Line 2a.", "Line 2b." });
		
		return document;
	}
}
