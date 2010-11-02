<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xalan">
    
<xsl:variable name="url" select="'incf-dev-local.crbs.ucsd.edu:8080'"/>

<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>

<xsl:template match="/">

<GetProcessesByIdentifierResponse
    xmlns="http://www.incf.org/WaxML/">
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
</GetProcessesByIdentifierResponse>

</xsl:template>
</xsl:stylesheet>
