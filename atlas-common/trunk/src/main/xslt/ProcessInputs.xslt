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

<xsl:variable name="prefix" select="'http://www.incf.oef/atlas/WaxML/schema/'"/>

<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>

<xsl:template match="/">

<ProcessInputs service="WPS" version="1.0.0" xml:lang="en-US"
    xmlns:ows="http://www.opengis.net/ows/1.1"
    xmlns:wps="http://www.opengis.net/wps/1.0.0">

      <inp id="srsName">
      <Input>
        <ows:Identifier>srsName</ows:Identifier>
        <ows:Title>Atlas SRS Name</ows:Title>
        <ows:Abstract>The Atlas SRS (Spatial Reference System) name.</ows:Abstract>
<xsl:copy-of select="SrsNames/LieralData"/>
      </Input>
      </inp>

      <inp id="inputSrsName">
      <Input>
        <ows:Identifier>inputSrsName</ows:Identifier>
        <ows:Title>Input SRS Name</ows:Title>
        <ows:Abstract>The "in" or "from" SRS (Spatial Reference System) name.</ows:Abstract>
        <LiteralData>
          <ows:AllowedValues>
            <ows:Value>Mouse_ABAreference_1.0</ows:Value>
            <ows:Value>Mouse_ABAvoxel_1.0</ows:Value>
            <ows:Value>Mouse_AGEA_1.0</ows:Value>
            <ows:Value>Mouse_ABA-T26_1.0</ows:Value>
            <ows:Value>Mouse_Paxinos_1.0</ows:Value>
            <ows:Value>Mouse_WHS_0.9</ows:Value>
            <ows:Value>Mouse_WHS_1.0</ows:Value>
          </ows:AllowedValues>
        </LiteralData>
      </Input>
      </inp>

      <inp id="outputSrsName">
      <Input>
        <ows:Identifier>outputSrsName</ows:Identifier>
        <ows:Title>Output SRS Name</ows:Title>
        <ows:Abstract>The "out" or "to" SRS (Spatial Reference System) name.</ows:Abstract>
        <LiteralData>
          <ows:AllowedValues>
            <ows:Value>Mouse_ABAreference_1.0</ows:Value>
            <ows:Value>Mouse_ABAvoxel_1.0</ows:Value>
            <ows:Value>Mouse_AGEA_1.0</ows:Value>
            <ows:Value>Mouse_ABA-T26_1.0</ows:Value>
            <ows:Value>Mouse_Paxinos_1.0</ows:Value>
            <ows:Value>Mouse_WHS_0.9</ows:Value>
            <ows:Value>Mouse_WHS_1.0</ows:Value>
          </ows:AllowedValues>
        </LiteralData>
      </Input>
      </inp>

      <inp id="filter">
      <Input minOccurs="0">
        <ows:Identifier>filter</ows:Identifier>
        <ows:Title>Filter</ows:Title>
        <ows:Abstract>.</ows:Abstract>
        <LiteralData>
          <ows:AllowedValues>
            <ows:Value>maptype:coronal</ows:Value>
            <ows:Value>maptype:horizontal</ows:Value>
            <ows:Value>maptype:sagittal</ows:Value>
            <ows:Value>structureset:anatomic</ows:Value>
            <ows:Value>structureset:fine</ows:Value>
            <ows:AnyValue/>
          </ows:AllowedValues>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="vocabulary">
      <Input minOccurs="0">
        <ows:Identifier>vocabulary</ows:Identifier>
        <ows:Title>Vocabulary</ows:Title>
        <ows:Abstract>.</ows:Abstract>
        <LiteralData>
          <ows:AnyValue/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="x">
      <Input>
        <ows:Identifier>x</ows:Identifier>
        <ows:Title>X-Coordinate Value</ows:Title>
        <ows:Abstract>Value in the X dimension as defined in the SRS.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="y">
      <Input>
        <ows:Identifier>y</ows:Identifier>
        <ows:Title>Y-Coordinate Value</ows:Title>
        <ows:Abstract>Value in the Y dimension as defined in the SRS.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="z">
      <Input>
        <ows:Identifier>z</ows:Identifier>
        <ows:Title>Z-Coordinate Value</ows:Title>
        <ows:Abstract>Value in the Z dimension as defined in the SRS.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="xmin">
      <Input>
        <ows:Identifier>xmin</ows:Identifier>
        <ows:Title>Left X-Coordinate Value</ows:Title>
        <ows:Abstract>Left x-coordinate of the desired image fragment.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="xmax">
      <Input>
        <ows:Identifier>xmax</ows:Identifier>
        <ows:Title>Right X-Coordinate Value</ows:Title>
        <ows:Abstract>Right x-coordinate of the desired image fragment.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="ymin">
      <Input>
        <ows:Identifier>ymin</ows:Identifier>
        <ows:Title>Lower Y-Coordinate Value</ows:Title>
        <ows:Abstract>Lower y-coordinate of the desired image fragment.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="ymax">
      <Input>
        <ows:Identifier>ymax</ows:Identifier>
        <ows:Title>Upper Y-Coordinate Value</ows:Title>
        <ows:Abstract>Upper y-coordinate of the desired image fragment.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:double"/>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="sourceType">
      <Input>
        <ows:Identifier>sourceType</ows:Identifier>
        <ows:Title>Source Type</ows:Title>
        <ows:Abstract>Type of image source that will be queried.</ows:Abstract>
        <LiteralData>
          <ows:AllowedValues>
            <ows:Value>WMS</ows:Value>
            <ows:Value>zoomify</ows:Value>
            <ows:Value>static</ows:Value>
            <ows:AnyValue/>
          </ows:AllowedValues>
        </LiteralData>
      </Input>
      </inp>
      
      <inp id="sourceURL">
      <Input>
        <ows:Identifier>sourceURL</ows:Identifier>
        <ows:Title>Source URL</ows:Title>
        <ows:Abstract>The URL of the image whose fragment URL will be returned.</ows:Abstract>
        <LiteralData>
          <ows:DataType ows:reference="xs:anyURL"/>
        </LiteralData>
      </Input>
      </inp>
      
</ProcessInputs>

</xsl:template>
</xsl:stylesheet>
