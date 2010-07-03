package org.incf.atlas.aba.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public final class XMLUtilities {

    /**
     * Print an XML string in "pretty" format to an output stream.
     * 
     * @param xmlString
     * @param out
     * @param indent the number of characters to indent
     */
    public static void prettyPrintXmlString(String xmlString, 
    		OutputStream out, int indent) {
    	prettyPrintXml(new ByteArrayInputStream(xmlString.getBytes()), out,
    			indent);
    }
    
    /**
     * Print XML in "pretty" format.
     * 
     * @param in an XML input stream
     * @param out where the result will go
     * @param indent the number of characters to indent
     */
    public static void prettyPrintXml(InputStream in, 
    		OutputStream out, int indent) {
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        TransformerFactory tfactory = TransformerFactory.newInstance();
        
        // set indent spaces on factory
//        tfactory.setAttribute("indent-number", new Integer(indent));
        
        try {
        	DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        	Document doc = docBuilder.parse(in);
        	Transformer serializer = tfactory.newTransformer();
            
            // turn on indenting on transformer (serializer)
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(
            		"{http://xml.apache.org/xslt}indent-amount", 
//            		String.valueOf(indent));
            		"2");

            serializer.transform(new DOMSource(doc),
            		new StreamResult(new OutputStreamWriter(out, "utf-8")));
        } catch (Exception e) {
        	
            // this is fatal, just dump stack and throw runtime exception
            e.printStackTrace();
            
            throw new RuntimeException(e);
        }
    }
    
    public static void prettyPrintXml(Document doc, 
    		OutputStream out, int indent) {
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        TransformerFactory tfactory = TransformerFactory.newInstance();
        
        // set indent spaces on factory
//        tfactory.setAttribute("indent-number", new Integer(indent));
        
        try {
        	Transformer serializer = tfactory.newTransformer();
            
            // turn on indenting on transformer (serializer)
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(
            		"{http://xml.apache.org/xslt}indent-amount", 
//            		String.valueOf(indent));
            		"2");

            serializer.transform(new DOMSource(doc),
            		new StreamResult(new OutputStreamWriter(out, "utf-8")));
        } catch (Exception e) {
        	
            // this is fatal, just dump stack and throw runtime exception
            e.printStackTrace();
            
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
    	String inFile = "src/main/resources/exampleResponses/GetCapabilitiesResponse.xml";
    	String outFile = "src/main/resources/exampleResponses/GetCapabilitiesResponse1.xml";
//    	String inFile = "dbDump.xml";
//    	String outFile = "dbDumpPretty.xml";
    	prettyPrintXml(new FileInputStream(inFile), new FileOutputStream(outFile), 4);
    }
    
}
