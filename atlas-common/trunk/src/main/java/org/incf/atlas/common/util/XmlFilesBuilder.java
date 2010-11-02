package org.incf.atlas.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class XmlFilesBuilder {

	// sources
    private static final String PROCESS_INPUTS = "ProcessInputs.xml";
    
    // stylesheets
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
    
    // ListHubs
    private static final String LIST_HUBS_SS = "ListHubs.xslt";
    private static final String LIST_HUBS = "ListHubs.xml";
    
    private XmlTransformer transformer;
    
    // directories
    private File ssDir;
    private File resourcesDir;

    // files
    private File procMaster;
    
    public XmlFilesBuilder() throws ParserConfigurationException {
        ssDir = new File(SS_DIR);
        resourcesDir = new File(RESOURCES_DIR);

        procMaster = new File(resourcesDir, PROC_MASTER);
        
        transformer = new XmlTransformer();
    }
    
    private void buildProcMasterFile() 
			throws TransformerException, IOException {

    	// generate process descriptions
    	transformer.transform(new FileReader(new File(INPUTS_DIR, PROCESS_INPUTS)),
    			new FileReader(new File(ssDir, PROC_MASTER_SS)),
    			new FileWriter(procMaster));
    }

    private void buildXmlFiles(int i) 
			throws TransformerException, IOException {

    	File processDescriptions = new File(resourcesDir, PROCESS_DESCRIPTIONS[i]);

    	// generate process descriptions
    	transformer.transform(new FileReader(procMaster),
    			new FileReader(new File(ssDir, PROCESS_DESCRIPTIONS_SS[i])),
    			new FileWriter(processDescriptions));

    	// generate capabilities
    	transformer.transform(new FileReader(processDescriptions),
    			new FileReader(new File(ssDir, CAPABILITIES_SS[i])),
    			new FileWriter(new File(resourcesDir, CAPABILITIES[i])));
    }

    private void buildListHubsFile() 
			throws TransformerException, IOException, ParserConfigurationException {
    	transformer.transform(new FileReader(new File(ssDir, LIST_HUBS_SS)), 
    			new FileWriter(new File(resourcesDir, LIST_HUBS)));
    }

    public static void main(String[] args) throws TransformerException, 
    		IOException, ParserConfigurationException, SAXException {
    	XmlFilesBuilder app = new XmlFilesBuilder();
    	app.buildProcMasterFile();
    	for (int i = 0; i < PROCESS_DESCRIPTIONS.length; i++) {
    		app.buildXmlFiles(i);
    	}
    	app.buildListHubsFile();
    }

}
