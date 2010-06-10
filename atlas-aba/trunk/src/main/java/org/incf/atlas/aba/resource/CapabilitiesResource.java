package org.incf.atlas.aba.resource;

//import generated.Capabilities;
//import generated.ObjectFactory;
//import generated.ServiceIdentification;
//import generated.ServiceProvider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.ows._1.CodeType;
import net.opengis.ows._1.LanguageStringType;
import net.opengis.ows._1.OperationsMetadata;
import net.opengis.ows._1.ServiceIdentification;
import net.opengis.ows._1.ServiceProvider;
import net.opengis.wps._1_0.Languages;
import net.opengis.wps._1_0.LanguagesType;
import net.opengis.wps._1_0.ObjectFactory;
import net.opengis.wps._1_0.ProcessBriefType;
import net.opengis.wps._1_0.ProcessOfferings;
import net.opengis.wps._1_0.WPSCapabilitiesType;

import org.incf.atlas.aba.util.AtlasNamespacePrefixMapper;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class CapabilitiesResource extends Resource {
	
	private static final Logger logger = LoggerFactory.getLogger(CapabilitiesResource.class);
	
	/*
	 * /incf-services/service/ABAServiceController?request=GetCapabilities&output=xml
	 */
	
	private String responseFormat;

	public CapabilitiesResource(Context context, Request request, Response response) {
		super(context, request, response);
		
		responseFormat = (String) 
		        getRequest().getAttributes().get("ResponseFormat");
		
//		logger.debug("*****query: {}", query);
//		Set<String> names = queryString.getNames();
//		Map<String, String> map = queryString.getValuesMap();
//		for (String name : names) {
//			logger.debug("name: {}, value: {}", name, map.get(name));
//		}
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
		
		
//		logger.debug("req: {}, output: {}", req, output);
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		ObjectFactory of = new ObjectFactory();
		net.opengis.ows._1.ObjectFactory owsFac = new net.opengis.ows._1.ObjectFactory();
		
		WPSCapabilitiesType capabilities = of.createWPSCapabilitiesType();
		
		// attributes
	    capabilities.setVersion("0.0");
	    capabilities.setUpdateSequence("some update sequence");
		capabilities.setService("test service");
		capabilities.setLang("EN");

		// service identification
	    ServiceIdentification si = owsFac.createServiceIdentification();
	    LanguageStringType siTitle = owsFac.createLanguageStringType();
	    siTitle.setValue("ABA Services");
	    si.getTitle().add(siTitle);
	    LanguageStringType siAbstract = owsFac.createLanguageStringType();
	    siAbstract.setValue("ABA Services are created to access ....");
	    si.getAbstract().add(siAbstract);
	    CodeType serviceType = owsFac.createCodeType();
	    serviceType.setValue("WPS");
	    si.setServiceType(serviceType);
	    si.getServiceTypeVersion().add("0.2.41.0.0");
	    si.setFees("NONE");
	    si.getAccessConstraints().add("NONE");
	    capabilities.setServiceIdentification(si);
	    
	    // service provider
        ServiceProvider serviceProvider = owsFac.createServiceProvider();
        serviceProvider.setProviderName("Asif Memon");
        capabilities.setServiceProvider(serviceProvider);
        
        // operations metadata
        OperationsMetadata operationsMetadata = 
                owsFac.createOperationsMetadata();
        net.opengis.ows._1.Operation operation = owsFac.createOperation();
        operation.setName("GetCapabilities");
        operationsMetadata.getOperation().add(operation);
        capabilities.setOperationsMetadata(operationsMetadata);

	    // process offerings
        ProcessOfferings processOfferings = of.createProcessOfferings();

        // process 1
        ProcessBriefType process1 = of.createProcessBriefType();
        CodeType identifier1 = owsFac.createCodeType();
        identifier1.setValue("GetCorrelationMapByPOI");
        process1.setIdentifier(identifier1);
        LanguageStringType title1 = owsFac.createLanguageStringType();
        title1.setValue("Get Correlation Map");
        process1.setTitle(title1);
        LanguageStringType abstract1 = owsFac.createLanguageStringType();
        abstract1.setValue("This method will return the URL and load the "
                + "correlation map interface in the browser, for a point of "
                + "interest (POI) in a specified Spatial Reference System " 
                + "(SRS)");
        process1.setAbstract(abstract1);
        processOfferings.getProcess().add(process1);
        
        // process 2
        ProcessBriefType process2 = of.createProcessBriefType();
        CodeType identifier2 = owsFac.createCodeType();
        identifier2.setValue("GetStructureNamesByPOI");
        process2.setIdentifier(identifier2);
        LanguageStringType title2 = owsFac.createLanguageStringType();
        title2.setValue("Get Structure Names by POI");
        process2.setTitle(title2);
        LanguageStringType abstract2 = owsFac.createLanguageStringType();
        abstract2.setValue("Returns Fine/Anatomic structures segmented for the "
                + "POI in a specified SRS");
        process2.setAbstract(abstract2);
		processOfferings.getProcess().add(process2);
		
		capabilities.setProcessOfferings(processOfferings);
		
		// languages
		Languages languages = of.createLanguages();
		Languages.Default defaultLanguage = of.createLanguagesDefault();
		defaultLanguage.setLanguage("en-US");
		LanguagesType supportedLanguages = of.createLanguagesType();
		supportedLanguages.getLanguage().add("en-US");
		languages.setDefault(defaultLanguage);
		languages.setSupported(supportedLanguages);
		capabilities.setLanguages(languages);
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return getDomRepresentation(capabilities);
		}
		
		return null;
	}
	
	/**
	 * For the server. Get a Restlet DomRepresentation of the given object,
	 * which should be a generated class type.
	 * 
	 * @param object
	 *            - generated class type.
	 * @return - the DomRepresentation that is constructed from that generated
	 *         class type
	 * @see DomRepresentation
	 */
	@SuppressWarnings("unchecked")
	public static synchronized DomRepresentation getDomRepresentation(
			Object object) {
		DomRepresentation representation = null;
		try {
			// create representation and get its empty dom
			representation = new DomRepresentation(MediaType.TEXT_XML);
			Document d = representation.getDocument();

			final Marshaller marshaller = getWBCJAXBContext()
					.createMarshaller();
			
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", 
					new AtlasNamespacePrefixMapper());

			// marshal object into representation's dom
			Class clazz = object.getClass();
			QName qName = new QName(
					"http://www.opengis.net/wps/1.0.0",
					"Capabilities");
			JAXBElement jaxbElement = new JAXBElement(qName, clazz, object);
			marshaller.marshal(jaxbElement, d);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return representation;
	}

	protected static JAXBContext jaxbContext = null;

	/**
	 * Lazily instantiates a single version of the JAXBContext since it is slow
	 * to create and can be reused throughout the lifetime of the app.
	 * 
	 * @return
	 */
	public static JAXBContext getWBCJAXBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance("net.opengis.wps._1_0");
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}
		return jaxbContext;
	}
    
}
