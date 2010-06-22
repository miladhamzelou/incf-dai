xquery version "1.0";

<wps:ProcessDescriptions service="WPS" version="1.0.0" xml:lang="en-US"
    xmlns:ows="http://www.opengis.net/ows/1.1"
    xmlns:wps="http://www.opengis.net/wps/1.0.0">
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>DescribeSRS</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>ListSRSs</ows:Identifier>
    <ows:Title>List SRS codes</ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>DescribeTransformation</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>ListTransformations</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>TransformPOI</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>GetTransformationChain</ows:Identifier>
    <ows:Title>Get Transformation Chain</ows:Title>
    <DataInputs>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in doc("atlas-aba/WEB-INF/classes/database/ProcessInputs.xml")/ProcessInputs/Input
        where $x/@id='inputSrsCode'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in doc("atlas-aba/WEB-INF/classes/database/ProcessInputs.xml")/ProcessInputs/Input
        where $x/@id='targetSrsCode'
        return $x/*
      }
      </Input>
      <Input minOccurs="1" maxOccurs="1">
      {
        for $x in doc("atlas-aba/WEB-INF/classes/database/ProcessInputs.xml")/ProcessInputs/Input
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
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>Get2DImagesByPOI</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>GetCorrelationMapByPOI</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>GetGenesByPOI</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
  <ProcessDescription wps:processVersion="">
    <ows:Identifier>GetStructureNamesByPOI</ows:Identifier>
    <ows:Title></ows:Title>
    <ows:Abstract></ows:Abstract>
  </ProcessDescription>
</wps:ProcessDescriptions>
