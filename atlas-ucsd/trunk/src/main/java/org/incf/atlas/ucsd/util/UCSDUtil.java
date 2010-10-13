package org.incf.atlas.ucsd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.common.util.XMLUtilities;
import org.incf.atlas.ucsd.resource.UCSDServiceVO;
import org.incf.atlas.waxml.generated.CoordinateChainTransformType;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseDocument;
import org.incf.atlas.waxml.generated.CoordinateTransformationInfoType;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.InputType;
import org.incf.atlas.waxml.generated.ListTransformationsResponseDocument;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseType.CoordinateTransformationChain;
import org.incf.atlas.waxml.generated.ListTransformationsResponseType.TransformationList;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.QueryInfoType.QueryUrl;
import org.incf.atlas.waxml.utilities.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UCSDUtil {

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	public String getTransformationChain( UCSDServiceVO vo ) { 

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		CoordinateTransformationChainResponseDocument co =   CoordinateTransformationChainResponseDocument.Factory.newInstance();
		co.addNewCoordinateTransformationChainResponse();
		
		//Query Info
		co.getCoordinateTransformationChainResponse().addNewQueryInfo();
		QueryInfoType qi = co.getCoordinateTransformationChainResponse().getQueryInfo();
		QueryUrl url = QueryUrl.Factory.newInstance();
		url.setName("GetTransformationChain");
		url.setStringValue(vo.getUrlString());
		qi.setQueryUrl(url);
		qi.setTimeCreated(Calendar.getInstance());
	    Criteria criterias = qi.addNewCriteria();

		InputType input1 =criterias.addNewInput();
		InputStringType inputSrsConstraint = (InputStringType) input1.changeType(InputStringType.type);

		//InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
		inputSrsConstraint.setName("inputSrsName");
		inputSrsConstraint.setValue(vo.getFromSRSCode());
			
		InputType input2 =criterias.addNewInput();
		InputStringType ouputSrsConstraint  = (InputStringType) input2.changeType(InputStringType.type);
		
		//InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
		ouputSrsConstraint.setName("outputSrsName");
		ouputSrsConstraint.setValue(vo.getToSRSCode());
		Utilities.addInputStringCriteria(criterias, "filter", vo.getFilter());

		CoordinateTransformationChain ct = co.getCoordinateTransformationChainResponse().addNewCoordinateTransformationChain();
		
/*		ObjectFactory of = new ObjectFactory();
		QueryInfo queryInfo = of.createQueryInfo();
		
		QueryURL queryURL = new QueryURL();
		queryURL.setName("GetTransformationChain");
		queryURL.setValue(vo.getUrlString());
		queryInfo.getQueryURL().add(queryURL);

		queryInfo.setTimeCreated(vo.getCurrentTime());
*/
		try { 

	 		  	//Exception handling somewhere here before going to the first transformation

	 		    String orderNumber = "";
	 		    String code = "";
	 		    String accuracy = "";
	 		    String implementingHub1 = "";
	 		    String implementingHub2 = "";
	 		    String implementingHub3 = "";
	 		    String implementingHub4 = "";
	 		    String transformationURL1 = "";
	 		    String transformationURL2 = "";
	 		    String transformationURL3 = "";
	 		    String transformationURL4 = "";

	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
/*	 			String ucsdHostName = config.getValue("ucsd.host.name");
	 			String ucsdPortNumber = config.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;
*/
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
/*	 			String abaHostName = config.getValue("ucsd.host.name");
	 			String abaPortNumber = config.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;
*/
	 			String whsServicePath = config.getValue("ucsd.whs.service.path");
/*	 			String whsHostName = config.getValue("ucsd.host.name");
	 			String whsPortNumber = config.getValue("ucsd.port.number");
	 			String incfTransformationMatrixURLPrefix = whsHostName + whsPortNumber + whsServicePath;
*/
	 			String incfDeploymentHostName = vo.getIncfDeployHostname();
	 			String incfportNumber = config.getValue("incf.deploy.port.delimitor")+vo.getIncfDeployPortNumber();

	 			String incfTransformationMatrixURLPrefix = incfDeploymentHostName + incfportNumber;

/* 				CoordinateTransformationChain coordinateTransformationInfo = 
 					of.createCoordinateTransformationChain();
*/
	 		    if ( vo.getFromSRSCodeOne() != null ) {

	 		    	if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && 
	 		    		 vo.getToSRSCodeOne().equalsIgnoreCase(whs09) || 
	 		    		 vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && 
	 		    		 vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {
		 		  		implementingHub1 = "WHS";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) ||
		 		    		vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();

		 		    	ex.setOrder(Integer.parseInt(orderNumber));
		 				
		 		    	ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {

	 		    if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs10) && 
	 		    		 vo.getToSRSCodeTwo().equalsIgnoreCase(whs09) || 
	 		    		 vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) && 
	 		    		 vo.getToSRSCodeTwo().equalsIgnoreCase(whs10) ) { 
	 		    	
	 		    	implementingHub2 = "WHS";
	 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
	 		  		vo.setTransformationTwoURL(transformationURL2);
	 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
	 		    	orderNumber = "2";
	 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
	 				ex.setOrder(Integer.parseInt(orderNumber));
	 				ex.setCode(code);
	 				ex.setHub(implementingHub2);
	 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
	 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationTwoURL());
	 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeTwo().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub2 = "UCSD";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) ) {
		 		  	
		 		    	implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(agea) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaVoxel) ) {
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		  		implementingHub2 = "ABA";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {

	 		    	if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs10) && 
	 		    		 vo.getToSRSCodeThree().equalsIgnoreCase(whs09) || 
	 		    		 vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) && 
	 		    		 vo.getToSRSCodeThree().equalsIgnoreCase(whs10) ) { 
	 		    	
						implementingHub3 = "WHS";
				  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
				  		vo.setTransformationThreeURL(transformationURL3);
				  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
				    	orderNumber = "3";
				    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
						ex.setOrder(Integer.parseInt(orderNumber));
						ex.setCode(code);
						ex.setHub(implementingHub3);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationThreeURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeThree().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub3 = "UCSD";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		//transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {

	 		    	if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs10) && 
		 		    		 vo.getToSRSCodeFour().equalsIgnoreCase(whs09) || 
		 		    		 vo.getFromSRSCodeFour().equalsIgnoreCase(whs09) && 
		 		    		 vo.getToSRSCodeFour().equalsIgnoreCase(whs10) ) { 

	 		    		implementingHub4 = "WHS";
				  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
				  		vo.setTransformationFourURL(transformationURL4);
				  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
				    	orderNumber = "4";
				    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
						ex.setOrder(Integer.parseInt(orderNumber));
						ex.setCode(code);
						ex.setHub(implementingHub4);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationFourURL());
		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(paxinos) || 
		 		    	 vo.getToSRSCodeFour().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub4 = "UCSD";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		//transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
		 		  	}
	 		    }

	 			//Put individual element in the super object
/*	 			coordinateChain.setQueryInfo(queryInfo);
	 			coordinateChain.setCoordinateTransformationChain(coordinateTransformationInfo);
*/

	 			 ArrayList errorList = new ArrayList();
	 			 opt.setErrorListener(errorList);
	 			 
	 			 // Validate the XML.
	 			 boolean isValid = co.validate(opt);
	 			 
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
	 			 
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return co.xmlText(opt);
/*		return coordinateChain;
*/
		}

	
	public String listTransformations( UCSDServiceVO vo ) { 

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		ListTransformationsResponseDocument co =   ListTransformationsResponseDocument.Factory.newInstance();
		co.addNewListTransformationsResponse();

		//Query Info
		co.getListTransformationsResponse().addNewQueryInfo();
		QueryInfoType qi = co.getListTransformationsResponse().getQueryInfo();
		QueryUrl url = QueryUrl.Factory.newInstance();
		url.setName("ListTransformations");
		url.setStringValue(vo.getUrlString());
		qi.setQueryUrl(url);
		qi.setTimeCreated(Calendar.getInstance());
	    Criteria criterias = qi.addNewCriteria();

		InputType input1 =criterias.addNewInput();
		InputStringType inputSrsConstraint = (InputStringType) input1.changeType(InputStringType.type);

		//InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
		inputSrsConstraint.setName("inputSrsName");
		inputSrsConstraint.setValue(vo.getFromSRSCode());
			
		InputType input2 =criterias.addNewInput();
		InputStringType ouputSrsConstraint  = (InputStringType) input2.changeType(InputStringType.type);
		
		//InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
		ouputSrsConstraint.setName("outputSrsName");
		ouputSrsConstraint.setValue(vo.getToSRSCode());
		Utilities.addInputStringCriteria(criterias, "filter", vo.getFilter());

		 
		TransformationList ct = co.getListTransformationsResponse().addNewTransformationList();
		
/*		ObjectFactory of = new ObjectFactory();
		QueryInfo queryInfo = of.createQueryInfo();
		
		QueryURL queryURL = new QueryURL();
		queryURL.setName("GetTransformationChain");
		queryURL.setValue(vo.getUrlString());
		queryInfo.getQueryURL().add(queryURL);

		queryInfo.setTimeCreated(vo.getCurrentTime());
*/
		try { 

	 		  	//Exception handling somewhere here before going to the first transformation

	 		    String orderNumber = "";
	 		    String code = "";
	 		    String accuracy = "";
	 		    String implementingHub1 = "";
	 		    String implementingHub2 = "";
	 		    String implementingHub3 = "";
	 		    String implementingHub4 = "";
	 		    String transformationURL1 = "";
	 		    String transformationURL2 = "";
	 		    String transformationURL3 = "";
	 		    String transformationURL4 = "";

	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
/*	 			String ucsdHostName = config.getValue("ucsd.host.name");
	 			String ucsdPortNumber = config.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;
*/
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
/*	 			String abaHostName = config.getValue("ucsd.host.name");
	 			String abaPortNumber = config.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;
*/
	 			String whsServicePath = config.getValue("ucsd.whs.service.path");
/*	 			String whsHostName = config.getValue("ucsd.host.name");
	 			String whsPortNumber = config.getValue("ucsd.port.number");
	 			String incfTransformationMatrixURLPrefix = whsHostName + whsPortNumber + whsServicePath;
*/
	 			String incfDeploymentHostName = vo.getIncfDeployHostname();
	 			String incfportNumber = config.getValue("incf.deploy.port.delimitor")+vo.getIncfDeployPortNumber();

	 			String incfTransformationMatrixURLPrefix = incfDeploymentHostName + incfportNumber;

/* 				CoordinateTransformationChain coordinateTransformationInfo = 
 					of.createCoordinateTransformationChain();
*/
	 		    if ( vo.getFromSRSCodeOne() != null ) {
	 		    	if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && 
		 		    		 vo.getToSRSCodeOne().equalsIgnoreCase(whs09) || 
		 		    		 vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && 
		 		    		 vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

		 		  		implementingHub1 = "WHS";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();

		 		    	ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());

/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {
	 		    	if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs10) && 
		 		    		 vo.getToSRSCodeTwo().equalsIgnoreCase(whs09) || 
		 		    		 vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) && 
		 		    		 vo.getToSRSCodeTwo().equalsIgnoreCase(whs10) ) { 
		 		  	
		 		    	implementingHub2 = "WHS";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(paxinos) || 
		 		    	 vo.getToSRSCodeTwo().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub2 = "UCSD";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) ) {
		 		  	
		 		    	implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(agea) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaVoxel) ) {
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		  		implementingHub2 = "ABA";
		 		  		CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {
	 		    	if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs10) && 
		 		    		 vo.getToSRSCodeThree().equalsIgnoreCase(whs09) || 
		 		    		 vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) && 
		 		    		 vo.getToSRSCodeThree().equalsIgnoreCase(whs10) ) { 
						implementingHub3 = "WHS";
				  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
				  		vo.setTransformationThreeURL(transformationURL3);
				  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
				    	orderNumber = "3";
				    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
						ex.setCode(code);
						ex.setHub(implementingHub3);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationThreeURL());
			/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
						coordinateTransformation.setCode(code);
						coordinateTransformation.setImplementingHub(implementingHub3);
						coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
						coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
						coordinateTransformation.setOrder(orderNumber);
						coordinateTransformation.setAccuracy(accuracy);
						coordinateTransformation.setValue(vo.getTransformationThreeURL());
						coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeThree().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub3 = "UCSD";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		//transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {
	 		    	if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs10) && 
		 		    		 vo.getToSRSCodeFour().equalsIgnoreCase(whs09) || 
		 		    		 vo.getFromSRSCodeFour().equalsIgnoreCase(whs09) && 
		 		    		 vo.getToSRSCodeFour().equalsIgnoreCase(whs10) ) { 
							implementingHub4 = "WHS";
				  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
				  		vo.setTransformationFourURL(transformationURL4);
				  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
				    	orderNumber = "4";
				    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
						ex.setCode(code);
						ex.setHub(implementingHub4);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationFourURL());
			/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
						coordinateTransformation.setCode(code);
						coordinateTransformation.setImplementingHub(implementingHub4);
						coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
						coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
						coordinateTransformation.setOrder(orderNumber);
						coordinateTransformation.setAccuracy(accuracy);
						coordinateTransformation.setValue(vo.getTransformationFourURL());
						coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(paxinos) || 
		 		    	 vo.getToSRSCodeFour().equalsIgnoreCase(paxinos) ) { 
		 		  		implementingHub4 = "UCSD";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		//transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(agea) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation(); 
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }

	 			//Put individual element in the super object
/*	 			coordinateChain.setQueryInfo(queryInfo);
	 			coordinateChain.setCoordinateTransformationChain(coordinateTransformationInfo);
*/

	 			 ArrayList errorList = new ArrayList();
	 			 opt.setErrorListener(errorList);
	 			 
	 			 // Validate the XML.
	 			 boolean isValid = co.validate(opt);
	 			 
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
	 			 
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return co.xmlText(opt);

		}


	//FIXME - amemon - will eventually go to commons
	public String spaceTransformation( UCSDServiceVO vo ) {

		System.out.println("Start - spaceTransformation Method...");

		String xmlResponseString = "";

		try { 

			System.out.println("Start - transformation matrix process...");

			System.out.println("****From SRSCode - " + vo.getFromSRSCodeOne());
			System.out.println("****To SRSCode - " + vo.getToSRSCodeOne());

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			UCSDUtil util = new UCSDUtil();

			if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else {

				xmlResponseString = "NOT SUPPORTED";

			}

			//End

			System.out.println( "XML Response String - " + xmlResponseString ); 
			System.out.println("Ends running transformation  matrix...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the client in a text/xml format
		return xmlResponseString;

	}


	//FIXME - amemon - will eventually go to commons
	public String directSpaceTransformation( String fromSpace, String toSpace, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";
	
    System.out.println("X is - " + originalCoordinateX);
    System.out.println("Y is - " + originalCoordinateY);
    System.out.println("Z is - " + originalCoordinateZ);

	System.out.println("DIRECT SPACE TRANSFORMATION...");

	try {
		//Alex
		if ( fromSpace.trim().equalsIgnoreCase(paxinos) && toSpace.trim().equalsIgnoreCase(whs09) ) { 

			System.out.println("Inside PAXINOS 2 WHS...");

			Paxinos2WHS paxinos2whs = new Paxinos2WHS();
			//client.getTransformation( 0.00, 4.29, -1.94 );
			transformedCoordinateString = paxinos2whs.getTransformation( Double.parseDouble(originalCoordinateX),  
			Double.parseDouble(originalCoordinateY), Double.parseDouble(originalCoordinateZ) );

			System.out.println("Paxinos to WHS - TransformedCoordinateString - "+transformedCoordinateString);

		//Alex
		} else if ( fromSpace.trim().equalsIgnoreCase(whs09) && toSpace.trim().equalsIgnoreCase(paxinos) ) { 

			System.out.println("Inside WHS 2 PAXINOS...");

			WHS2Paxinos whs2paxinos = new WHS2Paxinos();
			transformedCoordinateString = whs2paxinos.getTransformation( Long.parseLong(originalCoordinateX), 
					Long.parseLong(originalCoordinateY), Long.parseLong(originalCoordinateZ) );

			System.out.println("WHS to PAXINOS - TransformedCoordinateString - "+transformedCoordinateString);

		} 
		
		//By Steve Lamont
/*		if (fromSpace.trim().equalsIgnoreCase(abaVoxel) && toSpace.trim().equalsIgnoreCase(agea)) {

			System.out.println("Inside ABAVOX 2 mouse_agea_1.0...");
			
			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.atlas.path");
	
			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+fromSpace.toLowerCase()+"&direction=forward&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}

			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase(agea) && toSpace.trim().equalsIgnoreCase(abaVoxel)) {

			System.out.println("Inside mouse_agea_1.0 2 ABAVOX...");

			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.atlas.path");

			//Coordinates needs to be divided by 25 to come out of mouse_agea_1.0 coordinates
			originalCoordinateX = String.valueOf((Math.round( Double.parseDouble(originalCoordinateX)/25)));
			originalCoordinateY = String.valueOf((Math.round( Double.parseDouble(originalCoordinateY)/25)));
			originalCoordinateZ = String.valueOf((Math.round( Double.parseDouble(originalCoordinateZ)/25)));

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "direction=inverse&atlas="+toSpace.toLowerCase()+"&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);
		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase(whs09) && toSpace.trim().equalsIgnoreCase(agea)) {

			System.out.println("Inside mouse_whs_1.0 2 mouse_agea_1.0...");

			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.atlas.path");

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+fromSpace.toLowerCase()+"&direction=forward&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase(agea) && toSpace.trim().equalsIgnoreCase(whs09)) {

			System.out.println("Inside mouse_agea_1.0 2 mouse_whs_1.0...");

			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.atlas.path");

			//Coordinates needs to be divided by 25 to come out of mouse_agea_1.0 coordinates
			originalCoordinateX = String.valueOf((Math.round( Double.parseDouble(originalCoordinateX)/25)));
			originalCoordinateY = String.valueOf((Math.round( Double.parseDouble(originalCoordinateY)/25)));
			originalCoordinateZ = String.valueOf((Math.round( Double.parseDouble(originalCoordinateZ)/25)));

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "direction=inverse&atlas="+toSpace.toLowerCase()+"&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);
		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase(abaReference) && toSpace.trim().equalsIgnoreCase(abaVoxel)) {

			System.out.println("Inside ABAREF 2 ABAVOX...");

			int[] abar2abav = ABATransform.convertReferenceToVoxel(Double.parseDouble(originalCoordinateX), 
					Double.parseDouble(originalCoordinateY), Double.parseDouble(originalCoordinateZ));

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " "+ originalCoordinateZ + " " + abar2abav[0] + " " + abar2abav[1]  + " " + abar2abav[2];

			System.out.println("ABAR to ABAV - TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Steve Lamont
		else if ( fromSpace.trim().equalsIgnoreCase(abaVoxel) && toSpace.trim().equalsIgnoreCase(abaReference) ) { 

			System.out.println("Inside ABAVOX 2 ABAREF...");

			double[] abav2abar = ABATransform.convertVoxelToReference(Integer.parseInt(originalCoordinateX), 
					Integer.parseInt(originalCoordinateY), Integer.parseInt(originalCoordinateZ)); 

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " "+ originalCoordinateZ + " " + abav2abar[0] + " " + abav2abar[1]  + " " + abav2abar[2];

			System.out.println("ABAV to ABAR - TransformedCoordinateString - "+transformedCoordinateString);
}
*/	else {
		transformedCoordinateString = "No such transformation is available at this point under ABA hub.";
		return transformedCoordinateString;
	} 

	System.out.println("Ends running transformation  matrix...");

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return transformedCoordinateString;

	}

	
	//FIXME - amemon - will eventually go to commons
	public String indirectSpaceTransformation( UCSDServiceVO vo ) {

		System.out.println("Start - INDIRECT SPACE TRANSFORMATION METHOD...");

		//1) Define and Get parameters from URL
		//Define Properties
		System.out.println(" Parameters... " );

		String hostName = config.getValue("ucsd.host.name");
		String servicePath = config.getValue("ucsd.ucsd.service.path");
		String portNumber = config.getValue("ucsd.port.number");
		String transformationMatrixURLPrefix = hostName + portNumber + servicePath;
		
		System.out.println(" X... " + vo.getOriginalCoordinateX() );
		System.out.println(" Y... " + vo.getOriginalCoordinateY() );
		System.out.println(" Z... " + vo.getOriginalCoordinateZ() );

		StringBuffer responseString = new StringBuffer();

		StringBuffer transformedCoordinates = new StringBuffer();
		String transformedCoordinateString = "";
		String xmlResponseString = "";

		String rawTransformationStringOne = "";
		String rawTransformationStringTwo = "";
		String rawTransformationStringThree = "";
		String rawTransformationStringFour = "";
		StringBuffer transformationOne = new StringBuffer();
		StringBuffer transformationTwo = new StringBuffer();
		StringBuffer transformationThree = new StringBuffer();
		StringBuffer transformationFour = new StringBuffer();
		String[] arrayOfTransformedCoordinatesOne = new String[3];
		String[] arrayOfTransformedCoordinatesTwo = new String[3];
		String[] arrayOfTransformedCoordinatesThree = new String[3];
		String[] arrayOfTransformedCoordinatesFour = new String[3];
		
		try { 

			System.out.println("Start - transformation matrix process...");

			UCSDUtil util = new UCSDUtil();

			//via mouse_whs_1.0
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(paxinos);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeOne(whs09);
				vo.setToSRSCodeTwo(agea);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];

				System.out.println("mouse_paxinos_1.0 to mouse_agea_1.0 - TransformedCoordinateString - "+transformedCoordinateString);
				
			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(agea);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(paxinos);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];

				System.out.println("mouse_agea_1.0 to mouse_paxinos_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			//via  mouse_whs_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(paxinos);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - mouse_agea_1.0 to mouse_abavoxel_1.0
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];

				System.out.println("mouse_paxinos_1.0 to mouse_abavoxel_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			//via  mouse_whs_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);
				vo.setFromSRSCodeThree(whs09);
				vo.setToSRSCodeThree(paxinos);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");
				
				//Third convert - mouse_agea_1.0 to mouse_abavoxel_1.0
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];
				System.out.println("mouse_abavoxel_1.0 TO mouse_paxinos_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

				//via  mouse_whs_1.0, and then mouse_agea_1.0, then mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 

				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);
				vo.setFromSRSCodeFour(whs09);
				vo.setToSRSCodeFour(paxinos);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - mouse_agea_1.0 to mouse_abavoxel_1.0
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Fourth convert - mouse_abavoxel_1.0 to mouse_abareference_1.0
				rawTransformationStringFour = util.directSpaceTransformation(vo.getFromSRSCodeFour(), vo.getToSRSCodeFour(), arrayOfTransformedCoordinatesThree[0], arrayOfTransformedCoordinatesThree[1], arrayOfTransformedCoordinatesThree[2] );

				arrayOfTransformedCoordinatesFour = util.getTabDelimNumbers(rawTransformationStringFour);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesFour[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationFour.append(vo.getFromSRSCodeFour()).append(" ")
								 .append(vo.getToSRSCodeFour()).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[2]).append(" ");
				vo.setTransformationFour(transformationFour.toString());
				System.out.println("TransformationFour - " + vo.getTransformationFour());

				//Setting the transformation URL
				vo.setTransformationFourURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x="+arrayOfTransformedCoordinatesThree[0]+"&amp;y="+arrayOfTransformedCoordinatesThree[1]+"&amp;z="+arrayOfTransformedCoordinatesThree[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesFour[0] + " " + arrayOfTransformedCoordinatesFour[1]  + " " + arrayOfTransformedCoordinatesFour[2];
				System.out.println("mouse_abareference_1.0 TO mouse_paxinos_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

				//via  mouse_whs_1.0, and then mouse_agea_1.0, then mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

					//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
					vo.setFromSRSCodeOne(paxinos);
					vo.setToSRSCodeOne(whs09);
					vo.setFromSRSCodeTwo(whs09);
					vo.setToSRSCodeTwo(agea);
					vo.setFromSRSCodeThree(agea);
					vo.setToSRSCodeThree(abaVoxel);
					vo.setFromSRSCodeFour(abaVoxel);
					vo.setToSRSCodeFour(abaReference);

					//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
					rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

					arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
									 .append(vo.getToSRSCodeOne()).append(" ")
									 .append(vo.getOriginalCoordinateX()).append(" ")
									 .append(vo.getOriginalCoordinateY()).append(" ")
									 .append(vo.getOriginalCoordinateZ()).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
					vo.setTransformationOne(transformationOne.toString());
					System.out.println("TransformationOne - " + vo.getTransformationOne());

					//Setting the transformation URL
					vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

					//Second convert - mouse_whs_1.0 to mouse_agea_1.0
					rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

					arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
									 .append(vo.getToSRSCodeTwo()).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
					vo.setTransformationTwo(transformationTwo.toString());
					System.out.println("TransformationTwo - " + vo.getTransformationTwo());

					//Setting the transformation URL
					vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
					
					//Third convert - mouse_agea_1.0 to mouse_abavoxel_1.0
					rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

					arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
									 .append(vo.getToSRSCodeThree()).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
					vo.setTransformationThree(transformationThree.toString());
					System.out.println("TransformationThree - " + vo.getTransformationThree());

					//Setting the transformation URL
					vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

					//Fourth convert - mouse_abavoxel_1.0 to mouse_abareference_1.0
					rawTransformationStringFour = util.directSpaceTransformation(vo.getFromSRSCodeFour(), vo.getToSRSCodeFour(), arrayOfTransformedCoordinatesThree[0], arrayOfTransformedCoordinatesThree[1], arrayOfTransformedCoordinatesThree[2] );

					arrayOfTransformedCoordinatesFour = util.getTabDelimNumbers(rawTransformationStringFour);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesFour[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationFour.append(vo.getFromSRSCodeFour()).append(" ")
									 .append(vo.getToSRSCodeFour()).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[2]).append(" ");
					vo.setTransformationFour(transformationFour.toString());
					System.out.println("TransformationFour - " + vo.getTransformationFour());

					//Setting the transformation URL
					vo.setTransformationFourURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x="+arrayOfTransformedCoordinatesThree[0]+"&amp;y="+arrayOfTransformedCoordinatesThree[1]+"&amp;z="+arrayOfTransformedCoordinatesThree[2]+"&amp;output=html");

					//Get and Set xml response string for transformation info
					vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

					//Return A transformation string
					transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesFour[0] + " " + arrayOfTransformedCoordinatesFour[1]  + " " + arrayOfTransformedCoordinatesFour[2];
					System.out.println("mouse_paxinos_1.0 TO mouse_abareference_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("mouse_abavoxel_1.0 TO mouse_whs_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs09);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(abaVoxel);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("mouse_whs_1.0 TO mouse_abavoxel_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

            //via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("mouse_abareference_1.0 TO mouse_agea_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			//via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(agea);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(abaReference);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("mouse_agea_1.0 TO mouse_abareference_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

	        //via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - mouse_agea_1.0 to mouse_abavoxel_1.0
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];
				System.out.println("mouse_abareference_1.0 TO mouse_whs_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			//via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs09);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(abaVoxel);
				vo.setFromSRSCodeThree(abaVoxel);
				vo.setToSRSCodeThree(abaReference);

				//First convert - mouse_paxinos_1.0 to mouse_whs_1.0
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - mouse_whs_1.0 to mouse_agea_1.0
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - mouse_agea_1.0 to mouse_abavoxel_1.0
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
				
				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];
				System.out.println("mouse_whs_1.0 TO mouse_abareference_1.0 - TransformedCoordinateString - "+transformedCoordinateString);

			} else {
				transformedCoordinateString = "No such transformation is available at this point. Sorry for the inconvinience";
				return transformedCoordinateString;
			} 

			System.out.println( "Transformed Coordinate String - " + transformedCoordinateString ); 
			System.out.println( "Ends running transformation  matrix..." );

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return transformedCoordinateString;

	}

	

	
	//FIXME - amemon - will eventually go to commons
	public String getSpaceTransformationInfoXMLResponseString( UCSDServiceVO vo ) { 

		StringBuffer sb = new StringBuffer();

		try { 
			  
	 		  sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
			    .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\">\n ")

			    .append("<queryinfo>\n")
				//<queryURL name= GetTransformationInfo>URL</queryURL>
				.append("<criteria>\n");

				sb.append("<input name=\"fromSRSCode\">").append(vo.getFromSRSCode()).append("</input>\n");//fromSpaceName
				sb.append("<input name=\"toSRSCode\">").append(vo.getToSRSCode()).append("</input>\n");//vo.getToSRSCodeOne()

				sb.append("</criteria>\n")
				.append("</queryinfo>\n")
				.append("<coordinateTransformationInfo>\n");

	 		  	//Exception handling somewhere here before going to the first transformation

	 		    String orderNumber = "";
	 		    String code = "";
	 		    String implementingHub1 = "";
	 		    String implementingHub2 = "";
	 		    String implementingHub3 = "";
	 		    String implementingHub4 = "";
	 		    String transformationURL1 = "";
	 		    String transformationURL2 = "";
	 		    String transformationURL3 = "";
	 		    String transformationURL4 = "";
	 		    	
	 			String ucsdHostName = config.getValue("ucsd.host.name");
	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
	 			String ucsdPortNumber = config.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;

	 			String abaHostName = config.getValue("ucsd.host.name");
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
	 			String abaPortNumber = config.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;


	 		    //order Number, fromSRSCode2toSRSCode, condition about UCSD or ABA, fromSRSCode, toSRSCode, transformationURL
	 		    if ( vo.getFromSRSCodeOne() != null ) {
		 		    if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {
		 		    if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub2 = "UCSD";
		 		  		transformationURL2 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(agea) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaVoxel) ) {
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  		implementingHub2 = "ABA";
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {
		 		    if ( vo.getFromSRSCodeThree().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub3 = "UCSD";
		 		  		transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {
		 		    if ( vo.getFromSRSCodeFour().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub4 = "UCSD";
		 		  		transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	}
	 		    }
	 		    
/*	 		    if ( vo.getTransformationOne() != null ) {
				} 
	 		    
	 		    if ( vo.getTransformationTwo() != null ) { 
	 		    	String transformationTwo = vo.getTransformationTwo();
	 		    	StringTokenizer tokensTwo = new StringTokenizer(transformationTwo, " ");
	 		    	String[] transformationTwoArray = new String[tokensTwo.countTokens()];
	 		    	int i = 0;
	 		    	while ( tokensTwo.hasMoreTokens() ) {
	 		    		transformationTwoArray[i] = tokensTwo.nextToken();
	 		    	}
	 		    	code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
					orderNumber = "2";
	 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(vo.getTransformationTwoURL()).append("</coordinateTransformation>\n");
				} 
	 		    
	 		    if ( vo.getTransformationThree() != null ) { 
	 		    	String transformationThree = vo.getTransformationThree();
	 		    	StringTokenizer tokensThree = new StringTokenizer(transformationThree, " ");
	 		    	String[] transformationThreeArray = new String[tokensThree.countTokens()];
	 		    	int i = 0;
	 		    	while ( tokensThree.hasMoreTokens() ) {
	 		    		transformationThreeArray[i] = tokensThree.nextToken();
	 		    	}
	 		    	code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
					orderNumber = "3";
	 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(vo.getTransformationThreeURL()).append("</coordinateTransformation>\n");
				}
*/
	 		    sb.append("</coordinateTransformationInfo>\n");
				sb.append("</coordinateTransformationInfoResponse>\n");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

		}

	//FIXME - amemon - will eventually go to commons
	public String errorSpaceTransformationInfoXMLResponse( UCSDServiceVO vo ) { 

		StringBuffer sb = new StringBuffer();

		try { 
			  
	 		  sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
			    .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\">\n ")

			    .append("<queryinfo>\n")
				//<queryURL name= GetTransformationInfo>URL</queryURL>
				.append("<criteria>\n");

				sb.append("<input name=\"fromSRSCode\">").append(vo.getFromSRSCode()).append("</input>\n");//fromSpaceName
				sb.append("<input name=\"toSRSCode\">").append(vo.getToSRSCode()).append("</input>\n");//vo.getToSRSCodeOne()

				sb.append("</criteria>\n")
				.append("</queryinfo>\n")
				.append("<errormessage>\n")
				.append(vo.getErrorMessage());
	 		    sb.append("</errormessage>\n");
				sb.append("</coordinateTransformationInfoResponse>\n");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

		}


	//FIXME - amemon - will eventually go to commons
	public String[] getTabDelimNumbers(String tabLimNumbers ) {

		StringTokenizer tokens = new StringTokenizer(tabLimNumbers, " ");
		int tokensSize = tokens.countTokens();

		String[] coordinateString = new String[tokensSize]; 
		String[] transformedCoordinates = new String[3]; //Returned coordinates are 3
		System.out.println( " tokens - " +tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			i++;
		}

		
		if (coordinateString.length > 3) { 
			transformedCoordinates[0] = coordinateString[3];
			System.out.println( " transformedCoordinates x - " + transformedCoordinates[0] );
			if (tokensSize >= 5 ) {
			transformedCoordinates[1] = coordinateString[4];
			System.out.println( " transformedCoordinates y - " + transformedCoordinates[1] );
			}
			if (tokensSize >= 6 ) {
			transformedCoordinates[2] = coordinateString[5];
			System.out.println( " transformedCoordinates z - " + transformedCoordinates[2] );
			}
		} else if (coordinateString.length == 3) {
			transformedCoordinates[0] = coordinateString[0];
			System.out.println( " transformedCoordinates x - " + transformedCoordinates[0] );
			transformedCoordinates[1] = coordinateString[1];
			transformedCoordinates[2] = coordinateString[2];
		}
			
			
		return transformedCoordinates;

	}

	
	public static void main ( String args[] ) {

		String transformationChainURL = "http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_ABAVoxel_1.0;outputSrsName=Mouse_Paxinos_1.0;filter=Cerebellum";
/*		String x = ";x="+"1";
		String y = ";y="+"4.3";
		String z = ";z="+"1.78";
*/
		String x = ";x=15"; 
		String y = ";y=30";
		String z = ";z=45";


		XMLUtilities util = new XMLUtilities();
		String responseString = util.coordinateTransformation(transformationChainURL, x, y, z);
		System.out.println("ResponseString - " + responseString);
    	if (responseString.startsWith("Error:")) {
    		System.out.println("****ERROR");
    	} else {
    		System.out.println("NO ERROR****");
    	}
	}


/*	public String internalCoordinateTransformations( String transformationChainURL, String x, String y, String z ) {

		UCSDUtil util = new UCSDUtil();
		String responseString = "";
		String resultStringFromURL = "";

		System.out.println("transformationChainURL String - " + transformationChainURL);

		try { 

			resultStringFromURL = util.convertFromURLToString(transformationChainURL);
			Element xmlElement = util.getDocumentElementFromString(resultStringFromURL);
			String[] elementValue = util.getStringValuesForXMLTag(xmlElement, "CoordinateTransformation");

			String filter = ";filter=cerebellum";
			String resultURL = "";
			String resultURLReturnString = "";
			Element resultURLReturnXMLElement = null;

			for (int i = 0; i < elementValue.length; i++ ) {

				String resultURLReturnElementValue = null;
				String abavoxel = abaVoxel; 
				String abareference = abaReference; 
				String paxinos = paxinos; 
				String agea = agea; 
				String whs = whs09;
				String emap = emap;

				//resultURL = elementValue[i].replace("&amp;", "&").replace(";x=", x).replace(";y=", y).replace(";z=", z).replace(";filter=", filter);

				resultURL = elementValue[i].replace("&amp;", "&").replace(";x=", x).replace(";y=", y).replace(";z=", z).replace(";filter=", filter).replace(abaReference, abareference).replace(abaVoxel, abavoxel).replace(agea, agea).replace(whs09, whs).replace(paxinos, paxinos).replace(emap, emap);

				System.out.println("Element Value - " + i + ": " + resultURL);
				resultURLReturnString = util.convertFromURLToString(resultURL);
				System.out.println("1" + resultURLReturnString);

				if (resultURLReturnString.equalsIgnoreCase("transformation-error") ) {
					resultURLReturnElementValue = "Error: Please check the coordinates in the chain url - " + resultURL;
					return resultURLReturnElementValue;
				} else {
					resultURLReturnXMLElement = util.getDocumentElementFromString(resultURLReturnString);
					resultURLReturnElementValue = util.getStringValueForXMLTag(resultURLReturnXMLElement, "gml:pos");
					StringTokenizer tokens = new StringTokenizer(resultURLReturnElementValue, " ");
					while ( tokens.hasMoreTokens() ) {
						x = ";x=" + tokens.nextToken();
						y = ";y=" + tokens.nextToken();
						z = ";z=" + tokens.nextToken();
					}
					responseString = x + " " + y + " " + z;
				}
		
			}

			responseString = responseString.replace(";x=", "").replace(";y=", "").replace(";z=", "");
			//responseString = x + ", " + y + ", " + z;
			System.out.println("after responseString - " + responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseString;
		
	}

	
 	public static String getStringValueForXMLTag(Element xmlElement, String key) {
		NodeList nl = xmlElement.getElementsByTagName(key);
		if (nl.getLength() > 0) {
			Node node = nl.item(0).getFirstChild();
			if (node != null) {
				String output = node.getNodeValue().trim();
				return output;
			}
		}
		return "";
	}

 	public static String[] getStringValuesForXMLTag(Element xmlElement, String key) {
		NodeList nl = xmlElement.getElementsByTagName(key);

		if (nl.getLength() > 0) {

			String[] output = new String[nl.getLength()];

			for ( int i = 0; i < nl.getLength(); i++ ) { 
			Node node = nl.item(i).getFirstChild();
			if (node != null) {
				output[i] = node.getNodeValue().trim();
			}
			}
			return output;
		}
		return null;
	}
*/
 	public String convertFromFileToString(String path) {
		
		byte[] byteContents = null; 
		
		try {
			
			RandomAccessFile inputFile = new RandomAccessFile(path, "r");
		
			byteContents = new byte[(int) inputFile.length()];
		
			inputFile.readFully(byteContents);
		
			return new String(byteContents);
		
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return new String(byteContents);
		
		}


 	public String convertFromURLToString(String stringURL) {
 		
 		String responseString = "";
 		
		try {
			URL url = new URL(stringURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				responseString = responseString + inputLine;
			}
		} catch (MalformedURLException ex) {
			System.out.println("^^^^ERROR1^^^^^");
			System.err.println(ex);
	    } catch (IOException ex) {
			System.out.println("^^^^ERROR2^^^^^");
			System.err.println(ex);
			responseString = "transformation-error";
			return responseString;
	    } catch (Exception e) {
			e.printStackTrace();
		}
		return responseString.replace("&", "&amp;");
 	}

	
	/**
	 * Get root element from XML String
	 * 
	 * @param arg
	 *            XML String
	 * @return Root Element
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Element getDocumentElementFromString(String arg)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document document;
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		document = db.parse(new InputSource(new StringReader(arg)));
		Element element = document.getDocumentElement();
		return element;

	}

	
	public UCSDServiceVO splitCoordinatesFromStringToVO(UCSDServiceVO vo, String completeCoordinatesString ) {

		StringTokenizer tokens = new StringTokenizer(completeCoordinatesString, " ");
		int tokensSize = tokens.countTokens();

		String[] coordinateString = new String[tokensSize]; 
		String[] transformedCoordinates = new String[6]; //Returned coordinates are 3
		System.out.println( " tokens - " +tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			System.out.println( " Token Name - " + coordinateString[i]);
			i++;
		}

/*		vo.setOriginalCoordinateX(coordinateString[0]);
		vo.setOriginalCoordinateY(coordinateString[1]);
		vo.setOriginalCoordinateZ(coordinateString[2]);
*/		vo.setTransformedCoordinateX(coordinateString[3]);
		vo.setTransformedCoordinateY(coordinateString[4]);
		vo.setTransformedCoordinateZ(coordinateString[5]);

		return vo;
		
	}

	public String getCoordinateTransformationChain(UCSDServiceVO vo) {

		System.out.println("Start - getCoordinateTransformationChain Method...");
		String responseString = "";

		try { 

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			UCSDUtil util = new UCSDUtil();

			//mouse_abavoxel_1.0 to mouse_agea_1.0
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}
		
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
				
				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(paxinos);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeOne(whs09);
				vo.setToSRSCodeTwo(agea);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(agea);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(paxinos);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(paxinos);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);
				vo.setFromSRSCodeThree(whs09);
				vo.setToSRSCodeThree(paxinos);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0, then mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);
				vo.setFromSRSCodeFour(whs09);
				vo.setToSRSCodeFour(paxinos);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0, then mouse_abavoxel_1.0
		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				vo.setFromSRSCodeOne(paxinos);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);
				vo.setFromSRSCodeFour(abaVoxel);
				vo.setToSRSCodeFour(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs09);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(abaVoxel);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

            //via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);
				vo.setFromSRSCodeThree(whs09);
				vo.setToSRSCodeThree(whs10);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs10);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

            //via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(agea);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

	        //via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs09);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(abaVoxel);
				vo.setFromSRSCodeThree(abaVoxel);
				vo.setToSRSCodeThree(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}
				
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);
				vo.setFromSRSCodeFour(whs09);
				vo.setToSRSCodeFour(whs10);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs10);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);
				vo.setFromSRSCodeFour(abaVoxel);
				vo.setToSRSCodeFour(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}
				
			}

			//End

			System.out.println("Ends getSpaceTransformationChain Method...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the client in a text/xml format
		return responseString;

	}

}
