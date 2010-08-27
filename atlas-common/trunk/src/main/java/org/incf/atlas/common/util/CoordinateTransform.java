package org.incf.atlas.common.util;

import javax.vecmath.Point3d;

public class CoordinateTransform {
	
	/*
	 * agea - abaVoxel
	 * whs09 - whs10
	 * abaVoxel - abaReference
	 */
	
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

    private static final double[][] ABAr_to_ABAv = {
    	{ -9.635000e+02, -3.070000e+01, -1.980000e+01,  5.762658e+03 },
    	{ -8.700000e+00,  8.861000e+02, -2.930000e+01,  1.155562e+03 },
    	{ -1.780000e+01,  2.100000e+00,  9.197000e+02,  1.266430e+03 },
    	{  0.000000e+00,  0.000000e+00,  0.000000e+00,  1.000000e+00 },
    };

        private static final double[][] ABAv_to_ABAr = {
    	{ -1.037125e-03, -3.587682e-05, -2.347099e-05,  6.047780e+00 },
    	{ -1.084572e-05,  1.128080e-03,  3.570513e-05, -1.286285e+00 },
    	{ -2.004790e-05, -3.270171e-06,  1.086775e-03, -1.257017e+00 },
    	{  0.000000e+00,  0.000000e+00,  0.000000e+00,  1.000000e+00 },
    };

	/**
	 * Converts from ABA Reference coordinates to ABA Voxel
	 * 
	 * @param u U coordinate.
	 * @param v V coordinate.
	 * @param w W coordinate.
	 * @return an integer array of length 3 containing the ABA Voxel position.
	 */
	public static Point3d convertReferenceToVoxel(final Point3d abaReference) {
		double[] xyz1 = { abaReference.x, abaReference.y, abaReference.z, 1 };
		double[] xyz2 = { 0, 0, 0, 0 };

		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 4; i++)
				xyz2[j] += xyz1[i] * ABAr_to_ABAv[j][i];

		return new Point3d((int) Math.round(xyz2[0]),
				(int) Math.round(xyz2[1]), (int) Math.round(xyz2[2]));
	}

	/**
	 * Converts from ABA Voxel coordinates to ABA Reference
	 * 
	 * @param abaVoxel ABA Voxel coordinates
	 * @param v v ABA Voxel coordinate
	 * @param w w ABA Voxel coordinate
	 * @return an instace of an object of type ABAReference containing the
	 *         planar coordinates (u and v) and the image ID.
	 */

	public static Point3d convertVoxelToReference(Point3d abaVoxel) {
		double[] xyz1 = { abaVoxel.x, abaVoxel.y, abaVoxel.z, 1 };
		double[] xyz2 = new double[4];

		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 4; i++)
				xyz2[j] += xyz1[i] * ABAv_to_ABAr[j][i];

		return new Point3d(xyz2[0], xyz2[1], xyz2[2]);
	}

}
