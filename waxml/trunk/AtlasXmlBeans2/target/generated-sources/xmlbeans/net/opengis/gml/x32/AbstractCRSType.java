/*
 * XML Type:  AbstractCRSType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.AbstractCRSType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML AbstractCRSType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractCRSType extends net.opengis.gml.x32.IdentifiedObjectType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AbstractCRSType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("abstractcrstypea0f9type");
    
    /**
     * Gets array of all "domainOfValidity" elements
     */
    net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity[] getDomainOfValidityArray();
    
    /**
     * Gets ith "domainOfValidity" element
     */
    net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity getDomainOfValidityArray(int i);
    
    /**
     * Returns number of "domainOfValidity" element
     */
    int sizeOfDomainOfValidityArray();
    
    /**
     * Sets array of all "domainOfValidity" element
     */
    void setDomainOfValidityArray(net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity[] domainOfValidityArray);
    
    /**
     * Sets ith "domainOfValidity" element
     */
    void setDomainOfValidityArray(int i, net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity domainOfValidity);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "domainOfValidity" element
     */
    net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity insertNewDomainOfValidity(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "domainOfValidity" element
     */
    net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity addNewDomainOfValidity();
    
    /**
     * Removes the ith "domainOfValidity" element
     */
    void removeDomainOfValidity(int i);
    
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
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractCRSType newInstance() {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractCRSType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.AbstractCRSType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.AbstractCRSType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCRSType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractCRSType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractCRSType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractCRSType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
