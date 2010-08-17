package org.incf.atlas.common.server;


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
    public static final String SERV_VER = SERVICE + VERSION;
    public static final String REQ_KEY = "&request=";
    public static final String EXEC_ID_KEY = REQ_KEY +"Execute&Identifier=";
    public static final String DATA_INPUTS = "&DataInputs={dataInputs}";
    
    // supported query strings (not all are supported at all hubs)
    public static final String GET_CAPABILITIES =
        SERVICE + REQ_KEY + "GetCapabilities";

    public static final String DESCRIBE_PROCESS =
        SERV_VER + REQ_KEY + "DescribeProcess";
    
    public static final String DESCRIBE_SRS =
        SERV_VER + EXEC_ID_KEY + "DescribeSRS" + DATA_INPUTS;
    
    public static final String DESCRIBE_TRANSFORMATION =
        SERV_VER + EXEC_ID_KEY + "DescribeTransfomation" + DATA_INPUTS;
    
    public static final String GET_2D_IMAGES_BY_POI =
        SERV_VER + EXEC_ID_KEY + "Get2DImagesByPOI" + DATA_INPUTS;
    
    public static final String GET_2D_IMAGES_BY_URI =
        SERV_VER + EXEC_ID_KEY + "Get2DImagesByPOI" + DATA_INPUTS;
    
    public static final String GET_CELLS_BY_POI =
        SERV_VER + EXEC_ID_KEY + "Get2DImagesByPOI" + DATA_INPUTS;
    
    public static final String GET_CELLS_BY_URI =
        SERV_VER + EXEC_ID_KEY + "Get2DImagesByPOI" + DATA_INPUTS;
    
    public static final String GET_CORRELATION_MAP_BY_POI =
        SERV_VER + EXEC_ID_KEY + "GetCorrelationMapByPOI" + DATA_INPUTS;
    
    public static final String GET_GENES_BY_POI =
        SERV_VER + EXEC_ID_KEY + "GetGenesByPOI" + DATA_INPUTS;
    
    public static final String GET_STRUCTURE_NAMES_BY_POI =
        SERV_VER + EXEC_ID_KEY + "GetStructureNamesByPOI" + DATA_INPUTS;
    
    public static final String GET_TRANSFORMATION_CHAIN =
        SERV_VER + EXEC_ID_KEY + "GetTransformationChain" + DATA_INPUTS;
    
    public static final String LIST_SRS_S =
        SERV_VER + EXEC_ID_KEY + "ListSRSs";          // no data inputs
    
    public static final String LIST_TRANSFORMATIONS =
        SERV_VER + EXEC_ID_KEY + "ListTransformations" + DATA_INPUTS;
    
    public static final String RETRIEVE_2D_IMAGE =
        SERV_VER + EXEC_ID_KEY + "Retrieve2DImage" + DATA_INPUTS;
    
    public static final String TRANSFORM_POI =
        SERV_VER + EXEC_ID_KEY + "TransformPOI" + DATA_INPUTS;
    
}
