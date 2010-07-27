package org.incf.atlas.xmlbeans.examples;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       CoordinateTransform ct = new CoordinateTransform();
       String ctResponse = ct.asXml();
        try {
        	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/CoordinateTransformResponse.xml"));
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
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/TransformationResponseResponse.xml"));
         out.write(tranformationResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println("done");
       
	}

}
