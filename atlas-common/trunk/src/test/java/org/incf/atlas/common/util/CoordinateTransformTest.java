package org.incf.atlas.common.util;

import static org.junit.Assert.assertTrue;

import javax.vecmath.Point3d;

import org.junit.Test;

public class CoordinateTransformTest {
	
	@Test
	public void whs09ToFromWhs10() {
		
		// start with a point
		Point3d whs09 = new Point3d(100.0, 200.0, 300.0);
		
		// convert one way, check results
		Point3d whs10 = CoordinateTransform.whs09ToWhs10(whs09);
		assertTrue(Math.abs(whs10.x - (-3.2465)) < 0.0001);
		assertTrue(Math.abs(whs10.y - (-7.6970)) < 0.0001);
		assertTrue(Math.abs(whs10.z - ( 0.9244)) < 0.0001);

		// round trip it, check results
		Point3d whs09rt = CoordinateTransform.whs10ToWhs09(whs10);
		assertTrue(Math.abs(whs09rt.x - 100.0) < 0.00001);
		assertTrue(Math.abs(whs09rt.y - 200.0) < 0.00001);
		assertTrue(Math.abs(whs09rt.z - 300.0) < 0.00001);
	}

}
