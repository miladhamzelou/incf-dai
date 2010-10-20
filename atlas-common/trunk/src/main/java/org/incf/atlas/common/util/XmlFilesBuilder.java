package org.incf.atlas.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlFilesBuilder {

	// sources
    private static final String PROCESS_INPUTS = "ProcessInputs.xml";
    
    // stylesheets
    private static final String PRETTY_PRINT_SS = "/PrettyPrint.xslt";
    private static final String PROC_MASTER_SS = "/ProcessDescriptionsMaster.xslt";
    private static final String[] PROCESS_DESCRIPTIONS_SS = {
    	"CentralProcessDescriptions.xslt", 
    	"AbaProcessDescriptions.xslt", 
    	"EmapProcessDescriptions.xslt", 
    	"UcsdProcessDescriptions.xslt", 
    	"WhsProcessDescriptions.xslt", 
    };
    private static final String[] CAPABILITIES_SS = {
    	"CentralCapabilities.xslt", 
    	"AbaCapabilities.xslt", 
    	"EmapCapabilities.xslt", 
    	"UcsdCapabilities.xslt", 
    	"WhsCapabilities.xslt", 
    };
    
    // source and working directories
    private static final String INPUTS_DIR = "src/main/xml";
    private static final String SS_DIR = "src/main/xslt";
    private static final String TEMP_DIR = "temp";
    private static final String RESOURCES_DIR = "src/main/resources";
    
    // intermediate files
    private static final String TEMP_FILE = "temp.xml";
    private static final String PROC_MASTER = "ProcessDescriptionsMaster.xml";
    
    // final outputs
    private static final String[] PROCESS_DESCRIPTIONS = {
    	"CentralProcessDescriptions.xml", 
    	"AbaProcessDescriptions.xml", 
    	"EmapProcessDescriptions.xml", 
    	"UcsdProcessDescriptions.xml", 
    	"WhsProcessDescriptions.xml", 
    };
    private static final String[] CAPABILITIES = {
    	"CentralCapabilities.xml", 
    	"AbaCapabilities.xml", 
    	"EmapCapabilities.xml", 
    	"UcsdCapabilities.xml", 
    	"WhsCapabilities.xml", 
    };
    
    // empty file
    private static final String EMPTY_FILE = "EmptyFile.txt";
    
    // ListHubs
    private static final String LIST_HUBS_SS = "ListHubs.xslt";
    private static final String LIST_HUBS = "ListHubs.xml";
    
    private TransformerFactory transFac;
    
    // directories
    private File ssDir;
    private File tempDir;
    private File resourcesDir;

    // files
    private File tempFile;
    private File procMaster;
    private File prettyPrintSs;
    
    public XmlFilesBuilder() throws ParserConfigurationException {
        ssDir = new File(SS_DIR);
        tempDir = new File(TEMP_DIR);
        tempDir.mkdir();
        resourcesDir = new File(RESOURCES_DIR);

        tempFile = new File(tempDir, TEMP_FILE);
        prettyPrintSs = new File(ssDir, PRETTY_PRINT_SS);
        procMaster = new File(tempDir, PROC_MASTER);
        
        transFac = TransformerFactory.newInstance();
    }
    
    private void buildProcMasterFile() 
			throws TransformerException, FileNotFoundException {

    	// generate process descriptions
    	transform(new FileInputStream(new File(INPUTS_DIR, PROCESS_INPUTS)),
    			new FileInputStream(new File(ssDir, PROC_MASTER_SS)),
    			tempFile);

    	// pretty print process descriptions
    	transform(new FileInputStream(tempFile),
    			new FileInputStream(prettyPrintSs),
    			procMaster);

    	tempFile.delete();
    }

    private void buildXmlFiles(int i) 
			throws TransformerException, FileNotFoundException {

    	File processDescriptions = new File(resourcesDir, PROCESS_DESCRIPTIONS[i]);

    	// generate process descriptions
    	transform(new FileInputStream(procMaster),
    			new FileInputStream(new File(ssDir, PROCESS_DESCRIPTIONS_SS[i])),
    			tempFile);

    	// pretty print process descriptions
    	transform(new FileInputStream(tempFile),
    			new FileInputStream(prettyPrintSs),
    			//		new File(resourcesDir, PROCESS_DESCRIPTIONS[i]));
    			processDescriptions);

    	tempFile.delete();

    	// generate capabilities
    	transform(new FileInputStream(processDescriptions),
    			new FileInputStream(new File(ssDir, CAPABILITIES_SS[i])),
    			tempFile);

    	// pretty print capabilities
    	transform(new FileInputStream(tempFile),
    			new FileInputStream(prettyPrintSs),
    			new File(resourcesDir, CAPABILITIES[i]));

    	tempFile.delete();
    }

    private void buildListHubsFile() 
			throws TransformerException, FileNotFoundException, ParserConfigurationException {

    	// ss  new File(ssDir, LIST_HUBS_SS)
    	// input
    	// resutlt
//    	Source ss = new StreamSource(new FileInputStream(new File(ssDir, LIST_HUBS_SS)));
//    	Transformer transformer = transFac.newTransformer(ss);
//    	DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
//    	DocumentBuilder builder = fac.newDocumentBuilder();
//    	Document doc = builder.newDocument();
//    	Source source = new DOMSource(doc);
//    	Result result = new StreamResult(tempFile);
//    	if (transformer == null) {
//    		System.out.println("transformer");
//    	}
//    	if (source == null) {
//    		System.out.println("source");
//    	}
//    	if (result == null) {
//    		System.out.println("result");
//    	}
//    	transformer.transform(source, result);
    	
    	transform(new FileInputStream(new File(ssDir, LIST_HUBS_SS)), tempFile);
    	
    	// pretty print process descriptions
    	transform(new FileInputStream(tempFile),
    			new FileInputStream(prettyPrintSs),
    			new File(resourcesDir, LIST_HUBS));

    	tempFile.delete();
    }

    private void transform(InputStream source, InputStream stylesheet,
            File output) throws TransformerException {
        
        // set up inputs and outputs
        Source xmlSource = new StreamSource(source);
        Source xslt = new StreamSource(stylesheet);
        Result result = new StreamResult(output);
        
        // do transform
        transFac.newTransformer(xslt).transform(xmlSource, result);
    }
    
    private void transform(InputStream stylesheet, File output) 
    		throws TransformerException, ParserConfigurationException {
    	
    	// use an empty input source, i.e. no input
    	Document emptyDoc = DocumentBuilderFactory.newInstance()
    		.newDocumentBuilder().newDocument();
    	Source emptySource = new DOMSource(emptyDoc);
    	      
        // set up inputs and outputs
        Source xslt = new StreamSource(stylesheet);
        Result result = new StreamResult(output);
        
        // do transform
        transFac.newTransformer(xslt).transform(emptySource, result);
    }
    
    public static void main(String[] args) throws TransformerException, 
    		IOException, ParserConfigurationException, SAXException {
    	XmlFilesBuilder app = new XmlFilesBuilder();
//    	app.buildProcMasterFile();
//    	for (int i = 0; i < PROCESS_DESCRIPTIONS.length; i++) {
//    		app.buildXmlFiles(i);
//    	}
    	app.buildListHubsFile();
    }

}
