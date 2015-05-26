# Data Inputs #

The purpose of this page is to describe how string-valued data inputs in an INCF Atlas hub HTTP GET request are to be
  * specified in DescribeProcess
  * used in the query string of a GET request
  * processed within an Atlas process implementation

This page presents a combination of information some of which is designed for Atlas process developers (superscript d) and some for Atlas users (superscript u).

The combinations of required v. optional, allowed values v. any value, and default value Data Inputs are described in the cases below.

In each case, the specification in the Deegree ProcessDefinition, the response from WPS DescribeProcess, and the INCF Atlas implementation is shown with examples.

## Case 1: Required Input, Any Non-Null Value is Accepted ##

ProcessDefinition File (Deegree .../processes/Xxx.xml)<sup>d</sup>:
```
<LiteralInput>
  <Identifier>srsName</Identifier>
  <Title>Atlas SRS Name</Title>
  <Abstract>The Atlas SRS (Spatial Reference System) name.</Abstract>
</LiteralInput>
```

WPS DescribeProcess response<sup>u</sup>:
```
<Input minOccurs="1" maxOccurs="1">
  <ows:Identifier>srsName</ows:Identifier>
  <ows:Title>Atlas SRS Name</ows:Title>
  <ows:Abstract>The Atlas SRS (Spatial Reference System) name.</ows:Abstract>
  <LiteralData>
    <ows:AnyValue/>
  </LiteralData>
</Input>
```

The INCF Atlas implementation:
<table cellpadding='5' cellspacing='0' border='1'>
<blockquote><tr>
<blockquote><th>GET DataInput<sup>u</sup></th>
<th>Received value<sup>d</sup></th>
<th>Program action<sup>d</sup></th>
<th>Value used in Atlas<sup>u</sup></th>
</blockquote></tr>
<tr>
<blockquote><td>srsName=Mouse_AGEA_1.0;</td>
<td>Mouse_AGEA_1.0</td>
<td>Accept value</td>
<td>Mouse_AGEA_1.0</td>
</blockquote></tr>
<tr>
<blockquote><td>srsName=abcde;</td>
<td>abcde</td>
<td>Accept value</td>
<td>abcde</td>
</blockquote></tr>
<tr>
<blockquote><td>srsName=; (empty value)</td>
<td>null</td>
<td>Throw exception</td>
<td>EXCEPTION: A value is required</td>
</blockquote></tr>
<tr>
<blockquote><td>(DataInput ommitted)</td>
<td>none</td>
<td>none (handled by Deegree)</td>
<td>EXCEPTION: Required data input</td>
</blockquote></tr>
</table></blockquote>

## Case 2: Required Input, Any of Several Allowed Values is Accepted ##

ProcessDefinition File (Deegree .../processes/Xxx.xml)<sup>d</sup>:
```
<LiteralInput>
  <Identifier>filter</Identifier>
  <Title>Filter</Title>
  <Abstract></Abstract>
  <AllowedValues>
    <Value>maptype:coronal</Value>
    <Value>maptype:sagittal</Value>
  </AllowedValues>
</LiteralInput>
```
http
WPS DescribeProcess response<sup>u</sup>:
```
<Input minOccurs="1" maxOccurs="1">
  <ows:Identifier>filter</ows:Identifier>
  <ows:Title>Filter</ows:Title>
  <ows:Abstract/>
  <LiteralData>
    <ows:AllowedValues>
      <ows:Value>maptype:coronal</ows:Value>
      <ows:Value>maptype:sagittal</ows:Value>
    </ows:AllowedValues>
  </LiteralData>
</Input>
```

The INCF Atlas implementation:
<table cellpadding='5' cellspacing='0' border='1'>
<blockquote><tr>
<blockquote><th>GET DataInput<sup>u</sup></th>
<th>Received value<sup>d</sup></th>
<th>Program action<sup>d</sup></th>
<th>Value used in Atlas<sup>u</sup></th>
</blockquote></tr>
<tr>
<blockquote><td>filter=maptype:coronal;</td>
<td>maptype:coronal</td>
<td>Validate value</td>
<td>maptype:coronal</td>
</blockquote></tr>
<tr>
<blockquote><td>filter=maptype:sagittal;</td>
<td>maptype:sagittal</td>
<td>Validate value</td>
<td>maptype:sagittal</td>
</blockquote></tr>
<tr>
<blockquote><td>filter=abcde;</td>
<td>abcde</td>
<td>Validate, throw exception</td>
<td>EXCEPTION: Value not allowed</td>
</blockquote></tr>
<tr>
<blockquote><td>filter=; (empty value)</td>
<td>null</td>
<td>Validate, throw exception</td>
<td>EXCEPTION: Value not allowed</td>
</blockquote></tr>
<tr>
<blockquote><td>(DataInput ommitted)</td>
<td>none</td>
<td>None (handled by Deegree)</td>
<td>EXCEPTION: Required data input</td>
</blockquote></tr>
</table></blockquote>

## Case 3: Optional Input, Any Value is Accepted, With a Default Value ##
  * If the data input value is empty, the default value is used

ProcessDefinition File (Deegree .../processes/Xxx.xml)<sup>d</sup>:
```
<LiteralInput minOccurs="0">
  <Identifier>srsName</Identifier>
  <Title>Atlas SRS Name</Title>
  <Abstract>The Atlas SRS (Spatial Reference System) name.</Abstract>
  <DefaultValue>Mouse_AGEA_1.0</DefaultValue>
</LiteralInput>
```

WPS DescribeProcess response<sup>u</sup>:
```
<Input minOccurs="0" maxOccurs="1">
  <ows:Identifier>srsName</ows:Identifier>
  <ows:Title>Atlas SRS Name</ows:Title>
  <ows:Abstract>The Atlas SRS (Spatial Reference System) name.</ows:Abstract>
  <LiteralData>
    <ows:AnyValue/>
    <DefaultValue>Mouse_AGEA_1.0</DefaultValue>
  </LiteralData>
</Input>
```

The INCF Atlas implementation:
<table cellpadding='5' cellspacing='0' border='1'>
<blockquote><tr>
<blockquote><th>GET DataInput<sup>u</sup></th>
<th>Received value<sup>d</sup></th>
<th>Program action<sup>d</sup></th>
<th>Value used in Atlas<sup>u</sup></th>
</blockquote></tr>
<tr>
<blockquote><td>srsName=Mouse_AGEA_1.0;</td>
<td>Mouse_AGEA_1.0</td>
<td>Accept value</td>
<td>Mouse_AGEA_1.0</td>
</blockquote></tr>
<tr>
<blockquote><td>srsName=abcde;</td>
<td>abcde</td>
<td>Accept value</td>
<td>abcde</td>
</blockquote></tr>
<tr>
<blockquote><td>srsName=; (empty value)</td>
<td>null</td>
<td>Assign default value</td>
<td>Mouse_AGEA_1.0 (by default)</td>
</blockquote></tr>
<tr>
<blockquote><td>(DataInput ommitted)</td>
<td>none</td>
<td>Assign default value</td>
<td>Mouse_AGEA_1.0 (by default)</td>
</blockquote></tr>
</table></blockquote>

## Case 4: Optional Input, Any of Several Allowed Values, With a Default Value ##
  * If the data input is omitted, the default value is used

ProcessDefinition File (Deegree .../processes/Xxx.xml)<sup>d</sup>:
```
<LiteralInput minOccurs="0">
  <Identifier>filter</Identifier>
  <Title>Filter</Title>
  <Abstract></Abstract>
  <DefaultValue>maptype:coronal</DefaultValue>
  <AllowedValues>
    <Value>maptype:coronal</Value>
    <Value>maptype:sagittal</Value>
  </AllowedValues>
</LiteralInput>
```

WPS DescribeProcess response<sup>u</sup>:
```
<Input minOccurs="0" maxOccurs="1">
  <ows:Identifier>filter</ows:Identifier>
  <ows:Title>Filter</ows:Title>
  <ows:Abstract/>
  <LiteralData>
    <ows:AllowedValues>
      <ows:Value>maptype:coronal</ows:Value>
      <ows:Value>maptype:sagittal</ows:Value>
    </ows:AllowedValues>
    <DefaultValue>maptype:coronal</DefaultValue>
  </LiteralData>
</Input>
```


The INCF Atlas implementation:
<table cellpadding='5' cellspacing='0' border='1'>
<blockquote><tr>
<blockquote><th>GET DataInput<sup>u</sup></th>
<th>Received value<sup>d</sup></th>
<th>Program action<sup>d</sup></th>
<th>Value used in Atlas<sup>u</sup></th>
</blockquote></tr>
<tr>
<blockquote><td>filter=maptype:coronal;</td>
<td>maptype:coronal</td>
<td>Validate value</td>
<td>maptype:coronal</td>
</blockquote></tr>
<tr>
<blockquote><td>filter=maptype:sagittal;</td>
<td>maptype:sagittal</td>
<td>Validate value</td>
<td>maptype:sagittal</td>
</blockquote></tr>
<tr>
<blockquote><td>filter=abcde;</td>
<td>abcde</td>
<td>Validate, throw exception</td>
<td>EXCEPTION: Value not allowed</td>
</blockquote></tr>
<tr>
<blockquote><td>filter=; (empty value)</td>
<td>null</td>
<td>Assign default value</td>
<td>maptype:coronal (by default)</td>
</blockquote></tr>
<tr>
<blockquote><td>(DataInput ommitted)</td>
<td>none</td>
<td>Assign default value</td>
<td>maptype:coronal (by default)</td>
</blockquote></tr>
</table>