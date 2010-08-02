package org.incf.atlas.common.util;

public enum ExceptionCode {

	// See 05-008_OGC_Web_Services_Common_Specification_Corrigendum.pdf
	// Table 5
	// See 05-007r4_Web_Processing_Service_WPS_0.4.0.pdf
	// Tables 26 and 45
	
	MISSING_PARAMETER_VALUE ("MissingParameterValue"),		// all
	INVALID_PARAMETER_VALUE ("InvalidParameterValue"),		// all
	NOT_APPLICABLE_CODE ("NotApplicableCode"),				// all
	VERSION_NEGOTIATION_FAILED ("VersionNegotiationFailed"),// GetCapabilities
	INVALID_UPDATE_SEQUENCE ("InvalidUpdateSequence"),		// GetCapabilities
	SERVER_BUSY ("ServerBusy"),								// Execute
	FILE_SIZE_EXCEEDED ("FileSizeExceeded");				// Execute

	private final String exceptionCodeValue;

	private ExceptionCode(String exceptionCodeValue) {
		this.exceptionCodeValue = exceptionCodeValue;
	}

	public String toString() {
		return exceptionCodeValue;
	}

}
