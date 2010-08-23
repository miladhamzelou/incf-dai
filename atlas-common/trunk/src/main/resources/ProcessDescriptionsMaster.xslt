<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:variable name="ver" select="'1.0.0'"/>

<xsl:variable name="mime">
  <MimeType>application/vnd.incf.waxml</MimeType>
</xsl:variable>

<xsl:variable name="encode">
  <Encoding>UTF-8</Encoding>
</xsl:variable>

<xsl:variable name="prefix" select="'http://www.incf.oef/atlas/WaxML/schema/'"/>

<xsl:template match="/">

<wps:ProcessDescriptions service="WPS" version="1.0.0" xml:lang="en-US"
    xmlns:ows="http://www.opengis.net/ows/1.1"
    xmlns:wps="http://www.opengis.net/wps/1.0.0">
    
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>DescribeSRS</ows:Identifier>
    <ows:Title>Describe SRS</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
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
    <ows:Identifier>DescribeTransformations</ows:Identifier>
    <ows:Title>Describe Transformations</ows:Title>
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
  
  <ProcessDescription>
<xsl:attribute name="wps:processVersion">
  <xsl:value-of select="$ver" />
</xsl:attribute>
    <ows:Identifier>Get2DImagesByPOI</ows:Identifier>
    <ows:Title>Get 2D Images by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
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
    <ows:Identifier>Images2DByURI</ows:Identifier>
    <ows:Title>2D Images by URI Response</ows:Title>
    <ows:Abstract>2D Image(s) by URI response.</ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
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
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>CellsByPOI</ows:Identifier>
        <ows:Title>Cells by POI Response</ows:Title>
        <ows:Abstract>Cell(s) by POI response.</ows:Abstract>
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
    <ows:Title>Get 2D Images by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='filter']/*"/>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>CellsByURI</ows:Identifier>
        <ows:Title>Cells by URI Response</ows:Title>
        <ows:Abstract>Cells(s) by URI response.</ows:Abstract>
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
    <ows:Identifier>GetCorrelationMapByPOI</ows:Identifier>
    <ows:Title>Get Correlation Map by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
<xsl:copy-of select="ProcessInputs/inp[@id='srsName']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='x']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='y']/*"/>
<xsl:copy-of select="ProcessInputs/inp[@id='z']/*"/>
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
    <ows:Identifier>GetStructureNamesByPOI</ows:Identifier>
    <ows:Title>Get Structure Names by POI</ows:Title>
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
        <ows:Identifier>ImageURL</ows:Identifier>
        <ows:Title>2D Image at POI result</ows:Title>
        <ows:Abstract>2D Image at POI result</ows:Abstract>
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
    <ows:Abstract></ows:Abstract>
    <!-- There are no inputs for this function. -->
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
