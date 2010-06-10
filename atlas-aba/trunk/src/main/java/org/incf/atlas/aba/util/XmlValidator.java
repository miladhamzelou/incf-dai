package org.incf.atlas.aba.util;

import java.io.File;

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

	private Schema schema;

	public XmlValidator(String schemaFile) {
		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File(schemaFile));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public boolean validate(String xmlToValidate) {

		// allows for continuing validation after errors (rather that simply
		// stopping at that point
		ValidatorErrorHandler errorHandler = new ValidatorErrorHandler();

		try {
			Validator validator = schema.newValidator();
			validator.setErrorHandler(errorHandler);

			// prepare XML file as SAX source
			SAXSource source = new SAXSource(new InputSource(this.getClass()
					.getResourceAsStream(xmlToValidate)));

			// validate against the schema
			validator.validate(source);

		} catch (SAXParseException e) {
			System.out.printf("Line: %d - %s%n", e.getLineNumber(), e
					.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		if (errorHandler.exceptions()) {
			return false;
		} else {
			return true;
		}
	}

	private class ValidatorErrorHandler implements ErrorHandler {
		
		private boolean exceptions = false;

		public void error(SAXParseException e) throws SAXException {
			exceptions = true;
			System.out.printf("Line: %d - %s%n", e.getLineNumber(), e.toString());
		}

		public void fatalError(SAXParseException e) throws SAXException {
			exceptions = true;
			throw e; 
		}

		public void warning(SAXParseException e) throws SAXException {
			exceptions = true;
			System.out.printf("Line: %d - %s%n", e.getLineNumber(), e.toString());
		}
		
		public boolean exceptions() {
			return exceptions;
		}

	}
	
	// dev testing only
	public static void main(String[] a) {
		final String SCHEMA = "src/main/resources/wpsSchema/wpsGetCapabilities_response.xsd";
		final String XML = "/exampleResponses/GetCapabilitiesResponse.xml";
		XmlValidator validator = new XmlValidator(SCHEMA);
		if (validator.validate(XML)) {
			System.out.println("Passed");
		} else {
			System.out.println("Failed");
		}
	}

}
