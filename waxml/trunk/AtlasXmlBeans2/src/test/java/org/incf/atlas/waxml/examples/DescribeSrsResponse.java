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
import org.incf.atlas.waxml.generated.DescribeSRSResponseType.Slices;
import org.incf.atlas.waxml.generated.ListSRSResponseType.*;
import org.incf.atlas.waxml.generated.QueryInfoType.*;
import org.incf.atlas.waxml.generated.SRSType.*;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Ignore;
import org.junit.Test;

import org.incf.atlas.waxml.examples.ListSRSResponse;
import org.incf.atlas.waxml.examples.ListSRSResponse.*;

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
			
			rootDoc.newCursor().insertComment("Test Comment");
			ListSRSResponse.QueryInfoSrs(rootDoc.addNewQueryInfo(), "URL");
			SRSList srsList = rootDoc.addNewSRSList();
			SRSType srs1 =  srsList.addNewSRS();
			ListSRSResponse.SrsExample1(srs1);
			
			Orientations o = rootDoc.addNewOrientations();
			OrientationType orientaiton1 = o.addNewOrientation();
			//orientation(orientaiton1,code,name);
			ListSRSResponse.orientation(orientaiton1,"Left","Left");
			orientaiton1 = o.addNewOrientation();
			ListSRSResponse.orientation(orientaiton1,"Right","Right");
			orientaiton1 = o.addNewOrientation();
			ListSRSResponse.orientation(orientaiton1,"Ventral","Ventral");
			orientaiton1 = o.addNewOrientation();
			ListSRSResponse.orientation(orientaiton1,"Dorsal","Dorsal");
			orientaiton1 = o.addNewOrientation();
			ListSRSResponse.orientation(orientaiton1,"Posterior","Posterior");
			orientaiton1 = o.addNewOrientation();
			ListSRSResponse.orientation(orientaiton1,"Anterior","Anterior");
			
			Slices s = rootDoc.addNewSlices();
			s.addNewSlice();
			
			return document;
		}
	
	public static void exampleSlice(SliceType slice, int identifier){
		slice.setOrientation(SliceType.Orientation.HORIZONTAL);
		slice.setXOrientation("positive dorsal");
		slice.setYOrientation("positive coronal"); 
				slice.setConstant(1);
				slice.setCode("REference Number for Slice");
				
	}
	
}
