package org.incf.common.atlas.exception;

import org.deegree.services.controller.ows.OWSException;

public class InvalidDataInputValueException extends OWSException {

//	public InvalidDataInputValueException(String dataInputKey, String value,
//			String expectedType) {
//		super(String.format("The value of data input %s is invalid. "
//					+ "Value: %s, Expected type: %s.", dataInputKey, value, 
//						expectedType), 
//				OWSException.INVALID_PARAMETER_VALUE, dataInputKey);
//	}

	public InvalidDataInputValueException(String message, String dataInputKey) {
		super(message, OWSException.INVALID_PARAMETER_VALUE, dataInputKey);
	}

//	public InvalidDataInputValueException(String dataInputKey, String value,
//			String expectedType, Throwable cause) {
//		super(String.format("The value of data input %s is invalid. "
//					+ "Value: %s, Expected type: %s.", dataInputKey, value, 
//						expectedType), 
//				cause, OWSException.INVALID_PARAMETER_VALUE);
//	}

	public InvalidDataInputValueException(String message, Throwable cause) {
		super(message, cause, OWSException.INVALID_PARAMETER_VALUE);
	}

}
