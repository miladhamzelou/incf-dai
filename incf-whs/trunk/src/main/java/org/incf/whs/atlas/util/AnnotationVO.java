package org.incf.whs.atlas.util;

/**
 * <p>Title: Image Assembly VO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: BIRN-CC, UCSD</p>
 * @author Asif Memon
 * @version 1.0
 */

import java.util.ArrayList;

/**
 * @author Asif Memon
 * 
 */
public class AnnotationVO {

	/**
	 * The helper class to store the image assembly related data
	 */

	public AnnotationVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String polygonID = "";
	private String pointX = "";
	private String pointY = "";
	private String pointZ = "";
	private String filePath = "";
	private String polygonString = "";
	private String modality = "";
	
	private String tfwLine1 = "";
	private String tfwLine2 = "";
	private String tfwLine3 = "";
	private String tfwLine4 = "";
	private String tfwLine5 = "";
	private String tfwLine6 = "";

	
	public String getPolygonString() {
		return polygonString;
	}

	public String getTfwLine1() {
		return tfwLine1;
	}

	public void setTfwLine1(String tfwLine1) {
		this.tfwLine1 = tfwLine1;
	}

	public String getTfwLine3() {
		return tfwLine3;
	}

	public void setTfwLine3(String tfwLine3) {
		this.tfwLine3 = tfwLine3;
	}

	public String getTfwLine4() {
		return tfwLine4;
	}

	public void setTfwLine4(String tfwLine4) {
		this.tfwLine4 = tfwLine4;
	}

	public String getTfwLine5() {
		return tfwLine5;
	}

	public void setTfwLine5(String tfwLine5) {
		this.tfwLine5 = tfwLine5;
	}

	public String getTfwLine6() {
		return tfwLine6;
	}

	public void setTfwLine6(String tfwLine6) {
		this.tfwLine6 = tfwLine6;
	}

	public String getTfwLine2() {
		return tfwLine2;
	}

	public void setTfwLine2(String tfwLine2) {
		this.tfwLine2 = tfwLine2;
	}

	public void setPolygonString(String polygonString) {
		this.polygonString = polygonString;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPointX() {
		return pointX;
	}

	public void setPointX(String pointX) {
		this.pointX = pointX;
	}

	public String getPointY() {
		return pointY;
	}

	public void setPointY(String pointY) {
		this.pointY = pointY;
	}

	public String getPointZ() {
		return pointZ;
	}

	public void setPointZ(String pointZ) {
		this.pointZ = pointZ;
	}

	public String getPolygonID() {
		return polygonID;
	}

	public void setPolygonID(String polygonID) {
		this.polygonID = polygonID;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

}
