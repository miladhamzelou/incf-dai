package org.incf.atlas.waxml.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseDocument;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.InputType;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;

public class Utilities {
	public static HashMap SuggestedNamespaces() {
		HashMap suggestedPrefixes = new HashMap();

		suggestedPrefixes.put("http://www.opengis.net/gml/3.2", "gml");
		suggestedPrefixes.put("http://www.opengis.net/ows/2.0", "ows");
		// uncomment if ou want all the elements prefixed with wax:
		suggestedPrefixes.put("http://www.incf.org/WaxML/", null);
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance",
				"xsi");

		return suggestedPrefixes;
	}

	public static boolean validateXml(XmlOptions xmlOption,
			XmlObject documentResponse, ArrayList errorList) {
		if (errorList == null)
			errorList = new ArrayList();

		xmlOption.setErrorListener(errorList);

		// Validate the XML.
		boolean isValid = documentResponse.validate(xmlOption);

		// If the XML isn't valid, loop through the listener's contents,
		// printing contained messages.
		if (!isValid) {
			for (int i = 0; i < errorList.size(); i++) {
				XmlError error = (XmlError) errorList.get(i);

				System.out.println("\n");
				System.out.println("Message: " + error.getMessage() + "\n");
				System.out.println("Location of invalid XML: "
						+ error.getCursorLocation().xmlText() + "\n");
			}
		}
		return isValid;
	}

	/*
	 * add query url with method name
	 */

	public static void addMethodNameToQueryInfo(QueryInfoType queryInfo,
			String methodName, String url) {
		queryInfo.addNewQueryUrl().setName(methodName);
		queryInfo.getQueryUrl().setStringValue(url);

	}

	public static InputStringType addInputStringCriteria(
			Criteria queryInfoCriteria) {
		InputType input = queryInfoCriteria.addNewInput();

		InputStringType inputSrsConstraint = (InputStringType) input
				.changeType(InputStringType.type);

		return inputSrsConstraint;
	}

	public static InputStringType addInputStringCriteria(
			Criteria queryInfoCriteria, String name, String value) {

		InputStringType inputSrsConstraint = addInputStringCriteria(queryInfoCriteria);
		inputSrsConstraint.setName(name);
		inputSrsConstraint.setValue(value);

		return inputSrsConstraint;
	}
}
