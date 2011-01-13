package org.incf.common.atlas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

	public ValidationResult validateNEW(String dataInputKey, String dataInputValue) 
			throws IOException {
		ValidationResult validationResult = new ValidationResult();

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
		
		// debug
		//System.out.println("\ndataInputKey      : " + dataInputKey);
		//System.out.println("dataInputValue    : " + dataInputValue);
		//System.out.println("defaultValue      : " + defaultValue);
		//System.out.println("allowedValues size: " + allowedValues.size());

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
