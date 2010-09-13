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
    
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>DescribeSRS</ows:Identifier>
    <ows:Title>Describe Atlas SRS</ows:Title>
    <ows:Abstract>Describes the principal Atlas SRSs (Spatial Reference Systems) supported by this server.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>DescribeSRSOutput</ows:Identifier>
        <ows:Title>DescribeSRS Output</ows:Title>
        <ows:Abstract>The response from this request describes the principal SRSs (Spatial Reference Systems) supported by this server.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>DescribeSRSResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>DescribeSRSResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>GetGenesByPOI</ows:Identifier>
    <ows:Title>Get Genes by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='vocabulary']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>GenesAtPOI</ows:Identifier>
        <ows:Title>Genes at POI result</ows:Title>
        <ows:Abstract>Genes at POI result</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>GenesResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>StructureTermsResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>GetTransformationChain</ows:Identifier>
    <ows:Title>Get Transformation Chain</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='inputSrsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='outputSrsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ImageURL</ows:Identifier>
        <ows:Title>2D Image at POI result</ows:Title>
        <ows:Abstract>2D Image at POI result</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CoordinateChainTransformationResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CoordinateChainTransformationResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>ListSRSs</ows:Identifier>
    <ows:Title>List SRS Names</ows:Title>
    <ows:Abstract>List the SRSs (Spatial Reference Systems) supported at this server.</ows:Abstract>
    <!-- There are no inputs for this function. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ListSRSsOutput</ows:Identifier>
        <ows:Title>ListSRSs Output</ows:Title>
        <ows:Abstract>The response from this request lists the SRSs (Spatial Reference Systems) supported at this server.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ListSrsResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ListSrsResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>ListTransformations</ows:Identifier>
    <ows:Title>List Transformations</ows:Title>
    <ows:Abstract></ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ImageURL</ows:Identifier>
        <ows:Title>2D Image at POI result</ows:Title>
        <ows:Abstract>2D Image at POI result</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ListTransformation.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ListTransformation.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>TransformPOI</ows:Identifier>
    <ows:Title>Transform POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='inputSrsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='outputSrsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>TransformationResponse</ows:Identifier>
        <ows:Title>Transformation Response</ows:Title>
        <ows:Abstract>The transformation of input SRS coordinates to output SRS coordinates.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>TransformationResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>TransformationResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
</wps:ProcessDescriptions>

</xsl:template>
</xsl:stylesheet>
