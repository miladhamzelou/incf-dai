package org.incf.atlas.aba.util;
	   
import org.incf.atlas.aba.resource.ABAServiceVO;
import org.incf.atlas.generated.CoordinateTransformationChain;
import org.incf.atlas.generated.CoordinateTransformationChainResponse;
import org.incf.atlas.generated.ObjectFactory;
import org.incf.atlas.generated.QueryInfo;
import org.incf.atlas.generated.CoordinateTransformationChain.CoordinateTransformation;

public class ABAUtil {

	ABAConfigurator config = ABAConfigurator.INSTANCE;
	
	public CoordinateTransformationChainResponse getCoordinateTransformationChain(ABAServiceVO vo, CoordinateTransformationChainResponse coordinateChain) {

		System.out.println("Start - getCoordinateTransformationChain Method...");
 
		try { 

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			ABAUtil util = new ABAUtil();

			//ABAVOXEL to AGEA
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );
		
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {
				
				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeOne("WHS");
				vo.setToSRSCodeTwo("AGEA");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("PAXINOS");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("ABAVOXEL");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");
				vo.setFromSRSCodeThree("WHS");
				vo.setToSRSCodeThree("PAXINOS");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");
				vo.setFromSRSCodeFour("WHS");
				vo.setToSRSCodeFour("PAXINOS");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via  WHS, and then AGEA, then abavoxel
		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				vo.setFromSRSCodeOne("PAXINOS");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("ABAVOXEL");
				vo.setFromSRSCodeFour("ABAVOXEL");
				vo.setToSRSCodeFour("ABAREFERENCE");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

            //via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("ABAREFERENCE");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

	        //via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );

			//via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");
				vo.setFromSRSCodeThree("ABAVOXEL");
				vo.setToSRSCodeThree("ABAREFERENCE");

				coordinateChain = util.getTransformationChain( vo, coordinateChain );
				
			}
			//End

			System.out.println("Ends getSpaceTransformationChain Method...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return coordinateChain;

	}

	public CoordinateTransformationChainResponse getTransformationChain( ABAServiceVO vo, CoordinateTransformationChainResponse coordinateChain ) { 

		ObjectFactory of = new ObjectFactory();
		QueryInfo queryInfo = of.createQueryInfo();
		queryInfo.setQueryUrl(vo.getUrlString());
		queryInfo.setTimeCreated(vo.getCurrentTime());

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
	 		    	
/*	 			String ucsdHostName = config.getValue("ucsd.host.name");
	 			String ucsdServicePath = config.getValue("ucsd.ucsd.service.path");
	 			String ucsdPortNumber = config.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;

	 			String abaHostName = config.getValue("ucsd.host.name");
	 			String abaServicePath = config.getValue("ucsd.aba.service.path");
	 			String abaPortNumber = config.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;
*/
 				CoordinateTransformationChain coordinateTransformationInfo = 
 					of.createCoordinateTransformationChain();

	 		    if ( vo.getFromSRSCodeOne() != null ) {
		 		    if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub1 = "UCSD";
		 		  		//transformationURL1 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub1);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") ) {
		 		  		implementingHub1 = "ABA";
		 		  		//transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub1);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);

		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") ) {
		 		  		implementingHub1 = "ABA";
		 		  		//transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub1);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);

		 		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub1 = "ABA";
		 		  		//transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";

		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub1);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);

		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") ) {
		 		  		implementingHub1 = "ABA";
		 		  		//transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub1);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeOne());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeOne());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);

		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {
		 		    if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub2 = "UCSD";
		 		  		//transformationURL2 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";

		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub2);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);

		 		    } else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("whs") ) {
		 		  		implementingHub2 = "ABA";
		 		  		//transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub2);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("agea") ) {
		 		  		implementingHub2 = "ABA";
		 		  		//transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub2);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub2 = "ABA";
		 		  		//transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub2);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("abavoxel") ) {
		 		  		//transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		  		implementingHub2 = "ABA";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub2);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeTwo());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeTwo());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {
		 		    if ( vo.getFromSRSCodeThree().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub3 = "UCSD";
		 		  		//transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub3);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("whs") ) {
		 		  		implementingHub3 = "ABA";
		 		  		//transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub3);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("agea") ) {
		 		  		implementingHub3 = "ABA";
		 		  		//transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub3);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub3 = "ABA";
		 		  		//transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub3);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abavoxel") ) {
		 		  		implementingHub3 = "ABA";
		 		  		//transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub3);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeThree());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeThree());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {
		 		    if ( vo.getFromSRSCodeFour().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub4 = "UCSD";
		 		  		//transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub4);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("whs") ) {
		 		  		implementingHub4 = "ABA";
		 		  		//transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub4);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("agea") ) {
		 		  		implementingHub4 = "ABA";
		 		  		//transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub4);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub4 = "ABA";
		 		  		//transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub4);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abavoxel") ) {
		 		  		implementingHub4 = "ABA";
		 		  		//transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation(); 
		 				coordinateTransformation.setCode(code);
		 				coordinateTransformation.setHub(implementingHub4);
		 				coordinateTransformation.setInputSrsCode(vo.getFromSRSCodeFour());
		 				coordinateTransformation.setTargetSrsCode(vo.getToSRSCodeFour());
		 				coordinateTransformation.setOrder(orderNumber);
		 				coordinateTransformation.setAccuracy(accuracy);
		 				coordinateTransformationInfo.getCoordinateTransformation().add(coordinateTransformation);
		 		  	}
	 		    }

	 			//Put individual element in the super object
	 			coordinateChain.setQueryInfo(queryInfo);
	 			coordinateChain.setCoordinateTransformationChain(coordinateTransformationInfo);

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return coordinateChain;

		}

}
