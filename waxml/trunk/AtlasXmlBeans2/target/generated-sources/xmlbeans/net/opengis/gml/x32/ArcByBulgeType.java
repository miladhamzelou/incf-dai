/*
 * XML Type:  ArcByBulgeType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.ArcByBulgeType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML ArcByBulgeType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface ArcByBulgeType extends net.opengis.gml.x32.ArcStringByBulgeType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ArcByBulgeType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("arcbybulgetype48dbtype");
    
    /**
     * Gets array of all "pos" elements
     */
    net.opengis.gml.x32.DirectPositionType[] getPosArray();
    
    /**
     * Gets ith "pos" element
     */
    net.opengis.gml.x32.DirectPositionType getPosArray(int i);
    
    /**
     * Returns number of "pos" element
     */
    int sizeOfPosArray();
    
    /**
     * Sets array of all "pos" element
     */
    void setPosArray(net.opengis.gml.x32.DirectPositionType[] posArray);
    
    /**
     * Sets ith "pos" element
     */
    void setPosArray(int i, net.opengis.gml.x32.DirectPositionType pos);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "pos" element
     */
    net.opengis.gml.x32.DirectPositionType insertNewPos(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "pos" element
     */
    net.opengis.gml.x32.DirectPositionType addNewPos();
    
    /**
     * Removes the ith "pos" element
     */
    void removePos(int i);
    
    /**
     * Gets array of all "pointProperty" elements
     */
    net.opengis.gml.x32.PointPropertyType[] getPointPropertyArray();
    
    /**
     * Gets ith "pointProperty" element
     */
    net.opengis.gml.x32.PointPropertyType getPointPropertyArray(int i);
    
    /**
     * Returns number of "pointProperty" element
     */
    int sizeOfPointPropertyArray();
    
    /**
     * Sets array of all "pointProperty" element
     */
    void setPointPropertyArray(net.opengis.gml.x32.PointPropertyType[] pointPropertyArray);
    
    /**
     * Sets ith "pointProperty" element
     */
    void setPointPropertyArray(int i, net.opengis.gml.x32.PointPropertyType pointProperty);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "pointProperty" element
     */
    net.opengis.gml.x32.PointPropertyType insertNewPointProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "pointProperty" element
     */
    net.opengis.gml.x32.PointPropertyType addNewPointProperty();
    
    /**
     * Removes the ith "pointProperty" element
     */
    void removePointProperty(int i);
    
    /**
     * Gets array of all "pointRep" elements
     */
    net.opengis.gml.x32.PointPropertyType[] getPointRepArray();
    
    /**
     * Gets ith "pointRep" element
     */
    net.opengis.gml.x32.PointPropertyType getPointRepArray(int i);
    
    /**
     * Returns number of "pointRep" element
     */
    int sizeOfPointRepArray();
    
    /**
     * Sets array of all "pointRep" element
     */
    void setPointRepArray(net.opengis.gml.x32.PointPropertyType[] pointRepArray);
    
    /**
     * Sets ith "pointRep" element
     */
    void setPointRepArray(int i, net.opengis.gml.x32.PointPropertyType pointRep);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "pointRep" element
     */
    net.opengis.gml.x32.PointPropertyType insertNewPointRep(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "pointRep" element
     */
    net.opengis.gml.x32.PointPropertyType addNewPointRep();
    
    /**
     * Removes the ith "pointRep" element
     */
    void removePointRep(int i);
    
    /**
     * Gets array of all "bulge" elements
     */
    double[] getBulgeArray();
    
    /**
     * Gets ith "bulge" element
     */
    double getBulgeArray(int i);
    
    /**
     * Gets (as xml) array of all "bulge" elements
     */
    org.apache.xmlbeans.XmlDouble[] xgetBulgeArray();
    
    /**
     * Gets (as xml) ith "bulge" element
     */
    org.apache.xmlbeans.XmlDouble xgetBulgeArray(int i);
    
    /**
     * Returns number of "bulge" element
     */
    int sizeOfBulgeArray();
    
    /**
     * Sets array of all "bulge" element
     */
    void setBulgeArray(double[] bulgeArray);
    
    /**
     * Sets ith "bulge" element
     */
    void setBulgeArray(int i, double bulge);
    
    /**
     * Sets (as xml) array of all "bulge" element
     */
    void xsetBulgeArray(org.apache.xmlbeans.XmlDouble[] bulgeArray);
    
    /**
     * Sets (as xml) ith "bulge" element
     */
    void xsetBulgeArray(int i, org.apache.xmlbeans.XmlDouble bulge);
    
    /**
     * Inserts the value as the ith "bulge" element
     */
    void insertBulge(int i, double bulge);
    
    /**
     * Appends the value as the last "bulge" element
     */
    void addBulge(double bulge);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "bulge" element
     */
    org.apache.xmlbeans.XmlDouble insertNewBulge(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "bulge" element
     */
    org.apache.xmlbeans.XmlDouble addNewBulge();
    
    /**
     * Removes the ith "bulge" element
     */
    void removeBulge(int i);
    
    /**
     * Gets array of all "normal" elements
     */
    net.opengis.gml.x32.VectorType[] getNormalArray();
    
    /**
     * Gets ith "normal" element
     */
    net.opengis.gml.x32.VectorType getNormalArray(int i);
    
    /**
     * Returns number of "normal" element
     */
    int sizeOfNormalArray();
    
    /**
     * Sets array of all "normal" element
     */
    void setNormalArray(net.opengis.gml.x32.VectorType[] normalArray);
    
    /**
     * Sets ith "normal" element
     */
    void setNormalArray(int i, net.opengis.gml.x32.VectorType normal);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "normal" element
     */
    net.opengis.gml.x32.VectorType insertNewNormal(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "normal" element
     */
    net.opengis.gml.x32.VectorType addNewNormal();
    
    /**
     * Removes the ith "normal" element
     */
    void removeNormal(int i);
    
    /**
     * Gets the "numArc" attribute
     */
    java.math.BigInteger getNumArc();
    
    /**
     * Gets (as xml) the "numArc" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetNumArc();
    
    /**
     * True if has "numArc" attribute
     */
    boolean isSetNumArc();
    
    /**
     * Sets the "numArc" attribute
     */
    void setNumArc(java.math.BigInteger numArc);
    
    /**
     * Sets (as xml) the "numArc" attribute
     */
    void xsetNumArc(org.apache.xmlbeans.XmlInteger numArc);
    
    /**
     * Unsets the "numArc" attribute
     */
    void unsetNumArc();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static net.opengis.gml.x32.ArcByBulgeType newInstance() {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.ArcByBulgeType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ArcByBulgeType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ArcByBulgeType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ArcByBulgeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
