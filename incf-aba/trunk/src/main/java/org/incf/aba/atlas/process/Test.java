package org.incf.aba.atlas.process;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.incf.aba.atlas.util.ABAServiceDAOImpl;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	private static final Logger LOG = LoggerFactory
	.getLogger(Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//String srsName = "Mouse_ABAvoxel_1.0";
		
		//LOG.debug("SRSName: {}",srsName.replace("Mouse_", "").replaceAll("_1.0", ""));

		//CommonUtil util = new CommonUtil();
		//util.outOfBoundException(321, 321, 231, srsName);
		
/*		String userID = "";
		String password = "";
		String filter = "userID=asif:password=asif1234";
		StringTokenizer tokens1 = new StringTokenizer(filter, "=");
		System.out.println("Tokens: " + tokens1.countTokens() );
		while (tokens1.hasMoreTokens()){
			System.out.println("passtoken: " +tokens1.nextToken());	
			userID = tokens1.nextToken();
			password = tokens1.nextToken();
		}
		userID = userID.replaceAll(":password", "");
		System.out.println("userID after: " + userID);
		System.out.println("Password: " + password);
*/




		// Checking out of bound exception
		CommonUtil commonUtil = new CommonUtil();
		String outOfBoundCheck = commonUtil.outOfBoundException(
				Double.parseDouble("235"), Double.parseDouble("165"), Double.parseDouble("185"), "Mouse_ABAreference_1.0");

		StringTokenizer tokens = null;
		String var = "";
		String minRange = "";
		String maxRange = "";
		
/*			System.out.println("Tokens:"+tokens.nextToken());
			System.out.println("Tokens:"+tokens.nextToken());
			System.out.println("Tokens:"+tokens.nextToken());
*/		

		System.out.println("Before" + outOfBoundCheck);
		if (outOfBoundCheck.startsWith("Coordinates - Out of Range:x:")) {

			tokens = new StringTokenizer(outOfBoundCheck, ":");
			tokens.nextToken();
			var = tokens.nextToken();
			minRange = tokens.nextToken();
			maxRange = tokens.nextToken();
			System.out.println("Tokens: "+var+minRange+maxRange);
			
			System.out.println("The transformation '' has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\"235\")");
/*			throw new OWSException("Coordinates - Out of Range.",
					ControllerException.NO_APPLICABLE_CODE);
*/		} else if (outOfBoundCheck.equalsIgnoreCase("Coordinates - Out of Range:y:")) {

			System.out.println("The transformation '' has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\"235\")");
/*			throw new OWSException("Coordinates - Out of Range.",
			ControllerException.NO_APPLICABLE_CODE);
*/		} else if (outOfBoundCheck.equalsIgnoreCase("Coordinates - Out of Range:z:")) {

			System.out.println("The transformation '' has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\"235\")");
/*			throw new OWSException("Coordinates - Out of Range.",
			ControllerException.NO_APPLICABLE_CODE);
*/		}


	
	
	}

	
/*	public void runCmd(String[] args) {
		String cmd = "/home_dir/./my_shell_script.sh" ;
		Runtime run = Runtime.getRuntime() ;
		Process pr = run.exec(cmd); 
		pr.waitFor();
		BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;
		String line ; 
		
		while( buf.readLine() != null ) {  
			System.out.println(line) ;
		}
	}
*/
	public String outOfBoundException(double x, double y, double z, String srsName) {

		String responseString = "";
		String [] coordinatesRange = new String[6];
		
		try {

			ABAServiceDAOImpl impl = new ABAServiceDAOImpl();
			coordinatesRange = impl.getCoordinateRangeForSRS(srsName);

			if (        x < Double.parseDouble(coordinatesRange[0]) ||
				        x > Double.parseDouble(coordinatesRange[1]) ) {
				responseString = "Coordinates - Out of Range";
			} else if ( y < Double.parseDouble(coordinatesRange[2]) ||
				        y > Double.parseDouble(coordinatesRange[3]) ) {
				responseString = "Coordinates - Out of Range";
			} else if ( z < Double.parseDouble(coordinatesRange[4]) ||
				        z > Double.parseDouble(coordinatesRange[5]) ) {
				responseString = "Coordinates - Out of Range";
			} else {
				responseString = "SUCCESS";
			}

			LOG.debug("Response for Out of Bound Exception is - {}" , responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}

}
