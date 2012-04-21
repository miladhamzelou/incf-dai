package org.incf.aba.atlas.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

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
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.DataInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetGeneExpressionByGeneId implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
    		GetGeneExpressionByGeneId.class);

    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
//    	InputStream inStream = null;
//    	OutputStream outStream = null;
    	Reader reader = null;
    	Writer writer = null;
    	try {
    		
    		// validate against allowed values in process definition file
    		URL processDefinitionUrl = this.getClass().getResource(
    				"/" + this.getClass().getSimpleName() + ".xml");
    		
    		// get validated data inputs or default values
    		DataInputHandler dataInputHandler = new DataInputHandler(
    				new File(processDefinitionUrl.toURI()));
    		String geneSymbol = dataInputHandler.getValidatedStringValue(in, 
    				"geneSymbol");
    		String filter = dataInputHandler.getValidatedStringValue(in, 
					"filter");
    		
    		LOG.debug(String.format(
    				"DataInputs: geneSymbol: %s, filter: %s",
    				geneSymbol, filter));
    		
    		// get plane; defaults to coronal
    		ImageSeriesPlane desiredPlane = filter.equals("maptype:sagittal")
    				? ImageSeriesPlane.SAGITTAL : ImageSeriesPlane.CORONAL;

    		/*
Say you are interested in gene (symbol) "Coch", at a browser ...

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

Test
http://localhost:8080/aba/atlas?service=WPS&request=GetCapabilities
http://localhost:8080/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL
http://localhost:8080/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneSymbol=Coch;filter=maptype:coronal
http://localhost:8080/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneSymbol=Coch;filter=maptype:coronal&rawDataOutput=GetGeneExpressionByGeneIdOutput
    		 */
    		
    		// step 1: get imageseriesid for given gene and plane
    		String imageSeriesId = retrieveImagesSeriesIdForGene(geneSymbol,
    				desiredPlane);
    		
    		// step 2: get sparse volume file
    		URL u = new URL(assembleExpressionEnergyVolumeURI(imageSeriesId));
    		
    		// complex output approach
    		reader = new InputStreamReader(u.openStream());
    		ComplexOutput output = (ComplexOutput) out.getParameter(
    				"GetGeneExpressionByGeneIdOutput");
    		writer = new OutputStreamWriter(output.getBinaryOutputStream());
    		char[] chars = new char[1024];
    		int charsRead;
    		while ((charsRead = reader.read(chars)) != -1) {
    			writer.write(chars, 0, charsRead);
    		}
    		
//    		inStream = u.openStream();
//    		reader = new BufferedReader(new InputStreamReader(u.openStream()));

//    		ComplexOutput output = (ComplexOutput) out.getParameter(
//    				"GetGeneExpressionByGeneIdOutput");
//    		LiteralOutput output = (LiteralOutput) out.getParameter(
//					"GetGeneExpressionByGeneIdOutput");
    		
//    		LOG.debug("Setting output (requested=" + output.isRequested() + ")");
    		
//    		outStream = complexOutput.getBinaryOutputStream();
//    		writer = new OutputStreamWriter(output.getBinaryOutputStream());
//    		writer = new PrintWriter(new StringWriter());
    		
//    		byte[] bytes = new byte[1024];
//    		int bytesRead;
//    		while ((bytesRead = inStream.read(bytes)) != -1) {
//    			outStream.write(bytes, 0, bytesRead);
//    		}
    		
//    		char[] chars = new char[1024];
//    		int charsRead;
//    		while ((charsRead = reader.read(chars)) != -1) {
//    			writer.write(chars, 0, charsRead);
//    		}
    		
//    		output.setValue(writer.toString());
    		
//    		// get ComplexOutput object from ProcessletOutput...
//    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
//    				"Get2DImagesByPOIOutput");
//
//    		LOG.debug("Setting complex output (requested=" 
//    				+ complexOutput.isRequested() + ")");
//    		
//    		// ComplexOutput objects can be huge so stream it 
//    		XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
//    		XMLAdapter.writeElement(writer, reader);
    		
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
//        	close(inStream);
//        	close(outStream);
        	close(reader);
        	close(writer);
        }
    }
    
    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }
    
	private String retrieveImagesSeriesIdForGene(String geneSymbol,
			ImageSeriesPlane desiredPlane) throws IOException, 
					XMLStreamException {
		String imageSeriesId = null;
		URL u = new URL(ABAUtil.assembleGeneURI(geneSymbol));

		LOG.debug("Gene info URI: {}", u.toString());

		InputStream in = u.openStream();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(in);

		boolean inISid = false;
		boolean inPlane = false;
		String isId = null;
		String plane = null;
		for (int event = parser.next();  
		event != XMLStreamConstants.END_DOCUMENT;
		event = parser.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (parser.getLocalName().equals("imageseriesid")) {
					inISid = true;
				} else if (parser.getLocalName().equals("plane")) {
					inPlane = true;
				}
			} else if (event == XMLStreamConstants.CHARACTERS) {
				if (inISid) {
					isId = parser.getText();
					inISid = false;
				} else if (inPlane) {
					plane = parser.getText();
					if (plane.equals(desiredPlane.toString())) {
						imageSeriesId = isId;
					}
					inPlane = false;
				}
			}
		}
		try {
			parser.close();
		} catch (XMLStreamException e) {
			LOG.warn(e.getMessage(), e);		// log but go on
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
	
    private void close(InputStream inStream) {
    	if (inStream != null) {
    		try {
    			inStream.close();
    		} catch (IOException logOnly) {
    			LOG.warn("Problem closing InputStream", logOnly);
    		}
    	}
    }

    private void close(OutputStream outStream) {
    	if (outStream != null) {
    		try {
    			outStream.close();
    		} catch (IOException logOnly) {
    			LOG.warn("Problem closing OutputStream", logOnly);
    		}
    	}
    }

    private void close(Reader reader) {
    	if (reader != null) {
    		try {
    			reader.close();
    		} catch (IOException logOnly) {
    			LOG.warn("Problem closing InputStream", logOnly);
    		}
    	}
    }

    private void close(Writer writer) {
    	if (writer != null) {
    		try {
    			writer.close();
    		} catch (IOException logOnly) {
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
