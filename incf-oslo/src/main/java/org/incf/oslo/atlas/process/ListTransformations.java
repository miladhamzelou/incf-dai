package org.incf.oslo.atlas.process;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.incf.oslo.atlas.util.OsloConfigurator;
import org.incf.oslo.atlas.util.OsloServiceVO;
import org.incf.oslo.atlas.util.OsloUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ListTransformations implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(ListTransformations.class);

	OsloConfigurator config = OsloConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	String abavoxel2agea = config.getValue("code.abavoxel2agea.v1");
	String agea2abavoxel = config.getValue("code.agea2abavoxel.v1");
	String whs092agea = config.getValue("code.whs092agea.v1");
	String agea2whs09 = config.getValue("code.agea2whs09.v1");
	String whs092whs10 = config.getValue("code.whs092whs10.v1");
	String whs102whs09 = config.getValue("code.whs102whs09.v1");
	String abareference2abavoxel = config
			.getValue("code.abareference2abavoxel.v1");
	String abavoxel2abareference = config
			.getValue("code.abavoxel2abareference.v1");
	String paxinos2whs09 = config.getValue("code.paxinos2whs09.v1");
	String whs092paxinos = config.getValue("code.whs092paxinos.v1");

	// private String dataInputString;
	// private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {

		try {

			LOG.debug(" Inside ListTransformations");

			OsloServiceVO vo = new OsloServiceVO();

			String inputSrsName = "";
			String outputSrsName = "";
			String filter = "";
			// collect input values
			// String transformationCode = ((LiteralInput)
			// in.getParameter("transformationCode")).getValue();

			if (in != null) {
				LOG.debug(" Inside parameter value... ");
				URL processDefinitionUrl = this.getClass().getResource(
						"/" + this.getClass().getSimpleName() + ".xml");
				DataInputHandler dataInputHandler = new DataInputHandler(
						new File(processDefinitionUrl.toURI()));
				inputSrsName = dataInputHandler.getValidatedStringValue(in,
						"inputSrsName");
				outputSrsName = dataInputHandler.getValidatedStringValue(in,
						"outputSrsName");
				filter = dataInputHandler.getValidatedStringValue(in, "filter");
				/*
				 * inputSrsName = Util.getStringInputValue(in, "inputSrsName");
				 * outputSrsName = Util.getStringInputValue(in,
				 * "outputSrsName"); filter = Util.getStringInputValue(in,
				 * "filter");
				 */}

			// Start - FIXME - Uncomment below two lines and comment the other
			// three lines
			// String hostName = uri.getHost();
			// String portNumber = delimitor + uri.getPort();
			hostName = config.getValue("incf.deploy.host.name");
			portNumber = config.getValue("incf.aba.port.number");
			String delimitor = config.getValue("incf.deploy.port.delimitor");
			// portNumber = delimitor + portNumber;
			// End - FIXME

			// vo.setUrlString(uri.toString());
			vo.setIncfDeployHostname(hostName);
			vo.setIncfDeployPortNumber(portNumber);

			OsloUtil util = new OsloUtil();
			vo.setFlag("ListTransformations");

			OsloServiceVO vo1;
			ArrayList srsCodeList = new ArrayList();
			String responseString = "";

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("ListTransformationsOutput");

			LOG.debug(" inputSrsName = {}", inputSrsName);
			LOG.debug(" outputSrsName = {}", outputSrsName);
			LOG.debug("Before the check condition");

			if (inputSrsName.equals("") || inputSrsName == null
					&& outputSrsName.equals("") || outputSrsName == null) {
				LOG.debug("Inside Empty DataInputString.");
				vo.setFromSRSCodeOne("all");
				vo.setFromSRSCode("all");
				vo.setToSRSCodeOne("all");
				vo.setToSRSCode("all");
				vo.setFilter("");
				responseString = util.getCoordinateTransformationChain(vo,
						complexOutput);

			} else if (inputSrsName.equalsIgnoreCase("all")
					&& outputSrsName.equalsIgnoreCase("all")) {
				LOG.debug("Inside All Both.");
				vo.setFromSRSCodeOne("all");
				vo.setFromSRSCode("all");
				vo.setToSRSCodeOne("all");
				vo.setToSRSCode("all");
				vo.setFilter("");
				responseString = util.getCoordinateTransformationChain(vo,
						complexOutput);

			} else if (!inputSrsName.equals("")
					&& outputSrsName.equalsIgnoreCase("all")) {
				LOG.debug("Inside inputSRSName not empty.");
				if (inputSrsName.equals(whs09)) {
					vo1 = new OsloServiceVO();
					vo1.setFromSRSCode(whs09);
					vo1.setToSRSCode(whs10);
					srsCodeList.add(vo1);
					vo.setFilter("");
				} else if (inputSrsName.equals(whs10)) {
					vo1 = new OsloServiceVO();
					vo1.setFromSRSCode(whs10);
					vo1.setToSRSCode(whs09);
					srsCodeList.add(vo1);
					vo.setFilter("");
				}

				responseString = util.listTransformations(vo, complexOutput,
						srsCodeList);
			} else if (!outputSrsName.equals("")
					&& inputSrsName.equalsIgnoreCase("all")) {
				LOG.debug("Inside outputSRSName not empty.");
				if (outputSrsName.equals(whs09)) {
					vo1 = new OsloServiceVO();
					vo1.setFromSRSCode(whs10);
					vo1.setToSRSCode(whs09);
					srsCodeList.add(vo1);
					vo.setFilter("");
				} else if (outputSrsName.equals(whs10)) {
					vo1 = new OsloServiceVO();
					vo1.setFromSRSCode(whs09);
					vo1.setToSRSCode(whs10);
					srsCodeList.add(vo1);
					vo.setFilter("");
				}
				responseString = util.listTransformations(vo, complexOutput,
						srsCodeList);
			} else if (!inputSrsName.equals("") || inputSrsName != null
					&& !outputSrsName.equals("") || outputSrsName != null) {
				LOG.debug("Both Legitimate values.");
				vo.setFromSRSCodeOne(inputSrsName);
				vo.setFromSRSCode(inputSrsName);
				vo.setToSRSCodeOne(outputSrsName);
				vo.setToSRSCode(outputSrsName);
				vo.setFilter(filter);
				responseString = util.getCoordinateTransformationChain(vo,
						complexOutput);
			} else {
				LOG.debug("Nothing matched..");
				responseString = "Error: No such transformation chain is supported under this hub.";
			}

			LOG.debug(" Response String = {}", responseString);
			LOG.debug("After the check condition");

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			java.util.Date date = new java.util.Date();
			String currentTime = dateFormat.format(date);
			vo.setCurrentTime(currentTime);

			if (responseString.startsWith("Error:")) {
				responseString = responseString.replaceAll("Error: ", "");
				throw new OWSException(responseString,
						ControllerException.NO_APPLICABLE_CODE);
			}

		} catch (MissingParameterException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidParameterValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidDataInputValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(e); // is already OWSException
		} catch (OWSException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(e); // is already OWSException
		} catch (Throwable e) {
			String message = "Unexpected exception occurred: " + e.getMessage();
			LOG.error(message, e);
			throw new ProcessletException(new OWSException(message, e,
					ControllerException.NO_APPLICABLE_CODE));
		}

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
