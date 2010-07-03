package org.incf.atlas.aba.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
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
	
	public boolean validate(File xmlFile, File schemaFile) {
		try {
			
			// set up Validator with custom error handler
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();

			// allows for continuing validation after errors (rather that simply
			// stopping at that point)
			validator.setErrorHandler(new ValidatorErrorHandler(
					validationErrors));

			// prepare XML file as SAX source
			SAXSource source = new SAXSource(new InputSource(new FileReader(
					xmlFile)));

			// validate xml against schema
			validator.validate(source);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(validationErrors.toString());
		return validationErrors.length() > 0 ? false : true;
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
	public static void main(String[] a) {
//		final String SCHEMA = "src/main/resources/wpsSchema/wpsGetCapabilities_response.xsd";
//		final String XML = "src/main/resources/database/Capabilities.xq";
		
//		final String SCHEMA = "src/main/resources/wpsSchema/wpsDescribeProcess_response.xsd";
//		final String XML = "/database/ProcessDescriptions.xml";
		
		final String SCHEMA = "src/main/xsd/owsExceptionReport.xsd";
		final String XML = "src/main/resources/exampleResponses/ExceptionReport.xml";
		
		XmlValidator validator = new XmlValidator();
		if (validator.validate(new File(XML), new File(SCHEMA))) {
			System.out.println(XML + ": Passed validation");
		} else {
			System.out.println(XML + ": Failed validation");
		}
	}

}
