package org.incf.atlas.common.util;

import javax.vecmath.Point3d;

public class CoordinateTransform {
	
	/*
	 * AGEA coordinates are 25 times the corresponding ABAVoxel coordinate.
	 */
	
	private static final double ALLEN_FACTOR = 25.0;
	private static final double ALLEN_RECIP = 1 / ALLEN_FACTOR;
	
	public static Point3d abaVoxelToAgea(final Point3d abaVoxelPoint){
		Point3d newPoint = new Point3d(abaVoxelPoint);
		newPoint.scale(ALLEN_FACTOR);
		return new Point3d(newPoint);
	}
	
	public static Point3d ageaToAbaVoxel(final Point3d ageaPoint){
		Point3d newPoint = new Point3d(ageaPoint);
		newPoint.scale(ALLEN_RECIP);
		return new Point3d(newPoint);
	}
	
	/*
From the Nifti header, the translation from WHS_0.9 to WHS_1.0 is as follows:

X10 = (1/46.512)*X09 - 5.3965
Y10 = (1/46.512)*Y09 - 11.997
Z10 = (1/46.512)*Z09 - 5.5255

The translation from WHS_1.0 to WHS_0.9 is, of course:

X09 = (X10+5.3965)*46.512
Y09 = (Y10+11.997)*46.512
Z09 = (Z10+5.5255)*46.512

46.512 is the resolution (in pixels per mm), and 5.3965, 11.997 and 5.5255 are the offsets in mm. The pixel coordinates of the origin of WHS_1.0 are  (251, 558, 257).
	 */
	private static final double WHS_FACTOR   = 46.512;
	private static final double WHS_RECIP    = 1 / WHS_FACTOR;
	private static final double WHS_X_OFFSET =  5.3965;
	private static final double WHS_Y_OFFSET = 11.997;
	private static final double WHS_Z_OFFSET =  5.5255;
	
	public static Point3d whs09ToWhs10(final Point3d whs09Point) {
		return new Point3d(
			(whs09Point.x * WHS_RECIP) - WHS_X_OFFSET,
			(whs09Point.y * WHS_RECIP) - WHS_Y_OFFSET,
			(whs09Point.z * WHS_RECIP) - WHS_Z_OFFSET);
	}

	public static Point3d whs10ToWhs09(final Point3d whs10Point) {
		return new Point3d(
			(whs10Point.x + WHS_X_OFFSET) * WHS_FACTOR,
			(whs10Point.y + WHS_Y_OFFSET) * WHS_FACTOR,
			(whs10Point.z + WHS_Z_OFFSET) * WHS_FACTOR);
	}

}
