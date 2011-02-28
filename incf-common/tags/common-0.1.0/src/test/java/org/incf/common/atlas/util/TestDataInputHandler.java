package org.incf.common.atlas.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.deegree.commons.tom.ows.LanguageString;
import org.deegree.process.jaxb.java.LiteralInputDefinition;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.input.LiteralInputImpl;
import org.junit.Test;

public class TestDataInputHandler {

	private static final LanguageString TITLE = null;
	private static final LanguageString SUMMARY = null;
	private static final String UOM = null;
	private static final LiteralInputDefinition DEFINITION = 
		new LiteralInputDefinition();
	
	private DataInputHandler handler;
	
	private String dataInputKey = null;
	private String inputValue = null;
	private String expectedValue = null;
	private String returnedValue = null;

	
	public TestDataInputHandler() throws IOException, URISyntaxException {
		handler = new DataInputHandler(new File(this.getClass()
				.getResource("/ProcessDefinitionTest.xml").toURI()));
	}
	
	private String getValidatedValue(String dataInputKey, String inputValue) 
			throws OWSException, IOException {
		LiteralInput input = new LiteralInputImpl(DEFINITION, TITLE, SUMMARY, 
				inputValue, UOM);
		return handler.getValidatedStringValue(dataInputKey, input);
	}

	private String getValidatedValueOptionalOmitted(String dataInputKey, 
			String inputValue) throws OWSException, IOException {
		LiteralInput input = null;
		return handler.getValidatedStringValue(dataInputKey, input);
	}

	// Case 1: Required Input, Any Non-Null Value is Accepted
	
	@Test
	public void testValidateCase1a() throws IOException {
		dataInputKey = "srsName1";
		inputValue = "Mouse_AGEA_1.0";
		expectedValue = "Mouse_AGEA_1.0";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase1b() throws IOException {
		dataInputKey = "srsName1";
		inputValue = "abcde";
		expectedValue = "abcde";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase1c() throws IOException {
		dataInputKey = "srsName1";
		inputValue = null;
		expectedValue = null;
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			assertTrue(true);
			return;
		}
		fail("Expected exception.");
		
	}

	// Case 2: Required input, any allowed value
	
	@Test
	public void testValidateCase2a() throws IOException {
		dataInputKey = "filter2";
		inputValue = "maptype:coronal";
		expectedValue = "maptype:coronal";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase2b() throws IOException {
		dataInputKey = "filter2";
		inputValue = "maptype:sagittal";
		expectedValue = "maptype:sagittal";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase2c() throws IOException {
		dataInputKey = "filter2";
		inputValue = "abcde";
		expectedValue = null;
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			assertTrue(true);
			return;
		}
		fail("Expected exception.");
		
	}

	@Test
	public void testValidateCase2d() throws IOException {
		dataInputKey = "filter2";
		inputValue = null;
		expectedValue = null;
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			assertTrue(true);
			return;
		}
		fail("Expected exception.");
		
	}

	// Case 3: Optional input, any value, with default
	
	@Test
	public void testValidateCase3a() throws IOException {
		dataInputKey = "srsName3";
		inputValue = "Mouse_AGEA_1.0";
		expectedValue = "Mouse_AGEA_1.0";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase3b() throws IOException {
		dataInputKey = "srsName3";
		inputValue = "abcde";
		expectedValue = "abcde";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase3c() throws IOException {
		dataInputKey = "srsName3";
		inputValue = null;
		expectedValue = "Mouse_AGEA_1.0";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase3d() throws IOException {
		dataInputKey = "srsName3";
		inputValue = null;
		expectedValue = "Mouse_AGEA_1.0";
		returnedValue = null;
		try {
			returnedValue = getValidatedValueOptionalOmitted(dataInputKey, 
					inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	// Case 4: Optional input, any allowed value
	
	@Test
	public void testValidateCase4a() throws IOException {
		dataInputKey = "filter4";
		inputValue = "maptype:coronal";
		expectedValue = "maptype:coronal";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase4b() throws IOException {
		dataInputKey = "filter4";
		inputValue = "maptype:sagittal";
		expectedValue = "maptype:sagittal";
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

	@Test
	public void testValidateCase4c() throws IOException {
		dataInputKey = "filter4";
		inputValue = "abcde";
		expectedValue = null;
		returnedValue = null;
		try {
			returnedValue = getValidatedValue(dataInputKey, inputValue);
		} catch (OWSException e) {
			assertTrue(true);
			return;
		}
		fail("Expected exception.");
		
	}

	@Test
	public void testValidateCase4d() throws IOException {
		dataInputKey = "filter4";
		inputValue = null;
		expectedValue = "maptype:coronal";
		returnedValue = null;
		try {
			returnedValue = getValidatedValueOptionalOmitted(dataInputKey, 
					inputValue);
		} catch (OWSException e) {
			fail("Unexpected exception.");
		}
		assertTrue(returnedValue.equals(expectedValue));
	}

}
