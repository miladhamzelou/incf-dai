<wps:Capabilities service="WPS" version="0.0.0" xml:lang="en-US" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:wps="http://www.opengis.net/wps/1.0.0" 
    xmlns:ows="http://www.opengis.net/ows/1.1">
  <ows:ServiceIdentification>
    <ows:Title>ABA Atlas Services</ows:Title>
    <ows:Abstract>ABA Atlas Services provide access to data available from the
      Allen Brain Atlas resource.</ows:Abstract>
    <ows:ServiceType>WPS</ows:ServiceType>
    <ows:ServiceTypeVersion>1.0.0</ows:ServiceTypeVersion>
  </ows:ServiceIdentification>
  <wps:ProcessOfferings>
    {
      for $pd in doc("file:///usr/local/tomcat/webapps/atlas-aba/WEB-INF/classes/database/ProcessDescriptions.xq")/wps:ProcessDescriptions/ProcessDescription
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
