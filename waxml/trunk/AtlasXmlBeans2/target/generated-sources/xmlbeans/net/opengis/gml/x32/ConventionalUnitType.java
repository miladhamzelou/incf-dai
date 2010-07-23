/*
 * XML Type:  ConventionalUnitType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.ConventionalUnitType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML ConventionalUnitType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface ConventionalUnitType extends net.opengis.gml.x32.UnitDefinitionType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConventionalUnitType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("conventionalunittypeafa9type");
    
    /**
     * Gets the "conversionToPreferredUnit" element
     */
    net.opengis.gml.x32.ConversionToPreferredUnitType getConversionToPreferredUnit();
    
    /**
     * True if has "conversionToPreferredUnit" element
     */
    boolean isSetConversionToPreferredUnit();
    
    /**
     * Sets the "conversionToPreferredUnit" element
     */
    void setConversionToPreferredUnit(net.opengis.gml.x32.ConversionToPreferredUnitType conversionToPreferredUnit);
    
    /**
     * Appends and returns a new empty "conversionToPreferredUnit" element
     */
    net.opengis.gml.x32.ConversionToPreferredUnitType addNewConversionToPreferredUnit();
    
    /**
     * Unsets the "conversionToPreferredUnit" element
     */
    void unsetConversionToPreferredUnit();
    
    /**
     * Gets the "roughConversionToPreferredUnit" element
     */
    net.opengis.gml.x32.ConversionToPreferredUnitType getRoughConversionToPreferredUnit();
    
    /**
     * True if has "roughConversionToPreferredUnit" element
     */
    boolean isSetRoughConversionToPreferredUnit();
    
    /**
     * Sets the "roughConversionToPreferredUnit" element
     */
    void setRoughConversionToPreferredUnit(net.opengis.gml.x32.ConversionToPreferredUnitType roughConversionToPreferredUnit);
    
    /**
     * Appends and returns a new empty "roughConversionToPreferredUnit" element
     */
    net.opengis.gml.x32.ConversionToPreferredUnitType addNewRoughConversionToPreferredUnit();
    
    /**
     * Unsets the "roughConversionToPreferredUnit" element
     */
    void unsetRoughConversionToPreferredUnit();
    
    /**
     * Gets array of all "derivationUnitTerm" elements
     */
    net.opengis.gml.x32.DerivationUnitTermType[] getDerivationUnitTermArray();
    
    /**
     * Gets ith "derivationUnitTerm" element
     */
    net.opengis.gml.x32.DerivationUnitTermType getDerivationUnitTermArray(int i);
    
    /**
     * Returns number of "derivationUnitTerm" element
     */
    int sizeOfDerivationUnitTermArray();
    
    /**
     * Sets array of all "derivationUnitTerm" element
     */
    void setDerivationUnitTermArray(net.opengis.gml.x32.DerivationUnitTermType[] derivationUnitTermArray);
    
    /**
     * Sets ith "derivationUnitTerm" element
     */
    void setDerivationUnitTermArray(int i, net.opengis.gml.x32.DerivationUnitTermType derivationUnitTerm);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "derivationUnitTerm" element
     */
    net.opengis.gml.x32.DerivationUnitTermType insertNewDerivationUnitTerm(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "derivationUnitTerm" element
     */
    net.opengis.gml.x32.DerivationUnitTermType addNewDerivationUnitTerm();
    
    /**
     * Removes the ith "derivationUnitTerm" element
     */
    void removeDerivationUnitTerm(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static net.opengis.gml.x32.ConventionalUnitType newInstance() {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.ConventionalUnitType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ConventionalUnitType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ConventionalUnitType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ConventionalUnitType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
