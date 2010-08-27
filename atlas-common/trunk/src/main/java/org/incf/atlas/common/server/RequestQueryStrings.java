package org.incf.atlas.common.server;


/**
 * Specifies the supported Atlas HTTP GET query string formats.
 * 
 * @author dave
 */
public interface RequestQueryStrings {

    /*
    Atlas HTTP GET requests include an appended query string. Following the
    OGC (Open Geospacial Consortium) WPS (Web Processing Services), the query 
    string keys, case and order sensitive, are
    - service (all requests)
    - version (all requests except GetCapabilities)
    - request (all requests)
    - Identifier (all Execute requests)
    - DataInputs (most Execute requests)

    service = WPS
    version = version
    request = { GetCapabilities | DescribeProcess | Execute }
    Identifier = (enumerated below)
    DataInputs = { varies by identifier }
     */

    // elements of Atlas GET query strings (case sensitive!)
    public static final String SERVICE = "?service={service}";
    public static final String VERSION = "&version={version}";
    public static final String REQUEST_KEY = "&request=";
    public static final String EXECUTE_REQUEST = 
    		SERVICE + VERSION + "&request=Execute&Identifier=";
    public static final String DATA_INPUTS = "&DataInputs={dataInputs}";
    
    // GetCapabilites - supported at all hubs
    // does not include version because response specifies supported version
    public static final String GET_CAPABILITIES =
    		SERVICE + REQUEST_KEY + "GetCapabilities";

    // DescribeProcess - supported at all hubs
    public static final String DESCRIBE_PROCESS =
    		SERVICE + VERSION + REQUEST_KEY + "DescribeProcess";
    
    // execute requests (not all are supported at all hubs)
    public static final String DESCRIBE_SRS =
    		EXECUTE_REQUEST + "DescribeSRS" + DATA_INPUTS;
    
    public static final String DESCRIBE_TRANSFORMATION =
    		EXECUTE_REQUEST + "DescribeTransfomation" + DATA_INPUTS;
    
    public static final String GET_2D_IMAGES_BY_POI =
    		EXECUTE_REQUEST + "Get2DImagesByPOI" + DATA_INPUTS;
    
    public static final String GET_2D_IMAGES_BY_URI =
    		EXECUTE_REQUEST + "Get2DImagesByURI" + DATA_INPUTS;
    
    public static final String GET_CELLS_BY_POI =
    		EXECUTE_REQUEST + "GetCellsByPOI" + DATA_INPUTS;
    
    public static final String GET_CELLS_BY_URI =
    		EXECUTE_REQUEST + "GetCellsByURI" + DATA_INPUTS;
    
    public static final String GET_CORRELATION_MAP_BY_POI =
    		EXECUTE_REQUEST + "GetCorrelationMapByPOI" + DATA_INPUTS;
    
    public static final String GET_GENES_BY_POI =
    		EXECUTE_REQUEST + "GetGenesByPOI" + DATA_INPUTS;
    
    public static final String GET_STRUCTURE_NAMES_BY_POI =
    		EXECUTE_REQUEST + "GetStructureNamesByPOI" + DATA_INPUTS;
    
    public static final String GET_TRANSFORMATION_CHAIN =
    		EXECUTE_REQUEST + "GetTransformationChain" + DATA_INPUTS;
    	
    public static final String LIST_SRS_S =
        	EXECUTE_REQUEST + "ListSRSs";          // no data inputs
    
    public static final String LIST_TRANSFORMATIONS =
    		EXECUTE_REQUEST + "ListTransformations" + DATA_INPUTS;
    
    public static final String RETRIEVE_2D_IMAGE =
    		EXECUTE_REQUEST + "Retrieve2DImage" + DATA_INPUTS;
    
    public static final String TRANSFORM_POI =
    		EXECUTE_REQUEST + "TransformPOI" + DATA_INPUTS;
    
}
