package org.incf.atlas.waxml.examples;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;

import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.ListSRSResponseType.*;
import org.incf.atlas.waxml.generated.QueryInfoType.*;
import org.incf.atlas.waxml.generated.SRSType.*;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.incf.atlas.waxml.utilities.*;

public class DescribeSrsResponse {
	public String  AsXml(){
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		ListSRSResponseDocument document =	ListSRSResponseDocument.Factory.newInstance(); 
		
		ListSRSResponseType rootDoc =	document.addNewListSRSResponse();
		
		QueryUrl qurl =rootDoc.addNewQueryInfo().addNewQueryUrl();
		qurl.setName("ListSRSs");
		
		SRSList srsList = rootDoc.addNewSRSList();
	SRSType srs = 	srsList.addNewSRS();
		SRSType.Name srsname = srs.addNewName();
		srsname.setStringValue("Mouse_ABAreference_1.0");
		srsname.setUrn("INCF:0101");
		srsname.setSrsCode(new QName("INCF:0101"));
		srsname.setSrsBase("ABAreference");
		srsname.setSrsVersion("1.0");
		srsname.setSpecies("Mouse");
		
		Incfdescription desc = srs.addNewDescription();
		
		CIResponsiblePartyType author = srs.addNewAuthor();
		
		IncfCodeType orgin = srs.addNewOrigin();
		
		Area area = srs.addNewArea();
		
		NeurodimensionsType neurodimensions = srs.addNewNeurodimensions();
		
		IncfDocumentCitation cite = srs.addNewSource();
		
		
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
}
