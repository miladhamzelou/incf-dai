/*
 * XML Type:  AbstractCoordinateOperationType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.AbstractCoordinateOperationType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML AbstractCoordinateOperationType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractCoordinateOperationType extends net.opengis.gml.x32.IdentifiedObjectType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AbstractCoordinateOperationType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("abstractcoordinateoperationtype422etype");
    
    /**
     * Gets the "domainOfValidity" element
     */
    net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity getDomainOfValidity();
    
    /**
     * True if has "domainOfValidity" element
     */
    boolean isSetDomainOfValidity();
    
    /**
     * Sets the "domainOfValidity" element
     */
    void setDomainOfValidity(net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity domainOfValidity);
    
    /**
     * Appends and returns a new empty "domainOfValidity" element
     */
    net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity addNewDomainOfValidity();
    
    /**
     * Unsets the "domainOfValidity" element
     */
    void unsetDomainOfValidity();
    
    /**
     * Gets array of all "scope" elements
     */
    java.lang.String[] getScopeArray();
    
    /**
     * Gets ith "scope" element
     */
    java.lang.String getScopeArray(int i);
    
    /**
     * Gets (as xml) array of all "scope" elements
     */
    org.apache.xmlbeans.XmlString[] xgetScopeArray();
    
    /**
     * Gets (as xml) ith "scope" element
     */
    org.apache.xmlbeans.XmlString xgetScopeArray(int i);
    
    /**
     * Returns number of "scope" element
     */
    int sizeOfScopeArray();
    
    /**
     * Sets array of all "scope" element
     */
    void setScopeArray(java.lang.String[] scopeArray);
    
    /**
     * Sets ith "scope" element
     */
    void setScopeArray(int i, java.lang.String scope);
    
    /**
     * Sets (as xml) array of all "scope" element
     */
    void xsetScopeArray(org.apache.xmlbeans.XmlString[] scopeArray);
    
    /**
     * Sets (as xml) ith "scope" element
     */
    void xsetScopeArray(int i, org.apache.xmlbeans.XmlString scope);
    
    /**
     * Inserts the value as the ith "scope" element
     */
    void insertScope(int i, java.lang.String scope);
    
    /**
     * Appends the value as the last "scope" element
     */
    void addScope(java.lang.String scope);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "scope" element
     */
    org.apache.xmlbeans.XmlString insertNewScope(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "scope" element
     */
    org.apache.xmlbeans.XmlString addNewScope();
    
    /**
     * Removes the ith "scope" element
     */
    void removeScope(int i);
    
    /**
     * Gets the "operationVersion" element
     */
    java.lang.String getOperationVersion();
    
    /**
     * Gets (as xml) the "operationVersion" element
     */
    org.apache.xmlbeans.XmlString xgetOperationVersion();
    
    /**
     * True if has "operationVersion" element
     */
    boolean isSetOperationVersion();
    
    /**
     * Sets the "operationVersion" element
     */
    void setOperationVersion(java.lang.String operationVersion);
    
    /**
     * Sets (as xml) the "operationVersion" element
     */
    void xsetOperationVersion(org.apache.xmlbeans.XmlString operationVersion);
    
    /**
     * Unsets the "operationVersion" element
     */
    void unsetOperationVersion();
    
    /**
     * Gets array of all "coordinateOperationAccuracy" elements
     */
    net.opengis.gml.x32.CoordinateOperationAccuracyDocument.CoordinateOperationAccuracy[] getCoordinateOperationAccuracyArray();
    
    /**
     * Gets ith "coordinateOperationAccuracy" element
     */
    net.opengis.gml.x32.CoordinateOperationAccuracyDocument.CoordinateOperationAccuracy getCoordinateOperationAccuracyArray(int i);
    
    /**
     * Returns number of "coordinateOperationAccuracy" element
     */
    int sizeOfCoordinateOperationAccuracyArray();
    
    /**
     * Sets array of all "coordinateOperationAccuracy" element
     */
    void setCoordinateOperationAccuracyArray(net.opengis.gml.x32.CoordinateOperationAccuracyDocument.CoordinateOperationAccuracy[] coordinateOperationAccuracyArray);
    
    /**
     * Sets ith "coordinateOperationAccuracy" element
     */
    void setCoordinateOperationAccuracyArray(int i, net.opengis.gml.x32.CoordinateOperationAccuracyDocument.CoordinateOperationAccuracy coordinateOperationAccuracy);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "coordinateOperationAccuracy" element
     */
    net.opengis.gml.x32.CoordinateOperationAccuracyDocument.CoordinateOperationAccuracy insertNewCoordinateOperationAccuracy(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "coordinateOperationAccuracy" element
     */
    net.opengis.gml.x32.CoordinateOperationAccuracyDocument.CoordinateOperationAccuracy addNewCoordinateOperationAccuracy();
    
    /**
     * Removes the ith "coordinateOperationAccuracy" element
     */
    void removeCoordinateOperationAccuracy(int i);
    
    /**
     * Gets the "sourceCRS" element
     */
    net.opengis.gml.x32.CRSPropertyType getSourceCRS();
    
    /**
     * True if has "sourceCRS" element
     */
    boolean isSetSourceCRS();
    
    /**
     * Sets the "sourceCRS" element
     */
    void setSourceCRS(net.opengis.gml.x32.CRSPropertyType sourceCRS);
    
    /**
     * Appends and returns a new empty "sourceCRS" element
     */
    net.opengis.gml.x32.CRSPropertyType addNewSourceCRS();
    
    /**
     * Unsets the "sourceCRS" element
     */
    void unsetSourceCRS();
    
    /**
     * Gets the "targetCRS" element
     */
    net.opengis.gml.x32.CRSPropertyType getTargetCRS();
    
    /**
     * True if has "targetCRS" element
     */
    boolean isSetTargetCRS();
    
    /**
     * Sets the "targetCRS" element
     */
    void setTargetCRS(net.opengis.gml.x32.CRSPropertyType targetCRS);
    
    /**
     * Appends and returns a new empty "targetCRS" element
     */
    net.opengis.gml.x32.CRSPropertyType addNewTargetCRS();
    
    /**
     * Unsets the "targetCRS" element
     */
    void unsetTargetCRS();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractCoordinateOperationType newInstance() {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractCoordinateOperationType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractCoordinateOperationType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractCoordinateOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
