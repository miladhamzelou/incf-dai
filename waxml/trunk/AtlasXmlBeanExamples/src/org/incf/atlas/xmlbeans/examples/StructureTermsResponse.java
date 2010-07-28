package org.incf.atlas.xmlbeans.examples;

import java.util.ArrayList;

import net.opengis.gml.x32.BoundingShapeType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.FeatureReferenceType.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.StructureTermType.Code;
import org.incf.atlas.waxml.generated.StructureTermsResponseType.*;

public class StructureTermsResponse {
public String AsXML(){
	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
	opt.setSaveNamespacesFirst();
	opt.setSaveAggressiveNamespaces();
	opt.setUseDefaultNamespace();
	
	StructureTermsResponseDocument document =	StructureTermsResponseDocument.Factory.newInstance(); 
	
	StructureTermsResponseType rootDoc =	document.addNewStructureTermsResponse();
	QueryInfoType query = rootDoc.addNewQueryInfo();
	Criteria criterias =query.addNewCriteria();
	InputPOIType poiCriteria = (InputPOIType) criterias.addNewInput().changeType(InputPOIType.type);
	poiCriteria.setName("POI");
	PointType pnt = poiCriteria.addNewPOI().addNewPoint();
	pnt.setId("id-onGeomRequiredByGML");
	pnt.setSrsName("Mouse_ABAvoxel_1.0");
pnt.addNewPos();
pnt.getPos().setStringValue("1 1 1");
	InputStringType srsCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
	srsCodeCriteria.setName("StructureVocabulary");
	srsCodeCriteria.setValue("Mouse_ABAvoxel_1.0");
	InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
	filterCodeCriteria.setName("StructureFilter");
	filterCodeCriteria.setValue("structureset:Fine");

	
	StructureTerms terms = rootDoc.addNewStructureTerms();
	StructureTermType term1 = terms.addNewStructureTerm();
	Code t1code =  term1.addNewCode(); 
	t1code.setCodeSpace("Mouse_ABAvoxel_1.0");
	t1code.setIsDefault(true);
	t1code.setStructureID("1369");
	t1code.setStringValue("HIP");
	
	term1.setUri("uri:incf:pons-repository.org:structurenames:Hippocampus");
	IncfNameType t1name = term1.addNewName();
	t1name.setStringValue("Hippocampus");
	term1.addNewDescription().setStringValue("Hippocampus, segmented");
	
	FeatureReferenceType t1ft = term1.addNewFeature();
	Centroid t1c = t1ft.addNewCentroid();
	t1c.addNewPoint().addNewPos().setStringValue("1 1 1");
	t1c.getPoint().setId("requiredAnyId");
	t1c.getPoint().setSrsName("Mouse_ABAvoxel_1.0");
	BoundingShapeType t1bound = t1ft.addNewBoundedBy();
	t1bound.addNewEnvelope();
	t1bound.getEnvelope().setSrsName("Mouse_WHS_1.0");
	DirectPositionType t1lc = t1bound.getEnvelope().addNewLowerCorner();
	DirectPositionType t1uc = t1bound.getEnvelope().addNewUpperCorner();
	t1lc.setStringValue("10 10 10");
	t1uc.setStringValue("20 20 20");
	
	t1ft.addNewUrl().setStringValue("URI");
	t1ft.getUrl().setSrsName("Mouse_ABAvoxel_1.0");
	t1ft.setFormat(GeomFormatEnum.SHAPE.toString());
	
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
}
