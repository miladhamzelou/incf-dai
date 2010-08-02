package org.incf.atlas.waxml.examples;

import java.util.ArrayList;

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.*;

public class Retrieve2dImageResponse {

	public String AsXml(){
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		Retrieve2DImageResponseDocument document = Retrieve2DImageResponseDocument.Factory.newInstance();
		
		Retrieve2DImageResponseType imagesRes = document.addNewRetrieve2DImageResponse();
	// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		Utilities.addMethodNameToQueryInfo(query,"GetMap","URL");

		Criteria criterias = query.addNewCriteria();
		
		
		InputStringType crtieria1 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria1.setName("sourceType");
		crtieria1.setValue(IncfImageServicesEnum.WMS_JPG.toString());

		InputStringType crtieria2 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria2.setName("sourceURL");
		crtieria2.setValue("URL");
		
		InputStringType crtieria3 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria3.setName("srsName");
		crtieria3.setValue("Mouse_ABAvoxel_1.0");
		
		InputStringType crtieria4 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria4.setName("xmin");
		crtieria4.setValue("0");

	
		InputStringType crtieria6 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria6.setName("xmax");
		crtieria6.setValue("100");

		InputStringType crtieria7 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria7.setName("ymin");
		crtieria7.setValue("0");

		InputStringType crtieria8 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria8.setName("ymax");
		crtieria8.setValue("100");

		
		imagesRes.addImageUrl("URL");
		
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
	
}
