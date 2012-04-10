package org.incf.common.atlas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CommonServiceDAOImpl.class);
	
	public String outOfBoundException(double x, double y, double z, String srsName) {

		String responseString = "";
		String [] coordinatesRange = new String[6];

		try {

			CommonServiceDAOImpl impl = new CommonServiceDAOImpl();
			coordinatesRange = impl.getCoordinateRangeForSRS(srsName);

			if (        x < Double.parseDouble(coordinatesRange[0]) ||
				        x > Double.parseDouble(coordinatesRange[1]) ) {
				responseString = "Coordinates - Out of Range:x:"+coordinatesRange[0]+":"+coordinatesRange[1];
			} else if ( y < Double.parseDouble(coordinatesRange[2]) ||
				        y > Double.parseDouble(coordinatesRange[3]) ) {
				responseString = "Coordinates - Out of Range:y:"+coordinatesRange[2]+":"+coordinatesRange[3];
			} else if ( z < Double.parseDouble(coordinatesRange[4]) ||
				        z > Double.parseDouble(coordinatesRange[5]) ) {
				responseString = "Coordinates - Out of Range:z:"+coordinatesRange[4]+":"+coordinatesRange[5];
			} else {
				responseString = "SUCCESS";
			}

			LOG.debug("Response for Out of Bound Exception is - {}" , responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}


}
