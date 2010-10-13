package org.incf.atlas.aba.resource;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

/*		int response[] = ABATransform.convertReferenceToVoxel(-6, 1, 1);
		System.out.println("ABA 2 Voxel 0 - "+response[0]);
		System.out.println("ABA 2 Voxel 1 - "+response[1]);
		System.out.println("ABA 2 Voxel 2 - "+response[2]);
*/
/*		double response1[] = ABATransform.convertVoxelToReference(460, 85, 92);
		System.out.println("Voxel 2 ABA 0 - "+response1[0]);
		System.out.println("Voxel 2 ABA 1 - "+response1[1]);
		System.out.println("Voxel 2 ABA 2 - "+response1[2]);
*/
 
		ABAServiceDAOImpl impl = new ABAServiceDAOImpl();
		impl.getSRSsData();

	}

/*	   x = -6.5
	   y = 1.5
	   z = 1.1

	I get

	   u = 478
	   v = 103
	   w = 96
*/	

}
