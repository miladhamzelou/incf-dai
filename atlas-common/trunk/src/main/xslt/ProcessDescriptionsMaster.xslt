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
    
  <!-- DESCRIBE SRS -->  
  <proc id="DescribeSRS">
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
  </proc>
  
  <!-- DESCRIBE TRANSFORMATIONS -->  
  <proc id="DescribeTransformation">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>DescribeTransformation</ows:Identifier>
    <ows:Title>Describe Transformation</ows:Title>
    <ows:Abstract>Describes the transformation supported by this server.</ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>DescribeTransformationsOutput</ows:Identifier>
        <ows:Title>DescribeTransformations Output</ows:Title>
        <ows:Abstract>The response from this request describes the transformations supported by this server.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>DescribeTransformationResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>DescribeTransformationResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  </proc>
  
  <!-- GET 2D IMAGES BY POI -->  
  <proc id="Get2DImagesByPOI">
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
   </proc>
  
  <!-- GET 2D IMAGES BY URI -->  
  <proc id="Get2DImagesByURI">
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
  </proc>
  
  <!-- GET CELLS BY POI -->  
  <proc id="GetCellsByPOI">
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
  </proc>
  
  <!-- GET CELLS BY URI -->  
  <proc id="GetCellsByURI">
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
  </proc>
  
  <!-- GET CORRELATION MAP BY POI -->
  <proc id="GetCorrelationMapByPOI">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>GetCorrelationMapByPOI</ows:Identifier>
    <ows:Title>Get Correlation Map by POI</ows:Title>
    <ows:Abstract>Get correlation map at a POI (point of interest) specified in the request.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>GetCorrelationMapByPOIOutput</ows:Identifier>
        <ows:Title>GetCorrelationMapByPOI Output</ows:Title>
        <ows:Abstract>The response from this request contains a correlation map at a POI (point of interest) specified in the request.</ows:Abstract>
        <ComplexOutput>
           <Default>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CorrelationMapResponse.xsd</Schema> 
             </Format>
           </Default>
           <Supported>
             <Format>
<xsl:copy-of select="$mime" />               
<xsl:copy-of select="$encode" />               
<Schema><xsl:copy-of select="$prefix"/>CorrelationMapResponse.xsd</Schema> 
             </Format>
          </Supported>
        </ComplexOutput>  
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  </proc>
 
  <!-- GET GENES BY POI -->
  <proc id="GetGenesByPOI">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>GetGenesByPOI</ows:Identifier>
    <ows:Title>Get Genes by POI</ows:Title>
    <ows:Abstract>Get genes at a POI (point of interest) specified in the request.</ows:Abstract>
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
        <ows:Identifier>GetGenesByPOIOutput</ows:Identifier>
        <ows:Title>GetGenesByPOI Output</ows:Title>
        <ows:Abstract>The response from this request contains a list of genes at a POI (point of interest) specified in the request.</ows:Abstract>
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
  </proc>
  
  <!-- GET STRUCTURE NAMES BY POI -->  
  <proc id="GetStructureNamesByPOI">
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
  </proc>
  
  <!-- GET TRANSFORMATION CHAIN -->
  <proc id="GetTransformationChain">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>GetTransformationChain</ows:Identifier>
    <ows:Title>Get Transformation Chain</ows:Title>
    <ows:Abstract>Get the chain of transformations required to transform coordinates from and to the given input and output SRS (Spatial Reference System) names.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='inputSrsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='outputSrsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>GetTransformationChainOutput</ows:Identifier>
        <ows:Title>GetTransformationChain Output</ows:Title>
        <ows:Abstract>The response from this request contains the chain of transformations required to transform coordinates from and to the given input and output SRS (Spatial Reference System) names.</ows:Abstract>
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
  </proc>
  
  <!-- LIST SRS'S -->
  <proc id="ListSRSs">
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
  </proc>
  
  <!-- LIST TRANSFORMATIONS -->
  <proc id="ListTransformations">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>ListTransformations</ows:Identifier>
    <ows:Title>List Transformations</ows:Title>
    <ows:Abstract>List coordinate transformations available at this server.</ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ListTransformationsOutput</ows:Identifier>
        <ows:Title>ListTransformations Output</ows:Title>
        <ows:Abstract>The response from this request lists the transformations available at this server.</ows:Abstract>
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
  </proc>
  
  <!-- RETRIEVE 2D IMAGE -->
  <proc id="Retrieve2DImage">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>Retrieve2DImage</ows:Identifier>
    <ows:Title>Retrieve 2D Image</ows:Title>
    <ows:Abstract>This function gets a URI reference to a 2D image based
      on the source URI provided and "cropped" based on the specified lower
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
        <ows:Identifier>Retrieve2DImageOutput</ows:Identifier>
        <ows:Title>Retrieve2DImage Output</ows:Title>
        <ows:Abstract>The response from this request returns a URI reference to 
          a 2D image based on the source URI provided and "cropped" based on 
          the specified lower left (xmin, ymin) and upper right (xmax, ymax).
        </ows:Abstract>
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
  </proc>
  
  <!-- TRANSFORM POI -->
  <proc id="TransformPOI">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>TransformPOI</ows:Identifier>
    <ows:Title>Transform POI</ows:Title>
    <ows:Abstract>Transform a POI (point of interest) from one SRS (Spatial
      Reference System) to another.</ows:Abstract>
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
        <ows:Identifier>TransformPOIOutput</ows:Identifier>
        <ows:Title>TransformPOI Output</ows:Title>
        <ows:Abstract>The response from this request returns coordinates in
          the output SRS (Spatial Reference System) that were transformed from
          the givin coordinates in the input SRS.
        </ows:Abstract>
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
  </proc>

  <!--                        -->  
  <!-- CENTRAL ONLY PROCESSES -->  
  <!--                        -->  
  
  <!-- GET PROCESSES BY IDENTIFIER -->
  <proc id="GetProcessesByIdentifier">
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
  </proc>
  
  <!-- LIST HUBS -->
  <proc id="ListHubs">
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
  </proc>
  
  <!-- LIST HUB SERVICES -->
  <proc id="ListHubServices">
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>ListHubServices</ows:Identifier>
    <ows:Title>List Hub Services</ows:Title>
    <ows:Abstract>
      List the services available at Atlas hubs.
    </ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier>ListHubServicesOutput</ows:Identifier>
        <ows:Title>ListHubServices Output</ows:Title>
        <ows:Abstract>
          The response from this request lists the services available at Atlas 
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
  </proc>
  
  <!-- LIST PROCESSES -->
  <proc id="ListProcesses">
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
  </proc>
  
</wps:ProcessDescriptions>

</xsl:template>
</xsl:stylesheet>
