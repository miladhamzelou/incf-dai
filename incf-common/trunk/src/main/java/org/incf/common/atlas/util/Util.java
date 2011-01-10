package org.incf.common.atlas.util;

import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.input.LiteralInput;
import org.incf.common.atlas.exception.InvalidDataInputValueException;

public class Util {

    public static String getStringInputValue(ProcessletInputs in, 
            String dataInputKey) {
        
        // if no data input for given dataInputKey in.getParameter() should
        // return null
        Object literalInput = in.getParameter(dataInputKey);
        
        // if literalInput is null, just ruturn null, which is acceptable value 
        // if dataInput was optional
        if (literalInput == null) {
            return null;
        }
        
        // if literalInput is not null we can get and return its value
        return ((LiteralInput) literalInput).getValue();
    }
/*
    public static String getStringInputValue(ProcessletInputs in, 
    		String dataInputKey) {
		return ((LiteralInput) in.getParameter(dataInputKey)).getValue();
    }
*/
    public static double getDoubleInputValue(ProcessletInputs in, 
    		String dataInputKey) throws InvalidDataInputValueException {
    	String value = null;
    	double dValue = 0.0;
		try {
			LiteralInput literalInput = (LiteralInput) in.getParameter(
					dataInputKey);
			value = literalInput.getValue();
			dValue = Double.parseDouble(value);
		} catch (NumberFormatException e) {
//			throw new InvalidDataInputValueException(
//					String.format("The value of data input %s is invalid. "
//							+ "Value: %s, Expected type: %s.", dataInputKey, 
//							value, expectedType)
//					dataInputKey, value, 
//					"double", e);
			throw new InvalidDataInputValueException(
					String.format("The value of data input %s is invalid. "
							+ "Value: %s, Expected type: %s.", dataInputKey, 
							value, "double"), e);
		}
		return dValue;
    }

}
