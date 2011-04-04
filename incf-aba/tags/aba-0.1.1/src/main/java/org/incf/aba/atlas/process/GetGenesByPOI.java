package org.incf.aba.atlas.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

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
import org.incf.aba.atlas.util.ABAGene;
import org.incf.aba.atlas.util.ABAServiceVO;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.aba.atlas.util.XMLUtilities;
import org.incf.atlas.waxml.generated.AccessionIdType;
import org.incf.atlas.waxml.generated.GeneByPoiType;
import org.incf.atlas.waxml.generated.GeneExpressionLevelType;
import org.incf.atlas.waxml.generated.GeneSymbolType;
import org.incf.atlas.waxml.generated.GeneType;
import org.incf.atlas.waxml.generated.GenesResponseDocument;
import org.incf.atlas.waxml.generated.GenesResponseType;
import org.incf.atlas.waxml.generated.IncfCodeType;
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
    		
    		// 1. get strong gene(s) at POI (genesymbol and energy)
    		//    http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=6600,4000,5600
    		//    http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=[x],[y],[z]
    		List<ABAGene> strongGenes = ABAUtil.retrieveStrongGenesAtAGEAPOI(
    				x, y, z, NBR_STRONG_GENES);

    		// make sure we have something
    		if (strongGenes.size() == 0) {
    			throw new OWSException("No 'strong genes' found at "
    						+ "coordinates, hence no images to return.", 
    					ControllerException.NO_APPLICABLE_CODE);
    		}

    		if (LOG.isDebugEnabled()) {
    			StringBuilder buf = new StringBuilder();
    			for (ABAGene abaGene : strongGenes) {
    				buf.append(abaGene.getGenesymbol()).append(", ");
    			}
    			LOG.debug("Strong genes: {}", buf.toString());
    		}
    		
    		// 2. With genesymbol from 1, get additional gene info
    		//    http://www.brain-map.org/aba/api/gene/Plxnb1.xml
    		//    http://www.brain-map.org/aba/api/gene/[genesymbol].xml
    		for (ABAGene abaGene : strongGenes) {
    			retrieveGeneData(abaGene);
    		}

    		// 3. XML it
    		// GenesResponseDocument 'is a' org.apache.xmlbeans.XmlObject
    		//	'is a' org.apache.xmlbeans.XmlTokenSource
    		GenesResponseDocument document = completeResponse(strongGenes);

    		if (LOG.isDebugEnabled()) {
    			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
    			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
    			opt.setSaveNamespacesFirst();
    			opt.setSaveAggressiveNamespaces();
    			opt.setUseDefaultNamespace();
    			LOG.debug("Xml:\n{}", document.xmlText(opt));
    		}

    		// 4. Send it
    		// get reader on document
    		XMLStreamReader reader = document.newXMLStreamReader();
    		
    		// get ComplexOutput object from ProcessletOutput...
    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
    				"GetGenesByPOIOutput");

    		LOG.debug("Setting complex output (requested=" 
    				+ complexOutput.isRequested() + ")");
    		
    		// ComplexOutput objects can be huge so stream it 
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
	 * @param abaGene
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public void retrieveGeneData(ABAGene abaGene) 
			throws IOException, XMLStreamException {

        URL u = new URL(ABAUtil.assembleGeneURI(abaGene.getGenesymbol()));

        LOG.debug("Gene URI: {}", u.toString());

        InputStream in = u.openStream();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(in);

        boolean inGenename = false;
        boolean inMgimarkeraccessionid = false;
        boolean inOrganism = false;
        for (int event = parser.next();  
        		event != XMLStreamConstants.END_DOCUMENT;
        		event = parser.next()) {
        	if (event == XMLStreamConstants.START_ELEMENT) {
        		if (parser.getLocalName().equals("image-series")) {
        			break;			// we don't need info past this point
        		}
        		if (parser.getLocalName().equals("genename")) {
        			inGenename = true;
        		} else if (parser.getLocalName().equals("mgimarkeraccessionid")) {
        			inMgimarkeraccessionid = true;
        		} else if (parser.getLocalName().equals("organism")) {
        			inOrganism = true;
        		} 
        	} else if (event == XMLStreamConstants.CHARACTERS) {
        		if (inGenename) {
        			abaGene.setGenename(parser.getText());
        			inGenename = false;
        		} else if (inMgimarkeraccessionid) {
        			abaGene.setMgimarkeraccessionid(parser.getText());
        			inMgimarkeraccessionid = false;
        		} else if (inOrganism) {
        			abaGene.setOrganism(parser.getText());
        			inOrganism = false;
        		}
        	}
        } // for next parse event

        try {
        	parser.close();
        } catch (XMLStreamException e) {
        	LOG.warn(e.getMessage(), e);		// log but go on
        }

        // debug
        	LOG.debug("abaGene: {}", abaGene.getGenesymbol());
	}
	
    /* sample output from Dave V's example
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
		//String codeSpace = "uri:incf.org";	// not needed for now
		String expressionLevelCodeSpace = "ageaUrnTerms:energy:units";
		
		for (ABAGene abaGene : abaGenes) {
			LOG.debug(abaGene.toString());
		}
		
		GenesResponseDocument document = 
				GenesResponseDocument.Factory.newInstance();
		GenesResponseType eGenesResponse = document.addNewGenesResponse();
		GeneByPoiType eGenesByPOI = eGenesResponse.addNewGenesByPOI();
		for (ABAGene abaGene : abaGenes) {
			GeneType eGene = eGenesByPOI.addNewGene();
			GeneSymbolType eSymbol = eGene.addNewSymbol();
			//eSymbol.setCodeSpace(codeSpace);	// not needed for now
			//eSymbol.setId(abaGene.getId());	// not needed for now
			eSymbol.setStringValue(abaGene.getGenesymbol());
			IncfNameType eGeneName = eGene.addNewName();
			AccessionIdType eMarkerAccessionId = eGene.addNewMarkerAccessionId();
			eMarkerAccessionId.addNewSeparator().setStringValue(abaGene.getMgiSeparator());
			eMarkerAccessionId.addNewPrefix().setStringValue(abaGene.getMgiPrefix());
			eMarkerAccessionId.addNewIdentifier().setStringValue(abaGene.getMgiIdentifier());
			eMarkerAccessionId.setFullIdentifier(abaGene.getMgimarkeraccessionid());
			eGeneName.setStringValue(abaGene.getGenename());
			eGene.addNewOrganism().setStringValue(abaGene.getOrganism());
		}
		for (ABAGene abaGene : abaGenes) {
			GeneExpressionLevelType eExpressionLevel = eGenesByPOI.addNewExpressionLevel();
			GeneSymbolType eGeneSymbol = eExpressionLevel.addNewGeneSymbol();
			//eGeneSymbol.setHref(String.valueOf(abaGene.getId()));	// not needed for now
			eGeneSymbol.setStringValue(abaGene.getGenesymbol());
			IncfCodeType eLevel = eExpressionLevel.addNewLevel();
			eLevel.setCodeSpace(expressionLevelCodeSpace);
			eLevel.setStringValue(abaGene.getEnergy());
		}
		return document;
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
        LOG.debug("1.1:" );

        tempX = ";x="+String.valueOf(x);
        tempY = ";y="+String.valueOf(y);
        tempZ = ";z="+String.valueOf(z);

        String delimitor = config.getValue("incf.deploy.port.delimitor");
        
        hostName = config.getValue("incf.deploy.host.name");
        portNumber = config.getValue("incf.aba.port.number");
        portNumber = delimitor + portNumber;

        String servicePath = "/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+fromSrsName+";outputSrsName="+toSrsName+";filter=NONE";
        String transformationChainURL = "http://"+hostName+portNumber+servicePath;
        LOG.debug("1.4: {}" , transformationChainURL);

        try { 
            
            XMLUtilities xmlUtilities = new XMLUtilities();
            transformedCoordinatesString = xmlUtilities.coordinateTransformation(transformationChainURL, tempX, tempY, tempZ);
    
            LOG.debug("2:" );
            //Start - exception handling
/*          if (transformedCoordinatesString.startsWith("Error:")) {
                LOG.debug("********************ERROR*********************");
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
