package org.incf.whs.atlas.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.wps.output.ComplexOutput;

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
import org.incf.atlas.waxml.utilities.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WHSUtil {

	WHSConfigurator config = WHSConfigurator.INSTANCE;
	private static final Logger LOG = LoggerFactory
	.getLogger(WHSUtil.class);

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	//FIXME - amemon - will eventually go to commons
	public String spaceTransformation( WHSServiceVO vo ) {

		LOG.debug("Start - spaceTransformation Method...");
		
		String xmlResponseString = "";

		try { 

			LOG.debug("Start - transformation matrix process...");

			LOG.debug("****From SRSCode - {}", vo.getFromSRSCodeOne());
			LOG.debug("****To SRSCode - {}", vo.getToSRSCodeOne());

			LOG.debug("Start - transformation matrix process...");

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

			LOG.debug( "XML Response String - {}", xmlResponseString ); 
			LOG.debug("Ends running transformation  matrix...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		LOG.debug("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return xmlResponseString;

	}


	//FIXME - amemon - will eventually go to commons
	public String directSpaceTransformation( String fromSpace, String toSpace, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";

	LOG.debug("DIRECT SPACE TRANSFORMATION...");

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

			LOG.debug("Inside WHS09 2 WHS10");
			String X10 = String.valueOf((1/46.512)*Double.parseDouble(originalCoordinateX) - 5.3965);
			String Y10 = String.valueOf((1/46.512)*Double.parseDouble(originalCoordinateY) - 11.997);
			String Z10 = String.valueOf((1/46.512)*Double.parseDouble(originalCoordinateZ) - 5.5255);

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " " + originalCoordinateZ + " " + X10 + " " + Y10 + " " + Z10;
			LOG.debug("TransformedCoordinateString - {}",transformedCoordinateString);

		}

		//By Ilya
		else if (fromSpace.trim().equalsIgnoreCase(whs10) && toSpace.trim().equalsIgnoreCase(whs09)) {

			LOG.debug("Inside WHS10 2 WHS09");
			String X09 = String.valueOf((Double.parseDouble(originalCoordinateX)+5.3965)*46.512);
			String Y09 = String.valueOf((Double.parseDouble(originalCoordinateY)+11.997)*46.512);
			String Z09 = String.valueOf((Double.parseDouble(originalCoordinateZ)+5.5255)*46.512);

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " " + originalCoordinateZ + " " + X09 + " " + Y09 + " " + Z09;
			LOG.debug("TransformedCoordinateString - {}",transformedCoordinateString);

		} else {
		
			transformedCoordinateString = "No such transformation is available at this point under WHS hub.";
			return transformedCoordinateString;
	
		} 

	LOG.debug("Ends running transformation  matrix...");

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
		LOG.debug( " tokens - {}",tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			i++;
		}

		
		if (coordinateString.length > 3) { 
			transformedCoordinates[0] = coordinateString[3];
			LOG.debug( " transformedCoordinates x - {}", transformedCoordinates[0] );
			if (tokensSize >= 5 ) {
			transformedCoordinates[1] = coordinateString[4];
			LOG.debug( " transformedCoordinates y - {}", transformedCoordinates[1] );
			}
			if (tokensSize >= 6 ) {
			transformedCoordinates[2] = coordinateString[5];
			LOG.debug( " transformedCoordinates z - {}", transformedCoordinates[2] );
			}
		} else if (coordinateString.length == 3) {
			transformedCoordinates[0] = coordinateString[0];
			LOG.debug( " transformedCoordinates x - {}", transformedCoordinates[0] );
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
			LOG.debug("Structure Name is - {}", structureName);
		}
		
		//util.splitCoordinatesFromStringToVO(new ABAServiceVO(), "13 12 3 4 5 6");

	}

	public WHSServiceVO splitCoordinatesFromStringToVO(WHSServiceVO vo, String completeCoordinatesString ) {

		StringTokenizer tokens = new StringTokenizer(completeCoordinatesString, " ");
		int tokensSize = tokens.countTokens();

		String[] coordinateString = new String[tokensSize]; 
		String[] transformedCoordinates = new String[6]; //Returned coordinates are 3
		LOG.debug( " tokens - {}",tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			LOG.debug( " Token Name - {}", coordinateString[i]);
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
	
		if ( atlasSpaceName.trim().equalsIgnoreCase(whs09) ) { 

			String transformationHostName = config.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = config.getValue("incf.transformationservice.port.number");
			String transformationServicePath = config.getValue("incf.transformationservice.whsstructure.path");

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			LOG.debug("Transformation matrix url is - {}", transforMatrixURL);
			LOG.debug("X in transformation matrix method is - {}", originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				LOG.debug("inputLine - {}",inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			LOG.debug("TransformedCoordinateString - {}",transformedCoordinateString);

			// Start - Changes
			if (transformedCoordinateString == null || transformedCoordinateString.equals("")) {
				transformedCoordinateString = "No structure found";
			} else if(transformedCoordinateString != null && transformedCoordinateString.trim().endsWith("range.")) {
				transformedCoordinateString = "Out of range";
			} else if (transformedCoordinateString.trim().equals("-")) {
				transformedCoordinateString = "No structure found";
			} else {
				transformedCoordinateString = "Structure Name: "
					.concat(transformedCoordinateString);
			}
			// End - Changes
			
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

	
	public String getCoordinateTransformationChain(WHSServiceVO vo, ComplexOutput co) {

		LOG.debug("Start - getCoordinateTransformationChain Method...");
		String responseString = "";

		try { 

			LOG.debug("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			WHSUtil util = new WHSUtil();
			ArrayList srsCodeList = new ArrayList();
			WHSServiceVO vo1 = new WHSServiceVO();

			//mouse_abavoxel_1.0 to mouse_agea_1.0
			/*if ( vo.getFromSRSCodeOne().equalsIgnoreCase(abaVoxel) && vo.getToSRSCodeOne().equalsIgnoreCase(agea) ) {

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

			} */ if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs09) && vo.getToSRSCodeOne().equalsIgnoreCase(whs10) ) {

				vo1 = new WHSServiceVO();
				vo1.setFromSRSCode(whs09);
				vo1.setToSRSCode(whs10);
				srsCodeList.add(vo1);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo, co, srsCodeList );
				} else {
					responseString = util.getTransformationChain( vo, co, srsCodeList );
				}

			//via mouse_whs_1.0
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(whs09) ) {

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo, co, srsCodeList );
				} else {
					responseString = util.getTransformationChain( vo, co, srsCodeList );
				}

			} /*else if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(paxinos) ) {

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
				
			} */ else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("all") && vo.getToSRSCodeOne().equalsIgnoreCase("all") ) {

				//First convert from mouse_paxinos_1.0 to mouse_whs_1.0 
				vo1 = new WHSServiceVO();
				vo1.setFromSRSCode(whs09);
				vo1.setToSRSCode(whs10);
				srsCodeList.add(vo1);
				vo1 = new WHSServiceVO();
				vo1.setFromSRSCode(whs10);
				vo1.setToSRSCode(whs09);
				srsCodeList.add(vo1);

				if (vo.getFlag().equalsIgnoreCase("ListTransformations")) { 
					responseString = util.listTransformations( vo, co, srsCodeList );
				} else {
					responseString = util.getTransformationChain( vo, co, srsCodeList );
				}

			} else {
				responseString = "Error: No such transformation chain is supported under this hub."; 
			}

			//End

			LOG.debug("Ends getSpaceTransformationChain Method...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		LOG.debug("End - spaceTransformationForm Method...");

		//4) Return response back to the client in a text/xml format
		return responseString;

	}

	public String getTransformationChain( WHSServiceVO vo, ComplexOutput complexOutput, ArrayList srsCodeList ) { 

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
		Utilities.addInputStringCriteria(criterias, "filter", vo.getFilter());

		CoordinateTransformationChain ct = co.getCoordinateTransformationChainResponse().addNewCoordinateTransformationChain();
		
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

	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
	 			String whsServicePath = config.getValue("ucsd.whs.service.path");
	 			String incfDeploymentHostName = vo.getIncfDeployHostname();
	 			String incfportNumber = config.getValue("incf.deploy.port.delimitor")+vo.getIncfDeployPortNumber();

	 			String incfTransformationMatrixURLPrefix = incfDeploymentHostName + incfportNumber;

	 		    LOG.debug("Inside All Transformations....");
	 			Iterator iterator = srsCodeList.iterator();
	 			vo = null;

	 			int i = 0;

	 			while ( iterator.hasNext() ) {
	 			i++;
	 			vo = (WHSServiceVO)iterator.next();
	 		    	if ( vo.getFromSRSCode().equalsIgnoreCase(whs10) && 
		 		    		 vo.getToSRSCode().equalsIgnoreCase(whs09) || 
		 		    		 vo.getFromSRSCode().equalsIgnoreCase(whs09) && 
		 		    		 vo.getToSRSCode().equalsIgnoreCase(whs10) ) {

		 		  		implementingHub1 = "WHS";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
		 		    	orderNumber = String.valueOf(i);

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 		    	//CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);

		 				//ex.setOrder(Integer.parseInt(orderNumber));
		 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
		 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(paxinos) ||
		 		    	vo.getToSRSCode().equalsIgnoreCase(paxinos) ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
		 		    	orderNumber = String.valueOf(i);

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();

		 		    	ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				//ex.setOrder(Integer.parseInt(orderNumber));
		 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
		 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());

		 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(whs09) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
		 		    	orderNumber = String.valueOf(i);

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				//ex.setOrder(Integer.parseInt(orderNumber));
		 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
		 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(agea) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
		 		    	orderNumber = String.valueOf(i);

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				//ex.setOrder(Integer.parseInt(orderNumber));
		 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
		 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(abaReference) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
		 		    	orderNumber = String.valueOf(i);

		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				//ex.setOrder(Integer.parseInt(orderNumber));
		 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
		 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		  	} else if ( vo.getFromSRSCode().equalsIgnoreCase(abaVoxel) ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
		 		  		vo.setTransformationOneURL(transformationURL1);
		 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
		 		    	orderNumber = String.valueOf(i);
		 		    	
		 		    	CoordinateChainTransformType ex = ct.addNewCoordinateTransformation();
		 				ex.setCode(code);
		 				ex.setHub(implementingHub1);
		 				//ex.setOrder(Integer.parseInt(orderNumber));
		 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
		 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
		 				//ex.setAccuracy(Integer.parseInt(accuracy));
		 				ex.setStringValue(vo.getTransformationOneURL());
		 		  	}
	 			}

	 			 ArrayList errorList = new ArrayList();
	 			 opt.setErrorListener(errorList);
	 			 
	 			 // Validate the XML.
	 			 boolean isValid = co.validate(opt);

	 			XMLStreamReader reader = co.newXMLStreamReader();
	 			XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
	 			XMLAdapter.writeElement(writer, reader);
	 			 
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return co.xmlText(opt);
/*		return coordinateChain;
*/
		}

	
	public String listTransformations( WHSServiceVO vo, ComplexOutput complexOutput, ArrayList srsCodeList ) { 

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
		Utilities.addInputStringCriteria(criterias, "filter", vo.getFilter());

		 
		TransformationList ct = co.getListTransformationsResponse().addNewTransformationList();
		ct.setHubCode("WHS");
		
		try { 

 		    String orderNumber = "";
 		    String code = "";
 		    String accuracy = "";
 		    String implementingHub1 = "";
 		    String transformationURL1 = "";

 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
 			String abaServicePath = config.getValue("ucsd.aba.service.path");
 			String whsServicePath = config.getValue("ucsd.whs.service.path");
 			String incfDeploymentHostName = vo.getIncfDeployHostname();
 			String incfportNumber = config.getValue("incf.deploy.port.delimitor")+vo.getIncfDeployPortNumber();

 			String incfTransformationMatrixURLPrefix = incfDeploymentHostName + incfportNumber;

 		    LOG.debug("Inside All Transformations....");
 			Iterator iterator = srsCodeList.iterator();
 			vo = null;

 			int i = 0;

 			while ( iterator.hasNext() ) {
 			i++;
 			vo = (WHSServiceVO)iterator.next();
 		    	if ( vo.getFromSRSCode().equalsIgnoreCase(whs10) && 
	 		    		 vo.getToSRSCode().equalsIgnoreCase(whs09) || 
	 		    		 vo.getFromSRSCode().equalsIgnoreCase(whs09) && 
	 		    		 vo.getToSRSCode().equalsIgnoreCase(whs10) ) {

	 		  		implementingHub1 = "WHS";
	 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + whsServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
	 		  		vo.setTransformationOneURL(transformationURL1);
	 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
	 		    	orderNumber = String.valueOf(i);

	 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
	 				ex.setCode(code);
	 				ex.setHub(implementingHub1);
	 				
	 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
	 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationOneURL());
	 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(paxinos) ||
	 		    	 vo.getToSRSCode().equalsIgnoreCase(paxinos) ) {
	 		  		implementingHub1 = "UCSD";
	 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + ucsdServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
	 		  		vo.setTransformationOneURL(transformationURL1);
	 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
	 		    	orderNumber = String.valueOf(i);

	 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();

	 		    	ex.setCode(code);
	 				ex.setHub(implementingHub1);
	 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
	 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationOneURL());

	 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(whs09) ) {
	 		  		implementingHub1 = "ABA";
	 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
	 		  		vo.setTransformationOneURL(transformationURL1);
	 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
	 		    	orderNumber = String.valueOf(i);

	 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
	 				ex.setCode(code);
	 				ex.setHub(implementingHub1);
	 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
	 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationOneURL());
	 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(agea) ) {
	 		  		implementingHub1 = "ABA";
	 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
	 		  		vo.setTransformationOneURL(transformationURL1);
	 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
	 		    	orderNumber = String.valueOf(i);

	 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
	 				ex.setCode(code);
	 				ex.setHub(implementingHub1);
	 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
	 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationOneURL());
	 		    } else if ( vo.getFromSRSCode().equalsIgnoreCase(abaReference) ) {
	 		  		implementingHub1 = "ABA";
	 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
	 		  		vo.setTransformationOneURL(transformationURL1);
	 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
	 		    	orderNumber = String.valueOf(i);

	 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
	 				ex.setCode(code);
	 				ex.setHub(implementingHub1);
	 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
	 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationOneURL());
	 		  	} else if ( vo.getFromSRSCode().equalsIgnoreCase(abaVoxel) ) {
	 		  		implementingHub1 = "ABA";
	 		  		transformationURL1 = "http://" + incfTransformationMatrixURLPrefix + abaServicePath + "service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode="+vo.getFromSRSCode()+"_To_"+vo.getToSRSCode()+"_v1.0;x=;y=;z=";
	 		  		vo.setTransformationOneURL(transformationURL1);
	 		  		code = vo.getFromSRSCode() + "_To_" + vo.getToSRSCode()+"_v1.0"; 
	 		    	orderNumber = String.valueOf(i);
	 		    	
	 		    	CoordinateTransformationInfoType ex = ct.addNewCoordinateTransformation();
	 				ex.setCode(code);
	 				ex.setHub(implementingHub1);
	 				//ex.setInputSrsName(new QName(vo.getFromSRSCode()));
	 				//ex.setOutputSrsName(new QName(vo.getToSRSCode()));
	 				//ex.setAccuracy(Integer.parseInt(accuracy));
	 				ex.setStringValue(vo.getTransformationOneURL());
	 		  	}
 			}

	 			 ArrayList errorList = new ArrayList();
	 			 opt.setErrorListener(errorList);

	 			 // Validate the XML.
	 			 boolean isValid = co.validate(opt);

	  			XMLStreamReader reader = co.newXMLStreamReader();
	 			XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
	 			XMLAdapter.writeElement(writer, reader);

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return co.xmlText(opt);

		}

	
}
