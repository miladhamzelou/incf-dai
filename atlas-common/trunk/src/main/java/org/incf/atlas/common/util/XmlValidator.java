package org.incf.atlas.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Simple, all-JDK, SAX (not DOM) XML validator.
 * 
 * @author David Little
 */
public class XmlValidator {

	private SchemaFactory schemaFactory;
	private StringBuilder validationErrors;

	public XmlValidator() {
		String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
		schemaFactory = SchemaFactory.newInstance(schemaLanguage);
		validationErrors = new StringBuilder();
	}
	
	public boolean validate(File xmlFile, URL schemaUrl) {
		boolean valid = false;
		try {
			valid = validate(new FileInputStream(xmlFile),
					schemaUrl.openStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
	
	public boolean validate(File xmlFile, File schemaFile) {
		boolean valid = false;
		try {
			valid = validate(new FileInputStream(xmlFile),
					new FileInputStream(schemaFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
	
	public boolean validate(InputStream xmlStream, InputStream schemaStream) {
		try {
			
			// set up Validator
			Schema schema = schemaFactory.newSchema(
					new StreamSource(schemaStream));
			Validator validator = schema.newValidator();

			// add custom error handler which allows for continuing validation 
			// after errors (rather that simply stopping at that point)
			validator.setErrorHandler(new ValidatorErrorHandler(
					validationErrors));

			// prepare XML file as SAX source
			SAXSource source = new SAXSource(new InputSource(xmlStream));

			// validate xml against schema
			validator.validate(source);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return validationErrors.length() > 0 ? false : true;
	}
	
	public String getValidationErrors() {
		return validationErrors.toString();
	}

//	public boolean validate(String xmlToValidate) {
//
//		// allows for continuing validation after errors (rather that simply
//		// stopping at that point
//		ValidatorErrorHandler errorHandler = new ValidatorErrorHandler();
//
//		try {
//			Validator validator = schema.newValidator();
//			validator.setErrorHandler(errorHandler);
//
//			// prepare XML file as SAX source
//			SAXSource source = new SAXSource(new InputSource(this.getClass()
//					.getResourceAsStream(xmlToValidate)));
//
//			// validate against the schema
//			validator.validate(source);
//
//		} catch (SAXParseException e) {
//			System.out.printf("Line: %d - %s%n", e.getLineNumber(), e
//					.toString());
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//		if (errorHandler.exceptions()) {
//			return false;
//		} else {
//			return true;
//		}
//	}

	private class ValidatorErrorHandler implements ErrorHandler {
		
		private StringBuilder buf;
		
		public ValidatorErrorHandler(StringBuilder buf) {
			this.buf = buf;
		}

		public void error(SAXParseException e) throws SAXException {
			accrueValidationError(e, buf);
		}

		public void fatalError(SAXParseException e) throws SAXException {
			accrueValidationError(e, buf);
			throw e; 
		}

		public void warning(SAXParseException e) throws SAXException {
			accrueValidationError(e, buf);
		}
		
		private void accrueValidationError(SAXParseException e, 
				StringBuilder buf) {
			buf.append(String.format("Line: %d - %s%n", e.getLineNumber(), 
					e.toString()));
		}

	}
	
	// dev testing only
	public static void main(String[] a) throws Exception {
//		final String SCHEMA = "src/main/resources/wpsSchema/wpsGetCapabilities_response.xsd";
//		final String XML = "src/main/resources/database/Capabilities.xq";
		
		final String SCHEMA = "src/main/xsd/wpsSchema/wpsDescribeProcess_response.xsd";
		final String XML = "src/main/resources/ProcessDescriptionsMaster.xml";
		
//		final String SCHEMA = "src/main/xsd/owsExceptionReport.xsd";
//		final String XML = "src/main/resources/exampleResponses/ExceptionReport.xml";
		
//		final URL url = new URL("http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_response.xsd");
//		final InputStream isSchema = url.openStream();
//		final InputStream isFile = new FileInputStream(XML);
		
		XmlValidator validator = new XmlValidator();
		if (validator.validate(new File(XML), new File(SCHEMA))) {
//		if (validator.validate(isFile, isSchema)) {
			System.out.println(XML + ": Passed validation");
		} else {
			System.out.println(XML + ": Failed validation");
			System.out.println(validator.getValidationErrors());
		}
	}

}
