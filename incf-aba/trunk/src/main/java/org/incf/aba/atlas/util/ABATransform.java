package org.incf.aba.atlas.util;

/**
 * A converter between ABA Reference and ABA Voxel coordinates and
 * <i>vice versa</i>.
 * @author spl
 * @version 1.2
 */

public class ABATransform

{

    // Fudged.

    private static final double[][] ABAv_to_ABAr = {
	{ -2.592856e-02, -8.965538e-04, -5.293892e-04,  6.045235e+00 },
	{ -2.390147e-04,  2.820589e-02, -9.035575e-04, -1.203377e+00 },
	{ -5.017826e-04, -8.286750e-05,  2.717415e-02, -6.000000e+00 },
	{  0.000000e+00,  0.000000e+00,  0.000000e+00,  1.000000e+00 }
    };

    private static final double[][] ABAr_to_ABAv = {
	{ -3.854088e+01, -1.227388e+00, -7.916398e-01,  2.267618e+02 },
	{ -3.494248e-01,  3.544592e+01,  1.171792e+00,  5.179792e+01 },
	{ -7.127397e-01,  8.542800e-02,  3.678863e+01,  2.251433e+02 },
	{  0.000000e+00,  0.000000e+00,  0.000000e+00,  1.000000e+00 }
    };

    // Original???

    // private static final double[][] ABAr_to_ABAv = {
    //     { -3.854088e+01, -1.227388e+00, -7.916400e-01,  2.305164e+02 },
    // 	{ -3.494248e-01,  3.544592e+01,  1.171792e+00,  4.624043e+01 },
    // 	{ -7.127400e-01,  8.542800e-02,  3.678864e+01,  5.066534e+01 },
    // 	{  0.000000e+00,  0.000000e+00,  0.000000e+00,  1.000000e+00 }
    // };
    
    // private static final double[][] ABAv_to_ABAr = {
    // 	{ -2.592856e-02, -8.965538e-04, -5.293892e-04,  6.045235e+00 },
    // 	{ -2.390147e-04,  2.820589e-02, -9.035575e-04, -1.203377e+00 },
    // 	{ -5.017826e-04, -8.286750e-05,  2.717415e-02, -1.257287e+00 },
    // 	{  0.000000e+00,  0.000000e+00,  0.000000e+00,  1.000000e+00 }
    // };

    // Don't instantiate.

    private ABATransform() {}

    /**
     * Converts from ABA Reference coordinates to ABA Voxel
     * @param u U coordinate.
     * @param v V coordinate.
     * @param w W coordinate.
     * @throws IndexOutOfBoundsException Thrown when result is out of
     * the range x 0..527, y 0..319, z 0..455
     * @return an integer array of length 3 containing the ABA Voxel
     * position.
     */

    static public int[] convertReferenceToVoxel( double u, double v, double w )
        throws IndexOutOfBoundsException

    {

        double[] xyz1 = { u, v, w, 1 };
        double[] xyz2 = { 0, 0, 0, 0 };

	for ( int j = 0; j < 4; j++ )
	    for ( int i = 0; i < 4; i++ )
		xyz2[j] += xyz1[i] * ABAr_to_ABAv[j][i];

	int[] r = {
	    ( int ) Math.round( xyz2[0] ),
	    ( int ) Math.round( xyz2[1] ),
	    ( int ) Math.round( xyz2[2] ),
	};

	if ( !( ( ( 0 <= r[0] ) && ( r[0] <= 527 ) ) &&
		( ( 0 <= r[1] ) && ( r[1] <= 319 ) ) &&
		( ( 0 <= r[2] ) && ( r[2] <= 455 ) ) ) )
	    throw new IndexOutOfBoundsException( "Result out of range" );

	return r;

    }

    /**
     * Converts from ABA Voxel coordinates to ABA Reference
     * @param u u ABA Voxel coordinate
     * @param v v ABA Voxel coordinate
     * @param w w ABA Voxel coordinate
     * @throws IndexOutOfBoundsException Thrown when input parameters
     * are out of the range x 0..527, y 0..319, z 0..455
     * @return an instace of an object of type ABAReference containing
     * the planar coordinates (u and v) and the image ID.
     */

    static public double[] convertVoxelToReference( int u, int v, int w )
        throws IndexOutOfBoundsException

    {

	if ( !( ( ( 0 <= u ) && ( u <= 527 ) ) ||
		( ( 0 <= v ) && ( v <= 319 ) ) ||
		( ( 0 <= w ) && ( w <= 455 ) ) ) )
	    throw new IndexOutOfBoundsException( "Result out of range" );
	
	double[] xyz1 = { u, v, w, 1 };
	double[] xyz2 = new double[4];

	for ( int j = 0; j < 4; j++ )
	    for ( int i = 0; i < 4; i++ )
		xyz2[j] += xyz1[i] * ABAv_to_ABAr[j][i];

	return new double[] { xyz2[0], xyz2[1], xyz2[2] };

    }

}
    