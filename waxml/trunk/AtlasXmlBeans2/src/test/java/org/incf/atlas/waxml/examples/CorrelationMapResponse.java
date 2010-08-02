package org.incf.atlas.waxml.examples;

import java.util.ArrayList;

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.*;

public class CorrelationMapResponse {

	public String AsXml(){
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		CorrelationMapResponseDocument document = CorrelationMapResponseDocument.Factory.newInstance();
		
		CorrelatioMapType imagesRes = document.addNewCorrelationMapResponse();
	// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		Criteria criterias = query.addNewCriteria();
		InputPOIType poiCriteria = (InputPOIType) criterias.addNewInput().changeType(InputPOIType.type);
		poiCriteria.setName("POI");
		PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		pnt.setId("id-onGeomRequiredByGML");
		pnt.setSrsName("Mouse_ABAvoxel_1.0");
pnt.addNewPos();
pnt.getPos().setStringValue("1 1 1");
		
		InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("maptype:coronal");


		imagesRes.setCorrelationUrl("URL");
		
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
