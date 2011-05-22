package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import net.opengis.gml.x32.*;

import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.AnnotationType.GEOMETRIES;
import org.incf.atlas.waxml.generated.AnnotationType.ONTOTERMS;
import org.incf.atlas.waxml.generated.AnnotationType.RESOURCE;
import org.incf.atlas.waxml.utilities.*;

import org.junit.Test;

/*
 * ANNOTATION
 * - modified_date
 * - resource
 * * geometry
 * ** geometry (r)
 * *** polygon
 * ***- point (r)
 * * general_onto_model
 * ** Relations
 * *** onto property (R)
 * *** subject
 * **** onto term
 * *** object
 * **** onto term
 * (0r)
 * *** object
 * **** geometry
 * ***** @polygon reference
 * * ontoTerms
 * ** ontoterm
 * ***- comment
 */
public class SetAnnotationResponse {
	
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
		
		ANNOTATIONDocument2 co = completeResponse();
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
		
		ANNOTATIONDocument2 co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml =  Utilities.validateXml(opt, co, errorList);
		 assertTrue(errorList.toString(), validXml);
		
	}
	
	public ANNOTATIONDocument2 completeResponse() {
		ANNOTATIONDocument2 doc = ANNOTATIONDocument2.Factory.newInstance();

		AnnotationType ann = doc.addNewANNOTATION();
		
		ann.setMODIFIEDDATE(Calendar.getInstance());
		
		RESOURCE res = ann.addNewRESOURCE();
		res.setFilepath("http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_23_rec");
		/*
		 * also NumberofBytes X Y Z
		 *     <RESOURCE filepath="string" number_of_bytes="602" xsize="6567" ysize="-9389" zsize="1852" />

		 */

		GEOMETRIES geometries = ann.addNewGEOMETRIES();
		GEOMETRYTYPE geom1 = geometries.addNewGEOMETRY();
		geom1.setUserName("guest");
		geom1.setModifiedTime(1303328463);
		AnnPolygonType polygon1 = geom1.addNewPOLYGON();
		polygon1.setSrsName("mouse");
		polygon1.setId("80087");
		
		AbstractRingPropertyType poly1exterior =  polygon1.addNewExterior();
		poly1exterior.addNewAbstractRing();
		
		LinearRingType ring = LinearRingType.Factory.newInstance();
		poly1exterior.getAbstractRing().set(ring);
		poly1exterior.getAbstractRing().changeType(LinearRingType.type);
		
		
		double[][] pointList = new double[4][3];
		pointList[0] = new double[] { 1.0, 1.0, 1.0 };
		pointList[1] = new double[] { 10.0, 1.0, 1.0 };
		pointList[2] = new double[] { 10.0, 10.0, 1.0 };
		pointList[3] = new double[] { 1.0, 10.0, 1.0 };
		
		//DirectPositionListType pts1 = ring.addNewPosList();
		//pts1.set(ArrayToDirectPositionList(pointList));
		ring.setPosList(ArrayToDirectPositionList(pointList));
		
		// GEOMETRIES NOT WORKING
		
	GENERALONTOMODEL model1 = ann.addNewGENERALONTOMODEL();
	RELATIONSTYPE relations = model1.addNewRELATIONS();
	/*
	 *  <ONTO_PROPERTY onto_name="has_part" 
	 *  onto_uri="http://www.obofoundry.org/ro/ro.owl#has_part" 
	 *  user_name="guest" modified_time="1303328469">
                <SUBJECT>
                    <ONTO_TERM instance_id="85021" 
                    onto_name="mouse"
                     onto_uri="http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Organism.owl#birnlex_167" />
                </SUBJECT>
                <OBJECT>
                    <ONTO_TERM instance_id="85022" 
                    onto_name="cerebellum" 
                    onto_uri="http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-GrossAnatomy.owl#birnlex_1489" />
                </OBJECT>
            </ONTO_PROPERTY>
	 */
ONTOPROPERTYTYPE prop1=	relations.addNewONTOPROPERTY();
prop1.setTitle("has_part" );
prop1.setHref("http://www.obofoundry.org/ro/ro.owl#has_part");
prop1.setUserName("guest");
prop1.setModifiedTime(1303328469);

SUBJECTTYPE subj1 = prop1.addNewSUBJECT();
ONTOTERMTYPE term1_1 = subj1.addNewONTOTERM();
term1_1.setTitle("mouse");
term1_1.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Organism.owl#birnlex_167" );
term1_1.setId("85021");

OBJECTTYPE obj1 = prop1.addNewOBJECT();
ONTOTERMTYPE term1_2 = obj1.addNewONTOTERM();
term1_2.setTitle("cerebellum" );
term1_2.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-GrossAnatomy.owl#birnlex_1489" );
term1_2.setId("85022");

/*
 *            <ONTO_PROPERTY onto_name="has_geometry"
 *             onto_uri="http://ontology.neuinfo.org/NIF/Backend/NIFSTD_Datatype_properties.owl#hasGeometry" 
 *             user_name="guest" modified_time="1303328469">
                <SUBJECT>
                    <ONTO_TERM instance_id="85023" onto_name="pharmetten"
                     onto_uri="http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Chemical.owl#CHEBI_8069" />
                </SUBJECT>
                
                <OBJECT>
                    <GEOMETRY>
                        <POLYGON ID="80087" />
                    </GEOMETRY>
                </OBJECT>
            </ONTO_PROPERTY>
 */
ONTOPROPERTYTYPE prop2=	relations.addNewONTOPROPERTY();
prop2.setTitle("has_geometry");
prop2.setHref("http://ontology.neuinfo.org/NIF/Backend/NIFSTD_Datatype_properties.owl#hasGeometry" );
prop2.setUserName("guest");
prop2.setModifiedTime(1303328469);

SUBJECTTYPE subj2 = prop2.addNewSUBJECT();
ONTOTERMTYPE term2_1=subj2.addNewONTOTERM();
term2_1.setTitle("pharmetten");
term2_1.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Chemical.owl#CHEBI_8069" );
term2_1.setId("85023");

OBJECTTYPE obj2 = prop2.addNewOBJECT();
GEOMETRYTYPE term2_2 = obj2.addNewGEOMETRY();
term2_2.setHref("80087");


	ONTOTERMS terms = ann.addNewONTOTERMS();
	/*
	 * <ONTO_TERM
	      instance_id="85043"
	      onto_name="Azonal"
	      onto_uri="" />
	 */
	ONTOTERMTYPE term1 = terms.addNewONTOTERM();
	term1.setId("85043");
	term1.setTitle("Azonal");
	
	/*
	 *   <ONTO_TERM
	      instance_id="85031"
	      onto_name="purkinje cell"
	      onto_uri="http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Cell.owl#sao471801888" >
<COMMENT>color 0xffffff</COMMENT>
</ONTO_TERM>
	 */
		ONTOTERMTYPE term2 = terms.addNewONTOTERM();
		term2.setId("85031");
		term2.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Cell.owl#sao471801888");
		term2.setTitle("purkinje cell");
		term2.setCOMMENT("color 0xffffff");
		
		/*
		 * <ONTO_TERM
	      instance_id="85034"
	      onto_name="ectopic"
	      onto_uri="http://purl.org/obo/owl/PATO#PATO_0000628" />
		 */
		ONTOTERMTYPE term3 = terms.addNewONTOTERM();
		term2.setId("85034");
		term2.setHref("http://purl.org/obo/owl/PATO#PATO_0000628");
		term2.setTitle("ectopic");

		return doc;

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

}