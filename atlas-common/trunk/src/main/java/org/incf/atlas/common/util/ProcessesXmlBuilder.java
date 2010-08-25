package org.incf.atlas.common.util;

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


public class ProcessesXmlBuilder {

    // resources
    private static final String SRS_NAMES = "/SrsNames.xml";
    private static final String PROCESS_INPUTS_SS = "/ProcessInputs.xslt";
    private static final String PRETTY_PRINT_SS = "/PrettyPrint.xslt";

    // outputs
    private static final String CACHE_DIR = "src/main/webapp/WEB-INF/cache";
    private static final String TEMP_FILE = "temp.xml";
    private static final String PROCESS_DESCRIPTIONS = "PDs.xml";
    
    private TransformerFactory transFac;
    
    public ProcessesXmlBuilder() throws ParserConfigurationException {
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
        
        // generate process descriptions
        transform(this.getClass().getResourceAsStream(SRS_NAMES),
                this.getClass().getResourceAsStream(PROCESS_INPUTS_SS),
                tempFile);
        
        // pretty print process descriptions
        transform(new FileInputStream(tempFile),
                this.getClass().getResourceAsStream(PRETTY_PRINT_SS),
                processDescriptions);
        
        tempFile.delete();
        
    }

    public static void main(String[] args) throws TransformerException, 
            IOException, ParserConfigurationException, SAXException {
        new ProcessesXmlBuilder().execute();
    }

}
