package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.values.XmlObjectBase;

import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.ListSRSResponseType.*;
import org.incf.atlas.waxml.generated.QueryInfoType.*;
import org.incf.atlas.waxml.generated.SRSType.*;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Ignore;
import org.junit.Test;

public class DescribeSrsResponse   {
	@Ignore("not ready")
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
		
		DescribeSRSResponseDocument document = completeResponse();
		
		
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

	public DescribeSRSResponseDocument completeResponse() {
		DescribeSRSResponseDocument document =	DescribeSRSResponseDocument.Factory.newInstance(); 
		
		DescribeSRSResponseType rootDoc =	document.addNewDescribeSRSResponse();
		
		
		QueryUrl qurl =rootDoc.addNewQueryInfo().addNewQueryUrl();
		qurl.setName("ListSRSs");
		
		
	SRSType srs = 	 rootDoc.addNewSRS();
		SRSType.Name srsname = srs.addNewName();
		srsname.setStringValue("Mouse_ABAreference_1.0");
		srsname.setUrn("INCF:0101");
		srsname.setSrsCode("INCF:0101");
		srsname.setSrsBase("ABAreference");
		srsname.setSrsVersion("1.0");
		srsname.setSpecies("Mouse");
		
		Incfdescription desc = srs.addNewDescription();
		
		AuthorType author = srs.addNewAuthor();
		
		IncfCodeType orgin = srs.addNewOrigin();
		
		Area area = srs.addNewArea();
		
		NeurodimensionsType neurodimensions = srs.addNewNeurodimensions();
		
		IncfDocumentCitation cite = srs.addNewSource();
		return document;
	}
}
