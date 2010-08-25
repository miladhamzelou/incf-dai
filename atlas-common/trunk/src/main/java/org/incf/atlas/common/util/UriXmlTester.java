package org.incf.atlas.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class UriXmlTester {
	
	public static final String HUB = "atlas-emap";
	public static final String HOST = 
		"http://incf-dev-local.crbs.ucsd.edu:8080/";
	
	public static final String GETCAPABILITIES = HOST + HUB
		+ "?service=WPS&request=GetCapabilities";
	public static final String DESCRIBEPROCESS = HOST + HUB
		+ "?service=WPS&version=1.0.0&request=DescribeProcess";
	public static final String GET2DIMAGESBYPOI = HOST + HUB
		+ "?service=WPS&version=1.0.0&request=Execute"
		+ "&Identifier=Get2DImagesByPOI"
		+ "&DataInputs=srsName=Mouse_AGEA_1.0;x=6600;y=4000;z=5600"
		+ ";filter=maptype:coronal";

    private static final String PRETTY_PRINT_SS = "/PrettyPrint.xslt";

    private TransformerFactory transFac;
    
	public UriXmlTester() {
        transFac = TransformerFactory.newInstance();
	}
	
	public void executeSequence() throws TransformerException, IOException {

        execute(GETCAPABILITIES);
        execute(DESCRIBEPROCESS);
        
        System.out.println("Working ...");
        execute(GET2DIMAGESBYPOI);
	}

	public void execute(String function) throws TransformerException, IOException {
        transform(new URL(function).openStream());
        
        // prompt for go ahead
        System.in.read();
	}

	public void transform(InputStream source) throws TransformerException {
        
        // set up inputs and outputs
        Source xmlSource = new StreamSource(source);
        Source xslt = new StreamSource(
        		this.getClass().getResourceAsStream(PRETTY_PRINT_SS));
        Result result = new StreamResult(System.out);
        
        // do transform
        transFac.newTransformer(xslt).transform(xmlSource, result);
    }
    
	public static void main(String[] args) 
			throws TransformerException, IOException {
		UriXmlTester t = new UriXmlTester();
		t.executeSequence();
	}

}
