package org.incf.common.atlas.util;

import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.input.LiteralInput;
import org.incf.common.atlas.exception.InvalidDataInputValueException;

public class Util {

    public static String getStringInputValue(ProcessletInputs in, 
    		String dataInputKey) {
		return ((LiteralInput) in.getParameter(dataInputKey)).getValue();
    }

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
