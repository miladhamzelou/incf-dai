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
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlOptions;
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
import org.incf.aba.atlas.util.ABAServiceVO;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.aba.atlas.util.XMLUtilities;
import org.incf.atlas.waxml.generated.AccessionIdType;
import org.incf.atlas.waxml.generated.GeneByPoiType;
import org.incf.atlas.waxml.generated.GeneSymbolType;
import org.incf.atlas.waxml.generated.GeneType;
import org.incf.atlas.waxml.generated.GenesResponseDocument;
import org.incf.atlas.waxml.generated.GenesResponseType;
import org.incf.atlas.waxml.generated.IncfNameType;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.util.DataInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetGenesByPOI implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            Get2DImagesByPOI.class);
    
	private static final int NBR_STRONG_GENES = 10;	// strong genes to get
	
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
                ABAServiceVO vo = getTransformPOI(srsName, x, y, z);
                if (vo.getTransformationXMLResponseString().startsWith(
                        "Error:")) {
                    throw new OWSException("Transformation Coordinates Error: ", 
                            vo.getTransformationXMLResponseString());
                }
                x = Double.parseDouble(vo.getTransformedCoordinateX());
                y = Double.parseDouble(vo.getTransformedCoordinateY());
                z = Double.parseDouble(vo.getTransformedCoordinateZ());
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
    		List<String> strongGenes = ABAUtil.retrieveStrongGenesAtAGEAPOI(x, y, z,
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
    		List<ABAGene> abaGenes = new ArrayList<ABAGene>();
    		for (String geneSymbol : strongGenes) {
    			abaGenes.add(retrieveGeneData(geneSymbol));
    		}

    		// ComplexOutput objects can become very huge; it is essential to 
    		// stream result.
    		// get ComplexOutput object from ProcessletOutput...
    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
    				"GetGenesByPOIOutput");
    		LOG.debug("Setting complex output (requested=" 
    				+ complexOutput.isRequested() + ")");

    		// GenesResponseDocument 'is a' org.apache.xmlbeans.XmlObject
    		//	'is a' org.apache.xmlbeans.XmlTokenSource
    		GenesResponseDocument document = completeResponse(abaGenes);

    		if (LOG.isDebugEnabled()) {
    			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
    			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
    			opt.setSaveNamespacesFirst();
    			opt.setSaveAggressiveNamespaces();
    			opt.setUseDefaultNamespace();
    			LOG.debug("Xml:\n{}", document.xmlText(opt));
    		}

    		// get reader on document; reader --> writer
    		XMLStreamReader reader = document.newXMLStreamReader();
    		XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
    		XMLAdapter.writeElement(writer, reader);
    		
    		// transform any exceptions into ProcessletException wrapping
    		// OWSException
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
	public ABAGene retrieveGeneData(String geneSymbol) 
			throws IOException, XMLStreamException {

        URL u = new URL(ABAUtil.assembleGeneURI(geneSymbol));

        LOG.debug("Gene URI: {}", u.toString());

        InputStream in = u.openStream();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(in);

        boolean inGenename = false;
        boolean inGenesymbol = false;
        boolean inMgimarkeraccessionid = false;
        boolean inOrganism = false;
        ABAGene abaGene = new ABAGene();
        int i = 0;
        for (int event = parser.next();  
        		event != XMLStreamConstants.END_DOCUMENT;
        		event = parser.next()) {
        	if (event == XMLStreamConstants.START_ELEMENT) {
        		if (parser.getLocalName().equals("image-series")) {
        			break;			// we don't need info past this point
        		}
        		if (parser.getLocalName().equals("genename")) {
        			inGenename = true;
        		} else if (parser.getLocalName().equals("genesymbol")) {
        			inGenesymbol = true;
        		} else if (parser.getLocalName().equals("mgimarkeraccessionid")) {
        			inMgimarkeraccessionid = true;
        		} else if (parser.getLocalName().equals("organism")) {
        			inOrganism = true;
        		} 
        	} else if (event == XMLStreamConstants.CHARACTERS) {
        		if (inGenename) {
        			abaGene.genename = parser.getText();
        			inGenename = false;
        		} else if (inGenesymbol) {
        			abaGene.genesymbol = parser.getText();
        			inGenesymbol = false;
        		} else if (inMgimarkeraccessionid) {
        			abaGene.mgimarkeraccessionid = parser.getText();
        			inMgimarkeraccessionid = false;
        		} else if (inOrganism) {
        			abaGene.organism = parser.getText();
        			inOrganism = false;
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
        } // for next parse event

        abaGene.calculateMGIParts();
        
        try {
        	parser.close();
        } catch (XMLStreamException e) {
        	LOG.warn(e.getMessage(), e);		// log but go on
        }

        // debug
//        	LOG.debug("abaGene: {}", gene);

	    return abaGene;
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
	private GenesResponseDocument completeResponse(List<ABAGene> abaGenes) {
		String codeSpace = "uri:incf.org";
		String gmlIdPrefix = "G";
		
		GenesResponseDocument document = 
				GenesResponseDocument.Factory.newInstance();
		GenesResponseType eGenesResponse = document.addNewGenesResponse();
		GeneByPoiType eGenesByPOI = eGenesResponse.addNewGenesByPOI();
		int i = 1;
		for (ABAGene abaGene : abaGenes) {
			GeneType eGene = eGenesByPOI.addNewGene();
			GeneSymbolType eGeneSymbol = eGene.addNewSymbol();
			eGeneSymbol.setCodeSpace(codeSpace);
			eGeneSymbol.setId(gmlIdPrefix + String.valueOf(i));
			eGeneSymbol.setStringValue(abaGene.genesymbol);
			IncfNameType eGeneName = eGene.addNewName();
			AccessionIdType eMarkerAccessionId = eGene.addNewMarkerAccessionId();
			eMarkerAccessionId.addNewSeparator().setStringValue(abaGene.mgiSeparator);
			eMarkerAccessionId.addNewPrefix().setStringValue(abaGene.mgiPrefix);
			eMarkerAccessionId.addNewIdentifier().setStringValue(abaGene.mgiIdentifier);
			eMarkerAccessionId.setFullIdentifier(abaGene.mgimarkeraccessionid);
			eGeneName.setStringValue(abaGene.genename);
			eGene.addNewOrganism().setStringValue(abaGene.organism);
			i++;
		}
//		for (ExpressionLevel expressionLevel : expressionLevels) {
//			GeneExpressionLevelType eExpressionLevel = eGenesByPOI.addNewExpressionLevel();
//			eExpressionLevel.addNewGeneSymbol();
//			eExpressionLevel.addNewStage();
//			eExpressionLevel.addNewLevel();
//			eExpressionLevel.addNewResourceUri();
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
		
		// furnished values
		public String genename;
		public String genesymbol;
		public String mgimarkeraccessionid;
		public String organism;
		
		// derived from mgimarkeraccessionid
		public String mgiPrefix;
		public String mgiSeparator;
		public String mgiIdentifier;
		
		public void calculateMGIParts() {
			mgiSeparator = ":";
			String[] mgiSegments = mgimarkeraccessionid.split(mgiSeparator);
			mgiPrefix = mgiSegments[0];
			mgiIdentifier = mgiSegments[1];
		}
		
	}
	
    public ABAServiceVO getTransformPOI(String fromSrsName, 
            double x, double y, double z) throws OWSException{

        ABAConfigurator config = ABAConfigurator.INSTANCE;

        String tempX = "";
        String tempY = "";
        String tempZ = "";
        String toSrsName = "Mouse_AGEA_1.0";
        String hostName = "";
        String portNumber = "";
        String transformedCoordinatesString = "";
        ABAServiceVO vo = new ABAServiceVO();
        
        //Call getTransformationChain method here...
        //ABAVoxel
        System.out.println("1.1:" );

        tempX = ";x="+String.valueOf(x);
        tempY = ";y="+String.valueOf(y);
        tempZ = ";z="+String.valueOf(z);

        String delimitor = config.getValue("incf.deploy.port.delimitor");
        
        hostName = config.getValue("incf.deploy.host.name");
        portNumber = config.getValue("incf.aba.port.number");
        portNumber = delimitor + portNumber;

        String servicePath = "/atlas-central?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+fromSrsName+";outputSrsName="+toSrsName+";filter=Cerebellum";
        String transformationChainURL = "http://"+hostName+portNumber+servicePath;
        System.out.println("1.4: " + transformationChainURL);

        try { 
            
            XMLUtilities xmlUtilities = new XMLUtilities();
            transformedCoordinatesString = xmlUtilities.coordinateTransformation(transformationChainURL, tempX, tempY, tempZ);
    
            System.out.println("2:" );
            //Start - exception handling
/*          if (transformedCoordinatesString.startsWith("Error:")) {
                System.out.println("********************ERROR*********************");
                throw new OWSException( 
                        "Transformed Coordinates Error: ", transformedCoordinatesString);
            }
*/          //End - exception handling
            ABAUtil util = new ABAUtil();
            String[] tempArray = util.getTabDelimNumbers(transformedCoordinatesString);
            vo.setTransformedCoordinateX(tempArray[0]);
            vo.setTransformedCoordinateY(tempArray[1]);
            vo.setTransformedCoordinateZ(tempArray[2]);
            vo.setTransformationXMLResponseString(transformedCoordinatesString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vo;

    }
    
}
