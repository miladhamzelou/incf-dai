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
	System.out.println("done");
       
	}

}
