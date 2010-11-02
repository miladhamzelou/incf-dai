<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xalan">

<xsl:variable name="ver" select="'1.0.0'"/>

<xsl:variable name="mime">
  <MimeType>application/vnd.incf.waxml</MimeType>
</xsl:variable>

<xsl:variable name="encode">
  <Encoding>UTF-8</Encoding>
</xsl:variable>

<!--<xsl:variable name="prefix" select="'http://www.incf.org/atlas/WaxML/schema/'"/>-->
<xsl:variable name="prefix" select="'http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/'"/>

<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>

<xsl:template match="/">

<wps:ProcessDescriptions service="WPS" version="1.0.0" xml:lang="en-US"
    xmlns:ows="http://www.opengis.net/ows/1.1"
    xmlns:wps="http://www.opengis.net/wps/1.0.0">
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='GetProcessesByIdentifier']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListHubs']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListHubServices']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListProcesses']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='Get2DImagesByPOI']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='GetTransformationChain']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListSRSs']/*"/>
    
<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListTransformations']/*"/>
    
</wps:ProcessDescriptions>

</xsl:template>
</xsl:stylesheet>
