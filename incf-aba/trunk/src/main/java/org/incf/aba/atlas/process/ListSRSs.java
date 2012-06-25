package org.incf.aba.atlas.process;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.gml.x32.UnitOfMeasureType;

import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
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
import org.incf.aba.atlas.util.ABAConfigurator;
import org.incf.aba.atlas.util.ABAServiceDAOImpl;
import org.incf.aba.atlas.util.ABAServiceVO;
import org.incf.atlas.waxml.generated.AuthorType;
import org.incf.atlas.waxml.generated.IncfCodeType;
import org.incf.atlas.waxml.generated.IncfUriSliceSource;
import org.incf.atlas.waxml.generated.Incfdescription;
import org.incf.atlas.waxml.generated.ListSRSResponseDocument;
import org.incf.atlas.waxml.generated.ListSRSResponseType;
import org.incf.atlas.waxml.generated.NeurodimensionType;
import org.incf.atlas.waxml.generated.NeurodimensionsType;
import org.incf.atlas.waxml.generated.OrientationType;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.Orientations;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.SRSList;
import org.incf.atlas.waxml.generated.OrientationType.Author;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.SRSType;
import org.incf.atlas.waxml.generated.SRSType.Area;
import org.incf.atlas.waxml.generated.SRSType.DerivedFrom;
import org.incf.atlas.waxml.generated.SRSType.Name;
import org.incf.atlas.waxml.utilities.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListSRSs implements Processlet {

	private static final Logger LOG = LoggerFactory.getLogger(ListSRSs.class);

	ABAConfigurator config = ABAConfigurator.INSTANCE;

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

			ABAServiceVO vo = new ABAServiceVO();

			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			ListSRSResponseDocument document = completeResponse();

			ArrayList errorList = new ArrayList();
			opt.setErrorListener(errorList);
			boolean isValid = document.validate(opt);

			// If the XML isn't valid, loop through the listener's contents,
			// printing contained messages.
			if (!isValid) {
				for (int i = 0; i < errorList.size(); i++) {
					XmlError error = (XmlError) errorList.get(i);

					LOG.debug("\n");
					LOG.debug("Message: {}" , error.getMessage() + "\n");
					LOG.debug("Location of invalid XML: {}"
							,error.getCursorLocation().xmlText() + "\n");
				}
			}

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("ListSRSsOutput");

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

	public static void createNueroDimentions(NeurodimensionType dimension,
			String name, float maxValue, String xRef) {
		dimension.setStringValue(name);
		dimension.setMaxValue(maxValue);
		// dimension.setHref(xRef);
	}

	public static void QueryInfoSrs(QueryInfoType queryInfo, String callUrl) {
		queryInfo.addNewQueryUrl().setStringValue(callUrl);

		return;
	}

	public static OrientationType orientation(OrientationType orient,
			String code, String name, String gmlID, String authorCode,
			String authorName, String orientationDescription, String dateSubmitted) {

		orient.setCode(code);
		orient.setId(gmlID); // this is what is linked, to
		orient.setName(name);
		Author author = orient.addNewAuthor();

		Calendar c = Calendar.getInstance();
		try {
			//Start - Date submitted
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	        Date d = sdf.parse(dateSubmitted);
			c.setTime(d);
			//End - Date submitted
		} catch (Exception e) {
			e.printStackTrace();
		}

		author.setDateSubmitted(c);

		//author.setDateSubmitted(Calendar.getInstance());
		author.setAuthorCode(authorCode);
		author.setStringValue(authorName);

		Incfdescription desc = orient.addNewDescription();
		desc.setStringValue(orientationDescription);

		return orient;

	}

	// First SRS
	public static void addSRS(SRSList srsList, ArrayList list, int size) {

		ABAServiceVO vo = null;

		try {

			Iterator iterator = list.iterator();
			SRSType srs = null;

			while (iterator.hasNext()) {

				System.out
						.println("**************************Count is********************* "
								+ list.size());
				srs = srsList.addNewSRS();

				vo = (ABAServiceVO) iterator.next();

				Name name = srs.addNewName();
				name.setStringValue(vo.getSrsName());
				name.setSrsCode(vo.getSrsCode());

				if ( vo.getSrsName().equalsIgnoreCase("Mouse_WHS_0.9") ) {  
					name.setSrsBase(vo.getSrsName().replace("Mouse_", "").replaceAll("_0.9", ""));
				} else {
					name.setSrsBase(vo.getSrsName().replace("Mouse_", "").replaceAll("_1.0", ""));
				}

				name.setSrsVersion(vo.getSrsVersion());
				name.setSpecies(vo.getSpecies());
				// name.setUrn("ReferenceUrl");//Uncomment this once i find the
				// value

				Incfdescription srsdescription = srs.addNewDescription();
				srsdescription.setStringValue(vo.getSrsDescription());

				AuthorType author = srs.addNewAuthor();
				author.setAuthorCode(vo.getSrsAuthorCode());
				
				//Start - Date submitted
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		        Date d = sdf.parse(vo.getSrsDateSubmitted());
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				//End - Date submitted

				author.setDateSubmitted(c);

				IncfCodeType origin = srs.addNewOrigin();
				// origin.setCodeSpace("URN");
				origin.setStringValue(vo.getOrigin());

				// <Area structureName=whole brain structureURN=/>
				Area area = srs.addNewArea();
				area.setStructureName(vo.getRegionOfValidity());
				// area.setStructureURN("URN");

				UnitOfMeasureType unit = srs.addNewUnits();
				unit.setUom(vo.getUnitsAbbreviation());

				NeurodimensionsType dimensions = srs.addNewNeurodimensions();
				NeurodimensionType minusX = dimensions.addNewMinusX();
				createNueroDimentions(minusX, vo.getNeuroMinusXCode(), Float
						.parseFloat(vo.getDimensionMinX()), "#"
						+ vo.getNeuroMinusXCode());
				NeurodimensionType minusY = dimensions.addNewMinusY();
				createNueroDimentions(minusY, vo.getNeuroMinusYCode(), Float
						.parseFloat(vo.getDimensionMinY()), "#"
						+ vo.getNeuroMinusYCode());
				NeurodimensionType minusZ = dimensions.addNewMinusZ();
				createNueroDimentions(minusZ, vo.getNeuroMinusZCode(), Float
						.parseFloat(vo.getDimensionMinZ()), "#"
						+ vo.getNeuroMinusZCode());
				NeurodimensionType plusX = dimensions.addNewPlusX();
				createNueroDimentions(plusX, vo.getNeuroPlusXCode(), Float
						.parseFloat(vo.getDimensionMaxX()), "#"
						+ vo.getNeuroPlusXCode());
				NeurodimensionType plusY = dimensions.addNewPlusY();
				createNueroDimentions(plusY, vo.getNeuroPlusYCode(), Float
						.parseFloat(vo.getDimensionMaxY()), "#"
						+ vo.getNeuroPlusYCode());
				NeurodimensionType plusZ = dimensions.addNewPlusZ();
				createNueroDimentions(plusZ, vo.getNeuroPlusZCode(), Float
						.parseFloat(vo.getDimensionMaxZ()), "#"
						+ vo.getNeuroPlusZCode());

				IncfUriSliceSource cite = srs.addNewSource();
				cite.setStringValue(vo.getSourceURI());
				cite.setFormat(vo.getSourceFileFormat());// Could be null
				DerivedFrom derived = srs.addNewDerivedFrom();
				derived.setSrsName(vo.getDerivedFromSRSCode());
				// derived.setMethod("MethodName");
				//srs.setDateCreated(Calendar.getInstance());
				//srs.setDateUpdated(Calendar.getInstance());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public ListSRSResponseDocument completeResponse() {

		ListSRSResponseDocument document = ListSRSResponseDocument.Factory
				.newInstance();

		ListSRSResponseType rootDoc = document.addNewListSRSResponse();

		//Start - Changes
		//SRSList srsList = rootDoc.addNewSRSList();

		SRSCollection coll1 = rootDoc.addNewSRSCollection();
		coll1.setHubCode("ABA");

		org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.SRSList srsList = coll1.addNewSRSList();
		//SRSType srs1 =  srsList.addNewSRS();
		//SrsExample1(srs1);

		// Start - Get data from the database
		ArrayList list = new ArrayList();
		ABAServiceDAOImpl impl = new ABAServiceDAOImpl();
		list = impl.getSRSsData();
		// End

		addSRS(srsList, list, list.size());

		ArrayList list2 = impl.getOrientationData();
		
		org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.Orientations o = coll1.addNewOrientations();

		Iterator iterator2 = list2.iterator();
		ABAServiceVO vo = null;

		OrientationType orientaiton1 = o.addNewOrientation();
		//o = rootDoc.addNewOrientations();

		Random randomGenerator = new Random();
		
		while (iterator2.hasNext()) {
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID = randomGenerator.nextInt(100);
			}
			vo = (ABAServiceVO) iterator2.next();
			orientaiton1 = o.addNewOrientation();
			orientation(orientaiton1, vo.getOrientationName(), vo
					.getOrientationName(), String.valueOf(randomGMLID), vo
					.getOrientationAuthor(), vo.getOrientationAuthor(), vo
					.getOrientationDescription(), vo.getSrsDateSubmitted());
		}
		return document;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
