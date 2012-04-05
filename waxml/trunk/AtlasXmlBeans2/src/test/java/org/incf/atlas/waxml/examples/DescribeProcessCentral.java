package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;

import net.opengis.ows.x11.CodeType;
import net.opengis.ows.x11.DomainMetadataType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.wps.x100.DescriptionType;
import net.opengis.wps.x100.LiteralOutputType;
import net.opengis.wps.x100.OutputDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionType.ProcessOutputs;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.DescribeProcessForHubsResponseDocument;
import org.incf.atlas.waxml.generated.DescribeProcessForHubsResponseType;
import org.incf.atlas.waxml.generated.DescribeProcessForHubsResponseType.DescribeProcessCollection;
import org.incf.atlas.waxml.generated.DescribeSRSResponseDocument;
import org.incf.atlas.waxml.generated.DescribeSRSResponseType;
import org.incf.atlas.waxml.generated.OrientationType;
import org.incf.atlas.waxml.generated.SRSType;
import org.incf.atlas.waxml.generated.DescribeSRSResponseType.Fiducials;
import org.incf.atlas.waxml.generated.DescribeSRSResponseType.Slices;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.Orientations;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.SRSList;
import org.incf.atlas.waxml.utilities.Utilities;
import org.junit.Test;

public class DescribeProcessCentral {
	@Test 
	public void validFullResponse()
	{
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		XmlObject co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml =  Utilities.validateXml(opt, co, errorList);
		 assertTrue(errorList.toString(), validXml);
		
	}
	
	public String  AsXml(){
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		DescribeProcessForHubsResponseDocument document = completeResponse();
		
		
		ArrayList errorList = new ArrayList();
		 opt.setErrorListener(errorList);
		 boolean isValid = document.validate(opt);
		 
		 // If the XML isn't valid, loop through the listener's contents,
		 // printing contained messages.
		 if (!isValid)
		 {
		      for (int i = 0; i < errorList.size(); i++)
		      {
		          XmlError error = (XmlError)errorList.get(i);
		          
		          System.out.println("\n");
		          System.out.println("Message: " + error.getMessage() + "\n");
		          System.out.println("Location of invalid XML: " + 
		              error.getCursorLocation().xmlText() + "\n");
		      }
		 }
			return document.xmlText(opt);
	}

	public DescribeProcessForHubsResponseDocument completeResponse() {
		DescribeProcessForHubsResponseDocument document =	DescribeProcessForHubsResponseDocument.Factory.newInstance(); 
		
		DescribeProcessForHubsResponseType rootDoc =	document.addNewDescribeProcessForHubsResponse();
			
			rootDoc.newCursor().insertComment("Generated " + Calendar.getInstance().getTime());
			ListSRSResponse.QueryInfoSrs(rootDoc.addNewQueryInfo(), "URL");
			rootDoc.getQueryInfo().getQueryUrl().setName("DescribeProcessForHubs");
			DescribeProcessCollection coll1 = rootDoc.addNewDescribeProcessCollection();
			coll1.setHubCode("HUBA");
			
		ProcessDescriptionType pd1	= coll1.addNewProcessDescription();
		pd1.setProcessVersion("1.0.0");
		pd1.setStatusSupported(true);
		pd1.setStoreSupported(false);

CodeType	ident =	pd1.addNewIdentifier();
ident.setStringValue("example");
LanguageStringType title = pd1.addNewTitle();
title.setStringValue("Title");
title.setLang("en");
		LanguageStringType abs = 		pd1.addNewAbstract();
abs.setStringValue("abstract");
ProcessOutputs  po = pd1.addNewProcessOutputs();

OutputDescriptionType odt =po.addNewOutput();

CodeType	oident =	odt.addNewIdentifier();
oident.setStringValue("eample");
LanguageStringType otitle = odt.addNewTitle();
otitle.setStringValue("Tilte");
otitle.setLang("en");
		LanguageStringType oabs = 		odt.addNewAbstract();
oabs.setStringValue("absstract");
LiteralOutputType literal = odt.addNewLiteralOutput();
DomainMetadataType dt =  literal.addNewDataType();
dt.setReference("http://www.w3.org/TR/xmlschema-2/#double");
dt.setStringValue("double");

		/*DescribeProcessCollection coll2 = rootDoc.addNewDescribeProcessCollection();
		coll2.setHubCode("HUBA");

	ProcessDescriptionType pd2 = coll2.addNewProcessDescription();
		ProcessDescriptionType b;
		try {
			b = ProcessDescriptionType.Factory.parse(aProcess);
			pd2.set(b);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

			return document;
		}
}
