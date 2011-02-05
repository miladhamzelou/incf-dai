package org.incf.aba.atlas.process;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.vecmath.Point3d;

import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.incf.aba.atlas.util.ABAServiceVO;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.atlas.waxml.generated.AccessionIdType;
import org.incf.atlas.waxml.generated.GeneByPoiType;
import org.incf.atlas.waxml.generated.GeneExpressionLevelType;
import org.incf.atlas.waxml.generated.GeneSymbolType;
import org.incf.atlas.waxml.generated.GeneType;
import org.incf.atlas.waxml.generated.GenesResponseDocument;
import org.incf.atlas.waxml.generated.GenesResponseType;
import org.incf.common.atlas.util.DataInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetGenesByPOI implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            Get2DImagesByPOI.class);
    
	// used for ABA Get Image URI query string
	private static final String HI_RES = "-1";	// highest resolution available
	private static final String THUMB = "0";	// thumbnail
	private static final String MIME = "2";		// jpeg/image
	private static final int NBR_STRONG_GENES = 1;	// strong genes to get
	
//	private ResponseValues responseValues;
	
    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	try {

    		// validate against allowed values in process definition file
    		URL processDefinitionUrl = this.getClass().getResource(
    				"/" + this.getClass().getSimpleName() + ".xml");
    		
    		// retrieve validated inputs or default values
    		DataInputHandler dataInputHandler = new DataInputHandler(
    				new File(processDefinitionUrl.toURI()));
    		String srsName = dataInputHandler.getValidatedStringValue(in, 
    				"srsName");
    		String filter = dataInputHandler.getValidatedStringValue(in, 
					"filter");
    		double x = dataInputHandler.getDoubleInputValue(in, "x");
    		double y = dataInputHandler.getDoubleInputValue(in, "y");
    		double z = dataInputHandler.getDoubleInputValue(in, "z");

    		LOG.debug(String.format(
    				"DataInputs: srsName: %s, poi: (%f, %f, %f), filter: %s",
    				srsName, x, y, z, filter));
    		
    		// transform non-AGEA coordinates to AGEA
    		if (!srsName.equals("Mouse_AGEA_1.0")) {

//    			ABAServiceVO vo = getTransformPOI(srsName, x, y, z);
//    			
//	    		if (vo.getTransformationXMLResponseString().startsWith("Error:")) {
//					throw new OWSException( 
//							"Transformation Coordinates Error: ", vo.getTransformationXMLResponseString());
//		    	}
//
//    			x = Double.parseDouble(vo.getTransformedCoordinateX());
//    			y = Double.parseDouble(vo.getTransformedCoordinateY());
//    			z = Double.parseDouble(vo.getTransformedCoordinateZ());
    			
		    }

    		String srsFromClient = srsName;
    		Point3d poiFromClient = new Point3d(x, y, z);

    		// 1. get strong gene(s) at POI
    		List<String> strongGenes = ABAUtil.retrieveStrongGenesAtPOI(x, y, z,
    				NBR_STRONG_GENES);

    		// make sure we have something
    		if (strongGenes.size() == 0) {
    			throw new OWSException("No 'strong genes' found at "
    						+ "coordinates, hence no images to return.", 
    					ControllerException.NO_APPLICABLE_CODE);
    		}

    		if (LOG.isDebugEnabled()) {
    			StringBuilder buf = new StringBuilder();
    			for (String gene : strongGenes) {
    				buf.append(gene).append(", ");
    			}
    			LOG.debug("Strong genes: {}", buf.toString());
    		}


    		
    		

    		// transform any exceptions into ProcessletException
        } catch (OWSException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(e);	// is already OWSException
        } catch (Throwable e) {
        	String message = "Unexpected exception occurred: " + e.getMessage();
        	LOG.error(message, e);
        	throw new ProcessletException(new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE));
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }
        		
    /*
  <GenesByPOI>
    <Gene>
      <!--Codespace required. Ideally it will be a base identifier the org responsible for the naming-->
      <!--gml:id is required, and is used to reference this item in the document-->
      <Symbol codeSpace="uri:incf.org" gml:id="AAAAAB">Bmp4</Symbol>  <<--genesymbol
      <MarkerAccessionId separator=":">
        <Prefix>string</Prefix>
        <Identifier>string</Identifier>
        <FullIdentifier>string:string</FullIdentifier>
      </MarkerAccessionId>
      <Name>bone morphogenetic protein 4</Name>   <<--genename
      <Organism>mouse</Organism>
    </Gene>
    <Gene>
      <Symbol gml:id="AAAAD" codeSpace="http://www.incf.org/HUBS/ABA">Sox1</Symbol>
      <MarkerAccessionId separator=":">   <<-- mgimarkeraccessionid
        <Prefix>MGI</Prefix>
        <Identifier>98357</Identifier>
        <FullIdentifier>MGI:98357</FullIdentifier>
      </MarkerAccessionId>
      <Name>SRY-box containing gene 1</Name>
      <Organism codeSpace="http://www.incf.org/organisms" isDefault="true">mouse</Organism>
    </Gene>
    <ExpressionLevel>
      <!--href references Bmp4 by gml:id-->
      <!--references Bmp4 by name.-->
      <GeneSymbol xlin:href="#AAAAB"/>
      <Stage>TS11</Stage>
      <Level>strong</Level>
      <ResourceUri>http://www.emouseatlas.org/emagewebapp/pages/emage_entry_page.jsf?id=EMAGE:86</ResourceUri>
    </ExpressionLevel>
    <ExpressionLevel>
      <GeneSymbol>Bmp4</GeneSymbol>
      <Stage>10.5dpc</Stage>
      <Level>detected</Level>
      <ResourceUri>http://www.emouseatlas.org/emagewebapp/pages/emage_entry_page.jsf?id=EMAGE:131</ResourceUri>
    </ExpressionLevel>
    <ExpressionLevel>
      <GeneSymbol xlin:href="#AAAAD"/>
      <Stage codeSpace="http://www.incf.org/gene/expression/stage">E10.5</Stage>
      <Level codeSpace="http://www.incf.org/gene/expression/level">not detected</Level>
      <ResourceUri>http://www.emouseatlas.org/emagewebapp/pages/emage_entry_page.jsf?id=EMAGE:3066</ResourceUri>
    </ExpressionLevel>
  </GenesByPOI>
  
  GeneByPoiType
    GeneType
      GeneSymbolType
      AccessionIdType
    GeneExpressionLevelType
      GeneSymbolType
      IncfCodeType
     */
	private GenesResponseDocument completeResponse() {
		GenesResponseDocument document = 
				GenesResponseDocument.Factory.newInstance();
		GenesResponseType eGenesResponse = document.addNewGenesResponse();
		GeneByPoiType eGenesByPOI = eGenesResponse.addNewGenesByPOI();
//		for (Gene gene : genes) {
			GeneType eGene = eGenesByPOI.addNewGene();
			GeneSymbolType eSymbol = eGene.addNewSymbol();
			AccessionIdType eMarkerAccessionId = eGene.addNewMarkerAccessionId();
			eGene.addNewName();
			eGene.addNewOrganism();
//		}
//		for (ExpressionLevel expressionLevel : expressionLevels) {
			GeneExpressionLevelType eExpressionLevel = eGenesByPOI.addNewExpressionLevel();
			eExpressionLevel.addNewGeneSymbol();
			eExpressionLevel.addNewStage();
			eExpressionLevel.addNewLevel();
			eExpressionLevel.addNewResourceUri();
//		}
		
//		Image2Dcollection eImage2DCollection = 
//				eGenesResponse.addNewImage2Dcollection();
//		for (Image im : responseValues.images) {
//			Image2DType eImage2D = eImage2DCollection.addNewImage2D();
//			ImageSource eImageSource = eImage2D.addNewImageSource();
//			eImageSource.setStringValue(im.imageURI);
//			eImageSource.setFormat(IncfRemoteFormatEnum.IMAGE_JPEG.toString());
//			eImageSource.setName(im.imagedisplayname);
//			eImageSource.setSrsName(responseValues.clientSrsName);
//			eImageSource.setThumbnail(im.thumbnailurl);
//		}
		return document;
	}
	
}
