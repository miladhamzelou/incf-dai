/**
 * <p>Title: Image Assembly VO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: BIRN-CC, UCSD</p>
 * @author Asif Memon
 * @version 1.0
 */

package org.incf.atlas.whs.resource; 

import java.util.ArrayList;

/**
 * @author Asif Memon
 * 
 */
public class WHSServiceVO {

	/**
	 * The helper class to store the image assembly related data
	 */

	public WHSServiceVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String wms = "";
	private String flag = "";
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	private String incfDeployHostname = "";
	public String getIncfDeployHostname() {
		return incfDeployHostname;
	}

	public void setIncfDeployHostname(String incfDeployHostname) {
		this.incfDeployHostname = incfDeployHostname;
	}

	public String getIncfDeployPortNumber() {
		return incfDeployPortNumber;
	}

	public void setIncfDeployPortNumber(String incfDeployPortNumber) {
		this.incfDeployPortNumber = incfDeployPortNumber;
	}

	private String incfDeployPortNumber = "";
	private String imageBaseName = "";
	private String imageServiceName = "";
	private String minX = "";
	private String maxX = "";
	private String minY = "";
	private String maxY = "";
	private String topLeft = "";
	private String topRight = "";
	private String bottomLeft = "";
	private String bottomRight = "";
	private String tfw1 = "";
	private String tfw2 = "";
	private String tfw3 = "";
	private String tfw4 = "";
	private String tfw5 = "";
	private String tfw6 = "";
	private String filter = "";
	private String currentTime = "";
	private String urlString = "";

	private String originalCoordinateX = "";
	private String originalCoordinateY = "";
	private String originalCoordinateZ = "";
	private String transformedCoordinateX = "";
	private String transformedCoordinateY = "";
	private String transformedCoordinateZ = "";
	private String transformationXMLResponseString = "";
	private String srcSRSCode = "";
	private String destSRSCode = "";
	private String vocabulary = "";

	private String transformationOne;
	private String transformationTwo;
	private String transformationThree;
	private String transformationFour;

	private String fromSRSCode;
	private String toSRSCode;
	
	private String transformationOneURL;
	private String transformationTwoURL;
	private String transformationThreeURL;
	private String transformationFourURL;
	
	private String xmlStringForTransformationInfoWithExecution;
	private String xmlStringForTransformationInfo; 
	private String errorMessage;
	
	private String fromSRSCodeOne;
	private String fromSRSCodeTwo;
	private String fromSRSCodeThree;
	private String fromSRSCodeFour;
	
	private String toSRSCodeOne;
	private String toSRSCodeTwo;
	private String toSRSCodeThree;
	private String toSRSCodeFour;

	//Start - SRS Related
	private String srsCode = "";
	private String srsName = "";
	private String srsDescription = "";
	private String srsAuthorCode = "";
	private String srsDateSubmitted = "";
	private String origin = "";
	private String unitsAbbreviation = "";
	private String unitsName = "";
	private String neuroPlusXCode = "";
	private String neuroMinusXCode = "";
	private String neuroPlusYCode = "";
	private String neuroMinusYCode = "";
	private String neuroPlusZCode = "";
	private String neuroMinusZCode = "";
	private String sourceURI = "";
	private String sourceFileFormat = "";
	private String srsAbstract = "";
	private String derivedFromSRSCode = "";
	private String derivedMethod = "";
	private String species = "";
	private String srsBase = "";
	private String regionOfValidity = "";
	private String regionURI = "";
	private String srsVersion = "";
	private String dimensionMinX = "";
	private String dimensionMaxX = "";
	private String dimensionMinY = "";
	private String dimensionMaxY = "";
	private String dimensionMinZ = "";
	private String dimensionMaxZ = "";
	private String orientationName = "";
	private String orientationDescription = "";
	private String orientationCode = "";
	private String orientationAuthor = "";
	private String orientationDateSubmitted = "";

	//Slice Table
	private String spaceCode = "";
	private String slideValueOrigin = "";
	private String valueDirection = "";
	private String rightDirection = "";
	private String upDirection = "";
	private String plusX = "";
	private String plusY = "";
	private String plusZ = "";
	private String sliceID = "";
	private String slideValue = "";
	
	
	public String getSpaceCode() {
		return spaceCode;
	}

	public void setSpaceCode(String spaceCode) {
		this.spaceCode = spaceCode;
	}

	public String getSlideValueOrigin() {
		return slideValueOrigin;
	}

	public void setSlideValueOrigin(String slideValueOrigin) {
		this.slideValueOrigin = slideValueOrigin;
	}

	public String getValueDirection() {
		return valueDirection;
	}

	public void setValueDirection(String valueDirection) {
		this.valueDirection = valueDirection;
	}

	public String getRightDirection() {
		return rightDirection;
	}

	public void setRightDirection(String rightDirection) {
		this.rightDirection = rightDirection;
	}

	public String getUpDirection() {
		return upDirection;
	}

	public void setUpDirection(String upDirection) {
		this.upDirection = upDirection;
	}

	public String getPlusX() {
		return plusX;
	}

	public void setPlusX(String plusX) {
		this.plusX = plusX;
	}

	public String getPlusY() {
		return plusY;
	}

	public void setPlusY(String plusY) {
		this.plusY = plusY;
	}

	public String getPlusZ() {
		return plusZ;
	}

	public void setPlusZ(String plusZ) {
		this.plusZ = plusZ;
	}

	public String getSliceID() {
		return sliceID;
	}

	public void setSliceID(String sliceID) {
		this.sliceID = sliceID;
	}

	public String getSlideValue() {
		return slideValue;
	}

	public void setSlideValue(String slideValue) {
		this.slideValue = slideValue;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getTfw1() {
		return tfw1;
	}

	public void setTfw1(String tfw1) {
		this.tfw1 = tfw1;
	}

	public String getTfw2() {
		return tfw2;
	}

	public void setTfw2(String tfw2) {
		this.tfw2 = tfw2;
	}

	public String getTfw3() {
		return tfw3;
	}

	public void setTfw3(String tfw3) {
		this.tfw3 = tfw3;
	}

	public String getTfw4() {
		return tfw4;
	}

	public void setTfw4(String tfw4) {
		this.tfw4 = tfw4;
	}

	public String getTfw5() {
		return tfw5;
	}

	public void setTfw5(String tfw5) {
		this.tfw5 = tfw5;
	}

	public String getTfw6() {
		return tfw6;
	}

	public void setTfw6(String tfw6) {
		this.tfw6 = tfw6;
	}

	public String getWms() {
		return wms;
	}

	public void setWms(String wms) {
		this.wms = wms;
	}

	public String getImageBaseName() {
		return imageBaseName;
	}

	public void setImageBaseName(String imageBaseName) {
		this.imageBaseName = imageBaseName;
	}

	public String getImageServiceName() {
		return imageServiceName;
	}

	public void setImageServiceName(String imageServiceName) {
		this.imageServiceName = imageServiceName;
	}

	public String getMaxX() {
		return maxX;
	}

	public void setMaxX(String maxX) {
		this.maxX = maxX;
	}

	public String getMaxY() {
		return maxY;
	}

	public void setMaxY(String maxY) {
		this.maxY = maxY;
	}

	public String getMinX() {
		return minX;
	}

	public void setMinX(String minX) {
		this.minX = minX;
	}

	public String getMinY() {
		return minY;
	}

	public void setMinY(String minY) {
		this.minY = minY;
	}

	public String getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(String bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public String getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(String bottomRight) {
		this.bottomRight = bottomRight;
	}

	public String getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(String topLeft) {
		this.topLeft = topLeft;
	}

	public String getTopRight() {
		return topRight;
	}

	public void setTopRight(String topRight) {
		this.topRight = topRight;
	}

	public String getTransformedCoordinateX() {
		return transformedCoordinateX;
	}

	public void setTransformedCoordinateX(String transformedCoordinateX) {
		this.transformedCoordinateX = transformedCoordinateX;
	}

	public String getTransformedCoordinateY() {
		return transformedCoordinateY;
	}

	public void setTransformedCoordinateY(String transformedCoordinateY) {
		this.transformedCoordinateY = transformedCoordinateY;
	}

	public String getTransformedCoordinateZ() {
		return transformedCoordinateZ;
	}

	public void setTransformedCoordinateZ(String transformedCoordinateZ) {
		this.transformedCoordinateZ = transformedCoordinateZ;
	}

	public String getOriginalCoordinateX() {
		return originalCoordinateX;
	}

	public void setOriginalCoordinateX(String originalCoordinateX) {
		this.originalCoordinateX = originalCoordinateX;
	}

	public String getOriginalCoordinateY() {
		return originalCoordinateY;
	}

	public void setOriginalCoordinateY(String originalCoordinateY) {
		this.originalCoordinateY = originalCoordinateY;
	}

	public String getOriginalCoordinateZ() {
		return originalCoordinateZ;
	}

	public void setOriginalCoordinateZ(String originalCoordinateZ) {
		this.originalCoordinateZ = originalCoordinateZ;
	}

	public String getTransformationXMLResponseString() {
		return transformationXMLResponseString;
	}

	public void setTransformationXMLResponseString(
			String transformationXMLResponseString) {
		this.transformationXMLResponseString = transformationXMLResponseString;
	}

	public String getDestSRSCode() {
		return destSRSCode;
	}

	public void setDestSRSCode(String destSRSCode) {
		this.destSRSCode = destSRSCode;
	}

	public String getSrcSRSCode() {
		return srcSRSCode;
	}

	public void setSrcSRSCode(String srcSRSCode) {
		this.srcSRSCode = srcSRSCode;
	}

	public String getTransformationOne() {
		return transformationOne;
	}

	public void setTransformationOne(String transformationOne) {
		this.transformationOne = transformationOne;
	}

	public String getTransformationThree() {
		return transformationThree;
	}

	public void setTransformationThree(String transformationThree) {
		this.transformationThree = transformationThree;
	}

	public String getTransformationTwo() {
		return transformationTwo;
	}

	public void setTransformationTwo(String transformationTwo) {
		this.transformationTwo = transformationTwo;
	}

	public String getFromSRSCode() {
		return fromSRSCode;
	}

	public void setFromSRSCode(String fromSRSCode) {
		this.fromSRSCode = fromSRSCode;
	}

	public String getToSRSCode() {
		return toSRSCode;
	}

	public void setToSRSCode(String toSRSCode) {
		this.toSRSCode = toSRSCode;
	}

	public String getTransformationOneURL() {
		return transformationOneURL;
	}

	public void setTransformationOneURL(String transformationOneURL) {
		this.transformationOneURL = transformationOneURL;
	}

	public String getTransformationThreeURL() {
		return transformationThreeURL;
	}

	public void setTransformationThreeURL(String transformationThreeURL) {
		this.transformationThreeURL = transformationThreeURL;
	}

	public String getTransformationTwoURL() {
		return transformationTwoURL;
	}

	public void setTransformationTwoURL(String transformationTwoURL) {
		this.transformationTwoURL = transformationTwoURL;
	}

	public String getXmlStringForTransformationInfoWithExecution() {
		return xmlStringForTransformationInfoWithExecution;
	}

	public void setXmlStringForTransformationInfoWithExecution(
			String xmlStringForTransformationInfoWithExecution) {
		this.xmlStringForTransformationInfoWithExecution = xmlStringForTransformationInfoWithExecution;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getFromSRSCodeOne() {
		return fromSRSCodeOne;
	}

	public void setFromSRSCodeOne(String fromSRSCodeOne) {
		this.fromSRSCodeOne = fromSRSCodeOne;
	}

	public String getFromSRSCodeThree() {
		return fromSRSCodeThree;
	}

	public void setFromSRSCodeThree(String fromSRSCodeThree) {
		this.fromSRSCodeThree = fromSRSCodeThree;
	}

	public String getFromSRSCodeTwo() {
		return fromSRSCodeTwo;
	}

	public void setFromSRSCodeTwo(String fromSRSCodeTwo) {
		this.fromSRSCodeTwo = fromSRSCodeTwo;
	}

	public String getToSRSCodeOne() {
		return toSRSCodeOne;
	}

	public void setToSRSCodeOne(String toSRSCodeOne) {
		this.toSRSCodeOne = toSRSCodeOne;
	}

	public String getToSRSCodeThree() {
		return toSRSCodeThree;
	}

	public void setToSRSCodeThree(String toSRSCodeThree) {
		this.toSRSCodeThree = toSRSCodeThree;
	}

	public String getToSRSCodeTwo() {
		return toSRSCodeTwo;
	}

	public void setToSRSCodeTwo(String toSRSCodeTwo) {
		this.toSRSCodeTwo = toSRSCodeTwo;
	}

	public String getXmlStringForTransformationInfo() {
		return xmlStringForTransformationInfo;
	}

	public void setXmlStringForTransformationInfo(
			String xmlStringForTransformationInfo) {
		this.xmlStringForTransformationInfo = xmlStringForTransformationInfo;
	}

	public String getFromSRSCodeFour() {
		return fromSRSCodeFour;
	}

	public void setFromSRSCodeFour(String fromSRSCodeFour) {
		this.fromSRSCodeFour = fromSRSCodeFour;
	}

	public String getToSRSCodeFour() {
		return toSRSCodeFour;
	}

	public void setToSRSCodeFour(String toSRSCodeFour) {
		this.toSRSCodeFour = toSRSCodeFour;
	}

	public String getTransformationFour() {
		return transformationFour;
	}

	public void setTransformationFour(String transformationFour) {
		this.transformationFour = transformationFour;
	}

	public String getTransformationFourURL() {
		return transformationFourURL;
	}

	public void setTransformationFourURL(String transformationFourURL) {
		this.transformationFourURL = transformationFourURL;
	}
	
	public String getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getSrsCode() {
		return srsCode;
	}

	public void setSrsCode(String srsCode) {
		this.srsCode = srsCode;
	}

	public String getSrsName() {
		return srsName;
	}

	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}

	public String getSrsDescription() {
		return srsDescription;
	}

	public void setSrsDescription(String srsDescription) {
		this.srsDescription = srsDescription;
	}

	public String getSrsAuthorCode() {
		return srsAuthorCode;
	}

	public void setSrsAuthorCode(String srsAuthorCode) {
		this.srsAuthorCode = srsAuthorCode;
	}

	public String getSrsDateSubmitted() {
		return srsDateSubmitted;
	}

	public void setSrsDateSubmitted(String srsDateSubmitted) {
		this.srsDateSubmitted = srsDateSubmitted;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getUnitsAbbreviation() {
		return unitsAbbreviation;
	}

	public void setUnitsAbbreviation(String unitsAbbreviation) {
		this.unitsAbbreviation = unitsAbbreviation;
	}

	public String getUnitsName() {
		return unitsName;
	}

	public void setUnitsName(String unitsName) {
		this.unitsName = unitsName;
	}

	public String getNeuroPlusXCode() {
		return neuroPlusXCode;
	}

	public void setNeuroPlusXCode(String neuroPlusXCode) {
		this.neuroPlusXCode = neuroPlusXCode;
	}

	public String getNeuroMinusXCode() {
		return neuroMinusXCode;
	}

	public void setNeuroMinusXCode(String neuroMinusXCode) {
		this.neuroMinusXCode = neuroMinusXCode;
	}

	public String getNeuroPlusYCode() {
		return neuroPlusYCode;
	}

	public void setNeuroPlusYCode(String neuroPlusYCode) {
		this.neuroPlusYCode = neuroPlusYCode;
	}

	public String getNeuroMinusYCode() {
		return neuroMinusYCode;
	}

	public void setNeuroMinusYCode(String neuroMinusYCode) {
		this.neuroMinusYCode = neuroMinusYCode;
	}

	public String getNeuroPlusZCode() {
		return neuroPlusZCode;
	}

	public void setNeuroPlusZCode(String neuroPlusZCode) {
		this.neuroPlusZCode = neuroPlusZCode;
	}

	public String getNeuroMinusZCode() {
		return neuroMinusZCode;
	}

	public void setNeuroMinusZCode(String neuroMinusZCode) {
		this.neuroMinusZCode = neuroMinusZCode;
	}

	public String getSourceURI() {
		return sourceURI;
	}

	public void setSourceURI(String sourceURI) {
		this.sourceURI = sourceURI;
	}

	public String getSourceFileFormat() {
		return sourceFileFormat;
	}

	public void setSourceFileFormat(String sourceFileFormat) {
		this.sourceFileFormat = sourceFileFormat;
	}

	public String getSrsAbstract() {
		return srsAbstract;
	}

	public void setSrsAbstract(String srsAbstract) {
		this.srsAbstract = srsAbstract;
	}

	public String getDerivedFromSRSCode() {
		return derivedFromSRSCode;
	}

	public void setDerivedFromSRSCode(String derivedFromSRSCode) {
		this.derivedFromSRSCode = derivedFromSRSCode;
	}

	public String getDerivedMethod() {
		return derivedMethod;
	}

	public void setDerivedMethod(String derivedMethod) {
		this.derivedMethod = derivedMethod;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getSrsBase() {
		return srsBase;
	}

	public void setSrsBase(String srsBase) {
		this.srsBase = srsBase;
	}

	public String getRegionOfValidity() {
		return regionOfValidity;
	}

	public void setRegionOfValidity(String regionOfValidity) {
		this.regionOfValidity = regionOfValidity;
	}

	public String getRegionURI() {
		return regionURI;
	}

	public void setRegionURI(String regionURI) {
		this.regionURI = regionURI;
	}

	public String getSrsVersion() {
		return srsVersion;
	}

	public void setSrsVersion(String srsVersion) {
		this.srsVersion = srsVersion;
	}

	public String getDimensionMinX() {
		return dimensionMinX;
	}

	public void setDimensionMinX(String dimensionMinX) {
		this.dimensionMinX = dimensionMinX;
	}

	public String getDimensionMaxX() {
		return dimensionMaxX;
	}

	public void setDimensionMaxX(String dimensionMaxX) {
		this.dimensionMaxX = dimensionMaxX;
	}

	public String getDimensionMinY() {
		return dimensionMinY;
	}

	public void setDimensionMinY(String dimensionMinY) {
		this.dimensionMinY = dimensionMinY;
	}

	public String getDimensionMaxY() {
		return dimensionMaxY;
	}

	public void setDimensionMaxY(String dimensionMaxY) {
		this.dimensionMaxY = dimensionMaxY;
	}

	public String getDimensionMinZ() {
		return dimensionMinZ;
	}

	public void setDimensionMinZ(String dimensionMinZ) {
		this.dimensionMinZ = dimensionMinZ;
	}

	public String getDimensionMaxZ() {
		return dimensionMaxZ;
	}

	public void setDimensionMaxZ(String dimensionMaxZ) {
		this.dimensionMaxZ = dimensionMaxZ;
	}

	public String getOrientationName() {
		return orientationName;
	}

	public void setOrientationName(String orientationName) {
		this.orientationName = orientationName;
	}

	public String getOrientationDescription() {
		return orientationDescription;
	}

	public void setOrientationDescription(String orientationDescription) {
		this.orientationDescription = orientationDescription;
	}

	public String getOrientationCode() {
		return orientationCode;
	}

	public void setOrientationCode(String orientationCode) {
		this.orientationCode = orientationCode;
	}

	public String getOrientationAuthor() {
		return orientationAuthor;
	}

	public void setOrientationAuthor(String orientationAuthor) {
		this.orientationAuthor = orientationAuthor;
	}

	public String getOrientationDateSubmitted() {
		return orientationDateSubmitted;
	}

	public void setOrientationDateSubmitted(String orientationDateSubmitted) {
		this.orientationDateSubmitted = orientationDateSubmitted;
	}


}
