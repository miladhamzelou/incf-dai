<wps:ProcessDescriptions service="WPS" version="1.0.0" xml:lang="en-US"
    xmlns:ows="http://www.opengis.net/ows/1.1"
    xmlns:wps="http://www.opengis.net/wps/1.0.0">
    
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>DescribeSRS</ows:Identifier>
    <ows:Title>Describe SRS</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='srsName'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>ListSRSs</ows:Identifier>
    <ows:Title>List SRS Names</ows:Title>
    <ows:Abstract></ows:Abstract>
    <!-- There are no inputs for this function. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>DescribeTransformation</ows:Identifier>
    <ows:Title>Describe Transformation</ows:Title>
    <ows:Abstract></ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>ListTransformations</ows:Identifier>
    <ows:Title>List Transformations</ows:Title>
    <ows:Abstract></ows:Abstract>
    <!-- To be determined. -->
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>TransformPOI</ows:Identifier>
    <ows:Title>Transform POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='inputSrsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='targetSrsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='x'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='y'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='z'
        return $x/*
      }
      </Input>
      <Input minOccurs="0" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='filter'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>GetTransformationChain</ows:Identifier>
    <ows:Title>Get Transformation Chain</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='inputSrsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='targetSrsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="0" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='filter'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier>TransformationChain</ows:Identifier>
        <ows:Title>Transformation Chain</ows:Title>
        <LiteralOutput>
          <ows:DataType>xs:String</ows:DataType>
        </LiteralOutput>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>Get2DImagesByPOI</ows:Identifier>
    <ows:Title>Get 2D Images by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='srsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='x'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='y'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='z'
        return $x/*
      }
      </Input>
      <Input minOccurs="0" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='filter'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>GetCorrelationMapByPOI</ows:Identifier>
    <ows:Title>Get Correlation Map by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='srsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='x'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='y'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='z'
        return $x/*
      }
      </Input>
      <Input minOccurs="0" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='filter'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>GetGenesByPOI</ows:Identifier>
    <ows:Title>Get Genes by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='srsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='x'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='y'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='z'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>GetStructureNamesByPOI</ows:Identifier>
    <ows:Title>Get Structure Names by POI</ows:Title>
    <ows:Abstract></ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='srsName'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='x'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='y'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='z'
        return $x/*
      }
      </Input>
      <Input minOccurs="0" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='vocabulary'
        return $x/*
      }
      </Input>
      <Input minOccurs="0" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='filter'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
  <ProcessDescription wps:processVersion="1.0.0">
    <ows:Identifier>Retrieve2DImage</ows:Identifier>
    <ows:Title>Retrieve 2D Image</ows:Title>
    <ows:Abstract>This function gets a URL reference to a 2D image based
      on the source URL provided and "cropped" based on the specified lower
      left (xmin, ymin) and upper right (xmax, ymax).
    </ows:Abstract>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='sourceType'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='sourceURL'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='xmin'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='xmax'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='ymin'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in $doc/ProcessInputs/Input
        where $x/@id='ymax'
        return $x/*
      }
      </Input>
    </DataInputs>
    <ProcessOutputs>
      <Output>
        <ows:Identifier></ows:Identifier>
        <ows:Title></ows:Title>
        <ows:Abstract></ows:Abstract>
      </Output>
    </ProcessOutputs>
  </ProcessDescription>
  
</wps:ProcessDescriptions>
