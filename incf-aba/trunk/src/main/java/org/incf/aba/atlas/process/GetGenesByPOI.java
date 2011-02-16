package org.incf.aba.atlas.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
	private static final int NBR_STRONG_GENES = 3;	// strong genes to get
	
//	private ResponseValues responseValues;
	
	private GenesResponseDocument document;
	
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
    		double x = DataInputHandler.getDoubleInputValue(in, "x");
    		double y = DataInputHandler.getDoubleInputValue(in, "y");
    		double z = DataInputHandler.getDoubleInputValue(in, "z");
    		
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
    		
    		// TODO
    		/*
    		 * 1. Get genes at poi:
    		 *    http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=6600,4000,5600
    		 *    http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=[x],[y],[z]
    		 *    --> genesymbol/<Symbol>, imageseriesid, energy?, rank?
    		 * 2. With genesymbol from 1, get gene info
    		 *    http://www.brain-map.org/aba/api/gene/Plxnb1.xml
    		 *    http://www.brain-map.org/aba/api/gene/[genesymbol].xml
    		 *    --> chromosome, entrezgenid, geneid, genename, genesymbol, 
    		 *    	gensatid, mgiaccessionid/<MarkeraccessionId>, organism,
    		 *      projectname
    		 * 3. With imageseriesid from 1, get expression info
    		 * 	  http://www.brain-map.org/aba/api/expression/imageseries/1561.xml
    		 * 	  http://www.brain-map.org/aba/api/expression/imageseries/[imageseries].xml
    		 *    --> <stage>?, <level>
    		 */

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

    		// 2. 
    		document = GenesResponseDocument.Factory.newInstance();
    		GenesResponseType eGenesResponse = document.addNewGenesResponse();
    		GeneByPoiType eGenesByPOI = eGenesResponse.addNewGenesByPOI();
    		
    		for (String gene : strongGenes) {
    			GeneType eGene = eGenesByPOI.addNewGene();
    			GeneSymbolType eSymbol = eGene.addNewSymbol();
    			AccessionIdType eMarkerAccessionId = eGene.addNewMarkerAccessionId();
    			eGene.addNewName();
    			eGene.addNewOrganism();
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
        		
	/**
	 * Returns a list of genes from Allen Brain Atlas that express at
	 * the given location. The number returned is determined by the last 
	 * argument. The list is in order of the strength of expression.
	 *  
	 * @param x
	 * @param y
	 * @param z
	 * @param nbrStrongGenes
	 * @return
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static List<ABAGene> retrieveStrongGenesAtPOI(double x, double y, 
			double z, int nbrStrongGenes) throws IOException, 
					XMLStreamException {
        List<ABAGene> strongGenes = new ArrayList<ABAGene>();
	    	
        // round coords to nearest xx00, where xx is even
        URL u = new URL(ABAUtil.assembleGeneFinderURI(
        		String.valueOf(ABAUtil.round200(x)), 
        		String.valueOf(ABAUtil.round200(y)), 
        		String.valueOf(ABAUtil.round200(z))));

        LOG.debug("Gene finder URI: {}", u.toString());

        InputStream in = u.openStream();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(in);

        boolean inChromosome = false;
        boolean inEntrezgeneid = false;
        boolean inGeneid = false;
        boolean inGenename = false;
        boolean inGenesymbol = false;
        boolean inGensatid = false;
        boolean inMgimarkeraccessionid = false;
        boolean inOrganism = false;
        boolean inProjectname = false;
        ABAGene abaGene = new ABAGene();
        int i = 0;
        for (int event = parser.next();  
        		event != XMLStreamConstants.END_DOCUMENT;
        		event = parser.next()) {
        	if (event == XMLStreamConstants.START_ELEMENT) {
        		if (parser.getLocalName().equals("image-series")) {
        			break;			// we don't need info past this point
        		}
        		if (parser.getLocalName().equals("chromosome")) {
        			inChromosome = true;
        		} else if (parser.getLocalName().equals("entrezgeneid")) {
        			inEntrezgeneid = true;
        		} else if (parser.getLocalName().equals("geneid")) {
        			inGeneid = true;
        		} else if (parser.getLocalName().equals("genename")) {
        			inGenename = true;
        		} else if (parser.getLocalName().equals("genesymbol")) {
        			inGenesymbol = true;
        		} else if (parser.getLocalName().equals("gensatid")) {
        			inGensatid = true;
        		} else if (parser.getLocalName().equals("mgimarkeraccessionid")) {
        			inMgimarkeraccessionid = true;
        		} else if (parser.getLocalName().equals("organism")) {
        			inOrganism = true;
        		} else if (parser.getLocalName().equals("projectname")) {
        			inProjectname = true;
        		} 
        	} else if (event == XMLStreamConstants.CHARACTERS) {
        		if (inChromosome) {
        			abaGene.chromosome = parser.getText();
        			inChromosome = false;
        		} else if (inEntrezgeneid) {
        			abaGene.entrezgeneid = parser.getText();
        			inEntrezgeneid = false;
        		} else if (inGeneid) {
        			abaGene.geneid = parser.getText();
        			inGeneid = false;
        		} else if (inGenename) {
        			abaGene.genename = parser.getText();
        			inGenename = false;
        		} else if (inGenesymbol) {
        			abaGene.genesymbol = parser.getText();
        			inGenesymbol = false;
        		} else if (inGensatid) {
        			abaGene.gensatid = parser.getText();
        			inGensatid = false;
        		} else if (inMgimarkeraccessionid) {
        			abaGene.mgimarkeraccessionid = parser.getText();
        			inMgimarkeraccessionid = false;
        		} else if (inOrganism) {
        			abaGene.organism = parser.getText();
        			inOrganism = false;
        		} else if (inProjectname) {
        			abaGene.projectname = parser.getText();
        			inProjectname = false;
        		}
        		
//           		if (inGeneSymbol) {
//        			geneSymbol = parser.getText();
//        			strongGenes.add(geneSymbol);
//        			i++;
//        			if (i >= nbrStrongGenes) {
//        				break;
//        			}
//        			inGeneSymbol = false;
//        		}
        	}
        }
        try {
        	parser.close();
        } catch (XMLStreamException e) {
        	LOG.warn(e.getMessage(), e);		// log but go on
        }

        // debug
        for (ABAGene gene : strongGenes) {
        	LOG.debug("gene: {}", gene);
        }
	    return strongGenes;
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
	
	public static class ABAGene {
		public String chromosome;
		public String entrezgeneid;
		public String geneid;
		public String genename;
		public String genesymbol;
		public String gensatid;
		public String mgimarkeraccessionid;
		public String organism;
		public String projectname;
	}
	
}
