package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Test;

public class TransformationResponse_pointarray {
	public String AsXml() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		TransformationResponseDocument document = completeResponse();

		ArrayList errorList = new ArrayList();
		Utilities.validateXml(opt, document, errorList);

		return document.xmlText(opt);
	}

	@Test
	public void validFullResponse() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		TransformationResponseDocument co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml = Utilities.validateXml(opt, co, errorList);
		assertTrue(errorList.toString(), validXml);

	}

	public TransformationResponseDocument completeResponse() {
		TransformationResponseDocument document = TransformationResponseDocument.Factory
				.newInstance();

		TransformationResponseType rootDoc = document
				.addNewTransformationResponse();
		rootDoc.newCursor().insertComment("Generated " + Calendar.getInstance().getTime());

		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = rootDoc.addNewQueryInfo();
		query.addNewQueryUrl().setName("TransformPOI");
		query.getQueryUrl().setStringValue("URL");

		Criteria criterias = query.addNewCriteria();

		// InputPOIType poiCriteria = (InputPOIType) criterias.addNewInput()
		// .changeType(InputPOIType.type);
		// poiCriteria.setName("POI");
		// PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		// pnt.setId("id-distinctidRequiredByGML");
		// pnt.setSrsName("Mouse_ABAVoxel_1.0");
		// pnt.addNewPos();
		// pnt.getPos().setStringValue("280 112 162");

Utilities.addInputStringCriteria(criterias,"transformationCode", "Mouse_ABAvoxel_1.0_To_Mouse_ABAreference_1.0_v1.0");

		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue("280");

		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue("112");

		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("y");
		zCriteria.setValue("162");

		InputStringType filterCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("cerebellum");

		POIType poi = rootDoc.addNewPOI();
		DirectPositionListType poipnt = poi.addNewPosList();
		//poipnt.setId("AnyIndentifier");
		poipnt.newCursor().insertComment("id on Point Required By GML\n Scoped to the document only");
		poipnt.setSrsName("Mouse_AGEA_1.0");
	List<XmlDouble> list =	new ArrayList<XmlDouble>();
	list.add( XmlDouble.Factory.newValue(2.0));
	list.add( XmlDouble.Factory.newValue(2.0));
	list.add( XmlDouble.Factory.newValue(2.0));
		
	list.add( XmlDouble.Factory.newValue(3.0));
	list.add( XmlDouble.Factory.newValue(3.0));
	list.add( XmlDouble.Factory.newValue(3.0));
	
	poipnt.setListValue(list);
	poipnt.setSrsDimension( BigInteger.valueOf(3));
	poipnt.setCount( BigInteger.valueOf(list.size()));
//	ArrayList lbl = {"X", "Y", "Z" };
//	poipnt.setAxisLabels( new Vector(lbl) );
		poipnt.newCursor().insertComment("X1 Y2 Z2 X2 Y2 Z2");
		return document;
	}
}
