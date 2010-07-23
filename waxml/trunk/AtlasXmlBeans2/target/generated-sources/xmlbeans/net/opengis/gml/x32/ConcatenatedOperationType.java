/*
 * XML Type:  ConcatenatedOperationType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.ConcatenatedOperationType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML ConcatenatedOperationType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface ConcatenatedOperationType extends net.opengis.gml.x32.AbstractCoordinateOperationType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConcatenatedOperationType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("concatenatedoperationtype8283type");
    
    /**
     * Gets array of all "coordOperation" elements
     */
    net.opengis.gml.x32.CoordinateOperationPropertyType[] getCoordOperationArray();
    
    /**
     * Gets ith "coordOperation" element
     */
    net.opengis.gml.x32.CoordinateOperationPropertyType getCoordOperationArray(int i);
    
    /**
     * Returns number of "coordOperation" element
     */
    int sizeOfCoordOperationArray();
    
    /**
     * Sets array of all "coordOperation" element
     */
    void setCoordOperationArray(net.opengis.gml.x32.CoordinateOperationPropertyType[] coordOperationArray);
    
    /**
     * Sets ith "coordOperation" element
     */
    void setCoordOperationArray(int i, net.opengis.gml.x32.CoordinateOperationPropertyType coordOperation);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "coordOperation" element
     */
    net.opengis.gml.x32.CoordinateOperationPropertyType insertNewCoordOperation(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "coordOperation" element
     */
    net.opengis.gml.x32.CoordinateOperationPropertyType addNewCoordOperation();
    
    /**
     * Removes the ith "coordOperation" element
     */
    void removeCoordOperation(int i);
    
    /**
     * Gets the "aggregationType" attribute
     */
    net.opengis.gml.x32.AggregationType.Enum getAggregationType();
    
    /**
     * Gets (as xml) the "aggregationType" attribute
     */
    net.opengis.gml.x32.AggregationType xgetAggregationType();
    
    /**
     * True if has "aggregationType" attribute
     */
    boolean isSetAggregationType();
    
    /**
     * Sets the "aggregationType" attribute
     */
    void setAggregationType(net.opengis.gml.x32.AggregationType.Enum aggregationType);
    
    /**
     * Sets (as xml) the "aggregationType" attribute
     */
    void xsetAggregationType(net.opengis.gml.x32.AggregationType aggregationType);
    
    /**
     * Unsets the "aggregationType" attribute
     */
    void unsetAggregationType();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static net.opengis.gml.x32.ConcatenatedOperationType newInstance() {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ConcatenatedOperationType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ConcatenatedOperationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
