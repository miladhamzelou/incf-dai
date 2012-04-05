package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.CorrelatioMapType.CorrelationCollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Test;

public class CorrelationMapMultipointResponse   {
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

	public String AsXml() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		CorrelationMapResponseDocument document = completeResponse();

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

	public CorrelationMapResponseDocument completeResponse() {
		CorrelationMapResponseDocument document = CorrelationMapResponseDocument.Factory
				.newInstance();

		CorrelatioMapType imagesRes = document.addNewCorrelationMapResponse();
		imagesRes.newCursor().insertComment("Generated " + Calendar.getInstance().getTime());
	// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		Utilities.addMethodNameToQueryInfo(query, "GetCorrelationMapByPOI",
				"URL");

		Criteria criterias = query.addNewCriteria();

		// InputPOIType poiCriteria = (InputPOIType)
		// criterias.addNewInput().changeType(InputPOIType.type);
		// poiCriteria.setName("POI");
		// PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		// pnt.setId("id-onGeomRequiredByGML");
		// pnt.setSrsName("Mouse_ABAvoxel_1.0");
		// pnt.addNewPos();
		// pnt.getPos().setStringValue("1 1 1");

		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue("263");

		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue("159");

		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("y");
		zCriteria.setValue("227");

		InputStringType filterCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("maptype:coronal");

		CorrelationCollection coll1 = imagesRes.addNewCorrelationCollection();
		coll1.setCorrelationUrl("URL");
		coll1.setHubCode("HubA");
		
		CorrelationCollection coll2 = imagesRes.addNewCorrelationCollection();
		coll2.setCorrelationUrl("URL2");
		coll2.setHubCode("HubB");
		
		return document;
	}

}
