/**
 * <p>Title: Image Assembly VO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: BIRN-CC, UCSD</p>
 * @author Asif Memon
 * @version 1.0
 */

package org.incf.atlas.ucsd.resource; 

import java.util.ArrayList;

/**
 * @author Asif Memon
 * 
 */
public class UCSDServiceVO {

	/**
	 * The helper class to store the image assembly related data
	 */

	public UCSDServiceVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String sliceConstant = "";
	private String coefficientA = "";
	private String coefficientB = "";
	private String coefficientC = "";
	private String coefficientD = "";
	private String wms = "";
	private String flag = "";
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

	private String tolerance = "";
	public String getTolerance() {
		return tolerance;
	}

	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCoefficientA() {
		return coefficientA;
	}

	public void setCoefficientA(String coefficientA) {
		this.coefficientA = coefficientA;
	}

	public String getCoefficientB() {
		return coefficientB;
	}

	public void setCoefficientB(String coefficientB) {
		this.coefficientB = coefficientB;
	}

	public String getCoefficientC() {
		return coefficientC;
	}

	public void setCoefficientC(String coefficientC) {
		this.coefficientC = coefficientC;
	}

	public String getCoefficientD() {
		return coefficientD;
	}

	public void setCoefficientD(String coefficientD) {
		this.coefficientD = coefficientD;
	}

	public String getSliceConstant() {
		return sliceConstant;
	}

	public void setSliceConstant(String sliceConstant) {
		this.sliceConstant = sliceConstant;
	}

	
}
