package org.incf.atlas.waxml.examples;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import org.incf.atlas.waxml.utilities.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       CoordinateTransformResponse ct = new CoordinateTransformResponse();
       String ctResponse = ct.asXml();
        try {
        	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/CoordinateTransformatioChainResponse.xml"));
            out.write(ctResponse);
            out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  GenesResponse gr = new GenesResponse();
      String grResponse = gr.AsXml();
       try {
       	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/GenesResponse.xml"));
           out.write(grResponse);
           out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	StructureTermsResponse str = new StructureTermsResponse();
    String StructureTermsResponse = str.AsXML();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/StructureTermsResponse.xml"));
         out.write(StructureTermsResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	TransformationResponse tr = new TransformationResponse();
    String tranformationResponse = tr.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/TransformationResponse.xml"));
         out.write(tranformationResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ListTransformsResponse lr = new ListTransformsResponse();
    String listTransformsResponse = lr.asXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ListTransformsResponse.xml"));
         out.write(listTransformsResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ImagesResponse ir = new ImagesResponse();
    String ImagesResponse = ir.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ImagesResponse.xml"));
         out.write(ImagesResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ImagesMultiExampleResponse irm = new ImagesMultiExampleResponse();
    String ImagesResponsemulti = irm.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ImagesResponse_multipleImages.xml"));
         out.write(ImagesResponsemulti);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ImagesByUriResponse iur = new ImagesByUriResponse();
    String ImagesByUriResponse = iur.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ImagesResponseByUri.xml"));
         out.write(ImagesByUriResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Retrieve2dImageResponse r2di = new Retrieve2dImageResponse();
    String Images2dResponse = r2di.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/Retrieve2DImageResponse.xml"));
         out.write(Images2dResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	CorrelationMapResponse cmr = new CorrelationMapResponse();
    String CorrelationResponse = cmr.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/CorrelationMapResponse.xml"));
         out.write(CorrelationResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("done");
       
	}

}
