package org.incf.atlas.ucsd.util;

import java.io.IOException;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class ServerQuickTest {
	
	private static final String BASE_URL = 
		"http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba";
	
	private static final String[] URIS = {
		BASE_URL + "?service=WPS&request=GetCapabilities",
		BASE_URL + "?service=WPS&request=GetCapabilities&ResponseForm=f",
		BASE_URL + "?service=WPS&version=1.0.0&request=DescribeProcess",
		BASE_URL + "?service=WPS&version=1.0.0&request=DescribeProcess&ResponseForm=f",
		
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=inputSrsName=Mouse_ABAVoxel_1.0;targetSrsName=Mouse_AGEA_1.0;x=280;y=112;z=162;filter=cerebellum",
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=inputSrsName=Mouse_ABAVoxel_1.0;targetSrsName=Mouse_AGEA_1.0;x=280;y=112;z=162;filter=cerebellum&ResponseForm=f",
		
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_ABAVoxel_1.0;targetSrsName=Mouse_AGEA_1.0;x=280;y=112;z=162;filter=cerebellum",
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_ABAVoxel_1.0;targetSrsName=Mouse_AGEA_1.0;x=280;y=112;z=162;filter=cerebellum&ResponseForm=f",
		
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;targetSrsName=Mouse_ABAReference_1.0;filter=Cerebellum",
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;targetSrsName=Mouse_ABAReference_1.0;filter=Cerebellum&ResponseForm=f",
		
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine",
		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine&ResponseForm=f",
		
//		BASE_URL + "?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=ABAvoxel;x=6272;y=3678;z=4874;gene=C1ql2;zoom=25",
	};

	
	private Client client;
	
	public ServerQuickTest() {
		client = new Client(Protocol.HTTP); 
	}
	
	public boolean runTests() throws IOException {
		boolean totalSuccess = true;
		int passes = 0;
		int failures = 0;
		for (int i = 0; i < URIS.length; i++) {
			Response response = client.get(URIS[i]);
			Status status = response.getStatus();
			if (status.getCode() != Status.SUCCESS_OK.getCode()) {
				totalSuccess = false;
				System.out.printf("Status: %s, URI: %s%n", response.getStatus(),
						URIS[i]);
				failures++;
				continue;
			}
			passes++;
		}
		System.out.printf("Tests passed: %d, failed: %d%n", passes, failures);
		return totalSuccess;
	}

	public static void main(String[] args) throws IOException {
		ServerQuickTest t = new ServerQuickTest();
		System.out.println(String.valueOf(t.runTests()));
	}

}
