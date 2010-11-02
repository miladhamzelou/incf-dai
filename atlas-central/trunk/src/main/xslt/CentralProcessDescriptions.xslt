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
    
  <!-- GET PROCESSES BY IDENTIFIER -->
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>GetProcessesByIdentifier</ows:Identifier>
    <ows:Title>Get Processes by Identifier</ows:Title>
    <ows:Abstract>
      Get processes available on some or all Atlas hubs.
    </ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>GetProcessesByIdentifierOutput</ows:Identifier>
        <ows:Title>GetProcessesByIdentifier Output</ows:Title>
        <ows:Abstract>
          The response from this request lists the transformations available at 
          some or all Atlas hubs.
        </ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Xxx.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Xxx.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <!-- LIST HUBS -->
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>ListHubs</ows:Identifier>
    <ows:Title>List Hubs</ows:Title>
    <ows:Abstract>
      List the Atlas hubs.
    </ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ListHubsOutput</ows:Identifier>
        <ows:Title>ListHubs Output</ows:Title>
        <ows:Abstract>
          The response from this request lists all the Atlas hubs.
        </ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Xxx.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Xxx.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <!-- LIST PROCESSES -->
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>ListProcesses</ows:Identifier>
    <ows:Title>List Processes</ows:Title>
    <ows:Abstract>
      List the processes available at Atlas hubs.
    </ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ListProcessesOutput</ows:Identifier>
        <ows:Title>ListProcesses Output</ows:Title>
        <ows:Abstract>
          The response from this request lists processes available at Atlas 
          hubs.
        </ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Xxx.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Xxx.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
<!--<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='Get2DImagesByPOI']/*"/>-->
<!--    -->
<!--<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='GetTransformationChain']/*"/>-->
<!--    -->
<!--<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListSRSs']/*"/>-->
<!--    -->
<!--<xsl:copy-of select="wps:ProcessDescriptions/proc[@id='ListTransformations']/*"/>-->
    
</wps:ProcessDescriptions>

</xsl:template>
</xsl:stylesheet>
