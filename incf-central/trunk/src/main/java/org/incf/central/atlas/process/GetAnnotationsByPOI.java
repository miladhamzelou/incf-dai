package org.incf.central.atlas.process;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PointPropertyType;
import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlObject;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.atlas.waxml.generated.AnnotationResponseDocument.AnnotationResponse;
import org.incf.atlas.waxml.generated.AnnotationType.GEOMETRIES;
import org.incf.atlas.waxml.generated.AnnotationType.ONTOTERMS;
import org.incf.atlas.waxml.generated.AnnotationType.RESOURCE;
import org.incf.atlas.waxml.generated.AnnPolygonType;
import org.incf.atlas.waxml.generated.AnnotationResponseDocument;
import org.incf.atlas.waxml.generated.AnnotationType;
import org.incf.atlas.waxml.generated.GENERALONTOMODEL;
import org.incf.atlas.waxml.generated.GEOMETRYTYPE;
import org.incf.atlas.waxml.generated.OBJECTTYPE;
import org.incf.atlas.waxml.generated.ONTOPROPERTYTYPE;
import org.incf.atlas.waxml.generated.ONTOTERMTYPE;
import org.incf.atlas.waxml.generated.RELATIONSTYPE;
import org.incf.atlas.waxml.generated.SUBJECTTYPE;
import org.incf.central.atlas.util.CentralConfigurator;
import org.incf.central.atlas.util.CentralServiceDAOImpl;
import org.incf.central.atlas.util.CentralServiceVO;
import org.incf.central.atlas.util.CentralUtil;
import org.incf.central.atlas.util.XMLUtilities;
import org.incf.common.atlas.util.DataInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAnnotationsByPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(GetAnnotationsByPOI.class);

	CentralConfigurator config = CentralConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	String abavoxel2agea = config.getValue("code.abavoxel2agea.v1");
	String agea2abavoxel = config.getValue("code.agea2abavoxel.v1");
	String whs092agea = config.getValue("code.whs092agea.v1");
	String agea2whs09 = config.getValue("code.agea2whs09.v1");
	String whs092whs10 = config.getValue("code.whs092whs10.v1");
	String whs102whs09 = config.getValue("code.whs102whs09.v1");
	String abareference2abavoxel = config
			.getValue("code.abareference2abavoxel.v1");
	String abavoxel2abareference = config
			.getValue("code.abavoxel2abareference.v1");
	String paxinos2whs09 = config.getValue("code.paxinos2whs09.v1");
	String whs092paxinos = config.getValue("code.whs092paxinos.v1");

	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";
	int randomGMLID = 0;
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {

		try {

			CentralServiceVO vo = new CentralServiceVO();

			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));
			String srsName = dataInputHandler.getValidatedStringValue(in,
					"srsName");
			String x = String.valueOf(DataInputHandler.getDoubleInputValue(in,
					"x"));
			String y = String.valueOf(DataInputHandler.getDoubleInputValue(in,
					"y"));
			String z = String.valueOf(DataInputHandler.getDoubleInputValue(in,
					"z"));
			String tolerance = dataInputHandler.getValidatedStringValue(in,
					"tolerance");
			String filter = dataInputHandler.getValidatedStringValue(in,
					"filter");

			vo.setFromSRSCodeOne(srsName);
			vo.setFromSRSCode(srsName);
			//vo.setToSRSCodeOne(agea);
			//vo.setToSRSCode(agea);
			vo.setTolerance(tolerance);
			vo.setFilter(filter);

			if (vo.getFilter().toLowerCase().startsWith("filepath:")) {
				vo.setFilter(vo.getFilter().replaceAll("filePath:", ""));
			}

			LOG.debug("From SRS Code: {}" , vo.getFromSRSCodeOne());
			LOG.debug("Filter: {}" , vo.getFilter());

			// validate data inputs
			vo.setOriginalCoordinateX(x);
			vo.setOriginalCoordinateY(y);
			vo.setOriginalCoordinateZ(z);
			String transformedCoordinatesString = "";

			if (vo.getFromSRSCode().equalsIgnoreCase("image")) {
				vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
				vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
				vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
			} else if (vo.getFromSRSCode().equalsIgnoreCase(abaReference)) {
				vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
				vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
				vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
			} else {
				// Call getTransformationChain method here...
				// ABAVoxel
				vo.setOriginalCoordinateX(";x=" + vo.getOriginalCoordinateX());
				vo.setOriginalCoordinateY(";y=" + vo.getOriginalCoordinateY());
				vo.setOriginalCoordinateZ(";z=" + vo.getOriginalCoordinateZ());
				vo.setToSRSCode(abaReference);
				vo.setToSRSCodeOne(abaReference);

				// Construct GetTransformationChain URL
				// http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

				// Start - FIXME - Uncomment below two lines and comment the
				// other three lines
				// String hostName = uri.getHost();
				// String portNumber = delimitor + uri.getPort();
				String delimitor = config
						.getValue("incf.deploy.port.delimitor");
				hostName = config.getValue("incf.deploy.host.name");
				portNumber = config.getValue("incf.aba.port.number");
				portNumber = delimitor + portNumber;
				// End - FIXME

				//central/atlas
				String servicePath = "/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="
						+ vo.getFromSRSCode()
						+ ";outputSrsName="
						+ vo.getToSRSCode() + ";filter=NONE";
				String transformationChainURL = "http://" + hostName
						+ portNumber + servicePath;
				XMLUtilities xmlUtilities = new XMLUtilities();
				transformedCoordinatesString = xmlUtilities
						.coordinateTransformation(transformationChainURL, vo
								.getOriginalCoordinateX(), vo
								.getOriginalCoordinateY(), vo
								.getOriginalCoordinateZ());

				// Start - exception handling
				if (transformedCoordinatesString.startsWith("Error:")) {
					throw new OWSException("Transformed Coordinates Error: ",
							transformedCoordinatesString);
				}
				// End - exception handling
				CentralUtil util = new CentralUtil();
				String[] tempArray = util
						.getTabDelimNumbers(transformedCoordinatesString);
				vo.setTransformedCoordinateX(tempArray[0]);
				vo.setTransformedCoordinateY(tempArray[1]);
				vo.setTransformedCoordinateZ(tempArray[2]);
			}
			// End

			AnnotationResponseDocument document = completeResponse(vo);

			ArrayList errorList = new ArrayList();
			//opt.setErrorListener(errorList);
			//boolean isValid = document.validate(opt);

			// If the XML isn't valid, loop through the listener's contents,
			// printing contained messages.
/*			if (!isValid) {
				for (int i = 0; i < errorList.size(); i++) {
					XmlError error = (XmlError) errorList.get(i);

					LOG.debug("\n");
					LOG.debug("Message: {}" , error.getMessage() + "\n");
					LOG.debug("Location of invalid XML: {}"
							, error.getCursorLocation().xmlText() + "\n");
				}
			}
*/

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("GetAnnotationsByPOIOutput");

			// get reader on document; reader --> writer
			XMLStreamReader reader = document.newXMLStreamReader();
			XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
			XMLAdapter.writeElement(writer, reader);

		} catch (MissingParameterException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidParameterValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (Throwable e) {
			String message = "Unexpected exception occured";
			LOG.error(message, e);
			OWSException owsException = new OWSException(message, e,
					ControllerException.NO_APPLICABLE_CODE);
			throw new ProcessletException(owsException);
		}

	}


	public AnnotationResponseDocument completeResponse(CentralServiceVO vo) {

		LOG.debug("X1: " + vo.getTransformedCoordinateX());
		LOG.debug("Y1: " + vo.getTransformedCoordinateY());
		LOG.debug("Z1: " + vo.getTransformedCoordinateZ());

		String x1 = vo.getTransformedCoordinateX();
		String x2 = String.valueOf(Double
				.parseDouble(vo.getTransformedCoordinateX())
				+ Double.parseDouble(vo.getTolerance()));
		String x3 = String.valueOf(Double
				.parseDouble(vo.getTransformedCoordinateX())
				+ Double.parseDouble(vo.getTolerance()));
		String x4 = vo.getTransformedCoordinateX();
		String y1 = vo.getTransformedCoordinateY();
		String y2 = vo.getTransformedCoordinateY();
		String y3 = String.valueOf(Double
				.parseDouble(vo.getTransformedCoordinateY())
				- Double.parseDouble(vo.getTolerance()));
		String y4 = String.valueOf(Double
				.parseDouble(vo.getTransformedCoordinateY())
				- Double.parseDouble(vo.getTolerance()));

		AnnotationResponseDocument doc = AnnotationResponseDocument.Factory.newInstance();
		AnnotationResponse annResp = doc.addNewAnnotationResponse();
		String polygonString = x1 + " " + y1 + "," + x2 + " " + y2 + ","
				+ x3 + " " + y3 + "," + x4 + " " + y4 + "," + x1 + " " + y1;
		LOG.debug("Artibrary polygon string - {}", polygonString);

		ArrayList annotationDataList = new ArrayList();
		CentralServiceDAOImpl daoImpl = new CentralServiceDAOImpl();

		LOG.debug("*********************************SRSName outside: "+vo.getFromSRSCode());
		LOG.debug("*********************************filter outside: "+vo.getFilter());
		if (!vo.getFromSRSCode().equalsIgnoreCase("image")) {
			LOG.debug("*********************************SRSName inside Image: "+vo.getSrsName());
			annotationDataList = daoImpl.getAnnotationData(vo, polygonString);
		} else if (vo.getFromSRSCode().equalsIgnoreCase("image")) {
			LOG.debug("*********************************SRSName inside Not Actual SRSName: "+vo.getFromSRSCode());
			annotationDataList = daoImpl.getAnnotationData(vo);
		} else {
			//FIXME - Put some exception code here to throw an exception
			LOG.debug("SRS TYPE NOT SUPPORTED"); 
		}

		Iterator iterator0 = annotationDataList.iterator();
		String id = "";
		String path = "";
		HashMap map = new HashMap();
		while (iterator0.hasNext()) {
			vo = (CentralServiceVO)iterator0.next(); 
			id = vo.getUniqueID();
			path = vo.getOntoFilePath();
			map.put(id, path);
		}
		LOG.debug("Map Size: " + map.size() );

		Iterator annotationsIterator = map.keySet().iterator();
		String key = "";
		while (annotationsIterator.hasNext()) {

			key = annotationsIterator.next().toString();

			LOG.debug("*****************New Annotation*****************" );
			AnnotationType ann = annResp.addNewAnnotation();

			Iterator dataIterator = annotationDataList.iterator();
			GEOMETRYTYPE geom1 = null;
			AnnPolygonType polygon1 = null;
			GEOMETRIES geometries = null;

			geometries = ann.addNewGEOMETRIES();
			geom1 = geometries.addNewGEOMETRY();
			//Resource and Geometries should be only one time
			RESOURCE res = ann.addNewRESOURCE();

/*		CentralServiceVO vo1 = null;
		while (dataIterator.hasNext()) {

			vo1 = (CentralServiceVO)dataIterator.next();

			LOG.debug("**Key from Hash Map:**" + key );
			LOG.debug("**Key from Collection:**" + vo1.getUniqueID() );

		if (key.equals(vo1.getUniqueID())){
*/
			LOG.debug("*****************New Data Inside Annotation*****************" );
		//Loop here - New Annotation

		Iterator iterator1 = annotationDataList.iterator();
		CentralServiceVO vo2 = null; 

		//Delete this loop
		while (iterator1.hasNext()) {

			vo2 = (CentralServiceVO)iterator1.next();

			LOG.debug( "##Original Key MAP INSIDEPOLYGON:##" + key );
			LOG.debug( "##Key from Collection INSIDE POLYGON:##" + vo2.getUniqueID() );

			if (vo2.getUniqueID().equals(key)) {

				polygon1 = geom1.addNewPOLYGON();
				LOG.debug( "##Inside POLYGON:##" );
/*			LOG.debug("Date is: " + date );
			LOG.debug("Time is: " + date.getTime() );
			LOG.debug("Day is: " + date.getDate() );
*/

				//FIXME - update the unique url below from the db
				res.setFilepath(vo2.getOntoFilePath());

				geom1.setUserName(vo2.getUserName());

				if (vo2.getSrsName() == null || vo2.getSrsName().equals("")) {
					polygon1.setSrsName("Undefined");
				} else {
					polygon1.setSrsName(vo2.getSrsName());
				}
				polygon1.setId(gmlId(vo2.getPolygonID()));

				//date = vo.getUpdatedTime();

				//FIXME
				Calendar cal=Calendar.getInstance();
				cal.setTime(vo.getUpdatedTime());
				 
				ann.setMODIFIEDDATE(cal);

				//FIXME - modifiedTime
				//long modifiedTime = Long.parseLong(vo1.getUpdatedTime());
				Date date = null;

				//FIXME
				geom1.setModifiedTime(vo.getUpdatedTime().getTime());

			AbstractRingPropertyType poly1exterior = polygon1.addNewExterior();
			XmlObject ring = poly1exterior.addNewAbstractRing();
			XmlObject linearRing = ring.substitute(new QName ("http://www.opengis.net/gml/3.2","LinearRing"), LinearRingType.type);

			DirectPositionType pos1 =  ((LinearRingType)linearRing).addNewPos();

			pos1.setStringValue(vo2.getTransformedCoordinates().replaceAll(",", " "));
			}
		}

	GENERALONTOMODEL model1 = ann.addNewGENERALONTOMODEL();
	RELATIONSTYPE relations = model1.addNewRELATIONS();

	Iterator iterator2 = annotationDataList.iterator();

	CentralServiceVO vo3 = null;
	while (iterator2.hasNext()) {

		vo3 = (CentralServiceVO) iterator2.next();

		if (vo3.getUniqueID().equals(key)) {

			ONTOPROPERTYTYPE prop2 = relations.addNewONTOPROPERTY();
			prop2.setTitle("has_geometry");
			//Below URL never changes
			prop2.setHref("http://ontology.neuinfo.org/NIF/Backend/NIFSTD_Datatype_properties.owl#hasGeometry" );
			prop2.setUserName(vo3.getUserName());
			//FIXME - modifiedTime
			prop2.setModifiedTime(vo.getUpdatedTime().getTime());
			
			SUBJECTTYPE subj2 = prop2.addNewSUBJECT();
			ONTOTERMTYPE term2_1=subj2.addNewONTOTERM();
			term2_1.setTitle(vo3.getOntoName());
			term2_1.setHref(vo3.getOntoURI());
			term2_1.setId(gmlId(vo3.getPolygonID()));
			//term2_1.newCursor().insertComment(" title = onto_name, href = onto_url gml:id= instance_id");
			
			OBJECTTYPE obj2 = prop2.addNewOBJECT();
			GEOMETRYTYPE term2_2 = obj2.addNewGEOMETRY();
			term2_2.setHref(gmlIdInteralReference(gmlId(vo3.getPolygonID())));

		}
	}

	ONTOTERMS terms = ann.addNewONTOTERMS();

	Iterator iterator3 = annotationDataList.iterator();

	CentralServiceVO vo4 = null;

	while (iterator3.hasNext()) {
	
		vo4 = (CentralServiceVO) iterator3.next();
	
		if (vo4.getUniqueID().equals(key)) {

		ONTOTERMTYPE term2 = terms.addNewONTOTERM();
		term2.setId(gmlId(vo4.getPolygonID()));
		term2.setHref(vo4.getOntoURI());
		term2.setTitle(vo4.getOntoName());

	}
	}
		//}
		//}
		}
		return doc;

	}

	static String gmlId(String id){
		
		final String baseId = "ANN";
		if (isValidGmlId(id)){
			return id;
		} else
		{
			return baseId + id;
		}
	}
	static String gmlId(int id){

		final String baseId = "ANN";
		
			return String.format("{0}{1}", baseId,id);
		
	}
	static String gmlIdInteralReference(String id){
		 if (id == null || id.isEmpty()) throw new IllegalArgumentException("Parameter Cannot be null");
		final String baseReference = "#";
		 return baseReference+ id;
	}
	static Boolean isValidGmlId(String id){
		final  String NCNamePattern =  "[i-[:]][c-[:]]*";
		
		return id.matches(NCNamePattern);
			
	}
	
	static String posListString(double x, double y, double z) {
		String s = String.format("{0},{1},{2}", x, y, z);
		return s;
	}

	static DirectPositionListType ArrayToDirectPositionList(double[][] points) {
		DirectPositionListType posList = DirectPositionListType.Factory
				.newInstance();
		StringBuffer sb = new StringBuffer();
		for (int point = 0; point < points.length; point++) {
			String s = posListString( points[point][0],
					points[point][1], points[point][2]);
			sb.append(s);
			if (point < points.length + 1)
				sb.append(" ");

		}
		return posList;
	}

	
	static PointPropertyType TopointPropertyType(double x, double y, double z) {
		PointPropertyType ppt = PointPropertyType.Factory.newInstance();
		PointType pnt = ppt.addNewPoint();
		DirectPositionType pos = pnt.addNewPos();

		pos.setStringValue(posListString(x, y, z));
		return ppt;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
