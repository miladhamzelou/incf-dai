package org.incf.atlas.waxml.examples;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.PointType;

import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseType.CoordinateTransformationChain;
import org.incf.atlas.waxml.generated.Corners.Corner;
import org.incf.atlas.waxml.generated.Image2DType.*;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.QueryInfoType.QueryUrl;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Test;


public class ImagesByUriResponse  {
	
	@Test 
	public void validFullResponse()
	{
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		XmlObject co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml =  Utilities.validateXml(opt, co, errorList);
		 assertTrue(errorList.toString(), validXml);
		
	}
	
	public String AsXml(){
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		ImagesResponseDocument document = completeResponse();
		
		 ArrayList errorList = new ArrayList();
		 opt.setErrorListener(errorList);
		 boolean isValid = document.validate(opt);
		 
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
			return document.xmlText(opt);
		}
	public ImagesResponseDocument completeResponse() {
		ImagesResponseDocument document = ImagesResponseDocument.Factory.newInstance();
		
		ImagesResponseType imagesRes = document.addNewImagesResponse();
		imagesRes.newCursor().insertComment("Generated " + Calendar.getInstance().getTime());
// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		Utilities.addMethodNameToQueryInfo(query, "Get2DImagesByURI  ",
		"URL");

		
		Criteria criterias = query.addNewCriteria();
		
		
		InputStringType srsCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		srsCodeCriteria.setName("URI");
		srsCodeCriteria.setValue("uri:incf:pons-repository.org:structurenames:Hippocampus");
		InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("maptype:coronal");


		Image2Dcollection images = imagesRes.addNewImage2Dcollection();
		Image2DType image1 = images.addNewImage2D();
		String formatComment = "format ~ mime type " +
		"{WMS/jpg|" +

    "WMS/gif|" +
    "WMS/png|" +
   "zoomify|" +
    "image/jpeg|" +
    "image/png|" +
   "image/gif|" +
   "text/html|" +
    "url}"+ " found in incfRemoteFormatEnum";

String thumbnailComment = "optional thumbnail";
String typeComment = "type is type of service {wms-jpg|" +
   "wms-png|" +
    "wms-gif|" +
    "zoomify|" +
   "url}" +" from type incfImageServicesEnum";
image1.newCursor().insertComment(formatComment);
image1.newCursor().insertComment(thumbnailComment);
image1.newCursor().insertComment(typeComment);

ImageSource i1source = image1.addNewImageSource();

		i1source.setStringValue("URL");
		i1source.setFormat(IncfRemoteFormatEnum.IMAGE_JPEG.toString());
		i1source.setRelavance((float) 0.6);
		i1source.setSrsName("srscode");
		i1source.setThumbnail("http://example.com/image.jpg");
		i1source.setMetadata("URL");
		i1source.setType(IncfImageServicesEnum.URL.toString());
		
		ImagePosition i1position = image1.addNewImagePosition();
		IncfSrsType planeequation= i1position.addNewImagePlaneEquation();
		planeequation.setSrsName("SRS");
		planeequation.setStringValue("1 2 3 4");
		IncfSrsType placement= i1position.addNewImagePlanePlacement();
		placement.setSrsName("SRS");
		placement.setStringValue("1 2 3 4 5 6.0");
		Corners corners = i1position.addNewCorners();
		
		Corner corner1 = corners.addNewCorner();
		corner1.setPosition(PositionEnum.TOPLEFT);
		corner1.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner1.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner1.getPoint().setId("image1TopLeft");
		
		Corner corner2 = corners.addNewCorner();
		corner2.setPosition(PositionEnum.BOTTOMLEFT);
		corner2.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner2.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner2.getPoint().setId("image1BOTTOMLEFT");
		
		Corner corner3 = corners.addNewCorner();
		corner3.setPosition(PositionEnum.TOPRIGHT);
		corner3.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner3.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner3.getPoint().setId("image1TOPRIGHT");
		
		Corner corner4 = corners.addNewCorner();
		corner4.setPosition(PositionEnum.BOTTOMRIGHT);
		corner4.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner4.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner4.getPoint().setId("image1BOTTOMRIGHT");
		return document;
	}
}
