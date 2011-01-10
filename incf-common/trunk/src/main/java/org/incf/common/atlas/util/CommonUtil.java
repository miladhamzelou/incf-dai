package org.incf.common.atlas.util;

public class CommonUtil {

	public String outOfBoundException(double x, double y, double z, String srsName) {

		String responseString = "";
		String [] coordinatesRange = new String[6];

		try {

			CommonServiceDAOImpl impl = new CommonServiceDAOImpl();
			coordinatesRange = impl.getCoordinateRangeForSRS(srsName);

			if (        x < Double.parseDouble(coordinatesRange[0]) ||
				        x > Double.parseDouble(coordinatesRange[1]) ) {
				responseString = "Coordinates - Out of Range";
			} else if ( y < Double.parseDouble(coordinatesRange[2]) ||
				        y > Double.parseDouble(coordinatesRange[3]) ) {
				responseString = "Coordinates - Out of Range";
			} else if ( z < Double.parseDouble(coordinatesRange[4]) ||
				        z > Double.parseDouble(coordinatesRange[5]) ) {
				responseString = "Coordinates - Out of Range";
			} else {
				responseString = "SUCCESS";
			}

			System.out.println("Response for Out of Bound Exception is - " + responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}

}
