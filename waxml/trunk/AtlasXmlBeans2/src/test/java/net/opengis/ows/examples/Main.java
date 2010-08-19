package net.opengis.ows.examples;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		ExceptionReport exceptionReport = new ExceptionReport();
		String exRpt = exceptionReport.asXml();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ExceptionReport.xml"));
			out.write(exRpt);
			out.close();
		} catch (IOException e) {
			System.out.println("Error ExceptionReport");
			e.printStackTrace();
		}
		
		System.out.println("done");
	}

}
