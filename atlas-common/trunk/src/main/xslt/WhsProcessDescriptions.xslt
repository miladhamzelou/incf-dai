<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:variable name="ver" select="'1.0.0'"/>

<xsl:variable name="mime">
  <MimeType>application/vnd.incf.waxml</MimeType>
</xsl:variable>

<xsl:variable name="encode">
  <Encoding>UTF-8</Encoding>
</xsl:variable>

<!--<xsl:variable name="prefix" select="'http://www.incf.org/atlas/WaxML/schema/'"/>-->
<xsl:variable name="prefix" select="'http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/'"/>

<xsl:template match="/">

<wps:ProcessDescriptions service="WPS" version="1.0.0" xml:lang="en-US"
    xmlns:ows="http://www.opengis.net/ows/1.1"
    xmlns:wps="http://www.opengis.net/wps/1.0.0">
    
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>DescribeSRS</ows:Identifier>
    <ows:Title>Describe Atlas SRS</ows:Title>
    <ows:Abstract>Describes the principal Atlas SRSs (Spatial Reference Systems) supported by this server.</ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
        <ows:Identifier>srsName</ows:Identifier>
        <ows:Title>Atlas SRS Name</ows:Title>
        <ows:Abstract>The Atlas SRS (Spatial Reference System) name.</ows:Abstract>
        <LiteralData>
          <ows:AllowedValues>
            <ows:Value>Mouse_WHS_0.9</ows:Value>
            <ows:Value>Mouse_WHS_1.0</ows:Value>
          </ows:AllowedValues>
        </LiteralData>
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>DescribeSRSOutput</ows:Identifier>
        <ows:Title>DescribeSRS Output</ows:Title>
        <ows:Abstract>The response from this request describes the principal SRSs (Spatial Reference Systems) supported by this server.</ows:Abstract>
        <ComplexOutput>
          <Default>
            <Format>
              <MimeType>application/vnd.incf.waxml</MimeType>
              <Encoding>UTF-8</Encoding>
              <Schema>http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/DescribeSRSResponse.xsd</Schema>
            </Format>
          </Default>
          <Supported>
            <Format>
              <MimeType>application/vnd.incf.waxml</MimeType>
              <Encoding>UTF-8</Encoding>
              <Schema>http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/DescribeSRSResponse.xsd</Schema>
            </Format>
          </Supported>
        </ComplexOutput>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='GetStructureNamesByPOI']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='GetTransformationChain']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListSRSs']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListTransformations']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='TransformPOI']/*"/>
    
</wps:ProcessDescriptions>

</xsl:template>
</xsl:stylesheet>
