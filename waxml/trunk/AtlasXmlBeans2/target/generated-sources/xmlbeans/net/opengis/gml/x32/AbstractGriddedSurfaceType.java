/*
 * XML Type:  AbstractGriddedSurfaceType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.AbstractGriddedSurfaceType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML AbstractGriddedSurfaceType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractGriddedSurfaceType extends net.opengis.gml.x32.AbstractParametricCurveSurfaceType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AbstractGriddedSurfaceType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("abstractgriddedsurfacetypea877type");
    
    /**
     * Gets the "rows" element
     */
    net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows getRows();
    
    /**
     * Sets the "rows" element
     */
    void setRows(net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows rows);
    
    /**
     * Appends and returns a new empty "rows" element
     */
    net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows addNewRows();
    
    /**
     * Gets the "rows" attribute
     */
    java.math.BigInteger getRows2();
    
    /**
     * Gets (as xml) the "rows" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetRows2();
    
    /**
     * True if has "rows" attribute
     */
    boolean isSetRows2();
    
    /**
     * Sets the "rows" attribute
     */
    void setRows2(java.math.BigInteger rows2);
    
    /**
     * Sets (as xml) the "rows" attribute
     */
    void xsetRows2(org.apache.xmlbeans.XmlInteger rows2);
    
    /**
     * Unsets the "rows" attribute
     */
    void unsetRows2();
    
    /**
     * Gets the "columns" attribute
     */
    java.math.BigInteger getColumns();
    
    /**
     * Gets (as xml) the "columns" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetColumns();
    
    /**
     * True if has "columns" attribute
     */
    boolean isSetColumns();
    
    /**
     * Sets the "columns" attribute
     */
    void setColumns(java.math.BigInteger columns);
    
    /**
     * Sets (as xml) the "columns" attribute
     */
    void xsetColumns(org.apache.xmlbeans.XmlInteger columns);
    
    /**
     * Unsets the "columns" attribute
     */
    void unsetColumns();
    
    /**
     * An XML rows(@http://www.opengis.net/gml/3.2).
     *
     * This is a complex type.
     */
    public interface Rows extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Rows.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("rows816celemtype");
        
        /**
         * Gets array of all "Row" elements
         */
        net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row[] getRowArray();
        
        /**
         * Gets ith "Row" element
         */
        net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row getRowArray(int i);
        
        /**
         * Returns number of "Row" element
         */
        int sizeOfRowArray();
        
        /**
         * Sets array of all "Row" element
         */
        void setRowArray(net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row[] rowArray);
        
        /**
         * Sets ith "Row" element
         */
        void setRowArray(int i, net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row row);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Row" element
         */
        net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row insertNewRow(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Row" element
         */
        net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row addNewRow();
        
        /**
         * Removes the ith "Row" element
         */
        void removeRow(int i);
        
        /**
         * An XML Row(@http://www.opengis.net/gml/3.2).
         *
         * This is a complex type.
         */
        public interface Row extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Row.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("row6586elemtype");
            
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
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row newInstance() {
                  return (net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows.Row) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows newInstance() {
              return (net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (net.opengis.gml.x32.AbstractGriddedSurfaceType.Rows) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType newInstance() {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractGriddedSurfaceType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractGriddedSurfaceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
