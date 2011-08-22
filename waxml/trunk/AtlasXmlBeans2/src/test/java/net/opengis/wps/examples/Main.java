package net.opengis.wps.examples;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		RegistrationResponse registrationResponse = new RegistrationResponse();
		String exRpt = registrationResponse.asXml();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Examples/WpsRegistrationResponse.xml"));
			out.write(exRpt);
			out.close();
		} catch (IOException e) {
			System.out.println("Error WpsregistrationResponse");
			e.printStackTrace();
		}
		
		System.out.println("done");
	}

}
