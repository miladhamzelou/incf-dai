package org.incf.atlas.xmlbeans.examples;
import java.util.ArrayList;

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;

public class ListSRSResponse {
public String  AsXml(){
	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
	opt.setSaveNamespacesFirst();
	opt.setSaveAggressiveNamespaces();
	opt.setUseDefaultNamespace();
	
	ListSRSResponseDocument document =	ListSRSResponseDocument.Factory.newInstance(); 
	
	ListSRSResponseType rootDoc =	document.addNewListSRSResponse();
	
	
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
