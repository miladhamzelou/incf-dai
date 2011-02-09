package edu.ucsd.crbs.wps.demo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.ComplexOutput;
import org.deegree.services.wps.output.LiteralOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunSPARQLQuery implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
    		RunSPARQLQuery.class);
    
    /*
     * Vadim email 11 jan
     * 
http://rdf.neuinfo.org/sparql?default-graph-uri=&should-sponge=&query=prefix+nifstd:+<http://ontology.neuinfo.org>%0D%0Aprefix+nm:+<http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Molecule.owl%23>%0D%0Aprefix+be:+<http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-BrainRegion-Bridge.owl%23>%0D%0Aprefix+nb:+<http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-NT-Bridge.owl%23>%0D%0Aprefix+w3:+<http://www.w3.org/2002/07/owl%23>%0D%0Aprefix+rdfs:+<http://www.w3.org/2000/01/rdf-schema%23>%0D%0Aprefix+xsd:+<http://www.w3.org/2001/XMLSchema%23>%0D%0A%0D%0Aselect++%3Fe,%3Fl+where+{%0D%0A%3Fe+rdfs:label+%3Fl.+%0D%0A%3Fe+rdfs:subClassOf+%3Fhn.%0D%0A%3Fhn+rdfs:label+"Cerebellum+neuron"^^xsd:string.%0D%0A%3Fe+rdfs:subClassOf+%3Fo.%0D%0A%3Fo+w3:onProperty+nb:has_neurotransmitter.%0D%0A%3Fo+w3:someValuesFrom+%3Fgaba.%0D%0A%3Fgaba+rdfs:label+"GABA"^^xsd:string%0D%0A}&format=text/xml&debug=on&timeout=

inputs: rdf.neuinfo.org
sparql

default-graph-uri=
should-sponge=
query=prefix+nifstd:+<http://ontology.neuinfo.org>%0D%0Aprefix+nm:+<http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Molecule.owl%23>%0D%0Aprefix+be:+<http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-BrainRegion-Bridge.owl%23>%0D%0Aprefix+nb:+<http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-NT-Bridge.owl%23>%0D%0Aprefix+w3:+<http://www.w3.org/2002/07/owl%23>%0D%0Aprefix+rdfs:+<http://www.w3.org/2000/01/rdf-schema%23>%0D%0Aprefix+xsd:+<http://www.w3.org/2001/XMLSchema%23>%0D%0A%0D%0Aselect++%3Fe,%3Fl+where+{%0D%0A%3Fe+rdfs:label+%3Fl.+%0D%0A%3Fe+rdfs:subClassOf+%3Fhn.%0D%0A%3Fhn+rdfs:label+"Cerebellum+neuron"^^xsd:string.%0D%0A%3Fe+rdfs:subClassOf+%3Fo.%0D%0A%3Fo+w3:onProperty+nb:has_neurotransmitter.%0D%0A%3Fo+w3:someValuesFrom+%3Fgaba.%0D%0A%3Fgaba+rdfs:label+"GABA"^^xsd:string%0D%0A}
format=text/xml
debug=on
timeout=

test 1: 
http://localhost:8080/wps-demo?service=WPS&request=GetCapabilities
http://localhost:8080/wps-demo?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL
http://localhost:8080/wps-demo?service=WPS&version=1.0.0&request=Execute&identifier=RunSPARQLQuery&dataInputs=query=prefix+nifstd:+%3Chttp://ontology.neuinfo.org%3E%0D%0Aprefix+nm:+%3Chttp://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Molecule.owl%23%3E%0D%0Aprefix+be:+%3Chttp://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-BrainRegion-Bridge.owl%23%3E%0D%0Aprefix+nb:+%3Chttp://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-NT-Bridge.owl%23%3E%0D%0Aprefix+w3:+%3Chttp://www.w3.org/2002/07/owl%23%3E%0D%0Aprefix+rdfs:+%3Chttp://www.w3.org/2000/01/rdf-schema%23%3E%0D%0Aprefix+xsd:+%3Chttp://www.w3.org/2001/XMLSchema%23%3E%0D%0A%0D%0Aselect++%3Fe,%3Fl+where+{%0D%0A%3Fe+rdfs:label+%3Fl.+%0D%0A%3Fe+rdfs:subClassOf+%3Fhn.%0D%0A%3Fhn+rdfs:label+%22Cerebellum+neuron%22^^xsd:string.%0D%0A%3Fe+rdfs:subClassOf+%3Fo.%0D%0A%3Fo+w3:onProperty+nb:has_neurotransmitter.%0D%0A%3Fo+w3:someValuesFrom+%3Fgaba.%0D%0A%3Fgaba+rdfs:label+%22GABA%22^^xsd:string%0D%0A}&rawDataOutput=result
test 2: 
http://drlittle.ucsd.edu:8080/wps-demo?service=WPS&request=GetCapabilities
http://drlittle.ucsd.edu:8080/wps-demo?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL
http://drlittle.ucsd.edu:8080/wps-demo?service=WPS&version=1.0.0&request=Execute&identifier=RunSPARQLQuery&dataInputs=query=prefix+nifstd:+%3Chttp://ontology.neuinfo.org%3E%0D%0Aprefix+nm:+%3Chttp://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Molecule.owl%23%3E%0D%0Aprefix+be:+%3Chttp://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-BrainRegion-Bridge.owl%23%3E%0D%0Aprefix+nb:+%3Chttp://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Neuron-NT-Bridge.owl%23%3E%0D%0Aprefix+w3:+%3Chttp://www.w3.org/2002/07/owl%23%3E%0D%0Aprefix+rdfs:+%3Chttp://www.w3.org/2000/01/rdf-schema%23%3E%0D%0Aprefix+xsd:+%3Chttp://www.w3.org/2001/XMLSchema%23%3E%0D%0A%0D%0Aselect++%3Fe,%3Fl+where+{%0D%0A%3Fe+rdfs:label+%3Fl.+%0D%0A%3Fe+rdfs:subClassOf+%3Fhn.%0D%0A%3Fhn+rdfs:label+%22Cerebellum+neuron%22^^xsd:string.%0D%0A%3Fe+rdfs:subClassOf+%3Fo.%0D%0A%3Fo+w3:onProperty+nb:has_neurotransmitter.%0D%0A%3Fo+w3:someValuesFrom+%3Fgaba.%0D%0A%3Fgaba+rdfs:label+%22GABA%22^^xsd:string%0D%0A}&rawDataOutput=result
     */

    private static final String URL = "http://rdf.neuinfo.org/";
    private static final String CONTEXT = "sparql";
    private static final String PRE = "?default-graph-uri=&should-sponge=&query=";
    private static final String POST = "&format=text/xml&debug=on&timeout=";

    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	InputStream inStream = null;
    	OutputStream outStream = null;
    	try {

    		// collect input values
    		LiteralInput inQuery = (LiteralInput) in.getParameter("query");
    		if (inQuery == null) {
    			throw new MissingParameterException(
    					"Missing required parameter", "query");
    			
    		}

    		String query = inQuery.getValue();

    		LOG.debug("query: {}", query);

    		// do actual work of process
    		
    		// encode query
    		String encodedQuery = URLEncoder.encode(query, "UTF-8");
    		
    		// execute secondary http request
    		URL wbcContentUrl = new URL(URL + CONTEXT + PRE + encodedQuery 
    				+ POST);
    		inStream = wbcContentUrl.openStream();

    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
    				"result");
    		
    		LOG.debug("Setting complex output (requested=" 
    				+ complexOutput.isRequested() + ")");
    		
    		outStream = complexOutput.getBinaryOutputStream();
    		
    		byte[] bytes = new byte[1024];
    		int bytesRead;
    		while ((bytesRead = inStream.read(bytes)) != -1) {
    			outStream.write(bytes, 0, bytesRead);
    		}
        } catch (MissingParameterException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (Throwable e) {
        	String message = "Unexpected exception occured";
        	LOG.error(message, e);
        	OWSException owsException = new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE);
        	throw new ProcessletException(owsException);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

}
