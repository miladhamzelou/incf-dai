package org.incf.atlas.aba.resource;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int response[] = ABATransform.convertReferenceToVoxel(-6.5, 1.5, 1.1);
		System.out.println("0 - "+response[0]);
		System.out.println("1 - "+response[1]);
		System.out.println("2 - "+response[2]);
		
	}
	
	

}
