<wps:Capabilities service="WPS" version="1.0.0" xml:lang="en-US" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:wps="http://www.opengis.net/wps/1.0.0" 
    xmlns:ows="http://www.opengis.net/ows/1.1">
  <ows:ServiceIdentification>
    <ows:Title>UCSD Atlas Services</ows:Title>
    <ows:Abstract>UCSD Atlas Services provide access to data available from the
      University of California, San Diego resource.</ows:Abstract>
    <ows:ServiceType>WPS</ows:ServiceType>
    <ows:ServiceTypeVersion>1.0.0</ows:ServiceTypeVersion>
  </ows:ServiceIdentification>
  <wps:ProcessOfferings>
    {
      for $pd in $doc/wps:ProcessDescriptions/ProcessDescription
      return 
        <wps:Process wps:processVersion="{$pd/@wps:processVersion}"> {
          $pd/ows:Identifier | 
          $pd/ows:Title | 
          $pd/ows:Abstract }
        </wps:Process>
    }
  </wps:ProcessOfferings>
  <wps:Languages>
    <wps:Default>
      <ows:Language>en-US</ows:Language>
    </wps:Default>
    <wps:Supported>
      <ows:Language>en-US</ows:Language>
    </wps:Supported>
  </wps:Languages>
</wps:Capabilities>
