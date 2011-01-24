package org.incf.ucsd.atlas.util;

public class WHS2Paxinos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WHS2Paxinos client = new WHS2Paxinos();
		client.getTransformation( 250, 384, 458 );

	}

	
	public String getTransformation (long x_, long y_, long z_) { 

		//flipped x in the powerpoint defined by Ilya
		x_ = x_ * -1;
		String w = "";

		try {
			
			double p_x;
			double q_x;
			double p_y;
			double q_y;
			double p_z;
			double q_z;
			
			int slide_num = 0;
			
			long x = y_;
			long y = 512 - z_;
			long z = x_;
			
			p_z = (x - 564)/49.0;
			p_x = (z - 250)/54.0;
			p_y = (y - 200 - 9*p_z - 0.5*p_z*p_z)/54.0 + 3;
			
			if ( p_z < -6.72 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z));
				q_x = 1/54.0 * (z - 250);
			} else if ( p_z < -6.0 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + (p_z+6.72)*(30 - 8*(p_z+6.72)*(p_z+6.72)));
				q_x = 1/54.0 * (z - 250);
			} else if ( p_z < -5.6 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + (p_z+6.72)*(30 - 8*(p_z+6.72)*(p_z+6.72)) + 54*0.087*(p_y-3));
				q_x = 1/54.0 * (z - 250);
			} else if ( p_z < -4.8 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + (p_z+6.72)*(30 - 8*(p_z+6.72)*(p_z+6.72)) + 54*0.087*(p_y-3));
				q_x = 1/54.0 * (z - 250);
			} else if ( p_z < -1.5 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + 54*0.087*(p_y-3));
				q_x = 1/54.0 * (z - 250);
			} else if ( p_z < 1.1 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + 54*0.174*(p_y-3));
				q_x = 1/54.0 * (z - 250);
			} else if ( p_z < 1.7 ) {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + 54*0.087*(p_y-3));
				q_x = 1/54.0 * (z - 250);
			} else {
				q_z = 1/49.0 * (x - 564 + 54*(0.046*p_x + 0.0047*p_x*p_z) + 54*0.087*(p_y-3));
				q_x = 1/54.0 * (z - 250 - 6.15*(p_z-1.1));
			}
			q_y = (y - 200 - 9*p_z - 0.5*p_z*p_z)/54.0 + 3;
			
			double[] slidePos = {4.28, 3.92, 3.56, 3.20, 3.08, 2.96, 2.80, 2.68, 2.58, 2.46, 2.34, 2.22, 2.10, 1.98, 1.94, 1.78, 1.70, 1.54, 1.42, 1.34, 1.18, 1.10, 0.98, 0.86, 0.74, 0.62, 0.50, 0.38, 0.26, 0.14, 0.02, -0.10, -0.22, -0.34, -0.46, -0.58, -0.70, -0.82, -0.94, -1.06, -1.22, -1.34, -1.46, -1.58, -1.70, -1.82, -1.94, -2.06, -2.18, -2.30, -2.46, -2.54, -2.70, -2.80, -2.92, -3.08, -3.16, -3.28, -3.40, -3.52, -3.64, -3.80, -3.88, -4.04, -4.16, -4.24, -4.36, -4.48, -4.60, -4.72, -4.84, -4.96, -5.02, -5.20, -5.34, -5.40, -5.52, -5.68, -5.80, -5.88, -6.00, -6.12, -6.24, -6.36, -6.48, -6.64, -6.72, -6.84, -6.96, -7.08, -7.20, -7.32, -7.48, -7.56, -7.64, -7.76, -7.92, -8.00, -8.12, -8.24};
			
			if ( q_z > slidePos[0] ) {
				slide_num = 1;
			} else if ( q_z < slidePos[99] ) {
				slide_num = 100;
			} else {
				for (int i=0; i<99; i++) {
					if ( slidePos[i] >= q_z && slidePos[i+1] < q_z ) {
						if ( (slidePos[i]-q_z) < (q_z-slidePos[i+1]) ) {
							slide_num = i+1;
						} else {slide_num = i+2;}
						break;
					}
				}
			}
			
			q_x = Math.floor(q_x*100)/100;
			q_y = Math.floor(q_y*100)/100;

			w = String.valueOf(x) + " "+ String.valueOf(y) + " " + String.valueOf(z) +" " + String.valueOf(q_x) + " " + String.valueOf(q_y) + " " + String.valueOf(slidePos[slide_num-1]);

		} catch (Exception e) {

			e.printStackTrace();

		}

		System.out.println("Transformed - " + w);
		return w;

	}

}
