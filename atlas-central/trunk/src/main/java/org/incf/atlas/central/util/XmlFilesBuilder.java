package org.incf.atlas.central.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.incf.atlas.common.util.XmlTransformer;
import org.xml.sax.SAXException;

public class XmlFilesBuilder {
	
	/*
	 * ProcessDescriptions.xslt --> DescribeProcess.xml
	 * DescribeProcess.xml, Capabilities.xslt --> GetCapabilities.xml
	 */

	// sources
//    private static final String PROCESS_INPUTS = "ProcessInputs.xml";
    
    // stylesheets
//    private static final String PRETTY_PRINT_SS = "/PrettyPrint.xslt";
//    private static final String PROC_MASTER_SS = "/ProcessDescriptionsMaster.xslt";
//    private static final String[] PROCESS_DESCRIPTIONS_SS = {
//    	"CentralProcessDescriptions.xslt", 
//    };
//    private static final String[] CAPABILITIES_SS = {
//    	"CentralCapabilities.xslt", 
//    };
    
    // source and working directories
//    private static final String INPUTS_DIR = "src/main/xml";
    private static final String SS_DIR = "src/main/xslt";
//    private static final String TEMP_DIR = "temp";
//	private static final String RESOURCES_DIR = "src/main/resources";
	private static final String RESOURCES_DIR = "target/generated-resources";
    
    // intermediate files
//    private static final String TEMP_FILE = "temp.xml";
//    private static final String PROC_MASTER = "ProcessDescriptionsMaster.xml";
    
    // final outputs
//    private static final String[] PROCESS_DESCRIPTIONS = {
//    	"CentralProcessDescriptions.xml", 
//    };
//    private static final String[] CAPABILITIES = {
//    	"CentralCapabilities.xml", 
//    };
    
    // empty file
//    private static final String EMPTY_FILE = "EmptyFile.txt";
    
    // ListHubs
//    private static final String LIST_HUBS_SS = "ListHubs.xslt";
//    private static final String LIST_HUBS = "ListHubs.xml";
    
    private XmlTransformer transformer;
    
    // directories
    private File ssDir;
//    private File tempDir;
    private File resourcesDir;

    // files
//    private File tempFile;
//    private File procMaster;
//    private File prettyPrintSs;
    
    public XmlFilesBuilder() throws ParserConfigurationException {
        ssDir = new File(SS_DIR);
//        tempDir = new File(TEMP_DIR);
//        tempDir.mkdir();
        resourcesDir = new File(RESOURCES_DIR);
        resourcesDir.mkdir();

//        tempFile = new File(tempDir, TEMP_FILE);
//        prettyPrintSs = new File(ssDir, PRETTY_PRINT_SS);
//        procMaster = new File(tempDir, PROC_MASTER);
        
        transformer = new XmlTransformer();
    }
    
    public void buildDescribeProcess() throws TransformerException, 
    		ParserConfigurationException, IOException {
    	
    	File stylesheet = new File(ssDir, "CentralProcessDescriptions.xslt");
    	File out = new File(resourcesDir, "CentralDescribeProcess.xml");
    	
    	transformer.transform(new FileReader(stylesheet), new FileWriter(out));

////		transform(new FileInputStream(new File(ssDir, "CentralProcessDescriptions.xslt")), 
////				tempFile);
//		transform(new FileInputStream(new File(ssDir, "CentralProcessDescriptions.xslt")), 
//				tempFile);
//
////		prettyPrint(tempFile, new File(resourcesDir, "DescribeProcess.xml"));
//		prettyPrint(tempFile, out);
//
//		tempFile.delete();
	}

    public void buildGetCapabilities() throws TransformerException, 
    		ParserConfigurationException, IOException {

    	File in = new File(resourcesDir, "CentralDescribeProcess.xml");
    	File stylesheet = new File(ssDir, "CentralCapabilities.xslt");
    	File out = new File(resourcesDir, "CentralGetCapabilities.xml");
    	
    	transformer.transform(new FileReader(in), new FileReader(stylesheet), 
    			new FileWriter(out));

////    	transform(new FileInputStream(new File(resourcesDir, "CentralDescribeProcess.xml")),
////    			new FileInputStream(new File(ssDir, "CentralCapabilities.xslt")),
////    			tempFile);
////
////		prettyPrint(tempFile, new File(resourcesDir, "CentralGetCapabilities.xml"));
////
////		tempFile.delete();
//    	transform(new FileInputStream(new File(resourcesDir, "CentralDescribeProcess.xml")),
//    			new FileInputStream(new File(ssDir, "CentralCapabilities.xslt")),
//    			new File(resourcesDir, "CentralGetCapabilities.xml"));
	}

    
    
    
//    private void buildProcMasterFile() 
//			throws TransformerException, FileNotFoundException {
//
//    	// generate process descriptions
//    	transform(new FileInputStream(new File(INPUTS_DIR, PROCESS_INPUTS)),
//    			new FileInputStream(new File(ssDir, PROC_MASTER_SS)),
//    			tempFile);
//
//    	// pretty print process descriptions
//    	transform(new FileInputStream(tempFile),
//    			new FileInputStream(prettyPrintSs),
//    			procMaster);
//
//    	tempFile.delete();
//    }

//    private void buildXmlFiles(int i) 
//			throws TransformerException, FileNotFoundException {
//
//    	File processDescriptions = new File(resourcesDir, PROCESS_DESCRIPTIONS[i]);
//
//    	// generate process descriptions
//    	transform(new FileInputStream(procMaster),
//    			new FileInputStream(new File(ssDir, PROCESS_DESCRIPTIONS_SS[i])),
//    			tempFile);
//
//    	// pretty print process descriptions
//    	transform(new FileInputStream(tempFile),
//    			new FileInputStream(prettyPrintSs),
//    			//		new File(resourcesDir, PROCESS_DESCRIPTIONS[i]));
//    			processDescriptions);
//
//    	tempFile.delete();
//
//    	// generate capabilities
//    	transform(new FileInputStream(processDescriptions),
//    			new FileInputStream(new File(ssDir, CAPABILITIES_SS[i])),
//    			tempFile);
//
//    	// pretty print capabilities
//    	transform(new FileInputStream(tempFile),
//    			new FileInputStream(prettyPrintSs),
//    			new File(resourcesDir, CAPABILITIES[i]));
//
//    	tempFile.delete();
//    }

//    private void buildListHubsFile() 
//			throws TransformerException, FileNotFoundException, ParserConfigurationException {
//
//    	// ss  new File(ssDir, LIST_HUBS_SS)
//    	// input
//    	// resutlt
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
//    	
//    	transform(new FileInputStream(new File(ssDir, LIST_HUBS_SS)), tempFile);
//    	
//    	// pretty print process descriptions
//    	transform(new FileInputStream(tempFile),
//    			new FileInputStream(prettyPrintSs),
//    			new File(resourcesDir, LIST_HUBS));
//
//    	tempFile.delete();
//    }
    
    public static void main(String[] args) throws TransformerException, 
    		IOException, ParserConfigurationException, SAXException {
    	XmlFilesBuilder app = new XmlFilesBuilder();
//    	app.buildProcMasterFile();
//    	for (int i = 0; i < PROCESS_DESCRIPTIONS.length; i++) {
//    		app.buildXmlFiles(i);
//    	}
    	app.buildDescribeProcess();
    	app.buildGetCapabilities();
    }

}
