package org.incf.atlas.waxml.examples;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.namespace.QName;

import net.opengis.gml.x32.*;

import org.apache.xmlbeans.XmlObject;
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
public class SetAnnotationResponseLine {

	public String asXml() {
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
	public void validFullResponse() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ANNOTATIONDocument2 co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml = Utilities.validateXml(opt, co, errorList);
		assertTrue(errorList.toString(), validXml);

	}

	public ANNOTATIONDocument2 completeResponse() {
		ANNOTATIONDocument2 doc = ANNOTATIONDocument2.Factory.newInstance();

		AnnotationType ann = doc.addNewANNOTATION();

		ann.setMODIFIEDDATE(Calendar.getInstance());

		RESOURCE res = ann.addNewRESOURCE();
		res.setFilepath("http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_23_rec");
		/*
		 * also NumberofBytes X Y Z <RESOURCE filepath="string"
		 * number_of_bytes="602" xsize="6567" ysize="-9389" zsize="1852" />
		 */

		GEOMETRIES geometries = ann.addNewGEOMETRIES();
		geometries.newCursor().insertComment(
				"PolyLINE object with gml:id=" + gmlId("80087"));
		GEOMETRYTYPE geom1 = geometries.addNewGEOMETRY();

		geom1.setUserName("guest");
		geom1.setModifiedTime(1303328463);
		AnnPolylineType polyLine1 = geom1.addNewPOLYLINE();
		polyLine1.setSrsName("mouse");
		polyLine1.setId(gmlId("80087"));
		polyLine1.newCursor().insertComment(
				"PolyLINE object with gml:id=" + gmlId("80087"));
		
		  DirectPositionListType poly1exterior = polyLine1.addNewPosList();
		 
		 double[][] pointList = new double[4][3];
		 pointList[0] = new double[] { 1.0, 1.0, 1.0 };
		 pointList[1] = new double[] { 10.0, 1.0, 1.0 };
		 pointList[2] = new double[] { 10.0, 10.0, 1.0 };
		 pointList[3] = new double[] { 1.0, 10.0, 1.0 };
		 
		 XmlObject alist= poly1exterior.set(ArrayToDirectPositionList(pointList));
//		
		
		 
		// GEOMETRIES NOT WORKING

		GENERALONTOMODEL model1 = ann.addNewGENERALONTOMODEL();
		RELATIONSTYPE relations = model1.addNewRELATIONS();
		/*
		 * <ONTO_PROPERTY onto_name="has_part"
		 * onto_uri="http://www.obofoundry.org/ro/ro.owl#has_part"
		 * user_name="guest" modified_time="1303328469"> <SUBJECT> <ONTO_TERM
		 * instance_id="85021" onto_name="mouse" onto_uri=
		 * "http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Organism.owl#birnlex_167"
		 * /> </SUBJECT> <OBJECT> <ONTO_TERM instance_id="85022"
		 * onto_name="cerebellum" onto_uri=
		 * "http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-GrossAnatomy.owl#birnlex_1489"
		 * /> </OBJECT> </ONTO_PROPERTY>
		 */
		ONTOPROPERTYTYPE prop1 = relations.addNewONTOPROPERTY();
		prop1.setTitle("has_part");
		prop1.setHref("http://www.obofoundry.org/ro/ro.owl#has_part");
		prop1.setUserName("guest");
		prop1.setModifiedTime(1303328469);

		SUBJECTTYPE subj1 = prop1.addNewSUBJECT();
		ONTOTERMTYPE term1_1 = subj1.addNewONTOTERM();
		term1_1.setTitle("mouse");
		term1_1.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Organism.owl#birnlex_167");
		term1_1.setId(gmlId("85021"));

		OBJECTTYPE obj1 = prop1.addNewOBJECT();
		ONTOTERMTYPE term1_2 = obj1.addNewONTOTERM();
		term1_2.setTitle("cerebellum");
		term1_2.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-GrossAnatomy.owl#birnlex_1489");
		term1_2.setId(gmlId("85022"));

		/*
		 * <ONTO_PROPERTY onto_name="has_geometry" onto_uri=
		 * "http://ontology.neuinfo.org/NIF/Backend/NIFSTD_Datatype_properties.owl#hasGeometry"
		 * user_name="guest" modified_time="1303328469"> <SUBJECT> <ONTO_TERM
		 * instance_id="85023" onto_name="pharmetten" onto_uri=
		 * "http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Chemical.owl#CHEBI_8069"
		 * /> </SUBJECT>
		 * 
		 * <OBJECT> <GEOMETRY> <POLYGON ID="80087" /> </GEOMETRY> </OBJECT>
		 * </ONTO_PROPERTY>
		 */
		ONTOPROPERTYTYPE prop2 = relations.addNewONTOPROPERTY();
		prop2.setTitle("has_geometry");
		prop2.setHref("http://ontology.neuinfo.org/NIF/Backend/NIFSTD_Datatype_properties.owl#hasGeometry");
		prop2.setUserName("guest");
		prop2.setModifiedTime(1303328469);
		prop2.newCursor().insertComment(
				" title = onto_name, href = onto_url gml:id= instance_id");

		SUBJECTTYPE subj2 = prop2.addNewSUBJECT();
		ONTOTERMTYPE term2_1 = subj2.addNewONTOTERM();
		term2_1.setTitle("pharmetten");
		term2_1.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Chemical.owl#CHEBI_8069");
		term2_1.setId(gmlId("85023"));
		term2_1.newCursor().insertComment(
				" title = onto_name, href = onto_url gml:id= instance_id");

		OBJECTTYPE obj2 = prop2.addNewOBJECT();
		GEOMETRYTYPE term2_2 = obj2.addNewGEOMETRY();
		term2_2.setHref(gmlIdInteralReference(gmlId("80087")));
		term2_2.newCursor().insertComment(
				" href refers to polygon object with gml:id=" + gmlId("80087"));

		ONTOTERMS terms = ann.addNewONTOTERMS();
		/*
		 * <ONTO_TERM instance_id="85043" onto_name="Azonal" onto_uri="" />
		 */
		ONTOTERMTYPE term1 = terms.addNewONTOTERM();
		term1.setId(gmlId("85043"));
		term1.setTitle("Azonal");
		term1.newCursor().insertComment(
				" title = onto_name, href = onto_url gml:id= instance_id");

		term1.newCursor().insertComment("no uri; ");

		/*
		 * <ONTO_TERM instance_id="85031" onto_name="purkinje cell" onto_uri=
		 * "http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Cell.owl#sao471801888"
		 * > <COMMENT>color 0xffffff</COMMENT> </ONTO_TERM>
		 */
		ONTOTERMTYPE term2 = terms.addNewONTOTERM();
		term2.setId(gmlId("85031"));
		term2.setHref("http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Cell.owl#sao471801888");
		term2.setTitle("purkinje cell");
		term2.setCOMMENT("color 0xffffff");
		term2.newCursor().insertComment(
				" title = onto_name, href = onto_url gml:id= instance_id");
		term2.newCursor().insertComment(" uses comment element");

		/*
		 * <ONTO_TERM instance_id="85034" onto_name="ectopic"
		 * onto_uri="http://purl.org/obo/owl/PATO#PATO_0000628" />
		 */
		ONTOTERMTYPE term3 = terms.addNewONTOTERM();
		term3.setId(gmlId("85034"));
		term3.setHref("http://purl.org/obo/owl/PATO#PATO_0000628");
		term3.setTitle("ectopic");

		return doc;

	}

	static String gmlId(String id) {

		final String baseId = "ANN";
		if (isValidGmlId(id)) {
			return id;
		} else {
			return baseId + id;
		}
	}

	static String gmlId(int id) {

		final String baseId = "ANN";

		return String.format("{0}{1}", baseId, id);

	}

	static String gmlIdInteralReference(String id) {
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Parameter Cannot be null");
		final String baseReference = "#";
		return baseReference + id;
	}

	static Boolean isValidGmlId(String id) {
		final String NCNamePattern = "[i-[:]][c-[:]]*";

		return id.matches(NCNamePattern);

	}

	static String posListString(double x, double y, double z) {
		String s = String.format("%g %g %g", x, y, z);
		return s;
	}

	static DirectPositionListType ArrayToDirectPositionList(double[][] points) {
		DirectPositionListType posList = DirectPositionListType.Factory
				.newInstance();
		posList.setSrsDimension(BigInteger.valueOf((long) 3.0));
		
		StringBuffer sb = new StringBuffer();
		for (int point = 0; point < points.length; point++) {
			String s = posListString(points[point][0], points[point][1],
					points[point][2]);
			sb.append(s);
			if (point < points.length + 1)
				sb.append(" ");

		}
	
		posList.setStringValue(sb.toString());
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
