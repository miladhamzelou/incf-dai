package org.incf.atlas.whs.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;

import org.incf.atlas.waxml.generated.CoordinateChainTransformType;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseDocument;
import org.incf.atlas.waxml.generated.CoordinateTransformationInfoType;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.InputType;
import org.incf.atlas.waxml.generated.ListTransformationsResponseDocument;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseType.CoordinateTransformationChain;
import org.incf.atlas.waxml.generated.ListTransformationsResponseType.TransformationList;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.QueryInfoType.QueryUrl;
import org.incf.atlas.whs.resource.Utilities;
import org.incf.atlas.whs.resource.WHSServiceVO;


public class WHSUtil {

	WHSConfigurator config = WHSConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	//FIXME - amemon - will eventually go to commons
	public String spaceTransformation( WHSServiceVO vo ) {

		System.out.println("Start - spaceTransformation Method...");
		
		String xmlResponseString = "";

		try { 

			System.out.println("Start - transformation matrix process...");

			System.out.println("****From SRSCode - " + vo.getFromSRSCodeOne());
			System.out.println("****To SRSCode - " + vo.getToSRSCodeOne());

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			WHSUtil util = new WHSUtil();

			//mouse_abavoxel_1.0 to mouse_agea_1.0
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {
				
				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else {

				xmlResponseString = "NOT SUPPORTED";
				
			}

			//End

			System.out.println( "XML Response String - " + xmlResponseString ); 
			System.out.println("Ends running transformation  matrix...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return xmlResponseString;

	}


	//FIXME - amemon - will eventually go to commons
	public String directSpaceTransformation( String fromSpace, String toSpace, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";

	System.out.println("DIRECT SPACE TRANSFORMATION...");

/*	X10 = (1/46.512)*X09 - 5.3965
	Y10 = (1/46.512)*Y09 - 11.997
	Z10 = (1/46.512)*Z09 - 5.5255

	The translation from WHS_1.0 to WHS_0.9 is, of course:

	X09 = (X10+5.3965)*46.512
	Y09 = (Y10+11.997)*46.512
	Z09 = (Z10+5.5255)*46.512
*/
	try {

		//By Ilya
		if (fromSpace.trim().equalsIgnoreCase(whs09) && toSpace.trim().equalsIgnoreCase(whs10)) {

			System.out.println("Inside WHS09 2 WHS10");
			String X10 = String.valueOf((1/46.512)*Double.parseDouble(originalCoordinateX) - 5.3965);
			String Y10 = String.valueOf((1/46.512)*Double.parseDouble(originalCoordinateY) - 11.997);
			String Z10 = String.valueOf((1/46.512)*Double.parseDouble(originalCoordinateZ) - 5.5255);

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " " + originalCoordinateZ + " " + X10 + " " + Y10 + " " + Z10;
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Ilya
		else if (fromSpace.trim().equalsIgnoreCase(whs10) && toSpace.trim().equalsIgnoreCase(whs09)) {

			System.out.println("Inside WHS10 2 WHS09");
			String X09 = String.valueOf((Double.parseDouble(originalCoordinateX)+5.3965)*46.512);
			String Y09 = String.valueOf((Double.parseDouble(originalCoordinateY)+11.997)*46.512);
			String Z09 = String.valueOf((Double.parseDouble(originalCoordinateZ)+5.5255)*46.512);

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " " + originalCoordinateZ + " " + X09 + " " + Y09 + " " + Z09;
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		} else {
		
			transformedCoordinateString = "No such transformation is available at this point under WHS hub.";
			return transformedCoordinateString;
	
		} 

	System.out.println("Ends running transformation  matrix...");

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return transformedCoordinateString;

	}

	
	//FIXME - amemon - will eventually go to commons
	public String[] getTabDelimNumbers(String tabLimNumbers ) {

		StringTokenizer tokens = new StringTokenizer(tabLimNumbers, " ");
		int tokensSize = tokens.countTokens();

		String[] coordinateString = new String[tokensSize]; 
		String[] transformedCoordinates = new String[3]; //Returned coordinates are 3
		System.out.println( " tokens - " +tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			i++;
		}

		
		if (coordinateString.length > 3) { 
			transformedCoordinates[0] = coordinateString[3];
			System.out.println( " transformedCoordinates x - " + transformedCoordinates[0] );
			if (tokensSize >= 5 ) {
			transformedCoordinates[1] = coordinateString[4];
			System.out.println( " transformedCoordinates y - " + transformedCoordinates[1] );
			}
			if (tokensSize >= 6 ) {
			transformedCoordinates[2] = coordinateString[5];
			System.out.println( " transformedCoordinates z - " + transformedCoordinates[2] );
			}
		} else if (coordinateString.length == 3) {
			transformedCoordinates[0] = coordinateString[0];
			System.out.println( " transformedCoordinates x - " + transformedCoordinates[0] );
			transformedCoordinates[1] = coordinateString[1];
			transformedCoordinates[2] = coordinateString[2];
		}
			
			
		return transformedCoordinates;

	}


	public static void main ( String args[] ) {
		WHSUtil util = new WHSUtil();
		
		StringTokenizer tokens = new StringTokenizer("Fine Structure Name: DG"); 
		while ( tokens.hasMoreTokens() ) {
			String structureName = tokens.nextToken();
			System.out.println("Structure Name is - " + structureName);
		}
		
		//util.splitCoordinatesFromStringToVO(new ABAServiceVO(), "13 12 3 4 5 6");

	}

	public WHSServiceVO splitCoordinatesFromStringToVO(WHSServiceVO vo, String completeCoordinatesString ) {

		StringTokenizer tokens = new StringTokenizer(completeCoordinatesString, " ");
		int tokensSize = tokens.countTokens();

		String[] coordinateString = new String[tokensSize]; 
		String[] transformedCoordinates = new String[6]; //Returned coordinates are 3
		System.out.println( " tokens - " +tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			System.out.println( " Token Name - " + coordinateString[i]);
			i++;
		}

/*		vo.setOriginalCoordinateX(coordinateString[0]);
		vo.setOriginalCoordinateY(coordinateString[1]);
		vo.setOriginalCoordinateZ(coordinateString[2]);
*/		vo.setTransformedCoordinateX(coordinateString[3]);
		vo.setTransformedCoordinateY(coordinateString[4]);
		vo.setTransformedCoordinateZ(coordinateString[5]);

		return vo;
		
	}

	
	//http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/structure_lookup.cgi?atlas=aba&x=264&y=160&z=228
	public String getStructureNameLookup( String atlasSpaceName, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";

	try {
	
/*		if ( atlasSpaceName.trim().equalsIgnoreCase(abaVoxel) ) {

			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.abavoxelstructure.path");
	
			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+atlasSpaceName+"&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL);
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		} */
		
		if ( atlasSpaceName.trim().equalsIgnoreCase(whs09) ) { 

			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.whsstructure.path");

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL);
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return transformedCoordinateString;

	}

	
	public String getCoordinateTransformationChain(WHSServiceVO vo) {

		System.out.println("Start - getCoordinateTransformationChain Method...");
		String responseString = "";

		try { 

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			WHSUtil util = new WHSUtil();

			//mouse_abavoxel_1.0 to mouse_agea_1.0
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}
		
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
				
				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(paxinos);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeOne(whs09);
				vo.setToSRSCodeTwo(agea);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(agea);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(paxinos);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(paxinos);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);
				vo.setFromSRSCodeThree(whs09);
				vo.setToSRSCodeThree(paxinos);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0, then mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);
				vo.setFromSRSCodeFour(whs09);
				vo.setToSRSCodeFour(paxinos);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via  mouse_whs_1.0, and then mouse_agea_1.0, then mouse_abavoxel_1.0
		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				vo.setFromSRSCodeOne(paxinos);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);
				vo.setFromSRSCodeFour(abaVoxel);
				vo.setToSRSCodeFour(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs09);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(abaVoxel);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

            //via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaVoxel);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(whs09);
				vo.setFromSRSCodeThree(whs09);
				vo.setToSRSCodeThree(whs10);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs10);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

            //via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_abavoxel_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(agea);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

	        //via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs09);
				vo.setToSRSCodeOne(agea);
				vo.setFromSRSCodeTwo(agea);
				vo.setToSRSCodeTwo(abaVoxel);
				vo.setFromSRSCodeThree(abaVoxel);
				vo.setToSRSCodeThree(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}
				
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(abaReference);
				vo.setToSRSCodeOne(abaVoxel);
				vo.setFromSRSCodeTwo(abaVoxel);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(whs09);
				vo.setFromSRSCodeFour(whs09);
				vo.setToSRSCodeFour(whs10);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}

			//via mouse_abavoxel_1.0, and then mouse_agea_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(abaReference) ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo.setFromSRSCodeOne(whs10);
				vo.setToSRSCodeOne(whs09);
				vo.setFromSRSCodeTwo(whs09);
				vo.setToSRSCodeTwo(agea);
				vo.setFromSRSCodeThree(agea);
				vo.setToSRSCodeThree(abaVoxel);
				vo.setFromSRSCodeFour(abaVoxel);
				vo.setToSRSCodeFour(abaReference);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo );
				} else {
					responseString = util.getTransformationChain( vo );
				}
				
			}

			//End

			System.out.println("Ends getSpaceTransformationChain Method...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the client in a text/xml format
		return responseString;

	}

	public String getTransformationChain( WHSServiceVO vo ) { 

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		CoordinateTransformationChainResponseDocument co =   CoordinateTransformationChainResponseDocument.Factory.newInstance();
		co.addNewCoordinateTransformationChainResponse();
		
		//Query Info
		co.getCoordinateTransformationChainResponse().addNewQueryInfo();
		QueryInfoType qi = co.getCoordinateTransformationChainResponse().getQueryInfo();
		QueryUrl url = QueryUrl.Factory.newInstance();
		url.setName("GetTransformationChain");
		url.setStringValue(vo.getUrlString());
		qi.setQueryUrl(url);
		qi.setTimeCreated(Calendar.getInstance());
	    Criteria criterias = qi.addNewCriteria();

		InputType input1 =criterias.addNewInput();
		InputStringType inputSrsConstraint = (InputStringType) input1.changeType(InputStringType.type);

		//InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
		inputSrsConstraint.setName("inputSrsName");
		inputSrsConstraint.setValue(vo.getFromSRSCode());
			
		InputType input2 =criterias.addNewInput();
		InputStringType ouputSrsConstraint  = (InputStringType) input2.changeType(InputStringType.type);
		
		//InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
		ouputSrsConstraint.setName("outputSrsName");
		ouputSrsConstraint.setValue(vo.getToSRSCode());
		
		CoordinateTransformationChain ct = co.getCoordinateTransformationChainResponse().addNewCoordinateTransformationChain();
		
/*		ObjectFactory of = new ObjectFactory();
		QueryInfo queryInfo = of.createQueryInfo();
		
		QueryURL queryURL = new QueryURL();
		queryURL.setName("GetTransformationChain");
		queryURL.setValue(vo.getUrlString());
		queryInfo.getQueryURL().add(queryURL);

		queryInfo.setTimeCreated(vo.getCurrentTime());
*/
		try { 

	 		  	//Exception handling somewhere here before going to the first transformation

	 		    String orderNumber = "";
	 		    String code = "";
	 		    String accuracy = "";
	 		    String implementingHub1 = "";
	 		    String implementingHub2 = "";
	 		    String implementingHub3 = "";
	 		    String implementingHub4 = "";
	 		    String transformationURL1 = "";
	 		    String transformationURL2 = "";
	 		    String transformationURL3 = "";
	 		    String transformationURL4 = "";

	 			String ucsdHostName = config.getValue("ucsd.host.name");
	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
	 			String ucsdPortNumber = config.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;

	 			String abaHostName = config.getValue("ucsd.host.name");
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
	 			String abaPortNumber = config.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;

	 			String whsHostName = config.getValue("ucsd.host.name");
	 			String whsServicePath = config.getValue("ucsd.whs.service.path");
	 			String whsPortNumber = config.getValue("ucsd.port.number");
	 			String whsTransformationMatrixURLPrefix = whsHostName + whsPortNumber + whsServicePath;

	 			String incfDeploymentHostName = config.getValue("incf.deploy.host.name");
	 			String incfportNumber = config.getValue("ucsd.port.number");

	 			String incfTransformationMatrixURLPrefix = incfDeploymentHostName + incfportNumber;

/* 				CoordinateTransformationChain coordinateTransformationInfo = 
 					of.createCoordinateTransformationChain();
*/
	 		    if ( vo.getFromSRSCodeOne() != null ) {
		 		    if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) ||
		 		    		vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();

		 		    	ex.setOrder(Integer.parseInt(orderNumber));
		 				
		 		    	ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());

/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) ) {
		 		  		implementingHub1 = "WHS";
		 		  		transformationURL1 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {
		 		    if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeTwo().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub2 = "UCSD";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) ) {
		 		  	
		 		    	implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs10) ) {
		 		  	
		 		    	implementingHub2 = "WHS";
		 		  		transformationURL2 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(agea) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaVoxel) ) {
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		  		implementingHub2 = "ABA";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {
		 		    if ( vo.getFromSRSCodeThree().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeThree().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub3 = "UCSD";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		//transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs10) ) {
						implementingHub3 = "WHS";
				  		transformationURL3 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
				  		vo.setTransformationThreeURL(transformationURL3);
				  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
				    	orderNumber = "3";
				    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
						ex.setOrder(Integer.parseInt(orderNumber));
						ex.setCode(code);
						ex.setHub(implementingHub3);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationThreeURL());
			/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
						coordinateTransformation.setCode(code);
						coordinateTransformation.setImplementingHub(implementingHub3);
						coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
						coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
						coordinateTransformation.setOrder(orderNumber);
						coordinateTransformation.setAccuracy(accuracy);
						coordinateTransformation.setValue(vo.getTransformationThreeURL());
						coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {
		 		    if ( vo.getFromSRSCodeFour().equalsIgnoreCase(paxinos) || 
		 		    	 vo.getToSRSCodeFour().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub4 = "UCSD";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		//transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs10) ) {
					    implementingHub4 = "WHS";
				  		transformationURL4 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
				  		vo.setTransformationFourURL(transformationURL4);
				  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
				    	orderNumber = "4";
				    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
						ex.setOrder(Integer.parseInt(orderNumber));
						ex.setCode(code);
						ex.setHub(implementingHub4);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationFourURL());
			/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
						coordinateTransformation.setCode(code);
						coordinateTransformation.setImplementingHub(implementingHub4);
						coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
						coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
						coordinateTransformation.setOrder(orderNumber);
						coordinateTransformation.setAccuracy(accuracy);
						coordinateTransformation.setValue(vo.getTransformationFourURL());
						coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setOrder(Integer.parseInt(orderNumber));
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation(); 
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }

	 			//Put individual element in the super object
/*	 			coordinateChain.setQueryInfo(queryInfo);
	 			coordinateChain.setCoordinateTransformationChain(coordinateTransformationInfo);
*/

	 			 ArrayList errorList = new ArrayList();
	 			 opt.setErrorListener(errorList);
	 			 
	 			 // Validate the XML.
	 			 boolean isValid = co.validate(opt);
	 			 
	 			 // If the XML isn't valid, loop through the listener's contents,
	 			 // printing contained messages.
	 			 if (!isValid)
	 			 {
	 			      for (int i = 0; i < errorList.size(); i++)
	 			      {
	 			          XmlError error = (XmlError)errorList.get(i);
	 			          
	 			          System.out.println("\n");
	 			          System.out.println("Message: " + error.getMessage() + "\n");
	 			          System.out.println("Location of invalid XML: " + 
	 			              error.getCursorLocation().xmlText() + "\n");
	 			      }
	 			 }
	 			 
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return co.xmlText(opt);
/*		return coordinateChain;
*/
		}

	
	public String listTransformations( WHSServiceVO vo ) { 
		
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		ListTransformationsResponseDocument co =   ListTransformationsResponseDocument.Factory.newInstance();
		co.addNewListTransformationsResponse();

		//Query Info
		co.getListTransformationsResponse().addNewQueryInfo();
		QueryInfoType qi = co.getListTransformationsResponse().getQueryInfo();
		QueryUrl url = QueryUrl.Factory.newInstance();
		url.setName("ListTransformations");
		url.setStringValue(vo.getUrlString());
		qi.setQueryUrl(url);
		qi.setTimeCreated(Calendar.getInstance());
	    Criteria criterias = qi.addNewCriteria();

		InputType input1 =criterias.addNewInput();
		InputStringType inputSrsConstraint = (InputStringType) input1.changeType(InputStringType.type);

		//InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
		inputSrsConstraint.setName("inputSrsName");
		inputSrsConstraint.setValue(vo.getFromSRSCode());
			
		InputType input2 =criterias.addNewInput();
		InputStringType ouputSrsConstraint  = (InputStringType) input2.changeType(InputStringType.type);
		
		//InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
		ouputSrsConstraint.setName("outputSrsName");
		ouputSrsConstraint.setValue(vo.getToSRSCode());
		
		 
		TransformationList ct = co.getListTransformationsResponse().addNewTransformationList();
		
/*		ObjectFactory of = new ObjectFactory();
		QueryInfo queryInfo = of.createQueryInfo();
		
		QueryURL queryURL = new QueryURL();
		queryURL.setName("GetTransformationChain");
		queryURL.setValue(vo.getUrlString());
		queryInfo.getQueryURL().add(queryURL);

		queryInfo.setTimeCreated(vo.getCurrentTime());
*/
		try { 

	 		  	//Exception handling somewhere here before going to the first transformation

	 		    String orderNumber = "";
	 		    String code = "";
	 		    String accuracy = "";
	 		    String implementingHub1 = "";
	 		    String implementingHub2 = "";
	 		    String implementingHub3 = "";
	 		    String implementingHub4 = "";
	 		    String transformationURL1 = "";
	 		    String transformationURL2 = "";
	 		    String transformationURL3 = "";
	 		    String transformationURL4 = "";

	 			String ucsdHostName = config.getValue("ucsd.host.name");
	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
	 			String ucsdPortNumber = config.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;

	 			String abaHostName = config.getValue("ucsd.host.name");
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
	 			String abaPortNumber = config.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;

	 			String whsHostName = config.getValue("ucsd.host.name");
	 			String whsServicePath = config.getValue("ucsd.whs.service.path");
	 			String whsPortNumber = config.getValue("ucsd.port.number");
	 			String whsTransformationMatrixURLPrefix = whsHostName + whsPortNumber + whsServicePath;

	 			String incfDeploymentHostName = config.getValue("incf.deploy.host.name");
	 			String incfportNumber = config.getValue("ucsd.port.number");

	 			String incfTransformationMatrixURLPrefix = incfDeploymentHostName + incfportNumber;

/* 				CoordinateTransformationChain coordinateTransformationInfo = 
 					of.createCoordinateTransformationChain();
*/
	 		    if ( vo.getFromSRSCodeOne() != null ) {
		 		    if ( vo.getFromSRSCodeOne().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();

		 		    	ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());

/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) ) {
		 		  		implementingHub1 = "WHS";
		 		  		transformationURL1 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(agea) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeOne()+";outputSrsName="+vo.getToSRSCodeOne()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCodeOne() + "_To_" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeOne()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeOne()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub1);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationOneURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {
		 		    if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(paxinos) || 
		 		    	 vo.getToSRSCodeTwo().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub2 = "UCSD";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";

		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs09) ) {
		 		  	
		 		    	implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(whs10) ) {
		 		  	
		 		    	implementingHub2 = "WHS";
		 		  		transformationURL2 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/
		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(agea) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase(abaVoxel) ) {
		 		  		transformationURL2 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeTwo()+";outputSrsName="+vo.getToSRSCodeTwo()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationTwoURL(transformationURL2);
		 		  		code = vo.getFromSRSCodeTwo() + "_To_" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		  		implementingHub2 = "ABA";
		 		  		CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub2);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeTwo()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeTwo()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationTwoURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub2);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationTwoURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {
		 		    if ( vo.getFromSRSCodeThree().equalsIgnoreCase(paxinos) ||
		 		    	 vo.getToSRSCodeThree().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub3 = "UCSD";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		//transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(whs10) ) {
						implementingHub3 = "WHS";
				  		transformationURL3 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
				  		vo.setTransformationThreeURL(transformationURL3);
				  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
				    	orderNumber = "3";
				    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
						ex.setCode(code);
						ex.setHub(implementingHub3);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationThreeURL());
			/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
						coordinateTransformation.setCode(code);
						coordinateTransformation.setImplementingHub(implementingHub3);
						coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
						coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
						coordinateTransformation.setOrder(orderNumber);
						coordinateTransformation.setAccuracy(accuracy);
						coordinateTransformation.setValue(vo.getTransformationThreeURL());
						coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(agea) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeThree()+";outputSrsName="+vo.getToSRSCodeThree()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationThreeURL(transformationURL3);
		 		  		code = vo.getFromSRSCodeThree() + "_To_" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub3);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeThree()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeThree()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationThreeURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub3);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationThreeURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {
		 		    if ( vo.getFromSRSCodeFour().equalsIgnoreCase(paxinos) || 
		 		    	 vo.getToSRSCodeFour().equalsIgnoreCase(paxinos) ) { 
		 		  		implementingHub4 = "UCSD";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		//transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(whs10) ) {
						implementingHub4 = "WHS";
				  		transformationURL4 = "http://" + whsTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
				  		vo.setTransformationFourURL(transformationURL4);
				  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
				    	orderNumber = "4";
				    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
						ex.setCode(code);
						ex.setHub(implementingHub4);
						ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
						ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
						//ex.setAccuracy(Integer.parseInt(accuracy));
						ex.setStringValue(vo.getTransformationFourURL());
			/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
						coordinateTransformation.setCode(code);
						coordinateTransformation.setImplementingHub(implementingHub4);
						coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
						coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
						coordinateTransformation.setOrder(orderNumber);
						coordinateTransformation.setAccuracy(accuracy);
						coordinateTransformation.setValue(vo.getTransformationFourURL());
						coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(agea) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	} else if ( vo.getFromSRSCodeFour().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName="+vo.getFromSRSCodeFour()+";outputSrsName="+vo.getToSRSCodeFour()+";x=;y=;z=;filter=";
		 		  		vo.setTransformationFourURL(transformationURL4);
		 		  		code = vo.getFromSRSCodeFour() + "_To_" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub4);
		 				ex.setInputSrsName(new QName(vo.getFromSRSCodeFour()));
		 				ex.setOutputSrsName(new QName(vo.getToSRSCodeFour()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationFourURL());
/*		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation(); 
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setImplementingHub(implementingHub4);
		 				coordinateTransformation.setInputSrsName(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setOutputSrsName(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformation.setValue(vo.getTransformationFourURL());
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
*/		 		  	}
	 		    }

	 			//Put individual element in the super object
/*	 			coordinateChain.setQueryInfo(queryInfo);
	 			coordinateChain.setCoordinateTransformationChain(coordinateTransformationInfo);
*/

	 			 ArrayList errorList = new ArrayList();
	 			 opt.setErrorListener(errorList);
	 			 
	 			 // Validate the XML.
	 			 boolean isValid = co.validate(opt);
	 			 
	 			 // If the XML isn't valid, loop through the listener's contents,
	 			 // printing contained messages.
	 			 if (!isValid)
	 			 {
	 			      for (int i = 0; i < errorList.size(); i++)
	 			      {
	 			          XmlError error = (XmlError)errorList.get(i);
	 			          
	 			          System.out.println("\n");
	 			          System.out.println("Message: " + error.getMessage() + "\n");
	 			          System.out.println("Location of invalid XML: " + 
	 			              error.getCursorLocation().xmlText() + "\n");
	 			      }
	 			 }
	 			 
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return co.xmlText(opt);

		}

	
}
