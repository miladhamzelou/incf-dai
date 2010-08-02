package org.incf.atlas.common.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.restlet.data.MediaType;
import org.restlet.resource.DomRepresentation;
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
    
	public static synchronized DomRepresentation getDomRepresentation(
			Object object, String namespaceUri, String contextPath) {
		DomRepresentation representation = null;
		try {
			
			// create representation and get its empty dom
			representation = new DomRepresentation(MediaType.TEXT_XML);
			Document doc = representation.getDocument();

			final Marshaller marshaller = 
					JAXBContext.newInstance(contextPath).createMarshaller();
			
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", 
					new AtlasNamespacePrefixMapper());

			// marshal object into representation's dom
			Class clazz = object.getClass();
			QName qName = new QName(namespaceUri, clazz.getSimpleName());
			JAXBElement jaxbElement = new JAXBElement(qName, clazz, object);
			marshaller.marshal(jaxbElement, doc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return representation;
	}

//	protected static JAXBContext jaxbContext = null;
//
//	/**
//	 * Lazily instantiates a single version of the JAXBContext since it is slow
//	 * to create and can be reused throughout the lifetime of the app.
//	 * 
//	 * @return
//	 */
//	public static JAXBContext getWBCJAXBContext() {
//		if (jaxbContext == null) {
//			try {
//				jaxbContext = JAXBContext.newInstance("org.incf.waxml");
//			} catch (JAXBException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		return jaxbContext;
//	}

    public static void main(String[] args) throws FileNotFoundException {
    	String inFile = "src/main/resources/exampleResponses/GetCapabilitiesResponse.xml";
    	String outFile = "src/main/resources/exampleResponses/GetCapabilitiesResponse1.xml";
//    	String inFile = "dbDump.xml";
//    	String outFile = "dbDumpPretty.xml";
    	prettyPrintXml(new FileInputStream(inFile), new FileOutputStream(outFile), 4);
    }
    
}
