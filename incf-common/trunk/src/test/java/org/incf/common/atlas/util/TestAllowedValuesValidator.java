package org.incf.common.atlas.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class TestAllowedValuesValidator {
	
	@Test
	public void testValidate() throws IOException, URISyntaxException {
    	AllowedValuesValidator validator = new AllowedValuesValidator(new File(
    			this.getClass().getResource("/Get2DImagesByPOI.xml").toURI()));
		assertTrue(validator.validate("srsName", "Mouse_AGEA_1.0"));
		assertTrue(validator.validate("filter", "maptype:coronal"));
		assertTrue(validator.validate("filter", "maptype:sagittal"));
		assertFalse(validator.validate("srsName", "xxxMouse_AGEA_1.0"));
		assertFalse(validator.validate("filter", "xxxmaptype:coronal"));
	}

}
