package org.incf.aba.atlas.process;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.incf.aba.atlas.process.Get2DImagesByPOI.round200;

public class TestGet2DImagesByPOI {
	
	@Test
	public void testRound200() {
		assertTrue(round200(5301) == 5400.0);
		assertTrue(round200(5400) == 5400.0);
		assertTrue(round200(5499) == 5400.0);
		assertTrue(round200(5501) == 5600.0);
	}

}
