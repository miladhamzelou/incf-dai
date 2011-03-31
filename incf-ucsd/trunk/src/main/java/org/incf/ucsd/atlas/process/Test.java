package org.incf.ucsd.atlas.process;

import org.incf.ucsd.atlas.util.WHS2Paxinos;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WHS2Paxinos whs2paxinos = new WHS2Paxinos();
		String originalCoordinateX = "250.00"; 
		String originalCoordinateY = "251.0"; 
		String originalCoordinateZ = "262.0"; 

		String transformedCoordinateString = whs2paxinos.getTransformation( Long.parseLong(originalCoordinateX.replace(".0", "")),
				Long.parseLong(originalCoordinateY.replace(".0", "")), Long.parseLong(originalCoordinateZ.replace(".0", "")) );

	}

}
