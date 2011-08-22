package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;

import net.opengis.gml.x32.PointType;
import net.opengis.ows.x11.CodeType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.wps.x100.DescriptionType;
import noNamespace.RegistrationResponseType;


import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Test;

public class RegistrationResponse {
	public String asXml() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		RegistrationResponseDocument document = completeResponse();

		ArrayList errorList = new ArrayList();
		Utilities.validateXml(opt, document, errorList);

		return document.xmlText(opt);
	}
	
	public RegistrationResponseDocument completeResponse() {
		RegistrationResponseDocument doc =  	RegistrationResponseDocument.Factory.newInstance();
		
		RegistrationResponseType rr=	doc.addNewRegistrationResponse();
	 	rr.setStatusString("done");
rr.setMessage("worked");

return doc	;

		
	}
	
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
