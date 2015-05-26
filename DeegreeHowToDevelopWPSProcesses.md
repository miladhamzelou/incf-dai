
# HowTo: Develop Atlas/WPS Processes with Deegree #

## Project Structure ##

The Eclipse Maven project will normally be named incf-`<hub`>, for example, incf-aba.

The directory structure in Eclipse will look like this:

```

incf-<hub>

src/main/java/
org.incf.<hub>.atlas.process
<processIdentifier1>.java
<processIdentifier2>.java
...
<processIdentifierN>.java
org.incf.<hub>.atlas.util
src/main/resources
log4j.properties
src/main/webapp/WEB-INF/
workspace/
processes/
<processIdentifier1>.xml
<processIdentifier2>.xml
...
<processIdentifierN>.xml
services/
main.xml
metadata.xml
wps.xml
web.xml
target/
pom.xml
```

## Procedure ##
  1. Create two files:
    * An XML ProcessDefinition file in the src/main/webapp/WEB-INF/workspace/processes/ directory
    * A Java class in the org.incf.`<hub`>.atlas.process package that extends org.deegree.services.wps.Processlet
  1. Add any required dependencies to the pom.xml
  1. Run Maven install
  1. Deploy the `<hub`>.war file to a Tomcat server
  1. Test the process

## Notes ##
  * The names of the two files should be the process name, for example
    * Get2DImagesByPOI.xml
    * Get2DImagesByPOI.java
  * GetCapabilities and DescribeProcess are automatically generated based on the content of the `<processIdentifier`>.xml file and other configuration files
  * Other supporting classes may be placed in the org.incf.`<hub>`.atlas.util package or other packages you may create. Do not put them in the org.incf.`<hub>`.atlas.process package -- save that package for processlets.
  * Other resource files may be placed in the src/main/resources directory.
  * Find further information on the `<processIdentifier`>.xml file [here](http://wiki.deegree.org/deegreeWiki/deegree3/HowToCreateWPSProcesses#Createaprocessdefinitionfile)
  * Find further information on the `<processIdentifier`>.java class [here](http://wiki.deegree.org/deegreeWiki/deegree3/HowToCreateWPSProcesses#CreatetheJavaprocesscode.28processlet.29)