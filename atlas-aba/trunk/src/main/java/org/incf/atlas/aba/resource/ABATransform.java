package org.incf.atlas.aba.resource;

/**
 * A converter between ABA Reference and ABA Voxel coordinates and
 * <i>vice versa</i>.
 * @author spl
 * @version 1.1
 */

public class ABATransform

{

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

    // Don't instantiate.

    private ABATransform() {}

    /**
     * Converts from ABA Reference coordinates to ABA Voxel
     * @param u U coordinate.
     * @param v V coordinate.
     * @param w W coordinate.
     * @return an integer array of length 3 containing the ABA Voxel
     * position.
     */

    static public int[] convertReferenceToVoxel( double u, double v, double w )

    {

        double[] xyz1 = { u, v, w, 1 };
        double[] xyz2 = { 0, 0, 0, 0 };

	for ( int j = 0; j < 4; j++ )
	    for ( int i = 0; i < 4; i++ )
		xyz2[j] += xyz1[i] * ABAr_to_ABAv[j][i];

	return new int[] {
	    ( int ) Math.round( xyz2[0]/25 ),
	    ( int ) Math.round( xyz2[1]/25 ),
	    ( int ) Math.round( xyz2[2]/25 ),
	};

    }

    /**
     * Converts from ABA Voxel coordinates to ABA Reference
     * @param u u ABA Voxel coordinate
     * @param v v ABA Voxel coordinate
     * @param w w ABA Voxel coordinate
     * @return an instace of an object of type ABAReference containing
     * the planar coordinates (u and v) and the image ID.
     */

    static public double[] convertVoxelToReference( int u, int v, int w )
	throws ArrayIndexOutOfBoundsException 

    {

	double[] xyz1 = { u, v, w, 1 };

	double[] xyz2 = new double[4];

	for ( int j = 0; j < 4; j++ )
	    for ( int i = 0; i < 4; i++ )
		xyz2[j] += xyz1[i] * ABAv_to_ABAr[j][i];

	return new double[] { xyz2[0], xyz2[1], xyz2[2] };

    }

}
    