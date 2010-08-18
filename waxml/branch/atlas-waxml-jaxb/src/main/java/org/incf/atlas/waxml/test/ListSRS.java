package org.incf.atlas.waxml.test;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.incf.atlas.common.util.XMLUtilities;
import org.incf.waxml.ListSRSResponse;
import org.incf.waxml.ObjectFactory;
import org.incf.waxml.SRSType;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;

public class ListSRS {
	
	public static final String NAMESPACE_URI = "http://www.incf.org/WaxML/";
	public static final String CONTEXT_PATH = "org.incf.waxml";
	
	private ObjectFactory of;
	private ListSRSResponse lsr;
	
	public ListSRS() {
		of = new ObjectFactory();
		lsr = of.createListSRSResponse();
		
		SRSType sT = of.createSRSType();
		SRSType.Name sTN = of.createSRSTypeName();
		sTN.setValue("srs name value");
		sT.setName(sTN);
		
		ListSRSResponse.SRSList sl = of.createListSRSResponseSRSList();
		List<SRSType> l = sl.getSRS();
		l.add(sT);
		
		lsr.setSRSList(sl);
	}

	public String getXMLExceptionReport() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("org.incf.waxml");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", 
//				new AtlasNamespacePrefixMapper());
		StringWriter out = new StringWriter();
		marshaller.marshal(lsr, out);
		return out.toString();
	}
	
	public Representation getDomExceptionReport() {
		return XMLUtilities.getDomRepresentation(lsr, 
				NAMESPACE_URI, CONTEXT_PATH);
	}
	
	public Document getDocument() throws ParserConfigurationException, JAXBException {
		return XMLUtilities.object2Document(lsr, 
				NAMESPACE_URI, CONTEXT_PATH);
	}
	
	public static void main(String[] args) throws JAXBException, ParserConfigurationException {
		ListSRS ls = new ListSRS();
//		System.out.println(ls.getXMLExceptionReport());
		XMLUtilities.prettyPrintXml(ls.getDocument(), System.out, 4);
	}

}
