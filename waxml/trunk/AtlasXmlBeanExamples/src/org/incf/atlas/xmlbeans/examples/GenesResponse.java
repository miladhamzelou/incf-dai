package org.incf.atlas.xmlbeans.examples;
import java.util.ArrayList;

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;

public class GenesResponse {

	public String AsXml(){
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		GenesResponseDocument document = GenesResponseDocument.Factory.newInstance();
		
		GenesResponseType genes = document.addNewGenesResponse();
	// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = genes.addNewQueryInfo();
		Criteria criterias = query.addNewCriteria();
		
		InputPOIType poiCriteria = (InputPOIType) criterias.addNewInput().changeType(InputPOIType.type);
		poiCriteria.setName("POI");
		PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		pnt.setId("id-onGeomRequiredByGML");
pnt.addNewPos();
pnt.getPos().setStringValue("1 1 1");
		InputStringType srsCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		srsCodeCriteria.setName("srsCode");
		srsCodeCriteria.setValue("EMAP23");
		InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("AFilter");
	
	
	GeneByPoiType 	genePoiBlock = genes.addNewGenesByPOI();
//	adding a gene should be a utility method
	GeneType gene1 = genePoiBlock.addNewGene();
	GeneSymbolType g1symbol = gene1.addNewSymbol();
	g1symbol.setCodeSpace("required");
	g1symbol.setId("AAAAAB");
	g1symbol.setStringValue("Bmp4");
	IncfNameType g1name = gene1.addNewName();
	g1name.setStringValue("bone morphogenetic protein 4");
	AssessionIdType g1marker = gene1.addNewMarkerAccesionId();
	g1marker.addNewIdentifier().setStringValue("string");
	g1marker.addNewPrefix().setStringValue("string");
	g1marker.addNewSpearator().setStringValue(":");
	g1marker.setFullIdentifier("string:string");
	
	gene1.addNewOrganism().setStringValue("mouse");
	
//	adding a gene should be a utility method

	GeneType gene2 = genePoiBlock.addNewGene();
	GeneSymbolType g2symbol = gene2.addNewSymbol();
	g2symbol.setId("AAAAD");
	g2symbol.setStringValue("Sox1");
	g2symbol.setCodeSpace("http://www.incf.org/HUBS/ABA");
	IncfNameType g2name = gene2.addNewName();
	g2name.setStringValue("bone morphogenetic protein 4");
	
	AssessionIdType g2marker = gene2.addNewMarkerAccesionId();
	g2marker.addNewIdentifier().setStringValue("98357");
	g2marker.addNewPrefix().setStringValue("MGI");
	g2marker.addNewSpearator().setStringValue(":");
	g2marker.setFullIdentifier("MGI:98357");
	
	IncfCodeType g2org = gene2.addNewOrganism();
	g2org.setStringValue("mouse");
	g2org.setCodeSpace("http://www.incf.org/organisms");
	g2org.setIsDefault(true);
	
	GeneExpressionLevelType expression1 = genePoiBlock.addNewExpressionLevel();
	GeneSymbolType e1symbol = expression1.addNewGeneSymbol();
	e1symbol.setHref("#AAAAB");
	expression1.addNewLevel().setStringValue("strong");
	expression1.addNewStage().setStringValue("TS11");
	expression1.addNewResourceUri().setStringValue("http://www.emouseatlas.org/emagewebapp/pages/emage_entry_page.jsf?id=EMAGE:86");
	
	GeneExpressionLevelType expression2 = genePoiBlock.addNewExpressionLevel();
	GeneSymbolType e2symbol = expression2.addNewGeneSymbol();
	e2symbol.setStringValue("Bmp4");
	expression2.addNewLevel().setStringValue("detected");
	expression2.addNewStage().setStringValue("10.5dpc");
	expression2.addNewResourceUri().setStringValue("http://www.emouseatlas.org/emagewebapp/pages/emage_entry_page.jsf?id=EMAGE:131");

	GeneExpressionLevelType expression3 = genePoiBlock.addNewExpressionLevel();
	GeneSymbolType e3symbol = expression3.addNewGeneSymbol();
	e3symbol.setHref("#AAAAD");
	IncfCodeType g3l1= expression3.addNewLevel();
	g3l1.setStringValue("not detected");
	g3l1.setCodeSpace("http://www.incf.org/gene/expression/level");
	IncfCodeType g2stg =expression3.addNewStage();
	g2stg.setStringValue("E10.5");
	g2stg.setCodeSpace("http://www.incf.org/gene/expression/stage");
	expression3.addNewResourceUri().setStringValue("http://www.emouseatlas.org/emagewebapp/pages/emage_entry_page.jsf?id=EMAGE:3066");
 
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
