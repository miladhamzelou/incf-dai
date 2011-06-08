package org.incf.aba.atlas.process;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.incf.aba.atlas.util.ABAServiceDAOImpl;
import org.incf.common.atlas.util.AllowedValuesValidator;
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
		
		String points = "(1,2,3)(4,5,6)(7,8,9)";
		StringTokenizer tokens1 = new StringTokenizer(points, ")(");
		while (tokens1.hasMoreTokens()){
			System.out.println(":::while-loop:::");
			StringTokenizer tokens2 = new StringTokenizer(tokens1.nextToken(), ",");
			//while (tokens2.hasMoreTokens()) {
				System.out.println("Token:" +tokens2.nextToken());	
				System.out.println("Token:" +tokens2.nextToken());
				System.out.println("Token:" +tokens2.nextToken());
			//}
				
		}
		
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
