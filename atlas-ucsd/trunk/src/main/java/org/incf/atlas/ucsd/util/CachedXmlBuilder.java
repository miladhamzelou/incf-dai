package org.incf.atlas.ucsd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;


public class CachedXmlBuilder {

    // resources
    private static final String PROCESS_INPUTS = "/ProcessInputs.xml";
    private static final String PROCESSES_SS = "/ProcessDescriptions.xslt";
    private static final String CAPABILITIES_SS = "/Capabilities.xslt";
    private static final String PRETTY_PRINT_SS = "/PrettyPrint.xslt";

    // outputs
    private static final String CACHE_DIR = "src/main/webapp/WEB-INF/cache";
//    private static final String CACHE_DIR = "target";
    private static final String TEMP_FILE = "temp.xml";
    private static final String PROCESS_DESCRIPTIONS = "ProcessDescriptions.xml";
    private static final String CAPABILITIES = "Capabilities.xml";
    
    private TransformerFactory transFac;
    
    public CachedXmlBuilder() throws ParserConfigurationException {
        transFac = TransformerFactory.newInstance();
    }
    
    public void transform(InputStream source, InputStream stylesheet,
            File output) throws TransformerException {
        
        // set up inputs and outputs
        Source xmlSource = new StreamSource(source);
        Source xslt = new StreamSource(stylesheet);
        Result result = new StreamResult(output);
        
        // do transform
        transFac.newTransformer(xslt).transform(xmlSource, result);
    }
    
    public void execute() throws TransformerException, FileNotFoundException {
        
        File cacheDir = new File(CACHE_DIR);
        cacheDir.mkdir();
        File tempFile = new File(cacheDir, TEMP_FILE);
        File processDescriptions = new File(cacheDir, PROCESS_DESCRIPTIONS);
        File capabilities = new File(cacheDir, CAPABILITIES);
        
        // generate process descriptions
        transform(this.getClass().getResourceAsStream(PROCESS_INPUTS),
                this.getClass().getResourceAsStream(PROCESSES_SS),
                tempFile);
        
        // pretty print process descriptions
        transform(new FileInputStream(tempFile),
                this.getClass().getResourceAsStream(PRETTY_PRINT_SS),
                processDescriptions);
        
        tempFile.delete();
        
        // generate capabilities
        transform(new FileInputStream(processDescriptions),
                this.getClass().getResourceAsStream(CAPABILITIES_SS),
                tempFile);
        
        // pretty print capabilities
        transform(new FileInputStream(tempFile),
                this.getClass().getResourceAsStream(PRETTY_PRINT_SS),
                capabilities);
        
        tempFile.delete();
    }

    public static void main(String[] args) throws TransformerException, 
            IOException, ParserConfigurationException, SAXException {
        new CachedXmlBuilder().execute();
    }

}
