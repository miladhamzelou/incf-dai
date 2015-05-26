
# INCF Atlas Server Request Interface #

## Base URIs ##

The Atlas base URIs identify the server location (URL) and the server application or context.

  * Format: http://[server]:[port]/[central-or-hub]/atlas
  * Examples:
    * http://incf-dev.crbs.ucsd.edu/central/atlas
    * http://incf-dev.crbs.ucsd.edu/aba/atlas

## Full URIs ##
  * Full Atlas URIs are consistent with, but a subset of, the OGC (Open Geospatial Consortium) WPS (Web Processing Service) standard
  * The full URI consists of
    * One of the [Base URIs](AtlasRequestInterfaceSpec#Base_URIs.md)
    * And an appended [query string](AtlasRequestInterfaceSpec#Query_String_Details.md)
  * Examples:
    * http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&request=GetCapabilities
    * http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL
    * http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_AGEA_1.0;x=6600;y=4000;z=5600;filter=maptype:coronal

## Query String Details ##

  * A query string is appended to the base URI to comprise a full Atlas URI
    * Full URI = Base URI + Query String
  * The query string begins with question mark (?)
  * Consists of key=value pairs
  * After the first key=value pair, others are ampersand-delimited (&)

### Format ###
```
?service=WPS&version={version}&request={request}&Identifier={identifier}&DataInputs={data-inputs}&lineage={true|false}
```
### Keys ###

service
  * Required
  * Value: Always WPS

version
  * Required except when request=GetCapabilities
  * Value: The version returned by the GetCapabilities request

request
  * Required
  * Values: { GetCapabilities | DescribeProcess | Execute }

Identifier
  * Required except when request=GetCapabilities
  * If request=DescribeProcess
    * Value: { the process identifier to be described | ALL }
  * If request=Execute
    * Value: the process identifier to be executed

DataInputs
  * See [Data Inputs](AtlasRequestInterfaceSpec#DataInputs.md)

lineage
  * Optional -- defaults to lineage=false
  * Values: { true | false }
  * Applicable when request=Execute
  * If lineage=true, data inputs are echoed in the response

### Data Inputs ###

  * Applicable when request=Execute
  * Run request=[DescribeProcess](AtlasRequestInterfaceSpec#DescribeProcess.md) to see the data inputs applicable to each process for each hub
  * The data inputs is a sub-query string of key=value pairs
  * The key=value pairs are semicolon-delimited (;)
  * The data inputs vary by process and hub
  * Some processes will not have any data inputs

#### Format ####
DataInputs={key-1}={value-1};{key-2}={value-2};...;{key-n}={value-n}

#### Typical Data Input Keys ####

Data inputs vary for each process and to some extent at each hub, so this section shows some common Atlas data inputs but it is not complete. See [DescribeProcess](AtlasRequestInterfaceSpec#DescribeProcess.md) for the particular hub and process to see what data input keys are appropriate and what values are acceptable.

##### SRS Names #####
  * Keys: srsName, inputSrsName, outputSrsName
  * Example values:
    * Mouse\_ABAreference\_1.0
    * Mouse\_ABAvoxel\_1.0
    * Mouse\_AGEA\_1.0
    * Mouse\_EMAP-T26\_1.0
    * Mouse\_Paxinos\_1.0
    * Mouse\_WHS\_0.9
    * Mouse\_WHS\_1.0

##### Coordinates #####
  * Keys: x, y, z, xmin, xmax, ymin, ymax
  * Example value: double

##### Filter #####
  * Key: filter
  * Example values:
    * maptype:coronal
    * maptype:horizontal
    * maptype:sagittal
    * structureset:anatomic
    * structureset:fine

## Atlas Requests/Processes ##

Standard WPS Requests
  * [GetCapabilities](AtlasRequestInterfaceSpec#GetCapabilities.md)
  * [DescribeProcess](AtlasRequestInterfaceSpec#DescribeProcess.md)

Atlas Processes
  * [DescribeSRS](AtlasRequestInterfaceSpec#DescribeSRS.md)
  * [DescribeTransformation](AtlasRequestInterfaceSpec#DescribeTransformation.md)
  * [Get2DImagesByPOI](AtlasRequestInterfaceSpec#Get2DImagesByPOI.md)
  * [GetAnnotationsByPOI](AtlasRequestInterfaceSpec#GetAnnotationsByPOI.md)
  * [GetCorrelationMap](AtlasRequestInterfaceSpec#GetCorrlationMap.md)
  * [GetGenesByPOI](AtlasRequestInterfaceSpec#GetGenesByPOI.md)
  * [AtlasRequestInterfaceSpec#GetGenesExpressionByGeneId](AtlasRequestInterfaceSpec#GetGenesExpressionByGeneId.md)
  * [GetOjbectsByPOI](AtlasRequestInterfaceSpec#GetObjectsByPOI.md)
  * [GetStructureNamesByPOI](AtlasRequestInterfaceSpec#GetStructureNamesByPOI.md)
  * [GetTransformationChain](AtlasRequestInterfaceSpec#GetTransformationChain.md)
  * [ListSRSs](AtlasRequestInterfaceSpec#ListSRSs.md)
  * [ListTransformations](AtlasRequestInterfaceSpec#ListTransformations.md)
  * [Retrieve2DImage](AtlasRequestInterfaceSpec#Retrieve2DImage.md)
  * [SetAnnotation](AtlasRequestInterfaceSpec#SetAnnotation.md)
  * [TransformPOI](AtlasRequestInterfaceSpec#TransformPOI.md)
<br />

### GetCapabilities ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetCapabilities</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&request=GetCapabilities'>http://[host:port]/[hub]/atlas?service=WPS&amp;request=GetCapabilities</a></td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central and all hubs. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>None</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://schemas.opengis.net/wps/1.0.0/wpsGetCapabilities_response.xsd'>WPS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
<ul><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&request=GetCapabilities'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;request=GetCapabilities</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&request=GetCapabilities'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;request=GetCapabilities</a>
</li><li>EMAP: <a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&request=GetCapabilities'>http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&amp;request=GetCapabilities</a>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&request=GetCapabilities'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;request=GetCapabilities</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&request=GetCapabilities'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;request=GetCapabilities</a></td>
</tr>
</table>
<br /></li></ul>

### DescribeProcess ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>DescribeProcess</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=DescribeProcess&Identifier=[process-identifier'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=DescribeProcess&amp;Identifier=[process-identifier</a> | ALL]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central and all hubs. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>None</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_response.xsd'>WPS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
<ul><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=DescribeProcess&amp;Identifier=ALL</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=DescribeProcess&amp;Identifier=ALL</a>
</li><li>EMAP: <a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&amp;version=1.0.0&amp;request=DescribeProcess&amp;Identifier=ALL</a>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=DescribeProcess&amp;Identifier=ALL</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=DescribeProcess&amp;Identifier=ALL</a></td>
</tr>
</table>
<br /></li></ul>

### DescribeSRS ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>DescribeSRS</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=[srsName'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=DescribeSRS&amp;DataInputs=srsName=[srsName</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>All hubs, but not Central. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/DescribeSrsResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
<ul><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=Mouse_ABAreference_1.0'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=DescribeSRS&amp;DataInputs=srsName=Mouse_ABAreference_1.0</a>
</li><li>EMAP: (Planned, but not yet implemented)<br>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=Mouse_Paxinos_1.0'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=DescribeSRS&amp;DataInputs=srsName=Mouse_Paxinos_1.0</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=Mouse_WHS_0.9'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=DescribeSRS&amp;DataInputs=srsName=Mouse_WHS_0.9</a></td>
</tr>
</table>
<br /></li></ul>

### DescribeTransformation ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>DescribeTransformation</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=DescribeTransformation&DataInputs=...TO'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=DescribeTransformation&amp;DataInputs=...TO</a> BE DETERMINED...</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>All hubs, but not Central. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>inputSrsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>outputSrsName</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/TransformationResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>ABA: (Planned, but not yet implemented)<br>
</li><li>EMAP: (Planned, but not yet implemented)<br>
</li><li>UCSD: (Planned, but not yet implemented)<br>
</li><li>WHS: (Planned, but not yet implemented)</td>
</tr>
</table>
<br /></li></ul>

### Get2DImagesByPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>Get2DImagesByPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=[srsName];x=[x];y=[y];z=[z];filter=[filter'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=Get2DImagesByPOI&amp;DataInputs=srsName=[srsName];x=[x];y=[y];z=[z];filter=[filter</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central, ABA, and UCSD. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a>
</li><li>and see <a href='AtlasRequestInterfaceSpec#DescribeProcess.md'>DescribeProcess</a> for additional hub-specific data inputs<br>
<ul><li><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=Get2DImagesByPOI'>Central DescribeProcess</a>
</li><li><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=Get2DImagesByPOI'>ABA DescribeProcess</a>
</li><li><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=Get2DImagesByPOI'>UCSD DescribeProcess</a>
<blockquote></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/ImagesResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</blockquote></li></ul></li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=Get2DImagesByPOI&amp;DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3</a>
</li><li>ABA: (ABA has changed their web services API without maintaining backward compatibility with their old API on which break the ABA hub implementations of Get2DIMagesByPOI)<br>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=Get2DImagesByPOI&amp;DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3</a></td>
</tr>
</table>
<br /></li></ul>

### GetAnnotationsByPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetAnnotationsByPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=[srsName];x=[x];y=[y];z=[z];tolerance=[tolerance'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=GetAnnotationsByPOI&amp;DataInputs=srsName=[srsName];x=[x];y=[y];z=[z];tolerance=[tolerance</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central and all hubs. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a>
</li><li>and see <a href='AtlasRequestInterfaceSpec#DescribeProcess.md'>DescribeProcess</a> for additional hub-specific data inputs<br>
<ul><li><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=GetAnnotationsByPOI'>Central DescribeProcess</a>
</li><li><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=GetAnnotationsByPOI'>ABA DescribeProcess</a>
</li><li><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=GetAnnotationsByPOI'>UCSD DescribeProcess</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/SetAnnotationResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li></ul></li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetAnnotationsByPOI&amp;DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetAnnotationsByPOI&amp;DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec</a>
</li><li>EMAP: <a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetAnnotationsByPOI&amp;DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec</a>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetAnnotationsByPOI&amp;DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetAnnotationsByPOI&amp;DataInputs=srsName=image;x=402;y=826;z=597;tolerance=0;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec</a></td>
</tr>
</table>
<br /></li></ul>

### GetCorrelationMapByPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetCorrelationMapByPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=inputSrsName=[inputSrsName];outputSrsName=[outputSrsName];x=[x];y=[y];z=[z];filter=[filter'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=GetCorrelationMapByPOI&amp;DataInputs=inputSrsName=[inputSrsName];outputSrsName=[outputSrsName];x=[x];y=[y];z=[z];filter=[filter</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central and ABA. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a>
</li><li><a href='AtlasRequestInterfaceSpec#Filter.md'>filter</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/CorelationMapResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetCorrelationMapByPOI&amp;DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetCorrelationMapByPOI&amp;DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal</a></td>
</tr>
</table>
<br /></li></ul>

### GetGenesByPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetGenesByPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=GetGenesByPOI&DataInputs=srsName=[srsName];x=[x];y=[y];z=[z'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=GetGenesByPOI&amp;DataInputs=srsName=[srsName];x=[x];y=[y];z=[z</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central, ABA, and EMAP. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a>
</li><li><a href='AtlasRequestInterfaceSpec#Filter.md'>filter</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/GenesResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>Central: (ABA has changed their web services API without maintaining backward compatibility with their old API on which break the ABA hub implementations of GetGenesByPOI)<br>
</li><li>ABA: (ABA has changed their web services API without maintaining backward compatibility with their old API on which break the ABA hub implementations of GetGenesByPOI)<br>
</li><li>EMAP: (Planned, but not yet implemented)</td>
</tr>
</table>
<br /></li></ul>

### GetGeneExpressionByGeneId ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetGeneExpressionByGeneId</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneIdentifier=[geneIdentifier]&RawDataOutput=SparseValueVolumeXML'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetGeneExpressionByGeneId&amp;DataInputs=geneIdentifier=[geneIdentifier]&amp;RawDataOutput=SparseValueVolumeXML</a></td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>ABA. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li>geneIdentifier: entrez gene id OR gene symbol<br>
</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://code.google.com/p/wholebrain/source/browse/wbc-core/trunk/src/main/resources/SparseValueVolume.xsd'>http://code.google.com/p/wholebrain/source/browse/wbc-core/trunk/src/main/resources/SparseValueVolume.xsd</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneIdentifier=Coch&RawDataOutput=SparseValueVolumeXML'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetGeneExpressionByGeneId&amp;DataInputs=geneIdentifier=Coch&amp;RawDataOutput=SparseValueVolumeXML</a>
</td>
</tr>
</table>
<br /></li></ul>

### GetObjectsByPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetObjectsByPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=GetObjectsByPOI&DataInputs=srsName=[srsName];x=[x];y=[y];z=[z'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=GetObjectsByPOI&amp;DataInputs=srsName=[srsName];x=[x];y=[y];z=[z</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central only. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/DescribeProcesses_central.xsd'>WaxML Schema</a>.</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetObjectsByPOI&DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetObjectsByPOI&amp;DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0</a></td>
</tr>
</table>
<br /></li></ul>

### GetStructureNamesByPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetStructureNamesByPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=[srsName];x=[x];y=[y];z=[z];vocabulary=[vocabulary];filter=[filter'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=GetStructureNamesByPOI&amp;DataInputs=srsName=[srsName];x=[x];y=[y];z=[z];vocabulary=[vocabulary];filter=[filter</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central, ABA, UCSD, and WHS. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a>
</li><li><a href='AtlasRequestInterfaceSpec#Vocabulary.md'>vocabulary</a>
</li><li><a href='AtlasRequestInterfaceSpec#Filter.md'>filter</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/StructureTermsResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=structureset:anatomic'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetStructureNamesByPOI&amp;DataInputs=srsName=Mouse_Paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=structureset:anatomic</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:fine'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetStructureNamesByPOI&amp;DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:fine</a>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=-4;y=-2.3;z=2;vocabulary=Mouse_Paxinos_1.0;filter=NONE'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetStructureNamesByPOI&amp;DataInputs=srsName=Mouse_Paxinos_1.0;x=-4;y=-2.3;z=2;vocabulary=Mouse_Paxinos_1.0;filter=NONE</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter='>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetStructureNamesByPOI&amp;DataInputs=srsName=Mouse_paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=</a></td>
</tr>
</table>
<br /></li></ul>

### GetTransformationChain ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>GetTransformationChain</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=[inputSrsName];outputSrsName=[outputSrsName];filter=[filter'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=GetTransformationChain&amp;DataInputs=inputSrsName=[inputSrsName];outputSrsName=[outputSrsName];filter=[filter</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central only. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>inputSrsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>outputSrsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Filter.md'>filter</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/CoordinateChainTransformationResponses.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_ABAreference_1.0;outputSrsName=Mouse_WHS_1.0;filter=cerebellum'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=GetTransformationChain&amp;DataInputs=inputSrsName=Mouse_ABAreference_1.0;outputSrsName=Mouse_WHS_1.0;filter=cerebellum</a></td>
</tr>
</table>
<br /></li></ul>

### ListSRSs ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>ListSRSs</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=ListSRSs'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=ListSRSs</a></td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central and all hubs. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>None</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/ListSrsResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
<ul><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListSRSs</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListSRSs</a>
</li><li>EMAP: (Planned, but not yet implemented)<br>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListSRSs</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListSRSs</a></td>
</tr>
</table>
<br /></li></ul>

### ListTransformations ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>ListTransformations</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=[inputSrsName];outputSrsName=[outputSrsName'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=ListTransformations&amp;DataInputs=inputSrsName=[inputSrsName];outputSrsName=[outputSrsName</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>Central and all hubs. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>inputSrsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>outputSrsName</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchemaListTransformationResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>Central: <a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListTransformations</a>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListTransformations</a>
</li><li>EMAP: <a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListTransformations</a>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListTransformations</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=ListTransformations</a></td>
</tr>
</table>
<br /></li></ul>

### Retrieve2DImage ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>Retrieve2DImage</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=Retrieve2DImage&DataInputs=sourceType=[sourceType];sourceURL=[sourceURL]]srsName=[srsName];xmin=[xmin];xmax=[xmax];ymin=[ymin];ymax=[ymax];filter=[filter'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=Retrieve2DImage&amp;DataInputs=sourceType=[sourceType];sourceURL=[sourceURL]]srsName=[srsName];xmin=[xmin];xmax=[xmax];ymin=[ymin];ymax=[ymax];filter=[filter</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>ABA and UCSD. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#Source_Type.md'>sourceType</a>
</li><li><a href='AtlasRequestInterfaceSpec#Source_URL.md'>sourceURL</a>
</li><li><a href='AtlasRequestInterfaceSpec#SRS_Names.md'>srsName</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>xmin</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>xmax</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>ymin</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>ymax</a>
</li><li><a href='AtlasRequestInterfaceSpec#Filter.md'>filter</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/Retrieve2DImagesResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>ABA: (Planned, but not yet implemented.)<br>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Retrieve2DImage&DataInputs=sourceType=WMS;sourceURL=http%3A%2F%2Fimage.wholebraincatalog.org%2Fcgi-bin%2Fmapserv%3Fmap%3Dcrbsatlas%2Fmapfiles%2Fgensat_3363_modified_sm_transformed-ms.map%26LAYERS%3Dgensat_penk1_09%26FORMAT%3Dpng24%26VERSION%3D1.1.1%26REQUEST%3DGetMap;srsName=Mouse_ABAreference_1.0;xmin=-1.9298;xmax=8.73376;ymin=-9.92461;ymax=1.14128;filter=NONE'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=Retrieve2DImage&amp;DataInputs=sourceType=WMS;sourceURL=http%3A%2F%2Fimage.wholebraincatalog.org%2Fcgi-bin%2Fmapserv%3Fmap%3Dcrbsatlas%2Fmapfiles%2Fgensat_3363_modified_sm_transformed-ms.map%26LAYERS%3Dgensat_penk1_09%26FORMAT%3Dpng24%26VERSION%3D1.1.1%26REQUEST%3DGetMap;srsName=Mouse_ABAreference_1.0;xmin=-1.9298;xmax=8.73376;ymin=-9.92461;ymax=1.14128;filter=NONE</a></td>
</tr>
</table>
<br /></li></ul>

### SetAnnotation ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>SetAnnotation</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=[remoteServerFilePath];filter'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=SetAnnotation&amp;DataInputs=filePath=[remoteServerFilePath];filter</a></td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>All hubs, but not Central. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li>filePath<br>
</li><li>filter<br>
</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/SetAnnotationResponse.xsd'>WaxML Schema</a>. The process-specific content of the WPS ExecuteResponse will be an empty element.</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>ABA: <a href='http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password='>http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=SetAnnotation&amp;DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password=</a>]<br>
</li><li>EMAP: <a href='http://incf-dev-local.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password='>http://incf-dev-local.crbs.ucsd.edu/emap/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=SetAnnotation&amp;DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password=</a>]<br>
</li><li>UCSD: <a href='http://incf-dev-local.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password='>http://incf-dev-local.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=SetAnnotation&amp;DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password=</a>]<br>
</li><li>WHS: <a href='http://incf-dev-local.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password='>http://incf-dev-local.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=SetAnnotation&amp;DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml;filter=[userID=:password=</a>]<br>
</td>
</tr>
</table>
<br /></li></ul>

### TransformPOI ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>TransformPOI</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=[transformationCode];x=[x];y=[y];z=[z'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=[transformationCode];x=[x];y=[y];z=[z</a>] or <a href='http://[host:port]/[hub]/atlas?service=WPS&version=[version]&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=[transformationCode];points=[points'>http://[host:port]/[hub]/atlas?service=WPS&amp;version=[version]&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=[transformationCode];points=[points</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Applicability</th>
<td>All hubs, but not Central. See <a href='AtlasFunctionHubApplicabilityTable.md'>Applicability Table</a>.</td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li><a href='AtlasRequestInterfaceSpec#Transformation_Code.md'>transformationCode</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>x</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>y</a>
</li><li><a href='AtlasRequestInterfaceSpec#Coordinates.md'>z</a></td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML document. <a href='http://incf-dai.googlecode.com/svn/waxml/trunk/AtlasXmlBeans2/src/main/xsd/WaxMlSchema/TransformationResponse.xsd'>WaxML Schema</a></td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li>ABA: <a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_ABAvoxel_1.0_To_Mouse_AGEA_1.0_v1.0;x=1;y=112;z=162'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=Mouse_ABAvoxel_1.0_To_Mouse_AGEA_1.0_v1.0;x=1;y=112;z=162</a> or<br>
<a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_ABAvoxel_1.0_To_Mouse_AGEA_1.0_v1.0;points=(281,112,162)(281,112,162'>http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=Mouse_ABAvoxel_1.0_To_Mouse_AGEA_1.0_v1.0;points=(281,112,162)(281,112,162</a>)<br>
</li><li>EMAP: <a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_WHS_1.0_To_Mouse_EMAP-T26_1.0_v1.0;x=12;y=-29;z=-73'>http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=Mouse_WHS_1.0_To_Mouse_EMAP-T26_1.0_v1.0;x=12;y=-29;z=-73</a>
</li><li>UCSD: <a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_Paxinos_1.0_To_Mouse_WHS_0.9_v1.0;x=1;y=4.3;z=1.78'>http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=Mouse_Paxinos_1.0_To_Mouse_WHS_0.9_v1.0;x=1;y=4.3;z=1.78</a>
</li><li>WHS: <a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_WHS_1.0_To_Mouse_WHS_0.9_v1.0;x=1.5;y=1;z=0.6'>http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&amp;version=1.0.0&amp;request=Execute&amp;Identifier=TransformPOI&amp;DataInputs=transformationCode=Mouse_WHS_1.0_To_Mouse_WHS_0.9_v1.0;x=1.5;y=1;z=0.6</a></td>
</tr>
</table>
<br />