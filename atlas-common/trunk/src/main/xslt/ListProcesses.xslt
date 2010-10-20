<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:variable name="url" select="'incf-dev-local.crbs.ucsd.edu:8080'"/>

<xsl:template match="/">

<ListProcessesResponse
    xmlns="http://www.incf.org/WaxML/">
  <Processes>

    <Process>
      <ProcessIdentifier>DescribeSRS</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name>atlas-aba</Name>
        </Hub>
        <Hub>
          <Name>atlas-emap</Name>
        </Hub>
        <Hub>
          <Name>atlas-ucsd</Name>
        </Hub>
        <Hub>
          <Name>atlas-whs</Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>DescribeTransformation</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>Get2DImagesByPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>Get2DImagesByURI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetCellsByPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetCellsByURI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetCorrelationMapByPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetGenesByPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetProcessesByIdentifier</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetStructureNamesByPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>GetTransformationChain</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>ListHubs</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>ListProcesses</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>ListSRSs</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>ListTransformationss</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>Retrieve2DImagesByPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

    <Process>
      <ProcessIdentifier>TransformPOI</ProcessIdentifier>
      <ImplementingHubs>
        <Hub>
          <Name></Name>
        </Hub>
      </ImplementingHubs>
    </Process>

  </Processes>
</ListProcessesResponse>

</xsl:template>
</xsl:stylesheet>
