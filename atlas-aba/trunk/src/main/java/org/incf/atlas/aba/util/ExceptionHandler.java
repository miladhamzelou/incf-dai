package org.incf.atlas.aba.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.opengis.ows._1.ExceptionReport;
import net.opengis.ows._1.ExceptionType;
import net.opengis.ows._1.ObjectFactory;

public class ExceptionHandler {
	
	private ObjectFactory of;
	private ExceptionReport exceptionReport;
	
	public ExceptionHandler(String version) {
		this(version, null);
	}
	
	public ExceptionHandler(String version, String lang) {
		of = new ObjectFactory();
		exceptionReport = of.createExceptionReport();
		exceptionReport.setVersion(version);
		exceptionReport.setLang(lang);
	}
	
	public void handleException(ExceptionCode exceptionCode, 
			String locator, String[] exceptionText) {
				
		// handle one new exception
		ExceptionType exceptionType = of.createExceptionType();
		exceptionType.setExceptionCode(exceptionCode.toString());
		exceptionType.setLocator(locator);
		for (int i = 0; i < exceptionText.length; i++) {
			exceptionType.getExceptionText().add(exceptionText[i]);
		}
		
		// add it to report's list of exceptions
		exceptionReport.getException().add(exceptionType);
	}
	
	public ExceptionReport getExceptionReport() {
		return exceptionReport;
	}
	
	public String getXMLExceptionReport() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("net.opengis.ows._1");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", 
				new AtlasNamespacePrefixMapper());
		StringWriter out = new StringWriter();
		marshaller.marshal(exceptionReport, out);
		return out.toString();
	}
	
	public static void main(String[] args) throws JAXBException {
		ExceptionHandler eh = new ExceptionHandler("1.0.0");
		eh.handleException(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "Line 1.", "Line 2." });
		System.out.println(eh.getXMLExceptionReport());
	}

}
