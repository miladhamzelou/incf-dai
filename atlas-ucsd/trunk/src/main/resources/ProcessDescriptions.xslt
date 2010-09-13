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
    <ows:Identifier>Get2DImagesByPOI</ows:Identifier>
    <ows:Title>Get 2D Images by POI</ows:Title>
    <ows:Abstract>Get 2D images at a POI (point of interest) specified in the request.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>Get2DImagesByPOIOutput</ows:Identifier>
        <ows:Title>Get2DImagesByPOI Output</ows:Title>
        <ows:Abstract>The response from this request contains URIs to images at the specified POI (point of interest).</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ImagesResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ImagesResponse.xsd</Schema> 
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
    <ows:Identifier>Get2DImagesByURI</ows:Identifier>
    <ows:Title>Get 2D Images by URI</ows:Title>
    <ows:Abstract>Get 2D images with the URI specified in the request.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>Get2DImagesByURIOutput</ows:Identifier>
        <ows:Title>Get2DImagesByURI Output</ows:Title>
        <ows:Abstract>The response from this request contains URIs to 2D images with the specified URI.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ImagesResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>ImagesResponse.xsd</Schema> 
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
    <ows:Identifier>GetCellsByPOI</ows:Identifier>
    <ows:Title>Get Cells by POI</ows:Title>
    <ows:Abstract>Get cells at a POI (point of interest) specified in the request.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>GetCellsByPOIOutput</ows:Identifier>
        <ows:Title>GetCellsByPOI Output</ows:Title>
        <ows:Abstract>The response from this request contains URIs to cells at the specified POI (point of interest).</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CellsResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CellsResponse.xsd</Schema> 
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
    <ows:Identifier>GetCellsByURI</ows:Identifier>
    <ows:Title>Get Cells by URI</ows:Title>
    <ows:Abstract>Get cells with the URI specified in the request.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>GetCellsByURIOutput</ows:Identifier>
        <ows:Title>GetCellsByURI Output</ows:Title>
        <ows:Abstract>The response from this request contains URIs to cells with the specified URI.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CellsByUriResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CellsByUriResponse.xsd</Schema> 
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
    <ows:Identifier>GetStructureNamesByPOI</ows:Identifier>
    <ows:Title>Get Structure Names by POI</ows:Title>
    <ows:Abstract>Get structure names at a POI (point of interest) specified in the request.</ows:Abstract>
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
        <ows:Identifier>GetStructureNamesByPOIOutput</ows:Identifier>
        <ows:Title>GetStructureNamesByPOI Output</ows:Title>
        <ows:Abstract>The response from this request contains structure names at the specified POI (point of interest).</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>StructureTermsResponse.xsd</Schema> 
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
    <ows:Identifier>Retrieve2DImage</ows:Identifier>
    <ows:Title>Retrieve 2D Image</ows:Title>
    <ows:Abstract>This function gets a URL reference to a 2D image based
      on the source URL provided and "cropped" based on the specified lower
      left (xmin, ymin) and upper right (xmax, ymax).
    </ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='sourceType']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='sourceURL']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='xmin']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='xmax']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='ymin']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='ymax']/*"/>
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
<Schema><xsl:copy-of select="$prefix"/>Retrieve2DImagesResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>Retrieve2DImagesResponse.xsd</Schema> 
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
