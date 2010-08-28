package org.incf.atlas.aba.resource;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.UnitOfMeasureType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.AtlasNamespacePrefixMapper;
import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.ListSRSResponseType.Orientations;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSList;
import org.incf.atlas.waxml.generated.OrientationType.Author;
import org.incf.atlas.waxml.generated.SRSType.Area;
import org.incf.atlas.waxml.generated.SRSType.DerivedFrom;
import org.incf.atlas.waxml.generated.SRSType.Name;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class AllListSRS extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			AllListSRS.class);

	//private String dataInputString;
	//private DataInputs dataInputs;
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;
	int randomGMLID3 = 0;
	int randomGMLID4 = 0;
	int randomGMLID5 = 0;
	int randomGMLID6 = 0;

	String authorCode = "";
	String authorName = "";
	String orientationDescription = "";

	URI uri = null;
	
	public AllListSRS(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());

		try { 
			uri = new URI(request.getResourceRef().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		ABAServiceVO vo = new ABAServiceVO();

	    Random randomGenerator1 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID1 = randomGenerator1.nextInt(100);
	    }

	    Random randomGenerator2 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID2 = randomGenerator1.nextInt(100);
	    }

	    Random randomGenerator3 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID3 = randomGenerator1.nextInt(100);
	    }

	    Random randomGenerator4 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID4 = randomGenerator1.nextInt(100);
	    }

	    Random randomGenerator5 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID5 = randomGenerator1.nextInt(100);
	    }

	    Random randomGenerator6 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID6 = randomGenerator1.nextInt(100);
	    }

        try { 

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
	        
	    //return document.xmlText(opt);
		return new StringRepresentation(document.xmlText(opt),MediaType.APPLICATION_XML);

        } catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void createNueroDimentions(NeurodimensionType dimension, String name, float maxValue, String xRef){
		dimension.setStringValue(name);
		dimension.setMaxValue(maxValue);
		//dimension.setHref(xRef);
	}
	public static void QueryInfoSrs(QueryInfoType queryInfo, String callUrl)
	{
		queryInfo.addNewQueryUrl().setStringValue(callUrl);
		
		
		return ;
	}

	public static OrientationType orientation(OrientationType orient, String code, String name, String gmlID, String authorCode, String authorName, String orientationDescription) { 
		
		orient.setCode(code);
		orient.setId(gmlID); // this is what is linked, to
		orient.setName(name);
		Author author = orient.addNewAuthor();
		author.setDateSubmitted(Calendar.getInstance());
		author.setAuthorCode(authorCode);
		author.setStringValue(authorName);
		
		Incfdescription desc = orient.addNewDescription();
		desc.setStringValue(orientationDescription);
		
		return orient;
		
	}

	//First SRS
	public static void abaReferenceSRS(SRSType srs){
		
		Name name = srs.addNewName();
		name.setStringValue("Mouse_ABAreference_1.0");
		name.setSrsCode("INCF:0101");
		name.setSrsBase("ABAreference");
		name.setSrsVersion("1.0");
		name.setSpecies("Mouse");
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue("ABAreference");

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode("ABA");
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue("bregma");

	     //   <Area structureName=”whole brain” structureURN=”…”/>
	Area area = srs.addNewArea();
	area.setStructureName("whole brain");
	//area.setStructureURN("URN");

	UnitOfMeasureType unit = srs.addNewUnits();
	unit.setUom("mm");

	NeurodimensionsType dimensions = srs.addNewNeurodimensions();
	NeurodimensionType minusX = dimensions.addNewMinusX();
	createNueroDimentions(minusX, "Right", -6, "#left");
			NeurodimensionType minusY = dimensions.addNewMinusY();
			createNueroDimentions(minusY, "Dorsal", -1, "#Right");
	NeurodimensionType minusZ = dimensions.addNewMinusZ();
	createNueroDimentions(minusZ, "Posterior", -9, "#Ventral");
	NeurodimensionType plusX =dimensions.addNewPlusX();
	createNueroDimentions(plusX, "Left", 6, "#Dorsal");
	NeurodimensionType plusY =dimensions.addNewPlusY();
	createNueroDimentions(plusY, "Ventral", 7, "#Posterior");
	NeurodimensionType plusZ =dimensions.addNewPlusZ();
	createNueroDimentions(plusZ, "Anterior", 6, "#Anterior");

	IncfUriSliceSource cite = srs.addNewSource();
	cite.setStringValue("http://mouse.brain-map.org/welcome.do");
	cite.setFormat("Slices");
    DerivedFrom derived = srs.addNewDerivedFrom();
    derived.setSrsName("Mouse_ABAreference_1.0");
    //derived.setMethod("MethodName");
    srs.setDateCreated(Calendar.getInstance());
	srs.setDateUpdated(Calendar.getInstance());

	return;
	
	}

	//Second SRS
	public static void abaVoxelSRS(SRSType srs){
		
		Name name = srs.addNewName();
		name.setStringValue("Mouse_ABAvoxel_1.0");
		name.setSrsCode("INCF:0100");
		name.setSrsBase("ABAvoxel");
		name.setSrsVersion("1.0");
		name.setSpecies("Mouse");
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue("ABAvoxel");

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode("ABA");
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue("front-left-top");

	     //   <Area structureName=”whole brain” structureURN=”…”/>
	Area area = srs.addNewArea();
	area.setStructureName("whole brain");
	//area.setStructureURN("URN");

	UnitOfMeasureType unit = srs.addNewUnits();
	unit.setUom("px");

	NeurodimensionsType dimensions = srs.addNewNeurodimensions();
	NeurodimensionType minusX = dimensions.addNewMinusX();
	createNueroDimentions(minusX, "Anterior", 0, "#left");
	NeurodimensionType minusY = dimensions.addNewMinusY();
	createNueroDimentions(minusY, "Dorsal", 0, "#Right");
	NeurodimensionType minusZ = dimensions.addNewMinusZ();
	createNueroDimentions(minusZ, "Right", 0, "#Ventral");
	NeurodimensionType plusX =dimensions.addNewPlusX();
	createNueroDimentions(plusX, "Posterior", 528, "#Dorsal");
	NeurodimensionType plusY =dimensions.addNewPlusY();
	createNueroDimentions(plusY, "Ventral", 320, "#Posterior");
	NeurodimensionType plusZ =dimensions.addNewPlusZ();
	createNueroDimentions(plusZ, "Left", 456, "#Anterior");

	IncfUriSliceSource cite = srs.addNewSource();
	cite.setStringValue("http://mouse.brain-map.org/welcome.do");
	//cite.setFormat("Slices");
    DerivedFrom derived = srs.addNewDerivedFrom();
    derived.setSrsName("Mouse_ABAvoxel_1.0");
    //derived.setMethod("MethodName");
    srs.setDateCreated(Calendar.getInstance());
	srs.setDateUpdated(Calendar.getInstance());

	return;
	
	}

	//Third SRS
	public static void abaAGEASRS(SRSType srs){
		
		Name name = srs.addNewName();
		name.setStringValue("Mouse_AGEA_1.0");
		name.setSrsCode("INCF:0102");
		name.setSrsBase("AGEA");
		name.setSrsVersion("1.0");
		name.setSpecies("Mouse");
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue("AGEA");

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode("ABA");
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue("front-left-top");

	     //   <Area structureName=”whole brain” structureURN=”…”/>
	Area area = srs.addNewArea();
	area.setStructureName("whole brain");
	//area.setStructureURN("URN");

	UnitOfMeasureType unit = srs.addNewUnits();
	unit.setUom("px");

	NeurodimensionsType dimensions = srs.addNewNeurodimensions();
	NeurodimensionType minusX = dimensions.addNewMinusX();
	createNueroDimentions(minusX, "Anterior", 0, "#left");
	NeurodimensionType minusY = dimensions.addNewMinusY();
	createNueroDimentions(minusY, "Dorsal", 0, "#Right");
	NeurodimensionType minusZ = dimensions.addNewMinusZ();
	createNueroDimentions(minusZ, "Right", 0, "#Ventral");
	NeurodimensionType plusX =dimensions.addNewPlusX();
	createNueroDimentions(plusX, "Posterior", 13200, "#Dorsal");
	NeurodimensionType plusY =dimensions.addNewPlusY();
	createNueroDimentions(plusY, "Ventral", 8000, "#Posterior");
	NeurodimensionType plusZ =dimensions.addNewPlusZ();
	createNueroDimentions(plusZ, "Left", 11400, "#Anterior");

	IncfUriSliceSource cite = srs.addNewSource();
	cite.setStringValue("http://mouse.brain-map.org/welcome.do");
	//cite.setFormat("Slices");
    DerivedFrom derived = srs.addNewDerivedFrom();
    derived.setSrsName("Mouse_AGEA_1.0");
    //derived.setMethod("MethodName");
    srs.setDateCreated(Calendar.getInstance());
	srs.setDateUpdated(Calendar.getInstance());

	return;
	
	}

	//Fourth SRS
	public static void abaPaxinosSRS(SRSType srs){
		
		Name name = srs.addNewName();
		name.setStringValue("Mouse_Paxinos_1.0");
		name.setSrsCode("INCF:0200");
		name.setSrsBase("Paxinos");
		name.setSrsVersion("1.0");
		name.setSpecies("Mouse");
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue("Mouse Brain in Stereotaxic Coordinates, Paxinos-Watson.");

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode("Paxinos");
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue("bregma");

	     //   <Area structureName=”whole brain” structureURN=”…”/>
	Area area = srs.addNewArea();
	area.setStructureName("whole brain");
	//area.setStructureURN("URN");

	UnitOfMeasureType unit = srs.addNewUnits();
	unit.setUom("mm");

	NeurodimensionsType dimensions = srs.addNewNeurodimensions();
	NeurodimensionType minusX = dimensions.addNewMinusX();
	createNueroDimentions(minusX, "Right", -6, "#left");
			NeurodimensionType minusY = dimensions.addNewMinusY();
			createNueroDimentions(minusY, "Dorsal", -1, "#Right");
	NeurodimensionType minusZ = dimensions.addNewMinusZ();
	createNueroDimentions(minusZ, "Posterior", -9, "#Ventral");
	NeurodimensionType plusX =dimensions.addNewPlusX();
	createNueroDimentions(plusX, "Left", 6, "#Dorsal");
	NeurodimensionType plusY =dimensions.addNewPlusY();
	createNueroDimentions(plusY, "Ventral", 7, "#Posterior");
	NeurodimensionType plusZ =dimensions.addNewPlusZ();
	createNueroDimentions(plusZ, "Anterior", 6, "#Anterior");

	IncfUriSliceSource cite = srs.addNewSource();
	//cite.setStringValue("http://mouse.brain-map.org/welcome.do");
	cite.setFormat("Slices");
    DerivedFrom derived = srs.addNewDerivedFrom();
    derived.setSrsName("Mouse_Paxinos_1.0");
    //derived.setMethod("MethodName");
    srs.setDateCreated(Calendar.getInstance());
	srs.setDateUpdated(Calendar.getInstance());

	return;
	
	}

	//Fifth SRS
	public static void abaWHS09SRS(SRSType srs){
		
		Name name = srs.addNewName();
		name.setStringValue("Mouse_WHS_0.9");
		name.setSrsCode("INCF:0001");
		name.setSrsBase("WHS");
		name.setSrsVersion("0.9");
		name.setSpecies("Mouse");
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue("WHS pre-release.");

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode("WHS");
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue("back-left-bottom");

	     //   <Area structureName=”whole brain” structureURN=”…”/>
	Area area = srs.addNewArea();
	area.setStructureName("whole brain");
	//area.setStructureURN("URN");

	UnitOfMeasureType unit = srs.addNewUnits();
	unit.setUom("px");

	NeurodimensionsType dimensions = srs.addNewNeurodimensions();
	NeurodimensionType minusX = dimensions.addNewMinusX();
	createNueroDimentions(minusX, "Right", 0, "#left");
			NeurodimensionType minusY = dimensions.addNewMinusY();
			createNueroDimentions(minusY, "Posterior", 0, "#Right");
	NeurodimensionType minusZ = dimensions.addNewMinusZ();
	createNueroDimentions(minusZ, "Ventral", 0, "#Ventral");
	NeurodimensionType plusX =dimensions.addNewPlusX();
	createNueroDimentions(plusX, "Left", 511, "#Dorsal");
	NeurodimensionType plusY =dimensions.addNewPlusY();
	createNueroDimentions(plusY, "Anterior", 1023, "#Posterior");
	NeurodimensionType plusZ =dimensions.addNewPlusZ();
	createNueroDimentions(plusZ, "Dorsal", 511, "#Anterior");

	IncfUriSliceSource cite = srs.addNewSource();
	cite.setStringValue("http://software.incf.org/software/waxholm-space");
	cite.setFormat("Zipped NIFTI");
    DerivedFrom derived = srs.addNewDerivedFrom();
    derived.setSrsName("Mouse_WHS_0.9");
    //derived.setMethod("MethodName");
    srs.setDateCreated(Calendar.getInstance());
	srs.setDateUpdated(Calendar.getInstance());

	return;
	
	}

	//Sixth SRS
	public static void abaWHS10SRS(SRSType srs){
		
		Name name = srs.addNewName();
		name.setStringValue("Mouse_WHS_1.0");
		name.setSrsCode("INCF:0002");
		name.setSrsBase("WHS");
		name.setSrsVersion("1.0");
		name.setSpecies("Mouse");
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue("WHS release.");

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode("WHS");
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue("AC-midline");

	     //   <Area structureName=”whole brain” structureURN=”…”/>
	Area area = srs.addNewArea();
	area.setStructureName("whole brain");
	//area.setStructureURN("URN");

	UnitOfMeasureType unit = srs.addNewUnits();
	unit.setUom("mm");

	NeurodimensionsType dimensions = srs.addNewNeurodimensions();
	NeurodimensionType minusX = dimensions.addNewMinusX();
	createNueroDimentions(minusX, "Right", Float.parseFloat("-5.3965"), "#left");
			NeurodimensionType minusY = dimensions.addNewMinusY();
			createNueroDimentions(minusY, "Posterior", Float.parseFloat("-11.997"), "#Right");
	NeurodimensionType minusZ = dimensions.addNewMinusZ();
	createNueroDimentions(minusZ, "Ventral", Float.parseFloat("-5.5255"), "#Ventral");
	NeurodimensionType plusX =dimensions.addNewPlusX();
	createNueroDimentions(plusX, "Left", Float.parseFloat("5.59"), "#Dorsal");
	NeurodimensionType plusY =dimensions.addNewPlusY();
	createNueroDimentions(plusY, "Anterior", Float.parseFloat("10"), "#Posterior");
	NeurodimensionType plusZ =dimensions.addNewPlusZ();
	createNueroDimentions(plusZ, "Dorsal", Float.parseFloat("5.46"), "#Anterior");

	IncfUriSliceSource cite = srs.addNewSource();
	cite.setStringValue("http://software.incf.org/software/waxholm-space");
	cite.setFormat("Zipped NIFTI");
    DerivedFrom derived = srs.addNewDerivedFrom();
    derived.setSrsName("Mouse_WHS_1.0");
    //derived.setMethod("MethodName");
    srs.setDateCreated(Calendar.getInstance());
	srs.setDateUpdated(Calendar.getInstance());

	return;
	
	}
	
	public ListSRSResponseDocument completeResponse() {
		ListSRSResponseDocument document =	ListSRSResponseDocument.Factory.newInstance(); 
		
		ListSRSResponseType rootDoc =	document.addNewListSRSResponse();
		//rootDoc.newCursor().insertComment("Test Comment");
		QueryInfoSrs(rootDoc.addNewQueryInfo(), uri.toString());
		SRSList srsList = rootDoc.addNewSRSList();
		SRSType srs1 =  srsList.addNewSRS();
		SRSType srs2 =  srsList.addNewSRS();
		SRSType srs3 =  srsList.addNewSRS();
		SRSType srs4 =  srsList.addNewSRS();
		SRSType srs5 =  srsList.addNewSRS();
		SRSType srs6 =  srsList.addNewSRS();
		abaReferenceSRS(srs1);
		abaVoxelSRS(srs2);
		abaAGEASRS(srs3);
		abaPaxinosSRS(srs4);
		abaWHS09SRS(srs5);
		abaWHS10SRS(srs6);
		
		Orientations o = rootDoc.addNewOrientations();
		OrientationType orientaiton1 = o.addNewOrientation();
		//orientation(orientaiton1,code,name);
		orientation(orientaiton1,"Left","Left",String.valueOf(randomGMLID1), "standard", "Standard", "Left of midline.");
		orientaiton1 = o.addNewOrientation();
		orientation(orientaiton1,"Right","Right",String.valueOf(randomGMLID2), "standard", "Standard", "Right of midline.");
		orientaiton1 = o.addNewOrientation();
		orientation(orientaiton1,"Ventral","Ventral",String.valueOf(randomGMLID3), "standard", "Standard", "Towards the abdomen/front.");
		orientaiton1 = o.addNewOrientation();
		orientation(orientaiton1,"Dorsal","Dorsal",String.valueOf(randomGMLID4), "standard", "Standard", "Toward spinal column/back.");
		orientaiton1 = o.addNewOrientation();
		orientation(orientaiton1,"Posterior","Posterior",String.valueOf(randomGMLID5), "standard", "Standard", "Towards the back.");
		orientaiton1 = o.addNewOrientation();
		orientation(orientaiton1,"Anterior","Anterior",String.valueOf(randomGMLID6), "standard", "Standard", "Towards the front.");
		
		return document;
	}

}
