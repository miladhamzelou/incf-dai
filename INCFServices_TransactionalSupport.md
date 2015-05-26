# How to Manage INCF Portal Resources using web services. #

# Introduction #

The INCF CS-W implementation supports getAllApprovedRecords, insertCSWRecord, updateCSWRecord and deleteCSWRecord. The Transaction insert and update requests publish or update entire documents only. The Transaction delete request is based on file identifier alone. INCFportal publisher permissions for executing Transaction requests are included in the HTTP header in the client application that submits the CS-W request. The client application's header and credentials follows HTTP Authorization standards. Below are the webservice calls:

### InsertCSWRecord ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>InsertCSWRecord</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[service]?func=insertCSWRecord&title=[title]&serviceURL=[serviceURL]&description=[description]&creator=[creator]&userID=[userID]&password=[password'>http://[host:port]/[service]?func=insertCSWRecord&amp;title=[title]&amp;serviceURL=[serviceURL]&amp;description=[description]&amp;creator=[creator]&amp;userID=[userID]&amp;password=[password</a>]</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li>title<br>
</li><li>serviceURL<br>
</li><li>description<br>
</li><li>creator<br>
</li><li>userID<br>
</li><li>password<br>
</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML format.<br>
[<a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd</a> OWS<br>
</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li><a href='http://incf-dev-local.crbs.ucsd.edu/atlas-serverside/service/CSWService?func=insertCSWRecord&title=Test%20Brain%20Atlas&serviceURL=http://incf-dev-local.crbs.ucsd.edu/central/atlas?service=WPS&request=GetCapabilities&description=Test%20Service%20for%20Brain%20Atlas&creator=Anonymous&userID=[userID]&password=[password'>http://incf-dev-local.crbs.ucsd.edu/atlas-serverside/service/CSWService?func=insertCSWRecord&amp;title=Test%20Brain%20Atlas&amp;serviceURL=http://incf-dev-local.crbs.ucsd.edu/central/atlas?service=WPS&amp;request=GetCapabilities&amp;description=Test%20Service%20for%20Brain%20Atlas&amp;creator=Anonymous&amp;userID=[userID]&amp;password=[password</a>]<br>
</td>
</tr>
</table>
<br /></li></ul>

### UpdateCSWRecord ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>UpdateCSWRecord</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[service]?func=updateCSWRecord&title=[title]&serviceURL=[serviceURL]&description=[description]&creator=[creator]&id=[id]&userID=[userID]&password=[password'>http://[host:port]/[service]?func=updateCSWRecord&amp;title=[title]&amp;serviceURL=[serviceURL]&amp;description=[description]&amp;creator=[creator]&amp;id=[id]&amp;userID=[userID]&amp;password=[password</a>]<br>
</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li>title<br>
</li><li>serviceURL<br>
</li><li>description<br>
</li><li>creator<br>
</li><li>id<br>
</li><li>userID<br>
</li><li>password<br>
</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML format.<br>
[<a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd</a> OWS<br>
</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li><a href='http://incf-dev-local.crbs.ucsd.edu/atlas-serverside/service/CSWService?func=updateCSWRecord&title=Test%20Brain%20Atlas&serviceURL=http://incf-dev-local.crbs.ucsd.edu/central/atlas?service=WPS&request=GetCapabilities&description=Test%20Service%20for%20Brain%20Atlas&creator=AnonymousYou&id=toq4dm1rpl4roaqgh7eiqfccdf&userID=[userID]&password=[password'>http://incf-dev-local.crbs.ucsd.edu/atlas-serverside/service/CSWService?func=updateCSWRecord&amp;title=Test%20Brain%20Atlas&amp;serviceURL=http://incf-dev-local.crbs.ucsd.edu/central/atlas?service=WPS&amp;request=GetCapabilities&amp;description=Test%20Service%20for%20Brain%20Atlas&amp;creator=AnonymousYou&amp;id=toq4dm1rpl4roaqgh7eiqfccdf&amp;userID=[userID]&amp;password=[password</a>]<br>
</td>
</tr>
</table>
<br /></li></ul>

### DeleteCSWRecord ###

<table cellpadding='5' cellspacing='0' border='1'>
<tr>
<th>DeleteCSWRecord</th>
</tr>
<tr>
<th align='left'>GET Request</th>
<td><a href='http://[host:port]/[service]?func=deleteCSWRecord&id=[id]&userID=[userID]&password=[password'>http://[host:port]/[service]?func=deleteCSWRecord&amp;id=[id]&amp;userID=[userID]&amp;password=[password</a>]<br>
</td>
</tr>
<tr>
<th align='left'>Description</th>
<td></td>
</tr>
<tr>
<th align='left'>Data Inputs</th>
<td>
<ul><li>id<br>
</li><li>userID<br>
</li><li>password<br>
</td>
</tr>
<tr>
<th align='left'>Normal Return</th>
<td>XML format.<br>
[<a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd</a> OWS<br>
</td>
</tr>
<tr>
<th align='left'>Error Return</th>
<td>XML exception report. <a href='http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd'>OWS Schema</a>.</td>
</tr>
<tr>
<th align='left'>Examples</th>
<td>
</li><li><a href='http://incf-dev-local.crbs.ucsd.edu/atlas-serverside/service/CSWService?func=deleteCSWRecord&id=toq4dm1rpl4roaqgh7eiqfccdf;userID=[userID]&password=[password'>http://incf-dev-local.crbs.ucsd.edu/atlas-serverside/service/CSWService?func=deleteCSWRecord&amp;id=toq4dm1rpl4roaqgh7eiqfccdf;userID=[userID]&amp;password=[password</a>]<br>
</td>
</tr>
</table>
<br />