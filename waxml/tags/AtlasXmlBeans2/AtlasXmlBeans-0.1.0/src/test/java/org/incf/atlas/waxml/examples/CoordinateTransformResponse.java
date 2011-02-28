package org.incf.atlas.waxml.examples;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.PointType;

import org.incf.atlas.waxml.generated.CoordinateChainTransformType;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseType.CoordinateTransformationChain;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.QueryInfoType.QueryUrl;

import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTransformResponse  {
	
public String asXml(){
	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
	opt.setSaveNamespacesFirst();
	opt.setSaveAggressiveNamespaces();
	opt.setUseDefaultNamespace();
	HashMap dnsMap = new HashMap();
	dnsMap.put("wax", "http://www.incf.org/WaxML/");
	// dnsMap.put("http://www.incf.org/WaxML/", null);
	opt.setSaveImplicitNamespaces(Utilities.SuggestedNamespaces());
	
	CoordinateTransformationChainResponseDocument co = completeResponse();
	ArrayList errorList = new ArrayList();
	 Utilities.validateXml(opt, co, errorList);
	 
	
	return co.xmlText(opt);

	
}

@Test 
public void validFullResponse()
{
	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
	opt.setSaveNamespacesFirst();
	opt.setSaveAggressiveNamespaces();
	opt.setUseDefaultNamespace();
	
	CoordinateTransformationChainResponseDocument co = completeResponse();
	ArrayList errorList = new ArrayList();
	boolean validXml =  Utilities.validateXml(opt, co, errorList);
	 assertTrue(errorList.toString(), validXml);
	
}



public CoordinateTransformationChainResponseDocument completeResponse() {
	CoordinateTransformationChainResponseDocument co =   CoordinateTransformationChainResponseDocument.Factory.newInstance();
	co.addNewCoordinateTransformationChainResponse();
	
	co.getCoordinateTransformationChainResponse().newCursor().insertComment("Generated " + Calendar.getInstance().getTime());

	//Query Info
	co.getCoordinateTransformationChainResponse().addNewQueryInfo();
	QueryInfoType qi = co.getCoordinateTransformationChainResponse().getQueryInfo();
	QueryUrl url = QueryUrl.Factory.newInstance();
	url.setName("GetTransformationChain");
	url.setStringValue("URL");
	qi.setQueryUrl(url);
     Criteria criterias = qi.addNewCriteria();


	InputType input1 =criterias.addNewInput();
	
	InputStringType inputSrsConstraint = (InputStringType) input1.changeType(InputStringType.type);

	//InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
	inputSrsConstraint.setName("inputSrsName");
	inputSrsConstraint.setValue("Mouse_Paxinos_1.0");
		
	InputType input2 =criterias.addNewInput();
	InputStringType ouputSrsConstraint  = (InputStringType) input2.changeType(InputStringType.type);
	
	//InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
	ouputSrsConstraint.setName("outputSrsName");
	ouputSrsConstraint.setValue("Mouse_ABAreference_1.0");
	
	Utilities.addInputStringCriteria(criterias, "filter", "Cerebellum");
	 
	CoordinateTransformationChain ct= co.getCoordinateTransformationChainResponse().addNewCoordinateTransformationChain();
	ct.newCursor().insertComment("CoordinateTransformation  + a required order attribute");
	CoordinateChainTransformType ex1 = ct.addNewCoordinateTransformation();
	ex1.setOrder(1);
	
	ex1.setCode("Mouse_Paxinos_1.0_To_Mouse_WHS_1.0_v1.0");
	ex1.setHub("UCSD");
	ex1.setInputSrsName(new QName("Mouse_Paxinos_1.0"));
	ex1.setOutputSrsName(new QName("Mouse_WHS_1.0"));
	ex1.setAccuracy(1);
	ex1.setVersion("1.0");
	ex1.setStringValue("RequestUrl_1");
	
	CoordinateChainTransformType ex2 =ct.addNewCoordinateTransformation();
	ex2.setOrder(2);
	ex2.setCode("Mouse_WHS_1.0_To_Mouse_AGEA_1.0_v1.0");
	ex2.setHub("ABA");
	ex2.setInputSrsName(new QName("Mouse_WHS_1.0"));
	ex2.setOutputSrsName(new QName("Mouse_AGEA_1.0"));
	ex2.setAccuracy(1);
	ex1.setVersion("1.0");
	ex2.setStringValue("RequestUrl_2");
	
	
	CoordinateChainTransformType ex3 =ct.addNewCoordinateTransformation();
	ex3.setOrder(3);
	ex3.setCode("Mouse_AGEA_1.0_To_Mouse_ABAvoxel_1.0_v1.0");
	ex3.setHub("ABA");
	ex3.setInputSrsName(new QName("Mouse_AGEA_1.0"));
	ex3.setOutputSrsName(new QName("Mouse_ABAvoxel_1.0"));
//	ex3.setAccuracy(1);
	ex1.setVersion("1.0");
	ex3.setStringValue("RequestUrl_3");
	
	CoordinateChainTransformType ex4 =ct.addNewCoordinateTransformation();
	ex4.setOrder(4);
	ex4.setCode("Mouse_AGEA_1.0_To_Mouse_ABAvoxel_1.0_v1.0");
	ex4.setHub("ABA");
	ex4.setInputSrsName(new QName("Mouse_ABAvoxel_1.0"));
	ex4.setOutputSrsName(new QName("Mouse_ABAreference_1.0"));
//	ex4.setAccuracy(1);
	ex4.setStringValue("RequestUrl_4");
	return co;
}
}
