<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">

<wps:Capabilities service="WPS" version="1.0.0" xml:lang="en-US" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:wps="http://www.opengis.net/wps/1.0.0" 
    xmlns:ows="http://www.opengis.net/ows/1.1">
  <ows:ServiceIdentification>
    <ows:Title>EMAGE Atlas Services</ows:Title>
    <ows:Abstract>EMAGE Atlas Services provide access to data available from the
      EMAGE resource.</ows:Abstract>
    <ows:ServiceType>WPS</ows:ServiceType>
    <ows:ServiceTypeVersion>1.0.0</ows:ServiceTypeVersion>
  </ows:ServiceIdentification>
  <wps:ProcessOfferings>
<xsl:for-each select="wps:ProcessDescriptions/ProcessDescription">
    <wps:Process>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="@wps:processVersion" />
</xsl:attribute>
<xsl:copy-of select="ows:Identifier"/>
<xsl:copy-of select="ows:Title"/>
<xsl:copy-of select="ows:Abstract"/>
    </wps:Process>
</xsl:for-each>
  </wps:ProcessOfferings>
  <wps:Languages>
    <wps:Default>
      <ows:Language>en-US</ows:Language>
    </wps:Default>
    <wps:Supported>
      <ows:Language>en-US</ows:Language>
    </wps:Supported>
  </wps:Languages>
</wps:Capabilities>

</xsl:template>
</xsl:stylesheet>

