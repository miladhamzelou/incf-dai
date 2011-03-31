package org.incf.common.atlas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.input.LiteralInput;

public class AllowedValuesValidator {
	
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
	
	public AllowedValuesValidator(File processDefinitionFile) throws IOException {
		evaluator = new XPathEvaluator(processDefinitionFile);
	}
	
//	public boolean validate(String dataInputKey, String dataInputValue) 
//			throws IOException {
//		String xPathExpression = FIRST + dataInputKey + LAST;
//		List<String> allowedValues = evaluator.evaluateXPath(xPathExpression);
//		for (String allowedValue : allowedValues) {
//			if (dataInputValue.equals(allowedValue)) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * 
	 * 
	 * @param dataInputKey
	 * @param dataInputValue
	 * @return
	 * @throws IOException
	 */
	public boolean validate(String dataInputKey, String dataInputValue) 
			throws IOException {

		// TODO 3 return possibilities
		// - valid
		// - invalid
		// 		- 
		// - use default

		// if dataInputValue is null, assume it was optional and return true
		// (i.e. valid value); this should be safe because if it was not 
		// optional, Deegree should have already thrown MissingValueException;
		// also see: Util.getStringInputValue()
		//if (dataInputValue == null) {
		//	return true;
		//}

		// get AllowedVales, size 0 implies no AllowedValues
		String xPathExpression = FIRST + dataInputKey + ALLOWED_VALUES;
		List<String> allowedValues = evaluator.evaluateXPath(xPathExpression);

		// get DefaultValue, null implies no DefaultValue
		xPathExpression = FIRST + dataInputKey + DEFAULT_VALUE;
		List<String> defaultValues = evaluator.evaluateXPath(xPathExpression);
		String defaultValue = null;
		if (defaultValues.size() > 0) {
			defaultValue = defaultValues.get(0);
		}

		/*
Case 1: Required Input, Any Non-Null Value is Accepted 
 no <DefaultValue> and no <AllowedValues> elements
Case 2: Required Input, Any of Several Allowed Values is Accepted 
 <AllowedValues> element, but no <DefaultValue> element
Case 3: Optional Input, Any Value is Accepted, With a Default Value 
 If the data input value is empty, the default value is used
 <DefaultValue> element, but not <AllowedValues> element
Case 4: Optional Input, Any Value is Accepted, With a Default Value 
 If the data input value is empty, the default value is used
 <DefaultValue> element, but not <AllowedValues> element
		 */

		// empty dataInputValue means client believed input was optional
		// so <DefaultValue> must exist in ProcessDefinition xml file
		if (dataInputValue == null) {
			if (defaultValue == null){

				// ERROR "A valid data input value must be provided."
				return false;		// cases 1 and 2, not allowed
			} else {

				// INFO "No input value provide, using default value."
				return true;		// just use <DefaultValue>, cases 3 and 4
			}
		}

		// from here on, we have a non-null (i.e. not empty) dataInputValue

		if (allowedValues.size() == 0) {

			// case 1 or 3
			if (defaultValue == null) {

				// "Any value is ok."
				return true;		// case 1, any value is ok
			} else {
				if (dataInputValue.equals(defaultValue)) {

					// "Default value is ok."
					return true;	// case 3
				} else {

					// ERROR "This input value is optional, but it is not the default value, and no allowed values are specified."
					return false;	// case 3
				}
			}
		} else {

			// case 2 or 4 -- validate input value against AllowedValues
			for (String allowedValue : allowedValues) {
				if (dataInputValue.equals(allowedValue)) {

					// "Ok, matches an allowed value"
					return true;
				}
			}

			// ERROR "The input value is not one of the allowed values."
			return false;
		}
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
	
	public String getValidatedStringValue(ProcessletInputs in, 
            String dataInputKey) throws IOException, OWSException {
		
		// determine which data input case is setup for this data input
		ValidationResult validationResult = new ValidationResult();
		List<String> allowedValues = getAllowedValues(dataInputKey);
		String defaultValue = getDefaultValue(dataInputKey);
        int dataInputCase = determineCase(allowedValues, defaultValue);
		
        // see what request's data input looks like 
        Object literalInput = in.getParameter(dataInputKey);
        
        // case A: literalInput is null: data input omitted
        //  case 1, 2 - handled by deegree (i.e. we're not here)
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

	public ValidationResult validateNEW(String dataInputKey, String dataInputValue) 
			throws IOException {
		ValidationResult validationResult = new ValidationResult();
		
		List<String> allowedValues = getAllowedValues(dataInputKey);
		String defaultValue = getDefaultValue(dataInputKey);
		
		
		
		
		

		
		// debug
		//LOG.debug("\ndataInputKey      : " + dataInputKey);
		//LOG.debug("dataInputValue    : " + dataInputValue);
		//LOG.debug("defaultValue      : " + defaultValue);
		//LOG.debug("allowedValues size: " + allowedValues.size());

		/*
		Case 1: Required Input, Any Non-Null Value is Accepted 
		 no <DefaultValue> and no <AllowedValues> elements
		Case 2: Required Input, Any of Several Allowed Values is Accepted 
		 <AllowedValues> element, but no <DefaultValue> element
		Case 3: Optional Input, Any Value is Accepted, With a Default Value 
		 If the data input value is empty, the default value is used
		 <DefaultValue> element, but not <AllowedValues> element
		Case 4: Optional Input, Any Value is Accepted, With a Default Value 
		 If the data input value is empty, the default value is used
		 <DefaultValue> element, but not <AllowedValues> element
		 */

		// empty dataInputValue means client believed input was optional
		// so <DefaultValue> must exist in ProcessDefinition xml file
		if (dataInputValue == null) {
			if (defaultValue == null){

				// ERROR, cases 1 and 2, not allowed
				validationResult.valid = false;
				validationResult.message = 
						"A valid data input value must be provided.";
			} else {

				// cases 3 and 4, use default
				validationResult.valid = false;
				validationResult.defaultValue = defaultValue;
			}
		} else {

			// from here on, we have a non-null (i.e. not empty) dataInputValue
			if (allowedValues.size() == 0) {

				// case 1 or 3
				if (defaultValue == null) {

					// ok, any value is ok
					validationResult.valid = true;			// case 1
				} else {
					if (dataInputValue.equals(defaultValue)) {

						// ok, matches default
						validationResult.valid = true;		// case 3
					} else {

						// ERROR case 3
						validationResult.valid = false;
						validationResult.message = "If a value is provided, "
							+ "it must be the default value.";
					}
				}
			} else {

				// case 2 or 4 -- validate input value against AllowedValues
				for (String allowedValue : allowedValues) {
					if (dataInputValue.equals(allowedValue)) {

						// ok, matches allowed value
						validationResult.valid = true;
						break;
					}
				}

				// no allowed value match
				if (validationResult.valid == false) {
					validationResult.message = 
						"The input value is not one of the allowed values.";
				}
			}
		}
		
		return validationResult;
	}

	public class ValidationResult {
		private boolean valid;
		private String defaultValue;
		private String message;
		
		public boolean isValid() {
			return valid;
		}
		
		public String getDefaultValue() {
			return defaultValue;
		}
		
		public String getMessage() {
			return message;
		}
	}

}
