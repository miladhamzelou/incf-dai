# How To Create a Hub #

  1. Review the [INCF Atlas Hub Specification and INCF Atlasing Infrastructure Overview](https://docs.google.com/Doc?docid=0Af-22shvZFIQZGQ5eDk3d2tfMmRyaGhqNmZw&hl=en) which describes INCF Atlas rpoject.
  1. Have some data to offer. Remember that an INCF Atlas Hub is a _front end_ to your data that provides a common interface to your data.
  1. Have the desire to follow INCF [Atlas Interface Specification](http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec) which is based on the [OGC WPS standard](http://www.opengeospatial.org/standards/wps). This describes the client-server _contract_ in terms of requests from a client to the hub and responses from the hub to a client. Remember a hub offers web services. It is not a web application. Hubs use standard HTTP GET requests and responds with XML documents. There is no GUI and no HTML.
  1. Determine what coordinate system you would like to use.
  1. Learn and use the [WaxML schema](WaxML.md), or SRS (spacial reference system), for XML responses to a client.
  1. Choose a name the hub. Existing hub names are
    * ABA (Allen Brain Atlas)
    * EMAP (Edinburgh Mouse Atlas Project)
    * UCSD (University of California, San Diego, CCDB (Cell-Centered Database)
    * WHS (Waxholm)
    * Central (Central is not a hub but rather a compiler that unions data across the hubs)
  1. Consider using a framework library such as [Deegree](http://www.deegree.org/) which facilitates software development. The hubs listed above use Deegree.
  1. Implement the requests GetCapabilities and DescribeProcess. These standard WPS request describes the Execute requests methods offered by the hub and allow a hub to be cataloged. These implementations are required. Using a framework library like Deegree simplifies these implementations.
  1. Decide what processes are applicable to your data and you wish to offer. Examine the [Atlas Function-Hub Applicability Table](http://code.google.com/p/incf-dai/wiki/AtlasFunctionHubApplicabilityTable) to see the processes the current hubs offer. Here are some general suggestions:
    * SRSs (spatial reference systems). Every hub must have at least one, so plan to implement ListSRSs and DescribeSRS. This supports POIs (points of interest).
    * POIs. Depending on the data you have you would likely implement Get2DImagesByPOI and possibly GetGenesByPOI and GetStructureNamesByPOI. Depending on how many of the "ByPOI" processes you implement, you may choose to add GetObjectsByPOI.
    * Annotations. If you want to support annotations, implement SetAnnotations and GetAnnotationsByPOI.
    * Transformations. If you plan to support multiple SRSs, as the existing hubs do, then should implement ListTransformations, TransformPOI, and possibly GetTransformationChain.
  1. Review section 6 of the [INCF Atlas Hub Specification and INCF Atlasing Infrastructure Overview](https://docs.google.com/Doc?docid=0Af-22shvZFIQZGQ5eDk3d2tfMmRyaGhqNmZw&hl=en).
  1. Use the atlas-common code. This is optional, but this library, or hub building toolkit, is used by the INCF Atlas development team at UCSD. It includes code that is common across multiple hubs such as standard SRS transformations and exception reporting.
  1. Use the standard [OWS/WPS Exception Report](http://schemas.opengis.net/ows/0.3.0/owsExceptionReport.xsd) for reporting exceptions or conditions that prevent the hub from returning a normal response.
  1. Consider using the WPS exception report to report INCF Atlas-defined functions not supported at your hub or not yet implemented.
  1. Publish, test, and register your hub with INCF Atlas Central.

# INCF Common API #

  1. Review the [INCF Common API](http://132.239.131.188/incf-common/doc).

The common API is a module that can be used across all the hubs for code development purpose. It basically serves the common code to avoid any  redundancy in the code.

# Getting Started Guide on using the hub code #

> <strong>Softwares needed:</strong>
  1. Java 5 and above. This can be downloaded from: http://www.oracle.com/technetwork/java/javase/downloads/index-jdk5-jsp-142662.html
  1. Tomcat Server: This can be downloaded from: http://tomcat.apache.org/download-70.cgi
  1. Maven: This can be downloaded from: http://maven.apache.org/download.html
  1. Eclipse: This can be downloaded from: http://www.eclipse.org/downloads/

> <strong>Operating system environment supported:</strong>
  1. Windows 2003 Server, Windows Vista, Windows 7
  1. Linux

> <strong>Hub code downloads:</strong>
  1. ABA: http://code.google.com/p/incf-dai/source/browse/#svn%2Fincf-aba%2Ftrunk
  1. UCSD: http://code.google.com/p/incf-dai/source/browse/#svn%2Fincf-ucsd%2Ftrunk
  1. WHS: http://code.google.com/p/incf-dai/source/browse/#svn%2Fincf-whs%2Ftrunk
  1. EMAP: http://code.google.com/p/incf-dai/source/browse/#svn%2Fincf-emap%2Ftrunk
  1. Central: http://code.google.com/p/incf-dai/source/browse/#svn%2Fincf-central%2Ftrunk
  1. Common: http://code.google.com/p/incf-dai/source/browse/#svn%2Fincf-common%2Ftrunk
  1. AtlasXMLBeans: http://code.google.com/p/incf-dai/source/browse/#svn%2Fwaxml%2Ftrunk

Step 1:
First you need to download AtlasXMLBeans module from SVN from the above link, then run Maven Clean on AtlasXMLBeans > Maven Install on AtlasXMLBeans. This will create necessary jar files in your home directory where Maven can read the jar files.

Step 2:
Download common module from SVN from the above link, then run Maven Clean on Common > Maven Install on Common. This will create necessary jar files in your home directory where Maven can read the jar files.

Step 3:
Download the hub (ABA, WHS, UCSD, EMAP) of your interest, and work with it by following the instructions given in this page.

Step 4:
Here is some information about creating a new process/function in a hub: http://code.google.com/p/incf-dai/wiki/DeegreeHowToDevelopWPSProcesses.

Please feel free to contact Asif Memon at mamemon@ucsd.edu for any questions.