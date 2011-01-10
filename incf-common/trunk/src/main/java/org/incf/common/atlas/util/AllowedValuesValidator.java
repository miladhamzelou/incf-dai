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
	private static final String LAST =
		"']/" + PREFIX + ":AllowedValues/" + PREFIX + ":Value/text()";
	
	private XPathEvaluator evaluator;
	
	public AllowedValuesValidator(File processDefinitionFile) throws IOException {
		evaluator = new XPathEvaluator(processDefinitionFile);
	}
	
	public boolean validate(String dataInputKey, String dataInputValue) 
			throws IOException {
		String xPathExpression = FIRST + dataInputKey + LAST;
		List<String> allowedValues = evaluator.evaluateXPath(xPathExpression);
		for (String allowedValue : allowedValues) {
			if (dataInputValue.equals(allowedValue)) {
				return true;
			}
		}
		return false;
	}

	    public boolean validateNEW(String dataInputKey, String dataInputValue) 
	            throws IOException {

	        // if dataInputValue is null, assume it was optional and return true
	        // (i.e. valid value); this should be safe because if it was not 
	        // optional, Deegree should have already thrown MissingValueException;
	        // also see: Util.getStringInputValue()
	        if (dataInputValue == null) {
	            return true;
	        }

	        // if dataInputValue is not null, proceed with further validation
	        // against allowed values
	        String xPathExpression = FIRST + dataInputKey + LAST;
	        List<String> allowedValues = evaluator.evaluateXPath(xPathExpression);
	        for (String allowedValue : allowedValues) {
	            if (dataInputValue.equals(allowedValue)) {
	                return true;
	            }
	        }
	        return false;
	    }

}
