package org.incf.atlas.common.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class XmlTransformer {
	
	private static final String PP_SS = "src/main/xslt/PrettyPrint.xslt";

    private TransformerFactory transFac;
    
    public XmlTransformer() {
        transFac = TransformerFactory.newInstance();
    }
    
    /**
     * Transform an XML source but applying an XSLT stylesheet to produce an
     * XML result.
     * 
     * @param xmlSource
     * @param stylesheet
     * @param xmlResult
     * @throws TransformerException
     */
    public void transform(Reader xmlSource, Reader stylesheet,
            Writer xmlResult) throws TransformerException {
        
        // set up inputs and outputs
        Source source = new StreamSource(xmlSource);
        Source xslt = new StreamSource(stylesheet);
        Result result = new StreamResult(xmlResult);
        
        // do transform
        transFac.newTransformer(xslt).transform(source, result);
    }
    
    /**
     * Produce an XML result by applying an XSLT stylesheet with no XML source.
     * 
     * @param stylesheet
     * @param xmlResult
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public void transform(Reader stylesheet, Writer xmlResult) 
			throws TransformerException, ParserConfigurationException {

    	// use an empty input source, i.e. no input
    	Document emptyDoc = DocumentBuilderFactory.newInstance()
    			.newDocumentBuilder().newDocument();
    	Source emptySource = new DOMSource(emptyDoc);

    	// set up inputs and outputs
    	Source xslt = new StreamSource(stylesheet);
    	Result result = new StreamResult(xmlResult);

    	// do transform
    	transFac.newTransformer(xslt).transform(emptySource, result);
    }
    
    public void prettyPrint(Reader xmlSource, Writer xmlResult) 
    		throws TransformerException, FileNotFoundException {
    	transform(xmlSource, new FileReader(PP_SS), xmlResult);
    }
    
    public static void main(String[] args) throws FileNotFoundException, 
    		TransformerException, IOException {
    	XmlTransformer app = new XmlTransformer();
    	app.prettyPrint(
    			new FileReader("/home/dave/workspace/atlas-central/src/main/resources/ListHubs.xml"), 
    			new FileWriter("/home/dave/workspace/atlas-central/src/main/resources/ListHubs1.xml"));
    }

}
