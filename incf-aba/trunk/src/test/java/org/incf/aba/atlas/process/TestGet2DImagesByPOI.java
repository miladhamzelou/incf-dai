package org.incf.aba.atlas.process;

import static org.junit.Assert.assertTrue;

import org.incf.aba.atlas.util.ABAUtil;
import org.junit.Test;

public class TestGet2DImagesByPOI {
	
	@Test
	public void testRound200() {
		assertTrue(ABAUtil.round200(5301) == 5400.0);
		assertTrue(ABAUtil.round200(5400) == 5400.0);
		assertTrue(ABAUtil.round200(5499) == 5400.0);
		assertTrue(ABAUtil.round200(5501) == 5600.0);
	}

}
