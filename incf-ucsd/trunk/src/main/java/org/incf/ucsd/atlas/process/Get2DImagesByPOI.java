package org.incf.ucsd.atlas.process;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.output.ComplexOutput;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.Corners.Corner;
import org.incf.atlas.waxml.generated.Image2DType.ImagePosition;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.incf.ucsd.atlas.util.UCSDConfigurator;
import org.incf.ucsd.atlas.util.UCSDServiceVO;
import org.incf.ucsd.atlas.util.UCSDUtil;
import org.incf.ucsd.atlas.util.XMLUtilities;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class Get2DImagesByPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(Get2DImagesByPOI.class);

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	// private String dataInputString;
	// private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String filter = "";
	// URI uri = null;
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {

		try {

			UCSDServiceVO vo = new UCSDServiceVO();
			String paxinosCoordinatesString = "";
			String abareferenceCoordinatesString = "";
			UCSDUtil util = new UCSDUtil();
			XMLUtilities xmlUtilities = new XMLUtilities();
			ArrayList completeImageList = new ArrayList();

			// validate against allowed values in process definition file
			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");

			// get validated data inputs or default values
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));

			String srsName = dataInputHandler.getValidatedStringValue(in,
					"srsName");
			filter = dataInputHandler.getValidatedStringValue(in, "filter");
			double x = DataInputHandler.getDoubleInputValue(in, "x");
			double y = DataInputHandler.getDoubleInputValue(in, "y");
			double z = DataInputHandler.getDoubleInputValue(in, "z");
			String tolerance = dataInputHandler.getValidatedStringValue(in,
					"tolerance");

			LOG.debug("srsName Before ");
			// parse dataInputs string
			vo.setFromSRSCodeOne(srsName);
			vo.setFromSRSCode(srsName);
			vo.setOriginalCoordinateX(String.valueOf(x));
			vo.setOriginalCoordinateY(String.valueOf(y));
			vo.setOriginalCoordinateZ(String.valueOf(z));
			vo.setFilter(filter.replace("maptype:", ""));
			vo.setTolerance(tolerance);

			LOG.debug("Filter Before {}", filter);
			filter = vo.getFilter();
			LOG.debug("Filter After {}", filter);

			LOG.debug("Before getPaxinosImageList");

			// Convert the coordinates ABAVOXEL into PAXINOS
			if (vo.getFromSRSCode().equalsIgnoreCase(abaReference)
					|| vo.getFromSRSCode().equalsIgnoreCase(paxinos)) {
				vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
				vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
				vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
			} else {
				// Call getTransformationChain method here...
				// PAXINOS
				vo.setOriginalCoordinateX(";x=" + vo.getOriginalCoordinateX());
				vo.setOriginalCoordinateY(";y=" + vo.getOriginalCoordinateY());
				vo.setOriginalCoordinateZ(";z=" + vo.getOriginalCoordinateZ());
				vo.setToSRSCode(paxinos);

				// Construct GetTransformationChain URL
				// Start - FIXME - Uncomment below two lines and comment the
				// other three lines
				// String hostName = uri.getHost();
				// String portNumber = delimitor + uri.getPort();
				String delimitor = config
						.getValue("incf.deploy.port.delimitor");
				hostName = config.getValue("incf.deploy.host.name");
				portNumber = config.getValue("incf.aba.port.number");
				portNumber = delimitor + portNumber;
				// End - FIXME

				//central/atlas?
				String servicePath = "/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="
						+ vo.getFromSRSCode()
						+ ";outputSrsName="
						+ vo.getToSRSCode() + ";filter=NONE";
				String transformationChainURL = "http://" + hostName
						+ portNumber + servicePath;
				paxinosCoordinatesString = xmlUtilities
						.coordinateTransformation(transformationChainURL, vo
								.getOriginalCoordinateX(), vo
								.getOriginalCoordinateY(), vo
								.getOriginalCoordinateZ());

				// Start - exception handling
				if (paxinosCoordinatesString.startsWith("Error:")) {
					// LOG.debug("********************ERROR*********************");
					System.out
							.println("********************ERROR*********************");
					throw new OWSException("Transformed Coordinates Error: ",
							paxinosCoordinatesString);
				}
				// End - exception handling

				String[] tempArray = util
						.getTabDelimNumbers(paxinosCoordinatesString);
				vo.setTransformedCoordinateX(tempArray[0]);
				vo.setTransformedCoordinateY(tempArray[1]);
				vo.setTransformedCoordinateZ(tempArray[2]);

				completeImageList = getPaxinosImageList(vo.getFromSRSCode(),
						"paxinos", vo.getTransformedCoordinateX(), vo
								.getTransformedCoordinateY(), vo
								.getTransformedCoordinateZ(), vo.getFilter(),
						vo.getTolerance());

				// ABAREFERENCE Images
				vo.setToSRSCode(abaReference);

				// Construct GetTransformationChain URL
				//central/atlas
				servicePath = "/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="
						+ vo.getFromSRSCode()
						+ ";outputSrsName="
						+ vo.getToSRSCode() + ";filter=NONE";
				transformationChainURL = "http://" + hostName + portNumber
						+ servicePath;
				abareferenceCoordinatesString = xmlUtilities
						.coordinateTransformation(transformationChainURL, vo
								.getOriginalCoordinateX(), vo
								.getOriginalCoordinateY(), vo
								.getOriginalCoordinateZ());

				// Start - exception handling
				if (abareferenceCoordinatesString.startsWith("Error:")) {
					System.out
							.println("********************ERROR*********************");
					throw new OWSException("Transformed Coordinates Error: ",
							abareferenceCoordinatesString);
				}
				// End - exception handling

				String[] tempArray1 = util
						.getTabDelimNumbers(abareferenceCoordinatesString);
				vo.setTransformedCoordinateX(tempArray1[0]);
				vo.setTransformedCoordinateY(tempArray1[1]);
				vo.setTransformedCoordinateZ(tempArray1[2]);

				LOG.debug("Before getABAReferenceImageList");
				completeImageList = getABAReferenceImageList(completeImageList,
						vo.getFromSRSCode(), "abareference", vo
								.getTransformedCoordinateX(), vo
								.getTransformedCoordinateY(), vo
								.getTransformedCoordinateZ(), vo.getFilter(),
						vo.getTolerance());

			}

			if (completeImageList.size() < 0) {
				System.out
						.println("********************ERROR*********************");
				throw new OWSException("No images found within "
						+ vo.getTolerance() + " mm.",
						ControllerException.NO_APPLICABLE_CODE);
			}
			// Image Source URL for WMS-GETMAP:
			// http://image.wholebraincatalog.org/cgi-bin/mapserv?map=crbsatlas/mapfiles/asyn1_montage_downsampled1280384354411.map&LAYERS=BNSTBregma0_265x01_warped-ms&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX=0.240823,-9.24046,4.53082,-3.99522

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			java.util.Date date = new java.util.Date();
			String currentTime = dateFormat.format(date);
			vo.setCurrentTime(currentTime);

			// Generating 2 random number to be used as GMLID
			Random randomGenerator1 = new Random();
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID1 = randomGenerator1.nextInt(100);
			}
			Random randomGenerator2 = new Random();
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID2 = randomGenerator2.nextInt(100);
			}
			LOG.debug("Random GML ID1: - {}", randomGMLID1);
			LOG.debug("Random GML ID2: - {}", randomGMLID2);

			LOG.debug("Random GML ID2: - {}", randomGMLID2);
			// vo.setUrlString(uri.toString());

/*			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

*/			ImagesResponseDocument document = ImagesResponseDocument.Factory
					.newInstance();

			ImagesResponseType imagesRes = document.addNewImagesResponse();
			// QueryInfo and criteria should be done as a utility
			// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
			/*
			 * QueryInfoType query = imagesRes.addNewQueryInfo();
			 * query.setTimeCreated(Calendar.getInstance());
			 * Utilities.addMethodNameToQueryInfo(query,"Get2DImagesByPOI",
			 * uri.toString());
			 * 
			 * Criteria criterias = query.addNewCriteria();
			 * 
			 * InputStringType xCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * xCriteria.setName("x");
			 * xCriteria.setValue(vo.getOriginalCoordinateX().replace(";x=",
			 * ""));
			 * 
			 * InputStringType yCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * yCriteria.setName("y");
			 * yCriteria.setValue(vo.getOriginalCoordinateY().replace(";y=",
			 * ""));
			 * 
			 * InputStringType zCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * zCriteria.setName("z");
			 * zCriteria.setValue(vo.getOriginalCoordinateZ().replace(";z=",
			 * "")); InputStringType filterCodeCriteria = (InputStringType)
			 * criterias .addNewInput().changeType(InputStringType.type);
			 * filterCodeCriteria.setName("filter");
			 * filterCodeCriteria.setValue(vo.getFilter()); InputStringType
			 * toleranceCodeCriteria = (InputStringType) criterias
			 * .addNewInput().changeType(InputStringType.type);
			 * toleranceCodeCriteria.setName("Tolerance");
			 * toleranceCodeCriteria.setValue(vo.getTolerance());
			 */
			Image2Dcollection images = imagesRes.addNewImage2Dcollection();
			images.setHubCode("UCSD");
			
			String servicePath = "/cgi-bin/mapserv?map=crbsatlas/mapfiles/";
			String imageServerHostname = config
					.getValue("incf.imageserver.host.name");
			Iterator iterator = completeImageList.iterator();
			String wmsURL = "";
			int count = 0;
			while (iterator.hasNext()) {
				count++;
				vo = (UCSDServiceVO) iterator.next();
				// wmsURL =
				// "http://"+imageServerHostname+servicePath+vo.getImageServiceName()+".map&LAYERS="+vo.getImageBaseName()+"&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX="+vo.getMinX()+","+vo.getMinY()+","+vo.getMaxX()+","+vo.getMaxY();
				wmsURL = "http://" + imageServerHostname + servicePath
						+ vo.getImageServiceName() + ".map&LAYERS="
						+ vo.getImageBaseName()
						+ "&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap"
						+ "&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX=";
				Image2DType image1 = images.addNewImage2D();
				ImageSource i1source = image1.addNewImageSource();
				i1source.setStringValue(wmsURL);
				i1source.setFormat(IncfRemoteFormatEnum.IMAGE_PNG.toString());
				// i1source.setRelavance((float) 0.6);
				i1source.setSrsName(vo.getFlag());
				// i1source.setThumbnanil("http://example.com/image.jpg");
				// i1source.setMetadata("URL");
				i1source.setType("WMS");
				// i1source.setType(IncfImageServicesEnum.URL.toString());

				LOG.debug("coefficientA - {}", vo.getCoefficientA());
				LOG.debug("srsName - {}", srsName);

				ImagePosition i1position = image1.addNewImagePosition();
				IncfSrsType planeequation = i1position
						.addNewImagePlaneEquation();
				planeequation.setSrsName(vo.getFlag());
				planeequation.setStringValue(vo.getCoefficientA() + " "
						+ vo.getCoefficientB() + " " + vo.getCoefficientC()
						+ " " + vo.getCoefficientD());
				IncfSrsType placement = i1position.addNewImagePlanePlacement();
				placement.setSrsName(vo.getFlag());
				placement.setStringValue(vo.getTfw1() + " " + vo.getTfw2()
						+ " " + vo.getTfw3() + " " + vo.getTfw4() + " "
						+ vo.getTfw5() + " " + vo.getTfw6());
				Corners corners = i1position.addNewCorners();

				Corner corner1 = corners.addNewCorner();
				corner1.setPosition(PositionEnum.TOPLEFT);
				// corner1.addNewPoint().addNewPos().setSrsName(srsName);
				/*
				 * LOG.debug("10.1");
				 * corner1.getPoint().getPos().setSrsName("Mouse");
				 * LOG.debug("11");
				 * corner1.getPoint().getPos().setSrsName("Mouse_ABAvoxel");
				 * LOG.debug("12");
				 * corner1.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
				 * LOG.debug("13");
				 */

				LOG.debug("1st corner - filter - {}", filter);
				if (filter.equalsIgnoreCase("coronal")) {
					corner1.addNewPoint().addNewPos().setStringValue(
							vo.getSliceConstant() + " " + vo.getMaxY() + " "
									+ vo.getMinX());
				} else if (filter.equalsIgnoreCase("sagittal")) {
					corner1.addNewPoint().addNewPos().setStringValue(
							vo.getMinX() + " " + vo.getMaxY() + " "
									+ vo.getSliceConstant());
				}
				corner1.getPoint().setId("image" + count + "topleft");
				corner1.getPoint().getPos().setSrsName(vo.getFlag());

				Corner corner2 = corners.addNewCorner();
				corner2.setPosition(PositionEnum.BOTTOMLEFT);
				// corner2.addNewPoint().addNewPos().setSrsName(srsName);
				// corner2.getPoint().getPos().setSrsName(srsName);

				LOG.debug("2nd corner - filter - {}", filter);
				// corner2.addNewPoint().addNewPos().setStringValue(
				// vo.getMinX() + " " + vo.getMinY() );
				if (filter.equalsIgnoreCase("coronal")) {
					corner2.addNewPoint().addNewPos().setStringValue(
							vo.getSliceConstant() + " " + vo.getMinY() + " "
									+ vo.getMinX());
				} else if (filter.equalsIgnoreCase("sagittal")) {
					corner2.addNewPoint().addNewPos().setStringValue(
							vo.getMinX() + " " + vo.getMinY() + " "
									+ vo.getSliceConstant());
				}
				corner2.getPoint().getPos().setSrsName(vo.getFlag());
				corner2.getPoint().setId("image" + count + "bottomleft");

				Corner corner3 = corners.addNewCorner();
				corner3.setPosition(PositionEnum.TOPRIGHT);
				// corner3.addNewPoint().addNewPos().setSrsName(srsName);
				// corner3.getPoint().getPos().setSrsName(srsName);

				LOG.debug("3rd corner - filter - {}", filter);
				// corner3.addNewPoint().addNewPos().setStringValue(
				// vo.getMaxX() + " " + vo.getMaxY() );
				if (filter.equalsIgnoreCase("coronal")) {
					corner3.addNewPoint().addNewPos().setStringValue(
							vo.getSliceConstant() + " " + vo.getMaxY() + " "
									+ vo.getMaxX());
				} else if (filter.equalsIgnoreCase("sagittal")) {
					corner3.addNewPoint().addNewPos().setStringValue(
							vo.getMaxX() + " " + vo.getMaxY() + " "
									+ vo.getSliceConstant());
				}
				corner3.getPoint().getPos().setSrsName(vo.getFlag());
				corner3.getPoint().setId("image" + count + "topright");

				Corner corner4 = corners.addNewCorner();
				corner4.setPosition(PositionEnum.BOTTOMRIGHT);
				// corner4.addNewPoint().addNewPos().setSrsName(srsName);
				// corner4.getPoint().getPos().setSrsName(srsName);

				LOG.debug("4th corner - filter - {}", filter);
				// corner4.addNewPoint().addNewPos().setStringValue(
				// vo.getMaxX() + " " + vo.getMinY() );
				if (filter.equalsIgnoreCase("coronal")) {
					corner4.addNewPoint().addNewPos().setStringValue(
							vo.getSliceConstant() + " " + vo.getMinY() + " "
									+ vo.getMaxX());
				} else if (filter.equalsIgnoreCase("sagittal")) {
					corner4.addNewPoint().addNewPos().setStringValue(
							vo.getMaxX() + " " + vo.getMinY() + " "
									+ vo.getSliceConstant());
				}
				corner4.getPoint().getPos().setSrsName(vo.getFlag());
				corner4.getPoint().setId("image" + count + "bottomright");

			}

			ArrayList errorList = new ArrayList();
			//opt.setErrorListener(errorList);
			//boolean isValid = document.validate(opt);

			// If the XML isn't valid, loop through the listener's contents,
			// printing contained messages.
/*			if (!isValid) {
				for (int i = 0; i < errorList.size(); i++) {
					XmlError error = (XmlError) errorList.get(i);

					LOG.debug("\n");
					LOG.debug("Message: " + error.getMessage() + "\n");
					LOG.debug("Location of invalid XML: "
							+ error.getCursorLocation().xmlText() + "\n");
				}
			}
*/
			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("Get2DImagesByPOIOutput");

			// get reader on document; reader --> writer
			XMLStreamReader reader = document.newXMLStreamReader();
			XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
			XMLAdapter.writeElement(writer, reader);

		} catch (MissingParameterException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidParameterValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidDataInputValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(e); // is already OWSException
		} catch (OWSException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(e); // is already OWSException
		} catch (Throwable e) {
			String message = "Unexpected exception occurred: " + e.getMessage();
			LOG.error(message, e);
			throw new ProcessletException(new OWSException(message, e,
					ControllerException.NO_APPLICABLE_CODE));
		}

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	public ArrayList getPaxinosImageList(String fromSpaceName,
			String toSpaceName, String transformedCoordinateX,
			String transformedCoordinateY, String transformedCoordinateZ,
			String filterValue, String tolerance) {

		UCSDConfigurator config = UCSDConfigurator.INSTANCE;
		String hostName = config.getValue("ucsd.webservice.host.name");
		String port = config.getValue("ucsd.webservice.port.number");
		LOG.debug(" - hostName - {}", hostName);
		LOG.debug(" - PortNumber - {}", port);
		StringBuffer responseString = new StringBuffer();
		ArrayList list = new ArrayList();

		// FIXME - It is used for database "SPACE" value
		String tempCoordinateString = "";
		String srscode = "PAXINOS";
		UCSDServiceVO vo = new UCSDServiceVO();
		UCSDUtil util = new UCSDUtil();

		try {

			String webserviceName = "ImageMetadataForROI";
			String methodName = "get2DImageListForROI";

			// Create an arbitrary polygon with + or - 3
			String x1 = transformedCoordinateX;
			String x2 = String.valueOf(Double
					.parseDouble(transformedCoordinateX)
					+ Double.parseDouble(tolerance));
			String x3 = String.valueOf(Double
					.parseDouble(transformedCoordinateX)
					+ Double.parseDouble(tolerance));
			String x4 = transformedCoordinateX;
			String y1 = transformedCoordinateY;
			String y2 = transformedCoordinateY;
			String y3 = String.valueOf(Double
					.parseDouble(transformedCoordinateY)
					- Double.parseDouble(tolerance));
			String y4 = String.valueOf(Double
					.parseDouble(transformedCoordinateY)
					- Double.parseDouble(tolerance));

			String polygonString = x1 + "," + y1 + "," + x2 + "," + y2 + ","
					+ x3 + "," + y3 + "," + x4 + "," + y4 + "," + x1 + "," + y1;
			LOG.debug("PAXINOS - Made up polygon string - {}"
					,polygonString);

			String xmlQueryString = createXMLQueryStringForImageList(
					polygonString, srscode, filterValue);

			// Starts - Webserviceclient code
			String endpoint = "http://" + hostName + port + "/axis/services/"
					+ webserviceName;
			Service service = new Service();
			Call call = (Call) service.createCall();
			QName operationName = new QName(endpoint, methodName);
			call.setOperationName(operationName);
			call.setTargetEndpointAddress(endpoint);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlQueryString);

			responseString.append((String) call.invoke(new Object[] { sb
					.toString() }));

			LOG.debug("Response String - {}", responseString);
			// Ends

			list = parseXMLResponseStringForImageList(toSpaceName, list, responseString
					.toString());
			Iterator iterator = list.iterator();
			vo = new UCSDServiceVO();

			while (iterator.hasNext()) {
				vo = (UCSDServiceVO) iterator.next();
				LOG.debug("WMS - {}", vo.getWms());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public ArrayList getABAReferenceImageList(ArrayList completeImageList,
			String fromSpaceName, String toSpaceName,
			String transformedCoordinateX, String transformedCoordinateY,
			String transformedCoordinateZ, String filterValue, String tolerance) {

		UCSDConfigurator config = UCSDConfigurator.INSTANCE;
		UCSDUtil util = new UCSDUtil();

		String hostName = config.getValue("ucsd.webservice.host.name");
		String port = config.getValue("ucsd.webservice.port.number");
		LOG.debug(" - hostName - {}", hostName);
		LOG.debug(" - PortNumber - {}", port);
		StringBuffer responseString = new StringBuffer();

		try {

			UCSDServiceVO vo = new UCSDServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode(toSpaceName);
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne(toSpaceName);

			// Convert the coordinates ABAVoxel into ABAReference
			/*
			 * String tempCoordinateString1 = incfUtil.spaceTransformation(vo);
			 * String[] tempArray1 = incfUtil
			 * .getTabDelimNumbers(tempCoordinateString1);
			 * 
			 * coordinateX = incfUtil.getRoundCoordinateValue(tempArray1[0]);
			 * coordinateY = incfUtil.getRoundCoordinateValue(tempArray1[1]);
			 * coordinateZ = incfUtil.getRoundCoordinateValue(tempArray1[2]);
			 * 
			 * LOG.debug("***Coordinate X for Polygon String - " +
			 * coordinateX);
			 * LOG.debug("***Coordinate Y for Polygon String - " +
			 * coordinateY);
			 * LOG.debug("***Coordinate Z for Polygon String - " +
			 * coordinateZ);
			 */
			// FIXME
			String webserviceName = "ImageMetadataForROI";
			String methodName = "get2DImageListForROI";
			String srscode = "ABA_REFERENCE";

			// Create an arbitrary polygon with + or - 3
			String x1 = transformedCoordinateX;
			String x2 = String.valueOf(Double
					.parseDouble(transformedCoordinateX)
					+ Double.parseDouble(tolerance));
			String x3 = String.valueOf(Double
					.parseDouble(transformedCoordinateX)
					+ Double.parseDouble(tolerance));
			String x4 = transformedCoordinateX;
			String y1 = transformedCoordinateY;
			String y2 = transformedCoordinateY;
			String y3 = String.valueOf(Double
					.parseDouble(transformedCoordinateY)
					- Double.parseDouble(tolerance));
			String y4 = String.valueOf(Double
					.parseDouble(transformedCoordinateY)
					- Double.parseDouble(tolerance));

			String polygonString = x1 + "," + y1 + "," + x2 + "," + y2 + ","
					+ x3 + "," + y3 + "," + x4 + "," + y4 + "," + x1 + "," + y1;
			LOG.debug("ABAREFERENCE - Made up polygon string - {}"
					,polygonString);

			String xmlQueryString = createXMLQueryStringForImageList(
					polygonString, srscode, filterValue);

			// Starts - Webserviceclient code
			String endpoint = "http://" + hostName + port + "/axis/services/"
					+ webserviceName;
			Service service = new Service();
			Call call = (Call) service.createCall();
			QName operationName = new QName(endpoint, methodName);
			call.setOperationName(operationName);
			call.setTargetEndpointAddress(endpoint);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlQueryString);

			responseString.append((String) call.invoke(new Object[] { sb
					.toString() }));

			LOG.debug("Response String - {}",responseString);
			// Ends

			completeImageList = parseXMLResponseStringForImageList( toSpaceName, 
					completeImageList, responseString.toString());

			// It is a complete list containing paxinos as well as abaref images
			Iterator iterator = completeImageList.iterator();
			vo = new UCSDServiceVO();
			while (iterator.hasNext()) {
				vo = (UCSDServiceVO) iterator.next();
				LOG.debug("WMS - {}",vo.getWms());
				System.out
						.println("Image Base Name - " + vo.getImageBaseName());
				LOG.debug("Image service Name - {}"
						,vo.getImageServiceName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return completeImageList;

	}

	// Creates an xml string with buch of parameters to be passed in a
	// webservice
	private String createXMLQueryStringForImageList(String polygonString,
			String srsCode, String filterValue) {

		StringBuffer xmlString = new StringBuffer();

		// Read the proxy from a file
		try {

			xmlString.append("<request>");

			xmlString.append("<category>").append("mouse")
					.append("</category>");
			xmlString.append("<regionofinterest>").append(polygonString)
					.append("</regionofinterest>");

			if (filterValue.equalsIgnoreCase("sagittal")) {
				filterValue = "Sagittal";
			} else if (filterValue.equalsIgnoreCase("coronal")) {
				filterValue = "Coronal";
			}

			xmlString.append("<maptype>").append(filterValue).append(
					"</maptype>");
			xmlString.append("<srscode>").append(srsCode).append("</srscode>");

			xmlString.append("</request>");

		} catch (Exception e) {

			e.getStackTrace();

		}

		LOG.debug(" Query String for getImageMetaDataForROI - {}"
				,xmlString.toString());
		return xmlString.toString();

	}

	private ArrayList parseXMLResponseStringForImageList(String referenceSpaceName, ArrayList list,
			String xmlData) {

		// Make the xml document from the xml string
		org.jdom.Document document;
		String imageServerHostname = config
				.getValue("incf.imageserver.host.name");

		try {

			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser", false);

			document = builder.build(new InputSource(new ByteArrayInputStream(
					xmlData.getBytes())));

			// Get all the childrens
			List structuresList = document.getRootElement().getChildren();

			// Start - xml parsing
			Element responseElement;
			// Element responseElement =
			// document.getRootElement().getChild("imageinfo");
			UCSDServiceVO vo;
			UCSDUtil util = new UCSDUtil();

			for (int i = 0; i < structuresList.size(); i++) {

				responseElement = (Element) structuresList.get(i);
				vo = new UCSDServiceVO();

				vo = getSimpleImageEnvelope(imageServerHostname,
						responseElement.getChildText("imageServiceName"),
						"MAPSERVER");

				// vo.setWms(responseElement.getChildText("wmsURL"));
				vo.setImageBaseName(responseElement.getChildText("imageName"));
				vo.setImageServiceName(responseElement
						.getChildText("imageServiceName"));
				vo.setTfw1(responseElement.getChildText("tfw1"));
				vo.setTfw2(responseElement.getChildText("tfw2"));
				vo.setTfw3(responseElement.getChildText("tfw3"));
				vo.setTfw4(responseElement.getChildText("tfw4"));
				vo.setTfw5(responseElement.getChildText("tfw5"));
				vo.setTfw6(responseElement.getChildText("tfw6"));

				if (referenceSpaceName.equalsIgnoreCase("paxinos")) {
					vo.setFlag(paxinos);
				} else if (referenceSpaceName.equalsIgnoreCase("abareference")) {
					vo.setFlag(abaReference);
				}

				if (responseElement.getChildText("coefficientA") == null) {
					vo.setCoefficientA("0");
				}
				if (responseElement.getChildText("coefficientB") == null) {
					vo.setCoefficientB("0");
				}
				if (responseElement.getChildText("coefficientC") == null) {
					vo.setCoefficientC("0");
				}
				if (responseElement.getChildText("coefficientD") == null) {
					vo.setCoefficientD("0");
				}

				vo.setSliceConstant(responseElement
						.getChildText("sliceConstant"));

				list.add(vo);// Since data for multiimageservice is the same
				// for all the records based on sliceID, so we
				// are commenting this line

			}
			// End - xml parsing

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * 
	 * @param host
	 *            String Host name of the computer running ArcIMS
	 * @param imageService
	 *            String Name of Image Service from which to get image
	 * 
	 * @return SpatialAtlasClientDataModel dataModel
	 */
	public UCSDServiceVO getSimpleImageEnvelope(String host,
			String serviceName, String imageServerName) {

		LOG.debug("Begin - getSimpleImageEnvelope");

		String returnString = new String("");
		UCSDServiceVO dataModel = new UCSDServiceVO();
		LOG.debug("imageServerName - {}" ,imageServerName);

		try {

			if (imageServerName.trim().equalsIgnoreCase("MAPSERVER")) {

				LOG.debug("Inside MAPSERVER");

				try {

					// FIXME - Needs to come from the config file.
					String port = ":9090";
					String webDir = "crbsatlas/mapfiles";

					// Linux Map Server URL
					String imageURLString = "http://" + host
							+ "/cgi-bin/mapserv?map=" + webDir + "/"
							+ serviceName + ".map";

					// Windows Map Server URL
					// String imageURLString = "http://" + host + port +
					// "/cgi-bin/mapserv.exe?map="+webDir+"/" + serviceName +
					// ".map";

					LOG.debug("WMS URL String - {}",imageURLString);

					URL url = new URL(imageURLString);

					WebMapServer wms = new WebMapServer(url);

					WMSCapabilities capabilities = wms.getCapabilities();
					Layer layer = capabilities.getLayer();
					dataModel.setMinX(String.valueOf(layer
							.getLatLonBoundingBox().getMinX()));
					dataModel.setMaxX(String.valueOf(layer
							.getLatLonBoundingBox().getMaxX()));
					dataModel.setMinY(String.valueOf(layer
							.getLatLonBoundingBox().getMinY()));
					dataModel.setMaxY(String.valueOf(layer
							.getLatLonBoundingBox().getMaxY()));

					dataModel.setBottomLeft(dataModel.getMinX() + ", "
							+ dataModel.getMinY());
					dataModel.setTopRight(dataModel.getMaxX() + ", "
							+ dataModel.getMaxY());
					dataModel.setBottomRight(dataModel.getMaxX() + ", "
							+ dataModel.getMinY());
					dataModel.setTopLeft(dataModel.getMinX() + ", "
							+ dataModel.getMaxY());

					LOG.debug("minX is - {}",dataModel.getMinX());
					LOG.debug("maxX is - {}", dataModel.getMaxX());
					LOG.debug("minY is - {}", dataModel.getMinY());
					LOG.debug("maxY is - {}", dataModel.getMaxY());

				} catch (ServiceException e) {

					e.printStackTrace();

				} catch (Exception e) {

					e.printStackTrace();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.debug("End - getSimpleImageEnvelope");

		return dataModel;

	}

}
