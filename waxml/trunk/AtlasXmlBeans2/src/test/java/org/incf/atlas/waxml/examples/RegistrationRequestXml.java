package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;
import junit.framework.TestCase;

import java.util.ArrayList;


import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.RegistrationRequestDocument;
import org.incf.atlas.waxml.generated.RegistrationRequestDocument.RegistrationRequest;


import org.incf.atlas.waxml.utilities.Utilities;
import org.junit.Test;

public class RegistrationRequestXml {

	
		public String asXml() {
			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			RegistrationRequestDocument document = completeResponse();

			ArrayList errorList = new ArrayList();
			Utilities.validateXml(opt, document, errorList);

			return document.xmlText(opt);
		}
		
		public RegistrationRequestDocument completeResponse() {
			RegistrationRequestDocument doc =  	RegistrationRequestDocument.Factory.newInstance();
			
			RegistrationRequest rr=	doc.addNewRegistrationRequest();
		 	rr.setRegistrationType("itk_process");
		 	XmlAnyURI	o1 = rr.addNewObjectUrl();
		 	o1.setStringValue("http://example.com/");
		 	XmlAnyURI	o2 = rr.addNewObjectUrl();
		 	o2.setStringValue("http://example.com/");

	return doc	;

			
		}
		
		@Test
		public void validFullResponse() {
			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			XmlObject co = completeResponse();
			ArrayList errorList = new ArrayList();
			boolean validXml = Utilities.validateXml(opt, co, errorList);
			assertTrue(errorList.toString(), validXml);

		}
	
}
