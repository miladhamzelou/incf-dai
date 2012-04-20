package org.incf.ucsd.atlas.process;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.incf.ucsd.atlas.util.UCSDServiceDAOImpl;
import org.incf.ucsd.atlas.util.UCSDServiceVO;
import org.incf.ucsd.atlas.util.UCSDUtil;
import org.incf.ucsd.atlas.util.WHS2Paxinos;
import org.incf.ucsd.atlas.util.XMLReader;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import edu.ucsd.ncmir.insighttransform.InsightTransform;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

/*		WHS2Paxinos whs2paxinos = new WHS2Paxinos();
		String originalCoordinateX = "250.00"; 
		String originalCoordinateY = "251.0"; 
		String originalCoordinateZ = "262.0"; 
*/
/*		String transformedCoordinateString = whs2paxinos.getTransformation( Long.parseLong(originalCoordinateX.replace(".0", "")),
				Long.parseLong(originalCoordinateY.replace(".0", "")), Long.parseLong(originalCoordinateZ.replace(".0", "")) );
*/
		//UCSDUtil test = new UCSDUtil();
		String fromSRS = "Mouse_ABAreference_1.0";
		String toSRS = "Mouse_Paxinos_1.0";

		Test test = new Test();
		Set chainsList = test.spaceTransformationFromDB(fromSRS, toSRS);
		Iterator iter = chainsList.iterator();
		String chain = "";
		StringTokenizer tokens = null;
		StringTokenizer tokens2 = null;
		Set srcSet = new HashSet();
		Set destSet = new HashSet();
		
		while ( iter.hasNext()) {
			chain = (String)iter.next();
			//System.out.println("Resulting Chain is: " + chain);
			tokens = new StringTokenizer(chain, ":");
//			System.out.println("Token1 is: " + tokens.nextToken());
			
			String tokenModify = tokens.nextToken().replaceAll("_To_", ":");
			System.out.println("Chain is: " + tokenModify);
			tokens2 = new StringTokenizer(tokenModify, ":");
			srcSet.add(tokens2.nextToken());
			destSet.add(tokens2.nextToken());
			//System.out.println("Token2 is: " + tokens2.nextToken());
			//System.out.println("Token is: " + tokens2.nextToken());
		}
		while (!destSet.contains(toSRS)) { 
			String responseString = "No such transformation is supported in this hub.";
			System.out.println("Dest Not available");
			break;
		}
		while (!srcSet.contains(fromSRS)) { 
			String responseString = "No such transformation is supported in this hub.";
			System.out.println("Src Not available");
			break;
		}

	}


	public double[] runURL() {

		double[] forward = null;

		try {

			String updateSpaceTransformationsURL1 = "http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=updateSpaceTransformations&DataInputs=source=Mouse_Yuko_1.0;destination=Mouse_WHS_0.9;hub=UCSD;transformationURL=yuko12whs09"; 

			URL url = new URL(updateSpaceTransformationsURL1);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader inBuff = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = inBuff.readLine()) != null) {
				System.out.println("inputLine - {}"+inputLine);
			}

			String updateSpaceTransformationsURL2 = "http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=updateSpaceTransformations&DataInputs=source=Mouse_Yuko_1.0;destination=Mouse_WHS_0.9;hub=UCSD;transformationURL=yuko12whs09"; 

			url = new URL(updateSpaceTransformationsURL2);
			urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			inBuff = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			
			while ((inputLine = inBuff.readLine()) != null) {
				System.out.println("inputLine - {}"+inputLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
		
	}

	public Set spaceTransformationFromDB( String fromSRS, String toSRS ) {

		Set chainsList = new HashSet();
		
		UCSDServiceDAOImpl impl = new UCSDServiceDAOImpl();
		UCSDServiceVO vo1 = new UCSDServiceVO();
		UCSDServiceVO vo2 = new UCSDServiceVO();
		ArrayList list = impl.getUCSDSpaceTransformationData(vo1);

		Iterator iterator1 = list.iterator();
		Iterator iterator2 = list.iterator();
		String oldToSRS = " ";
		String chain = "";

		//Check for direct transformation
		try {
		for ( int i=0; iterator1.hasNext(); i++ ) {
			
			vo1 = (UCSDServiceVO) iterator1.next();
			
			//System.out.println("fromSRS from List: " + vo1.getTransformationSource() + ", toSRS from List: " + vo1.getTransformationDestination());
			
			if ( fromSRS == null || fromSRS.equals("") || toSRS == null ||
				  toSRS.equals("") || fromSRS.equals("all") || toSRS.equals("all")) {
				chain = vo1.getTransformationSource() + "_To_" + vo1.getTransformationDestination()+":"+vo1.getTransformationHub();
				//System.out.println("Inside empty from.." + chain);
				chainsList.add(chain);
			}
		}
		//return chainsList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set srcSet = new HashSet();
		while (iterator2.hasNext()) {
			vo2 = (UCSDServiceVO) iterator2.next();
			srcSet.add(vo2.getTransformationSource().toLowerCase());
		}

		iterator2 = list.iterator();
		Set destSet = new HashSet();
		while (iterator2.hasNext()) {
			vo2 = (UCSDServiceVO) iterator2.next();
			destSet.add(vo2.getTransformationDestination().toLowerCase());
		}

		if ( !srcSet.contains(fromSRS.toLowerCase()) ) {
			//System.out.println("*****************************NOT PRESENT in FROM***************************************");
			chain = "Error: No such transformation is available under this hub.";
			chainsList.add(chain);
		}

		if ( !destSet.contains(toSRS.toLowerCase()) ) {
			//System.out.println("*****************************NOT PRESENT in TO***************************************");
			chain = "Error: No such transformation is available under this hub.";
			chainsList.add(chain);
		}


		iterator1 = list.iterator();
		//Check for direct transformation
		for ( int i=0; iterator1.hasNext(); i++ ) {

			vo1 = (UCSDServiceVO) iterator1.next();

			if ( vo1.getTransformationSource().equalsIgnoreCase(fromSRS) &&
				 vo1.getTransformationDestination().equalsIgnoreCase(toSRS) ) {
				System.out.println("Inside Direct... fromSRS from List: " + vo1.getTransformationSource() + ", toSRS from List: " + vo1.getTransformationDestination());
				//System.out.println("Inside Direct Transformation..");
				chain = vo1.getTransformationSource() + "_To_" + vo1.getTransformationDestination()+":"+vo1.getTransformationHub();
				chainsList.add(chain);
				chain = vo1.getTransformationDestination() + "_To_" + vo1.getTransformationSource()+":"+vo1.getTransformationHub();
				chainsList.add(chain);
				return chainsList;
			}
			
		}

		//Check for indirect transformation
		iterator1 = list.iterator();
		for ( int i=0; iterator1.hasNext(); i++ ) {
			vo1 = (UCSDServiceVO) iterator1.next();
			
			
			//System.out.println("fromSRS from List: " + vo1.getTransformationSource() + ", toSRS from List: " + vo1.getTransformationDestination());
			
		if ( vo1.getTransformationDestination().equalsIgnoreCase(toSRS) ) {

					System.out.println("Inside ToSRS match..");
					if (vo1.getTransformationSource().equalsIgnoreCase(oldToSRS)) {
						System.out.println("Inside OldToSRS match. So ignore this and move on.");
						
					} else if (vo1.getTransformationSource().equalsIgnoreCase(fromSRS)) {
						System.out.println("Closing step to build chain.");
						chain = vo1.getTransformationSource() + "_To_" + vo1.getTransformationDestination()+":"+vo1.getTransformationHub();
						chainsList.add(chain);
						
						//Reverse Chain
						chain = vo1.getTransformationDestination() + "_To_" + vo1.getTransformationSource()+":"+vo1.getTransformationHub();
						chainsList.add(chain);

						System.out.println("****DONE CHAIN***" + chain);
						//break;
					} else {
						//System.out.println("Ongoing step to build chain.");
						chain = vo1.getTransformationSource() + "_To_" + vo1.getTransformationDestination()+":"+vo1.getTransformationHub();
						chainsList.add(chain);
						
						//Reverse Chain
						chain = vo1.getTransformationDestination() + "_To_" + vo1.getTransformationSource()+":"+vo1.getTransformationHub();
						chainsList.add(chain);

						toSRS = vo1.getTransformationSource();
						oldToSRS = vo1.getTransformationDestination();

						//Rewrite the list to default to go against the new toSRS value
						iterator1 = list.iterator();

						System.out.println("****ONGOING CHAIN ***" + chain);	 
					}

					//chainsList.add(chain);

				}

		}

		return chainsList;
	}

}
