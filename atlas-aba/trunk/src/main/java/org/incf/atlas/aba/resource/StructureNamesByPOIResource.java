package org.incf.atlas.aba.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.generated.transformpoi.ObjectFactory;
import org.incf.atlas.generated.transformpoi.POI;
import org.incf.atlas.generated.transformpoi.QueryInfo;
import org.incf.atlas.generated.transformpoi.TransformationResponse;
import org.incf.atlas.generated.transformpoi.QueryInfo.Criteria;
import org.incf.atlas.generated.transformpoi.QueryInfo.QueryURL;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructureNamesByPOIResource extends Resource {

	private final Logger logger = LoggerFactory.getLogger(
			TransformPOIResource.class);

	private String dataInputString;
	private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String url = "";

	public StructureNamesByPOIResource(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		System.out.println("You are in StructureNamesByPOIResource");
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		System.out.println("dataInputString " + dataInputString );
		
		dataInputs = new DataInputs(dataInputString);

		//FIXME - amemon - read the hostname from the config file 
		ABAConfigurator config = ABAConfigurator.INSTANCE;
		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		portNumber = ":8080";
		servicePath = "/atlas-aba?Request=Execute&Identifier=StructureNamesByPOI";
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));

	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		try { 

		String fromSRSCode = "";
		String vocabulary = "";
		String filter = "";
		String coordinateX = "";
		String coordinateY = "";
		String coordinateZ = "";
		
		// text return for debugging
		ABAServiceVO vo = new ABAServiceVO();
		Set<String> dataInputKeys = dataInputs.getKeys();
		for (String key : dataInputKeys) {
			if (key.equalsIgnoreCase("srsCode")) {
				fromSRSCode = dataInputs.getValue(key);
				vo.setFromSRSCode(fromSRSCode);
				vo.setFromSRSCodeOne(fromSRSCode);
			} else if (key.equalsIgnoreCase("vocabulary")) {
				vocabulary = dataInputs.getValue(key);
				vo.setVocabulary(vocabulary);
			} else if (key.equalsIgnoreCase("filter")) {
				filter = dataInputs.getValue(key);
				vo.setFilter(filter);
			} else if (key.equalsIgnoreCase("x")) {
				coordinateX = dataInputs.getValue(key);
				vo.setOriginalCoordinateX(coordinateX);
			} else if (key.equalsIgnoreCase("y")) {
				coordinateY = dataInputs.getValue(key);
				vo.setOriginalCoordinateY(coordinateY);
			} else if (key.equalsIgnoreCase("z")) {
				coordinateZ = dataInputs.getValue(key);
				vo.setOriginalCoordinateZ(coordinateZ);
			}
		}

		//Start - Call the main method here
		ABAUtil util = new ABAUtil();
		String completeCoordinatesString = util.spaceTransformation(vo);

		vo = util.splitCoordinatesFromStringToVO(vo, completeCoordinatesString);
		//End

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputString;
        vo.setUrlString(url);
        
		ObjectFactory of = new ObjectFactory();
		TransformationResponse transformationResponse = 
			of.createTransformationResponse();

		//Query Info setters
		QueryInfo queryInfo = new QueryInfo();
		QueryInfo.Criteria.Input.Point srcPoint = new QueryInfo.Criteria.Input.Point();
		srcPoint.setSrcName(vo.getFromSRSCode());//Change
		srcPoint.setPos(vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " " + vo.getOriginalCoordinateZ());//Change
		QueryURL queryURL = new QueryURL();
		queryURL.setName("TransformPOI");
		queryURL.setValue(url);//Change
		Criteria.Input input = new Criteria.Input();
		input.setName("POI");
		input.setPoint(srcPoint);
		Criteria criteria = new Criteria();
		criteria.getInput().add(input);
		queryInfo.getTimeCreatedAndQueryURLAndCriteria().add(queryURL);
		queryInfo.getTimeCreatedAndQueryURLAndCriteria().add(currentTime);//Change
		queryInfo.getTimeCreatedAndQueryURLAndCriteria().add(criteria);

		//TransformPOI responses
		POI poi = new POI();
		POI.Point destPoint = new POI.Point();
		destPoint.setDestName(vo.getToSRSCode());//Change
		destPoint.setPos(vo.getTransformedCoordinateX() + " " + vo.getTransformedCoordinateY() + " " + vo.getTransformedCoordinateZ());//Change
		poi.setPoint(destPoint);

		transformationResponse.setQueryInfo(queryInfo);
		transformationResponse.setPOI(poi);
		
		//ABAUtil util = new ABAUtil();
		//transformationPOIMain = util.getCoordinateTransformationChain(vo, coordinateChain);

		//generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<TransformationResponse>(transformationResponse);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
