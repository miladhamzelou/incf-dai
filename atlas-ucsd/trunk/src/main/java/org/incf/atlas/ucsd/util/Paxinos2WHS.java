package org.incf.atlas.ucsd.util;


public class Paxinos2WHS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Paxinos2WHS client = new Paxinos2WHS();
		// client.getTransformation( 1.00, 4.3, 1.78 );
		client.getTransformation( 0.0, 0.8, -3.88 );

	}


	public String getTransformation (double x, double y, double z) {

		String w = "";
		double phi = 0;
		double a = 0;
		double b = 0;
		double c = 0;
		double theta = 0;

		try {

			a = 564 + 49*z;
			b = 200 + 9*z + 0.5*z*z;
			c = 250;

			if ( z < -4.8 && z > -6.72) {
				a += -30*(z+6.72) + 8*(z+6.72)*(z+6.72)*(z+6.72);
			}

			if ( z < -6.0 ) {
				b -= 16;
			} else if ( z < -5.6 ) {
				b += (z+5.6)*40;
			}

			if ( z > 1.1 ) {
				c = 250 + 6.15*(z-1.1);
			}

			theta = 0.046 + 0.0047*z;
			
			if ( z > -6 ) {
				phi = -0.087;
			}
			if ( z > -1.5 && z < 1.7 ) {
				phi = -0.174;
			}
			
			String y1 = String.valueOf(Math.round(a - x * 54 * theta + (y-3) * 54 * phi));
			String z1 = String.valueOf(512 - Math.round(b + (y-3) * 54));
			String x1 = String.valueOf(Math.round(c + x * 54));
			w = String.valueOf(x)+" "+String.valueOf(y)+" "+String.valueOf(z)+" "+x1+" "+y1+" "+z1;

		} catch (Exception e) {

			e.printStackTrace();

		}

		System.out.println("Transformed - " + w);
		return w;

	}

	
}

