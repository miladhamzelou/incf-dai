/*
 * XML Type:  BSplineType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.BSplineType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML BSplineType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface BSplineType extends net.opengis.gml.x32.AbstractCurveSegmentType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(BSplineType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("bsplinetypef768type");
    
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
     * Gets the "posList" element
     */
    net.opengis.gml.x32.DirectPositionListType getPosList();
    
    /**
     * True if has "posList" element
     */
    boolean isSetPosList();
    
    /**
     * Sets the "posList" element
     */
    void setPosList(net.opengis.gml.x32.DirectPositionListType posList);
    
    /**
     * Appends and returns a new empty "posList" element
     */
    net.opengis.gml.x32.DirectPositionListType addNewPosList();
    
    /**
     * Unsets the "posList" element
     */
    void unsetPosList();
    
    /**
     * Gets the "coordinates" element
     */
    net.opengis.gml.x32.CoordinatesType getCoordinates();
    
    /**
     * True if has "coordinates" element
     */
    boolean isSetCoordinates();
    
    /**
     * Sets the "coordinates" element
     */
    void setCoordinates(net.opengis.gml.x32.CoordinatesType coordinates);
    
    /**
     * Appends and returns a new empty "coordinates" element
     */
    net.opengis.gml.x32.CoordinatesType addNewCoordinates();
    
    /**
     * Unsets the "coordinates" element
     */
    void unsetCoordinates();
    
    /**
     * Gets the "degree" element
     */
    java.math.BigInteger getDegree();
    
    /**
     * Gets (as xml) the "degree" element
     */
    org.apache.xmlbeans.XmlNonNegativeInteger xgetDegree();
    
    /**
     * Sets the "degree" element
     */
    void setDegree(java.math.BigInteger degree);
    
    /**
     * Sets (as xml) the "degree" element
     */
    void xsetDegree(org.apache.xmlbeans.XmlNonNegativeInteger degree);
    
    /**
     * Gets array of all "knot" elements
     */
    net.opengis.gml.x32.KnotPropertyType[] getKnotArray();
    
    /**
     * Gets ith "knot" element
     */
    net.opengis.gml.x32.KnotPropertyType getKnotArray(int i);
    
    /**
     * Returns number of "knot" element
     */
    int sizeOfKnotArray();
    
    /**
     * Sets array of all "knot" element
     */
    void setKnotArray(net.opengis.gml.x32.KnotPropertyType[] knotArray);
    
    /**
     * Sets ith "knot" element
     */
    void setKnotArray(int i, net.opengis.gml.x32.KnotPropertyType knot);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "knot" element
     */
    net.opengis.gml.x32.KnotPropertyType insertNewKnot(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "knot" element
     */
    net.opengis.gml.x32.KnotPropertyType addNewKnot();
    
    /**
     * Removes the ith "knot" element
     */
    void removeKnot(int i);
    
    /**
     * Gets the "interpolation" attribute
     */
    net.opengis.gml.x32.CurveInterpolationType.Enum getInterpolation();
    
    /**
     * Gets (as xml) the "interpolation" attribute
     */
    net.opengis.gml.x32.CurveInterpolationType xgetInterpolation();
    
    /**
     * True if has "interpolation" attribute
     */
    boolean isSetInterpolation();
    
    /**
     * Sets the "interpolation" attribute
     */
    void setInterpolation(net.opengis.gml.x32.CurveInterpolationType.Enum interpolation);
    
    /**
     * Sets (as xml) the "interpolation" attribute
     */
    void xsetInterpolation(net.opengis.gml.x32.CurveInterpolationType interpolation);
    
    /**
     * Unsets the "interpolation" attribute
     */
    void unsetInterpolation();
    
    /**
     * Gets the "isPolynomial" attribute
     */
    boolean getIsPolynomial();
    
    /**
     * Gets (as xml) the "isPolynomial" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetIsPolynomial();
    
    /**
     * True if has "isPolynomial" attribute
     */
    boolean isSetIsPolynomial();
    
    /**
     * Sets the "isPolynomial" attribute
     */
    void setIsPolynomial(boolean isPolynomial);
    
    /**
     * Sets (as xml) the "isPolynomial" attribute
     */
    void xsetIsPolynomial(org.apache.xmlbeans.XmlBoolean isPolynomial);
    
    /**
     * Unsets the "isPolynomial" attribute
     */
    void unsetIsPolynomial();
    
    /**
     * Gets the "knotType" attribute
     */
    net.opengis.gml.x32.KnotTypesType.Enum getKnotType();
    
    /**
     * Gets (as xml) the "knotType" attribute
     */
    net.opengis.gml.x32.KnotTypesType xgetKnotType();
    
    /**
     * True if has "knotType" attribute
     */
    boolean isSetKnotType();
    
    /**
     * Sets the "knotType" attribute
     */
    void setKnotType(net.opengis.gml.x32.KnotTypesType.Enum knotType);
    
    /**
     * Sets (as xml) the "knotType" attribute
     */
    void xsetKnotType(net.opengis.gml.x32.KnotTypesType knotType);
    
    /**
     * Unsets the "knotType" attribute
     */
    void unsetKnotType();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static net.opengis.gml.x32.BSplineType newInstance() {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static net.opengis.gml.x32.BSplineType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.BSplineType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.BSplineType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.BSplineType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.BSplineType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.BSplineType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.BSplineType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.BSplineType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.BSplineType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
