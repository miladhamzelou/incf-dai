package org.incf.atlas.xmlbeans.examples;
import org.incf.atlas.waxml.generated.*;

public class GenesResponse {

	public String AsXml(){
		GenesResponseDocument document = GenesResponseDocument.Factory.newInstance();
		
		GenesResponseType genes = document.addNewGenesResponse();
		QueryInfoType query = genes.addNewQueryInfo();
		
		InputStringType type = InputStringType.Factory.newInstance();
		type.setName("MyParameter");
		type.setValue(arg0)
		query.addNewCriteria();
		
	GeneByPoiType 	genePoiBlock = genes.addNewGenesByPOI();
	GeneType gene1 = genePoiBlock.addNewGene();
	
		return document.toString();
	}
}
