package org.incf.common.atlas.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class TestAllowedValuesValidator {
	
	private AllowedValuesValidator validator;
	
	public TestAllowedValuesValidator() throws IOException, URISyntaxException {
		validator = new AllowedValuesValidator(new File(this.getClass()
				.getResource("/ProcessDefinitionTest.xml").toURI()));
	}
	
//	@Test
//	public void testValidate() throws IOException, URISyntaxException {
//    	AllowedValuesValidator validator = new AllowedValuesValidator(new File(
//    			this.getClass().getResource("/ProcessDefinitionTest.xml").toURI()));
//    	
//    	// Case 1 -- Required Input, Any Non-Null Value is Accepted
//		assertTrue(validator.validate("srsName1", "Mouse_AGEA_1.0"));
//		assertTrue(validator.validate("srsName1", "xxxMouse_AGEA_1.0"));
//		assertFalse(validator.validate("srsName1", null));
//
//		// Case 2 -- Required Input, One of Several Allowed Values is Accepted
//		assertTrue(validator.validate("srsName2", "Mouse_AGEA_1.0"));
//		assertFalse(validator.validate("srsName2", "xxxMouse_AGEA_1.0"));
//		assertFalse(validator.validate("srsName1", null));
//		
//		assertTrue(validator.validate("filter", "maptype:coronal"));
//		assertTrue(validator.validate("filter", "maptype:sagittal"));
//		assertFalse(validator.validate("filter", "xxxmaptype:coronal"));
//	}

	@Test
	public void testValidateCase1() throws IOException, URISyntaxException {
		
		// Case 1: Required Input, Any Non-Null Value is Accepted
		AllowedValuesValidator.ValidationResult vr = null;
		
		// allowed value
		vr = validator.validateNEW("srsName1", "Mouse_AGEA_1.0");
		assertTrue(vr.isValid());
		
		// not allowed, but any value ok
		vr = validator.validateNEW("srsName1", "xxxMouse_AGEA_1.0");
		assertTrue(vr.isValid());
		
		// not optional --> return message
		vr = validator.validateNEW("srsName1", null);
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() == null);
		assertTrue(vr.getMessage() != null);
	}

	@Test
	public void testValidateCase2() throws IOException, URISyntaxException {
		
		// Case 2: Required Input, Any of Several Allowed Values is Accepted
		AllowedValuesValidator.ValidationResult vr = null;
		
		// allowed value
		vr = validator.validateNEW("srsName2", "Mouse_AGEA_1.0");
		assertTrue(vr.isValid());
		
		// not allowed value --> return message
		vr = validator.validateNEW("srsName2", "xxxMouse_AGEA_1.0");
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() == null);
		assertTrue(vr.getMessage() != null);
		
		// not optional --> return message
		vr = validator.validateNEW("srsName2", null);
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() == null);
		assertTrue(vr.getMessage() != null);
	}

	@Test
	public void testValidateCase3() throws IOException, URISyntaxException {
		
		// Case 3: Optional Input, Any Value is Accepted, With a Default Value 
		AllowedValuesValidator.ValidationResult vr = null;
		
		// matches default
		vr = validator.validateNEW("srsName3", "Mouse_AGEA_1.0");
		assertTrue(vr.isValid());
		
		// does not match default --> return message
		vr = validator.validateNEW("srsName3", "xxxMouse_AGEA_1.0");
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() == null);
		assertTrue(vr.getMessage() != null);
		
		// use default
		vr = validator.validateNEW("srsName3", null);
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() != null);
		assertTrue(vr.getMessage() == null);
	}

	@Test
	public void testValidateCase4() throws IOException, URISyntaxException {
		
		// Case 2: Required Input, Any of Several Allowed Values is Accepted
		AllowedValuesValidator.ValidationResult vr = null;
		
		// allowed value
		vr = validator.validateNEW("filter4", "maptype:coronal");
		assertTrue(vr.isValid());
		
		// allowed value
		vr = validator.validateNEW("filter4", "maptype:sagittal");
		assertTrue(vr.isValid());
		
		// not allowed value, not default --> return message
		vr = validator.validateNEW("filter4", "XXXmaptype:coronal");
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() == null);
		assertTrue(vr.getMessage() != null);
		
		// use default
		vr = validator.validateNEW("filter4", null);
		assertTrue(vr.isValid() == false);
		assertTrue(vr.getDefaultValue() != null);
		assertTrue(vr.getMessage() == null);
	}

}
