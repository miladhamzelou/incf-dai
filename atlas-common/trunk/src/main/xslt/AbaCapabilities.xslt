<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xalan">

<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>

<xsl:template match="/">

<wps:Capabilities service="WPS" version="1.0.0" xml:lang="en-US" 
    xmlns:wps="http://www.opengis.net/wps/1.0.0" 
    xmlns:ows="http://www.opengis.net/ows/2.0" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 
        http://http://schemas.opengis.net/wps/1.0.0/wpsGetCapabilities_response.xsd">
<xsl:copy-of select="document('src/main/xml/HubServiceIdentifications.xml')/HubServiceIdentifications/serviceId[@id='aba']/*"/>
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
