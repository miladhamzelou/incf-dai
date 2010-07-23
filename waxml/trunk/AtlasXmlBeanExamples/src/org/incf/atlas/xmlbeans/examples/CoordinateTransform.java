package org.incf.atlas.xmlbeans.examples;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.PointType;

import org.incf.atlas.waxml.generated.CoordinateTransformationInfoResponseDocument;
import org.incf.atlas.waxml.generated.CoordinateTransformationInfoResponseDocument.Factory;
import org.incf.atlas.waxml.generated.CoordinateTransformationInfoType.CoordinateTransformation;
import org.incf.atlas.waxml.generated.QueryInfoType.QueryUrl;

import org.incf.atlas.waxml.generated.*
;
public class CoordinateTransform {
public String asXml(){
	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
	
	CoordinateTransformationInfoResponseDocument co =   CoordinateTransformationInfoResponseDocument.Factory.newInstance();
	co.addNewCoordinateTransformationInfoResponse();
	//Query Info
	co.getCoordinateTransformationInfoResponse().addNewQueryInfo();
	QueryInfoType qi = co.getCoordinateTransformationInfoResponse().getQueryInfo();
	QueryUrl url = QueryUrl.Factory.newInstance();
	url.setName("GetTransformationChain");
	url.setStringValue("URL");
	qi.setQueryUrl(url);
	
	qi.addNewCriteria();

	ArrayList<InputType> inputs = new ArrayList<InputType>();
	InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
	inputSrsConstraint.setName("inputSrsName");
	inputSrsConstraint.setValue("Mouse_Paxinos_1.0");
	InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
	ouputSrsConstraint.setName("outputSrsName");
	ouputSrsConstraint.setValue("Mouse_ABAreference_1.0");
	inputs.add(inputSrsConstraint);
	inputs.add(ouputSrsConstraint);
	qi.addNewCriteria().setInputArray(inputs.toArray(qi.addNewCriteria().getInputArray()));
	
	
	CoordinateTransformationInfoType ct= co.addNewCoordinateTransformationInfoResponse().addNewCoordinateTransformationInfo();

	CoordinateTransformation ex1 =ct.addNewCoordinateTransformation();
	ex1.setOrder(1);
	ex1.setCode("Mouse_Paxinos_1.0_To_Mouse_WHS_1.0_1.0");
	ex1.setHub("UCSD");
	ex1.setInputSrsName(new QName("Mouse_Paxinos_1.0"));
	ex1.setTargetSrsName(new QName("Mouse_WHS_1.0"));
	ex1.setAccuracy(1);
	ex1.setStringValue("RequestUrl_1");
	
	CoordinateTransformation ex2 =ct.addNewCoordinateTransformation();
	ex2.setOrder(2);
	ex2.setCode("Mouse_WHS_1.0_To_Mouse_AGEA_1.0_1.0");
	ex2.setHub("ABA");
	ex2.setInputSrsName(new QName("Mouse_WHS_1.0"));
	ex2.setTargetSrsName(new QName("Mouse_AGEA_1.0"));
	ex2.setAccuracy(1);
	ex2.setStringValue("RequestUrl_2");
	
	CoordinateTransformation ex3 =ct.addNewCoordinateTransformation();
	ex3.setOrder(3);
	ex3.setCode("Mouse_AGEA_1.0_To_Mouse_ABAvoxel_1.0_1.0");
	ex3.setHub("ABA");
	ex3.setInputSrsName(new QName("Mouse_AGEA_1.0"));
	ex3.setTargetSrsName(new QName("Mouse_ABAvoxel_1.0"));
//	ex3.setAccuracy(1);
	ex3.setStringValue("RequestUrl_3");
	
	CoordinateTransformation ex4 =ct.addNewCoordinateTransformation();
	ex4.setOrder(4);
	ex4.setCode("Mouse_AGEA_1.0_To_Mouse_ABAvoxel_1.0_1.0");
	ex4.setHub("ABA");
	ex4.setInputSrsName(new QName("Mouse_ABAvoxel_1.0"));
	ex4.setTargetSrsName(new QName("Mouse_ABAreference_1.0"));
//	ex4.setAccuracy(1);
	ex4.setStringValue("RequestUrl_3");
	
	return co.xmlText();
}
}
