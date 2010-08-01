package org.incf.atlas.ucsd.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.opengis.ows._1.ExceptionReport;
import net.opengis.ows._1.ExceptionType;
import net.opengis.ows._1.ObjectFactory;

/**
 * The constructor creates a WPS ExceptionReport object. This object has a list
 * of one or more ExceptionTypes. Exception cannot be used because it's a
 * reserved word. Each ExceptionType has a list of one or more ExceptionText's.
 * 
 * @author davidlittle
 */
public class ExceptionHandler {
	
	private ObjectFactory of;
	private ExceptionReport exceptionReport;
	
    public ExceptionHandler() {
        this(Constants.getInstance().getDefaultVersion(), 
                Constants.getInstance().getDefaultLanguage());
    }
    
    public ExceptionHandler(String version) {
        this(version, Constants.getInstance().getDefaultLanguage());
    }
    
	/**
	 * Creates a WPS ExceptionReport, specifying the version and the language.
	 * 
	 * @param version the ExceptionReport version
	 * @param language the ExceptionReport language
	 */
	public ExceptionHandler(String version, String language) {
		of = new ObjectFactory();
		exceptionReport = of.createExceptionReport();
		exceptionReport.setVersion(version);
		exceptionReport.setLang(language);
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
	
	/**
	 * Marshal the ExceptionReport into an XML string.
	 * 
	 * @return the ExceptionReport as an XML string
	 * @throws JAXBException if any marshalling problem
	 */
	public String getXMLExceptionReport() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("net.opengis.ows._1");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", 
//				new AtlasNamespacePrefixMapper());
		StringWriter out = new StringWriter();
		marshaller.marshal(exceptionReport, out);
		return out.toString();
	}
	
	
	// testing only
	public static void main(String[] args) throws JAXBException {
		ExceptionHandler eh = new ExceptionHandler("1.0.0");
		eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "Line 1.", "Line 2." });
		System.out.println(eh.getXMLExceptionReport());
	}

}
