package org.incf.common.atlas.exception;

import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;

public class InvalidDataInputValueException extends OWSException {

	public InvalidDataInputValueException(String dataInputKey, String value,
			String expectedType, Throwable cause) {
		super(String.format("The value of data input %s is invalid. "
					+ "Value: %s, Expected type: %s.", dataInputKey, value, 
						expectedType), 
				cause, ControllerException.NO_APPLICABLE_CODE);
	}

}
