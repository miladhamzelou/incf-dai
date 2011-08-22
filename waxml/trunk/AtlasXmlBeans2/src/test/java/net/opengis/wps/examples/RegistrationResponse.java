package net.opengis.wps.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;

import net.opengis.gml.x32.PointType;
import net.opengis.ows.x11.CodeType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.wps.x100.DescriptionType;
import net.opengis.wps.x100.ExecuteResponseDocument;
import net.opengis.wps.x100.ExecuteResponseDocument.ExecuteResponse;
import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.StatusType;

import org.apache.xmlbeans.XmlError;
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

		ExecuteResponseDocument document = completeResponse();

		ArrayList errorList = new ArrayList();
		Utilities.validateXml(opt, document, errorList);

		return document.xmlText(opt);
	}
	
	public ExecuteResponseDocument completeResponse() {
		ExecuteResponseDocument doc =  ExecuteResponseDocument.Factory.newInstance();
		
		ExecuteResponse er=	doc.addNewExecuteResponse();
		er.setServiceInstance("http://example.com/serviceinstance");
		er.setService("WPS");
		er.setVersion("1.0.0");
		er.setLang("es-US");
		
	ProcessBriefType proc = 	er.addNewProcess();
CodeType aId = 	proc.addNewIdentifier();
aId.setStringValue("RegisterWPSIdentifier");
LanguageStringType title = proc.addNewTitle();
title.setStringValue("RegisterWPSName");
proc.setProcessVersion("1.0.0.0");

StatusType status = er.addNewStatus();
status.setCreationTime(Calendar.getInstance());
status.setProcessSucceeded("suceeded");

return doc	;

		
	}
}
