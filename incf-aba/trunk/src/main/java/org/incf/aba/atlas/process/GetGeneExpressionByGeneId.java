package org.incf.aba.atlas.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.IOUtils;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.DataInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * E.g.
 * http://<server:port>/aba/atlas?service=WPS&request=GetCapabilities
 * http://<server:port>/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL
 * http://<server:port>/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=GetGeneExpressionByGeneId
 * 
 * Using geneIdentifier=<entrez-gene-id> 
 * http://<server:port>/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneIdentifier=12810;filter=maptype:coronal&RawDataOutput=SparseValueVolumeXML
 * 
 * Using geneIdentifier=<aba-gene-symbol> 
 * http://<server:port>/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneIdentifier=Coch;filter=maptype:coronal&RawDataOutput=SparseValueVolumeXML
 * 
 * @author dave
 */
public class GetGeneExpressionByGeneId implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
    		GetGeneExpressionByGeneId.class);
    
    private static final String ENTREZ_TO_SYMBOL_FILE = "/entrezGeneIdToABAGeneSymbol.csv";
    
    private Map<String, String> entrezToSymbol;
    
    private String entrezGeneId;
    private String abaGeneSymbol;
    private String geneName;
    private String organism;
    private String chromosome;
    private String plane;

    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	Reader reader = null;
    	Writer writer = null;
    	try {
    		
    		// validate against allowed values in process definition file
    		URL processDefinitionUrl = this.getClass().getResource(
    				"/" + this.getClass().getSimpleName() + ".xml");
    		
    		// get validated data inputs or default values
    		DataInputHandler dataInputHandler = new DataInputHandler(
    				new File(processDefinitionUrl.toURI()));
    		String geneIdentifier = dataInputHandler.getValidatedStringValue(in, 
    				"geneIdentifier");
    		String filter = dataInputHandler.getValidatedStringValue(in, 
					"filter");
    		
    		LOG.debug(String.format(
    				"DataInputs: geneSymbol: %s, filter: %s",
    				geneIdentifier, filter));
    		
    		// if geneIdentifer int, it's entrez gene id otherwise ABA gene symbol
    		boolean isIdEntrezGeneId = true; 
    		try {
				Integer.parseInt(geneIdentifier);	// see if exception thrown
			} catch (NumberFormatException e) {
				isIdEntrezGeneId = false;
			}

			// if geneIdentifier is entrez gene id, translate to ABA gene symbol
			abaGeneSymbol = isIdEntrezGeneId 
					? entrezToSymbol.get(geneIdentifier) : geneIdentifier;
    		
			ImageSeriesPlane desiredPlane = null;	
//    		// get plane; defaults to coronal
//    		ImageSeriesPlane desiredPlane = filter.equals("maptype:sagittal")
//    				? ImageSeriesPlane.SAGITTAL : ImageSeriesPlane.CORONAL;

    		/*
Say you are interested in gene symbol "Coch", at a browser ...

Step 1: http://www.brain-map.org/aba/api/gene/Coch.xml

This returns 3 groups of xml data
(1) <image-series> (array, plural). Sadly there multi-level use of the <image-series> tag.
	(a) <image-series> (singular), <plane> = coronal
	(b) <image-series> (singular), <plane> = sagittal
(2) <gene-expressions>
(3) <gene-aliases>

Pick coronal and get <imageseriesid>. 71717614 in this example.

Step 2: http://www.brain-map.org/aba/api/expression/71717614.sva

This returns a "sparse volume file". You can read about this file content at
http://community.brain-map.org/confluence/download/attachments/525267/TheABAAPI_Final.pdf?version=2
About 3 pages from the end of the PDF file under the heading "The Sparse Volume (.sva) Format".

Test: See class javadoc comment
    		 */
    		
    		// step 1: get imageseriesid for given gene and plane
    		String imageSeriesId = null;
    		String xmlString = null;
			try {
				imageSeriesId = retrieveImagesSeriesIdForGene(abaGeneSymbol,
						desiredPlane);
			} catch (FileNotFoundException e) {
				String message = String.format(
						"404 - Not found: Gene identifier '%s' is not "
						+ "recognized as an entrez gene id or an ABA gene symbol.", 
						geneIdentifier);
	            LOG.info(message);
				xmlString = buildNotFoundXMLString(message);
			}
			if (imageSeriesId == null) {
				String notFound = ImageSeriesPlane.CORONAL.toString();
				String tryInstead = ImageSeriesPlane.SAGITTAL.toString();
				if (desiredPlane == ImageSeriesPlane.SAGITTAL) {
					notFound = ImageSeriesPlane.SAGITTAL.toString();
					tryInstead = ImageSeriesPlane.CORONAL.toString();
				}
				String message = String.format(
						"404 - Not found: There is no maptype:%s image series "
						+ "available for gene identifier '%s'. Try maptype:%s "
						+ "instead.", notFound, geneIdentifier, tryInstead);
	            LOG.info(message);
				xmlString = buildNotFoundXMLString(message);
			}
    		
    		// step 2: get sparse volume file
			if (imageSeriesId != null) {
				URL u = new URL(assembleExpressionEnergyVolumeURI(imageSeriesId));
				xmlString = buildXMLString(u.openStream());
			}
    		
    		reader = new StringReader(xmlString);

    		ComplexOutput output = (ComplexOutput) out.getParameter(
    				"SparseValueVolumeXML");
    		writer = new OutputStreamWriter(output.getBinaryOutputStream());
    		char[] chars = new char[1024];
    		int charsRead;
    		while ((charsRead = reader.read(chars)) != -1) {
    			writer.write(chars, 0, charsRead);
    		}
    		
    		// transform any exceptions into ProcessletException wrapping
    		// OWSException
        } catch (MissingParameterException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (InvalidParameterValueException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (InvalidDataInputValueException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(e);	// is already OWSException
        } catch (OWSException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(e);	// is already OWSException
        } catch (Throwable e) {
        	String message = "Unexpected exception occurred: " + e.getMessage();
        	LOG.error(message, e);
        	throw new ProcessletException(new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE));
        } finally {
        	IOUtils.closeQuietly(reader);
        	IOUtils.closeQuietly(writer);
        }
    }
    
    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    	entrezToSymbol = new HashMap<String, String>();
    	InputStream is = this.getClass().getResourceAsStream(ENTREZ_TO_SYMBOL_FILE);
    	BufferedReader in = null;
    	try {
			in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] parts = line.split(",");
				entrezToSymbol.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			entrezToSymbol = null;
			LOG.error("Problem building entrezGeneId to ABAGeneSymbol map.", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignore) {
				}
			}
		}
    }
    
	private String retrieveImagesSeriesIdForGene(String geneSymbol,
			ImageSeriesPlane desiredPlane) throws IOException, XMLStreamException {
		String imageSeriesId = null;
		InputStream in = null;
		XMLStreamReader parser = null;
		try {
			URL u = new URL(ABAUtil.assembleGeneURI(geneSymbol));

			LOG.debug("Gene info URI: {}", u.toString());

			in = u.openStream();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			parser = factory.createXMLStreamReader(in);

			boolean inChromosome = false;
			boolean inEntrezGeneId = false;
			boolean inGeneName = false;
			boolean inOrganism = false;
			boolean inISid = false;
			boolean inPlane = false;
			String isId = null;
			plane = null;
			chromosome = null;
			entrezGeneId = null;
			geneName = null;
			organism = null;
			for (int event = parser.next();  
			event != XMLStreamConstants.END_DOCUMENT;
			event = parser.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					if (imageSeriesId == null && parser.getLocalName().equals("imageseriesid")) {
						inISid = true;
					} else if (plane == null && parser.getLocalName().equals("plane")) {
						inPlane = true;
					} else if (chromosome == null && parser.getLocalName().equals("chromosome")) {
						inChromosome = true;
					} else if (entrezGeneId == null && parser.getLocalName().equals("entrezgeneid")) {
						inEntrezGeneId = true;
					} else if (geneName == null && parser.getLocalName().equals("genename")) {
						inGeneName = true;
					} else if (organism == null && parser.getLocalName().equals("organism")) {
						inOrganism = true;
					}
				} else if (event == XMLStreamConstants.CHARACTERS) {
					if (inISid) {
						isId = parser.getText();
						imageSeriesId = isId;
						inISid = false;
					} else if (inPlane) {
						plane = parser.getText();
//						if (plane.equals(desiredPlane.toString())) {
//							imageSeriesId = isId;
//						} else {
//							plane = null;
//						}
						inPlane = false;
					} else if (inChromosome) {
						chromosome = parser.getText();
						inChromosome = false;
					} else if (inEntrezGeneId) {
						entrezGeneId = parser.getText();
						inEntrezGeneId = false;
					} else if (inGeneName) {
						geneName = parser.getText();
						inGeneName = false;
					} else if (inOrganism) {
						organism = parser.getText();
						inOrganism = false;
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(in);
			close(parser);
		}
	    return imageSeriesId;
	}
	
	/**
	 * Example: http://www.brain-map.org/aba/api/expression/71717614.sva
	 * 
	 * @param imageSeriesId
	 * @return
	 */
	private String assembleExpressionEnergyVolumeURI(String imageSeriesId) {
		return String.format(
				"http://www.brain-map.org/aba/api/expression/%s.sva", 
				imageSeriesId);
	}
	
	private String buildXMLString(InputStream svaText) throws XMLStreamException, IOException {
		StringWriter stringWriter = new StringWriter();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter out = null;
		try {
			int indentDepth = 0;
			out = factory.createXMLStreamWriter(stringWriter);
			startXMLDoc(out);
			indentDepth++;

			BufferedReader in = new BufferedReader(new InputStreamReader(svaText));
			String line = null;
			int count = 0;
			while ((line = in.readLine()) != null) {
				count++;
				switch (count) {
				case 1:		// 1st line
					if (line.startsWith("Comment:")) {
						
						// expecting: Comment:Xxx
						String comment = line.split(":")[1];
						indentXML(out, indentDepth);
						out.writeStartElement("Comment");
						out.writeCharacters(String.format(
								"Entrez gene id: %s; ABA gene symbol: %s; "
								+ "Organism: %s; Chromosome: %s; Gene name: %s; "
								+ "Plane: %s; Description: %s", 
								entrezGeneId, abaGeneSymbol, 
								organism, chromosome, geneName, 
								plane, comment));
						out.writeEndElement();
					} else {
						// TODO unexpected
					}
					break;
				case 2:		// 2d line
					if (line.startsWith("Dimensions:")) {
						
						// expecting: Dimensions:67,41,58
						String[] dimensions = line.split(":")[1].split(",");
						indentXML(out, indentDepth);
						out.writeStartElement("MaxDimension");
						out.writeAttribute("x", dimensions[0]);
						out.writeAttribute("y", dimensions[1]);
						out.writeAttribute("z", dimensions[2]);
						out.writeEndElement();
					} else {
						// TODO unexpected
					}
					break;
				case 3:		// 3d line
					
					// 1st data point, e.g. 39,2,1,3.40352e-06
					indentXML(out, indentDepth++);
					out.writeStartElement("SparseVolumeData");
					
					// fall thru
				default:	// all subsequent lines
					
					// 1st and additional data points, e.g. 39,2,1,3.40352e-06
					String[] values = line.split(",");
					indentXML(out, indentDepth);
					out.writeStartElement("Datum");
					out.writeAttribute("x", values[0]);
					out.writeAttribute("y", values[1]);
					out.writeAttribute("z", values[2]);
					out.writeAttribute("value", values[3]);
					out.writeEndElement();
				} // switch
			} // while

			indentXML(out, --indentDepth);
			out.writeEndElement();		// SparseValueData

			endXMLDoc(out);
		} finally {
        	IOUtils.closeQuietly(svaText);
			IOUtils.closeQuietly(stringWriter);
			close(out);
		}
		return stringWriter.toString();
	}
	
	private String buildNotFoundXMLString(String message) throws XMLStreamException {
		StringWriter stringWriter = new StringWriter();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter out = null;
		try {
			int indentDepth = 0;
			out = factory.createXMLStreamWriter(stringWriter);

			startXMLDoc(out);
			
			indentDepth++;

			indentXML(out, indentDepth);
			out.writeStartElement("Comment");
			out.writeCharacters(message);
			out.writeEndElement();

			endXMLDoc(out);
		} finally {
			IOUtils.closeQuietly(stringWriter);
			close(out);
		}
		return stringWriter.toString();
	}
	
	private void indentXML(XMLStreamWriter out, int indentDepth) throws XMLStreamException {
		out.writeCharacters("\n");
		for (int i = 0; i < indentDepth; i++) {
			out.writeCharacters("  ");
		}
	}
	
	private void startXMLDoc(XMLStreamWriter out) throws XMLStreamException {
		out.writeStartDocument("UTF-8", "1.0");
		
		indentXML(out, 0);
		out.writeStartElement("SparseValueVolume");
		out.writeAttribute(
				"xmlns", 
				"http://wholebrainproject.org/wbc/generated/sparsevaluevolume");
		out.writeAttribute(
				"xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		out.writeAttribute(
				"xsi:schemaLocation", 
				"http://wholebrainproject.org/wbc/generated/sparsevaluevolume "
				+ "http://wholebrain.googlecode.com/svn/wbc-core/trunk/src/main/resources/SparseValueVolume.xsd");
	}
	
	private void endXMLDoc(XMLStreamWriter out) throws XMLStreamException {
		indentXML(out, 0);
		out.writeEndElement();		// SparseValueVolume
		out.writeEndDocument();
		indentXML(out, 0);
	}
	
    private void close(XMLStreamReader reader) {
    	if (reader != null) {
    		try {
    			reader.close();
    		} catch (XMLStreamException logOnly) {
    			LOG.warn("Problem closing InputStream", logOnly);
    		}
    	}
    }

    private void close(XMLStreamWriter writer) {
    	if (writer != null) {
    		try {
    			writer.close();
    		} catch (XMLStreamException logOnly) {
    			LOG.warn("Problem closing OutputStream", logOnly);
    		}
    	}
    }

	public enum ImageSeriesPlane {
		CORONAL, SAGITTAL;
		
		@Override 
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
}
