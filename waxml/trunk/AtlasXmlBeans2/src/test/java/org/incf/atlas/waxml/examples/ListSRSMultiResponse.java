package org.incf.atlas.waxml.examples;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.namespace.QName;

import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.UnitOfMeasureType;

import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection;
import org.incf.atlas.waxml.generated.SRSType.*;
import org.incf.atlas.waxml.generated.OrientationType.Author;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.*;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.junit.Test;
import org.junit.Ignore;

public class ListSRSMultiResponse {
	//@Ignore("not ready")
	@Test
	public void validFullResponse() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		XmlObject co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml = Utilities.validateXml(opt, co, errorList);
		assertTrue(errorList.toString(), validXml);
		

	}
public String  AsXml(){
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
		return document.xmlText(opt);
}
public ListSRSResponseDocument completeResponse() {
	ListSRSResponseDocument document =	ListSRSResponseDocument.Factory.newInstance(); 
	
	ListSRSResponseType rootDoc =	document.addNewListSRSResponse();
	rootDoc.newCursor().insertComment("Test Comment");
	QueryInfoSrs(rootDoc.addNewQueryInfo(), "URL");
	
	SRSCollection coll1 = rootDoc.addNewSRSCollection();
	coll1.setHubCode("HUBA");
	
	org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.SRSList srsList = coll1.addNewSRSList();
	SRSType srs1 =  srsList.addNewSRS();
	SrsExample1(srs1);
	
	SRSType srs2 =  srsList.addNewSRS();
	SrsExample1(srs2);
	
	generateOrientation(coll1);

	return document;
}
private void generateOrientation(SRSCollection coll1) {
	org.incf.atlas.waxml.generated.ListSRSResponseType.SRSCollection.Orientations o = coll1.addNewOrientations();
	OrientationType orientaiton1 = o.addNewOrientation();
	//orientation(orientaiton1,code,name);
	orientation(orientaiton1,"Left","Left");
	orientaiton1 = o.addNewOrientation();
	orientation(orientaiton1,"Right","Right");
	orientaiton1 = o.addNewOrientation();
	orientation(orientaiton1,"Ventral","Ventral");
	orientaiton1 = o.addNewOrientation();
	orientation(orientaiton1,"Dorsal","Dorsal");
	orientaiton1 = o.addNewOrientation();
	orientation(orientaiton1,"Posterior","Posterior");
	orientaiton1 = o.addNewOrientation();
	orientation(orientaiton1,"Anterior","Anterior");
}
public static void SrsExample1(SRSType srs){
	
	 // <SRSName srsCode=”INCF:0101” srsBase=”ABAreference” srsVersion=”1.0” Species=”Mouse” URN=””> Mouse_ABAreference_1.0/>
	Name name =srs.addNewName();
	name.setStringValue("Mouse_ABAreference_1.0");
	name.setSrsCode("INCF:0101");
	name.setSrsBase("ABAreference");
	name.setSrsVersion("1.0");
	name.setSpecies("mouse");
	name.setUrn("ReferenceUrl");
	
	//  <SRSDescription>some description here</SRSDescription>
	Incfdescription srsdescription = srs.addNewDescription();
	srsdescription.setStringValue("some description here");
	
     //   <Author datesubmitted=”DD/MM/YYY” authorCode=”123”>authorname</Author>
AuthorType author = 	srs.addNewAuthor();
 author.setAuthorCode("123");
 author.setDateSubmitted(new XmlCalendar("2011-07-04"));
	
     //   <Origin URN… codespace>bregma</Origin>
IncfCodeType origin = 	srs.addNewOrigin();
origin.setCodeSpace("URN");
origin.setStringValue("bregma");

     //   <Area structureName=”whole brain” structureURN=”…”/>
Area area = srs.addNewArea();
area.setStructureName("whole brain");
area.setStructureURN("URN");

     //   <Units unitsName=”mm” unitsAbbreviation=”mm”/>
UnitOfMeasureType unit = srs.addNewUnits();
unit.setUom("nm");

     /*   <Neurodimensions>
              <MinusX ‘xlink to a record in Orientations element’ maxValue=”7”>Left</MinusX>
              <PlusX ‘xlink to a record in Orientations table’ maxValue=”7”>Right</PlusX>
              <MinusY ‘xlink to a record in Orientations table’ maxValue=”0”>Ventral</MinusY>
              <PlusY ‘xlink to a record in Orientations table’ maxValue=”10”>Dorsal</PlusY>
              <MinusZ ‘xlink to a record in Orientations table’ maxValue=”2”>Posterior</MinusZ>
              <PlusZ ‘xlink to a record in Orientations table’ maxValue=”9”>Anterior</PlusZ>
        </Neurodimensions>
        */
NeurodimensionsType dimensions = srs.addNewNeurodimensions();
NeurodimensionType minusX = dimensions.addNewMinusX();
createNueroDimentions(minusX, "Left", 7, "#left");
		NeurodimensionType minusY = dimensions.addNewMinusY();
		createNueroDimentions(minusY, "Right", 7, "#Right");
NeurodimensionType minusZ = dimensions.addNewMinusZ();
createNueroDimentions(minusZ, "Ventral", 0, "#Ventral");
NeurodimensionType plusX =dimensions.addNewPlusX();
createNueroDimentions(plusX, "Dorsal", 10, "#Dorsal");
NeurodimensionType plusY =dimensions.addNewPlusY();
createNueroDimentions(plusY, "Posterior", 2, "#Posterior");
NeurodimensionType plusZ =dimensions.addNewPlusZ();
createNueroDimentions(plusZ, "Anterior", 9, "#Anterior");

      /*  <Source  format=’…’>
            URI
        </Source>
      */
IncfUriSliceSource cite = srs.addNewSource();
cite.setStringValue("UriREferece");
cite.setFormat("Nifti_1.0");
       //<DerivedFrom srsName=”…” method=”…”/>
       DerivedFrom derived = srs.addNewDerivedFrom();
       derived.setSrsName("Mouse_ABAvoxel_1.0");
       derived.setMethod("MethodName");

       srs.setDateCreated(Calendar.getInstance());
       srs.setDateUpdated(Calendar.getInstance());
	
	return;
}
public static void createNueroDimentions(NeurodimensionType dimension, String name, float maxValue, String xRef){
	dimension.setStringValue(name);
	dimension.setMaxValue(maxValue);
	dimension.setHref(xRef);
}
public static void QueryInfoSrs(QueryInfoType queryInfo, String callUrl)
{
	queryInfo.addNewQueryUrl().setStringValue(callUrl);
	
	
	return ;
}

public static OrientationType orientation(OrientationType orient, String code, String name) {
	
	orient.setCode(code);
	orient.setId(code); // this is what is linked, to
	orient.setName(name);
	Author author = orient.addNewAuthor();
	author.setDateSubmitted(new XmlCalendar("2004-07-04"));
	author.setAuthorCode("AuthorCode");
	author.setStringValue("Author Name");
	
	Incfdescription desc = orient.addNewDescription();
	desc.setStringValue("text");
	
	return orient;
	
}
}

