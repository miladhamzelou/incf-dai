/*
 * XML Type:  AbstractGMLType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.AbstractGMLType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML AbstractGMLType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractGMLType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AbstractGMLType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("abstractgmltype6757type");
    
    /**
     * Gets array of all "metaDataProperty" elements
     */
    net.opengis.gml.x32.MetaDataPropertyType[] getMetaDataPropertyArray();
    
    /**
     * Gets ith "metaDataProperty" element
     */
    net.opengis.gml.x32.MetaDataPropertyType getMetaDataPropertyArray(int i);
    
    /**
     * Returns number of "metaDataProperty" element
     */
    int sizeOfMetaDataPropertyArray();
    
    /**
     * Sets array of all "metaDataProperty" element
     */
    void setMetaDataPropertyArray(net.opengis.gml.x32.MetaDataPropertyType[] metaDataPropertyArray);
    
    /**
     * Sets ith "metaDataProperty" element
     */
    void setMetaDataPropertyArray(int i, net.opengis.gml.x32.MetaDataPropertyType metaDataProperty);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "metaDataProperty" element
     */
    net.opengis.gml.x32.MetaDataPropertyType insertNewMetaDataProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "metaDataProperty" element
     */
    net.opengis.gml.x32.MetaDataPropertyType addNewMetaDataProperty();
    
    /**
     * Removes the ith "metaDataProperty" element
     */
    void removeMetaDataProperty(int i);
    
    /**
     * Gets the "description" element
     */
    net.opengis.gml.x32.StringOrRefType getDescription();
    
    /**
     * True if has "description" element
     */
    boolean isSetDescription();
    
    /**
     * Sets the "description" element
     */
    void setDescription(net.opengis.gml.x32.StringOrRefType description);
    
    /**
     * Appends and returns a new empty "description" element
     */
    net.opengis.gml.x32.StringOrRefType addNewDescription();
    
    /**
     * Unsets the "description" element
     */
    void unsetDescription();
    
    /**
     * Gets the "descriptionReference" element
     */
    net.opengis.gml.x32.ReferenceType getDescriptionReference();
    
    /**
     * True if has "descriptionReference" element
     */
    boolean isSetDescriptionReference();
    
    /**
     * Sets the "descriptionReference" element
     */
    void setDescriptionReference(net.opengis.gml.x32.ReferenceType descriptionReference);
    
    /**
     * Appends and returns a new empty "descriptionReference" element
     */
    net.opengis.gml.x32.ReferenceType addNewDescriptionReference();
    
    /**
     * Unsets the "descriptionReference" element
     */
    void unsetDescriptionReference();
    
    /**
     * Gets the "identifier" element
     */
    net.opengis.gml.x32.CodeWithAuthorityType getIdentifier();
    
    /**
     * True if has "identifier" element
     */
    boolean isSetIdentifier();
    
    /**
     * Sets the "identifier" element
     */
    void setIdentifier(net.opengis.gml.x32.CodeWithAuthorityType identifier);
    
    /**
     * Appends and returns a new empty "identifier" element
     */
    net.opengis.gml.x32.CodeWithAuthorityType addNewIdentifier();
    
    /**
     * Unsets the "identifier" element
     */
    void unsetIdentifier();
    
    /**
     * Gets array of all "name" elements
     */
    net.opengis.gml.x32.CodeType[] getNameArray();
    
    /**
     * Gets ith "name" element
     */
    net.opengis.gml.x32.CodeType getNameArray(int i);
    
    /**
     * Returns number of "name" element
     */
    int sizeOfNameArray();
    
    /**
     * Sets array of all "name" element
     */
    void setNameArray(net.opengis.gml.x32.CodeType[] nameArray);
    
    /**
     * Sets ith "name" element
     */
    void setNameArray(int i, net.opengis.gml.x32.CodeType name);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "name" element
     */
    net.opengis.gml.x32.CodeType insertNewName(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "name" element
     */
    net.opengis.gml.x32.CodeType addNewName();
    
    /**
     * Removes the ith "name" element
     */
    void removeName(int i);
    
    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();
    
    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();
    
    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);
    
    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractGMLType newInstance() {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractGMLType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.AbstractGMLType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.AbstractGMLType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGMLType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractGMLType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractGMLType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractGMLType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
