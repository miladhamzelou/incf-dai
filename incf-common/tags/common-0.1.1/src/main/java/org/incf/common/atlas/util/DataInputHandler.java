package org.incf.common.atlas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.input.LiteralInput;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator.ValidationResult;

public class DataInputHandler {

	private static final String PREFIX = XPathNamespaceResolver.DEFAULT_PREFIX;
	
	// //PREFIX:LiteralInput[PREFIX:Identifier='
	private static final String FIRST =
		"//" + PREFIX + ":LiteralInput[" + PREFIX + ":Identifier='";
	
	// ']/PREFIX:AllowedValues/PREFIX:Value/text()
	private static final String ALLOWED_VALUES =
		"']/" + PREFIX + ":AllowedValues/" + PREFIX + ":Value/text()";
	
	// ']/PREFIX:DefaultValue/text()
	private static final String DEFAULT_VALUE =
		"']/" + PREFIX + ":DefaultValue/text()";
	
	private XPathEvaluator evaluator;
	
	public DataInputHandler(File processDefinitionFile) 
			throws IOException {
		evaluator = new XPathEvaluator(processDefinitionFile);
	}
	
    public static double getDoubleInputValue(ProcessletInputs in, 
    		String dataInputKey) throws OWSException {
    	String value = null;
    	double dValue = 0.0;
		try {
			LiteralInput literalInput = (LiteralInput) in.getParameter(
					dataInputKey);
			value = literalInput.getValue();
			dValue = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new OWSException("Error parsing '" + value + "' to double.", 
					OWSException.INVALID_PARAMETER_VALUE, dataInputKey);
		}
		return dValue;
    }
    
    public String getValidatedStringValue(ProcessletInputs in,
    		String dataInputKey) throws IOException, OWSException {

		// see what request's data input looks like 
		Object literalInput = in.getParameter(dataInputKey);
		return getValidatedStringValue(dataInputKey, literalInput);
    }

	protected String getValidatedStringValue(/*ProcessletInputs in,*/ 
			String dataInputKey, Object literalInput) throws IOException, OWSException {

		// determine which data input case is setup for this data input
		List<String> allowedValues = getAllowedValues(dataInputKey);
		String defaultValue = getDefaultValue(dataInputKey);
		int dataInputCase = determineCase(allowedValues, defaultValue);

		// see what request's data input looks like 
//		Object literalInput = in.getParameter(dataInputKey);

		// case A: literalInput is null: data input omitted
		//  case 1, 2 - handled by deegree (i.e. we're not even here)
		//  case 3, 4 - dataInputValue = defaultValue
		if (literalInput == null) {
			return defaultValue;		// case 3 or 4
		}

		// get value of input
		String dataInputValue = ((LiteralInput) literalInput).getValue();

		// case B: dataInputValue == null --> empty string x=;
		//  case 1 - throw exception (a value is required)
		//  case 2 - throw exception (value not allowed)
		//  case 3, 4 - dataInputValue = defaultValue
		if (dataInputValue == null) {
			switch (dataInputCase) {
			case 1:
				throw new OWSException("A value was not provided.", 
						OWSException.INVALID_PARAMETER_VALUE, dataInputKey);
			case 2:
				throw new OWSException("The value is not one of the allowed "
						+ "values. Check DescribeProcess.", 
						OWSException.INVALID_PARAMETER_VALUE, dataInputKey);
			}
			return defaultValue;		// case 3 or 4
		}

		// case C: dataInputValue has value
		//  case 1, 3 - accept value
		//  case 2, 4 - validate value against allowed values
		//    accept | throw exception (value not allowed)
		switch (dataInputCase) {
		case 1:
		case 3:
			return dataInputValue;
		}

		// case 2 or 4 -- validate input value against AllowedValues
		for (String allowedValue : allowedValues) {
			if (dataInputValue.equals(allowedValue)) {
				return dataInputValue;
			}
		}

		// input value not allowed
		throw new OWSException("Value '" + dataInputValue 
				+ "' is not one of the allowed values. Check DescribeProcess.", 
				OWSException.INVALID_PARAMETER_VALUE, dataInputKey);
	}

	private List<String> getAllowedValues(String dataInputKey) 
			throws IOException {
		String xPathExpression = FIRST + dataInputKey + ALLOWED_VALUES;

		// get AllowedVales, size 0 implies no AllowedValues
		List<String> allowedValues = evaluator.evaluateXPath(xPathExpression);
		if (allowedValues.size() == 0) {
			return null;
		}

		return allowedValues;
	}

	private String getDefaultValue(String dataInputKey) 
			throws IOException {
		String xPathExpression = FIRST + dataInputKey + DEFAULT_VALUE;

		// get DefaultValue, null implies no DefaultValue
		List<String> defaultValues = evaluator.evaluateXPath(xPathExpression);
		String defaultValue = null;
		if (defaultValues.size() > 0) {
			defaultValue = defaultValues.get(0);
		}
		return defaultValue;
	}

	private int determineCase(List<String> allowedValues, String defaultValue) {
		int dataInputCase = 0;
		if (defaultValue == null) {
			if (allowedValues == null) {
				dataInputCase = 1;
			} else {
				dataInputCase = 2;
			}
		} else {
			if (allowedValues == null) {
				dataInputCase = 3;
			} else {
				dataInputCase = 4;
			}
		}
		return dataInputCase;
	}

}
