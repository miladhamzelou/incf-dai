package org.incf.atlas.waxml.examples;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import org.incf.atlas.waxml.utilities.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       CoordinateTransformResponse ct = new CoordinateTransformResponse();
       String ctResponse = ct.asXml();
        try {
        	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/CoordinateTransformatioChainResponse.xml"));
            out.write(ctResponse);
            out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error CoordinateTransformResponse");
		e.printStackTrace();
	}
	  GenesResponse gr = new GenesResponse();
      String grResponse = gr.AsXml();
       try {
       	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/GenesResponse.xml"));
           out.write(grResponse);
           out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error GenesREsponse");
		e.printStackTrace();
	}
	
	
	StructureTermsResponse str = new StructureTermsResponse();
    String StructureTermsResponse = str.AsXML();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/StructureTermsResponse.xml"));
         out.write(StructureTermsResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error StructureTermsResponse");
		e.printStackTrace();
	}
	
	TransformationResponse tr = new TransformationResponse();
    String tranformationResponse = tr.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/TransformationResponse.xml"));
         out.write(tranformationResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error TransformationResponse");
		e.printStackTrace();
	}
	
	TransformationResponsePointarray tr2 = new TransformationResponsePointarray();
    String tranformationResponse2 = tr2.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/TransformationResponse_pointarray.xml"));
         out.write(tranformationResponse2);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error TransformationResponse_pointarray");
		e.printStackTrace();
	}
	
	ListTransformsResponse lr = new ListTransformsResponse();
    String listTransformsResponse = lr.asXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ListTransformsResponse.xml"));
         out.write(listTransformsResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error ListTransformsRespone");
		e.printStackTrace();
	}
	
	ImagesResponse2DImagesByPOI ir = new ImagesResponse2DImagesByPOI();
    String ImagesResponse = ir.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ImagesResponse_2DImagesByPOI.xml"));
         out.write(ImagesResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated ca tch block
		System.out.println("Error ImagesResponse");
		e.printStackTrace();
	}
	
	ImagesMultiExampleResponse irm = new ImagesMultiExampleResponse();
    String ImagesResponsemulti = irm.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ImagesResponse_multipleImages.xml"));
         out.write(ImagesResponsemulti);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error ImagesMultiExampleResponse");
		e.printStackTrace();
	}
	
	ImagesByUriResponse iur = new ImagesByUriResponse();
    String ImagesByUriResponse = iur.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ImagesResponseByUri.xml"));
         out.write(ImagesByUriResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error ImagesByUriResponse");
		e.printStackTrace();
	}
	
	Retrieve2dImageResponse r2di = new Retrieve2dImageResponse();
    String Images2dResponse = r2di.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/Retrieve2DImageResponse.xml"));
         out.write(Images2dResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error Images2drESPONSE");e.printStackTrace();
	}
	
	CorrelationMapResponse cmr = new CorrelationMapResponse();
    String CorrelationResponse = cmr.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/CorrelationMapResponse.xml"));
         out.write(CorrelationResponse);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error CorrelationMapResponse");
		e.printStackTrace();
	}
	CorrelationMapResponseMultipoint cmr2 = new CorrelationMapResponseMultipoint();
    String CorrelationResponse_Multipoint = cmr2.AsXml();
     try {
     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/CorrelationMapResponse_Multipoint.xml"));
         out.write(CorrelationResponse_Multipoint);
         out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error CorrelationMapResponse");
		e.printStackTrace();
	}
	
	ListSRSResponse srs1 = new ListSRSResponse();
	String SrsResponseFull = srs1.AsXml();
	 try {
	     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ListSRSResponse_full.xml"));
	         out.write(SrsResponseFull);
	         out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error ListSRSResponse");
			e.printStackTrace();
		}
		DescribeSrsResponse ssrs = new DescribeSrsResponse();
		String dSrsResponseFull = ssrs.AsXml();
		 try {
		     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/DescribeSRSResponse_full.xml"));
		         out.write(dSrsResponseFull);
		         out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error DescribeSrsResponse");
				e.printStackTrace();
			}
			
			ListSRSResponse_Multi srs2 = new ListSRSResponse_Multi();
			String SrsResponseFull_Multi = srs2.AsXml();
			 try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/ListSRSResponse_multi.xml"));
			         out.write(SrsResponseFull_Multi);
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error ListSRSResponse");
					e.printStackTrace();
				}
				
				SetAnnotationResponse sar = new SetAnnotationResponse();
			    String SetAnnotationResponse = sar.asXml();
			     try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/SetAnnotationResponsePolygon.xml"));
			         out.write(SetAnnotationResponse);
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error SetAnnotationResponse");
					e.printStackTrace();
				}
				SetAnnotationResponseLine sar2 = new SetAnnotationResponseLine();
			    String SetAnnotationResponseLine = sar2.asXml();
			     try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/SetAnnotationResponseLine.xml"));
			         out.write(SetAnnotationResponseLine);
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error SetAnnotationResponse");
					e.printStackTrace();
				}		
				
				SetAnnotationResponseSurface sar3 = new SetAnnotationResponseSurface();
			    String SetAnnotationResponsePoint = sar3.asXml();
			     try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/SetAnnotationResponseSurface.xml"));
			         out.write(SetAnnotationResponsePoint);
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error SetAnnotationResponse");
					e.printStackTrace();
				}	
				
				DescribeProcessCentral pdc = new DescribeProcessCentral();
			    String DescribeProcess_Xml = pdc.AsXml();
			     try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/DescribeProcess_central.xml"));
			         out.write( DescribeProcess_Xml );
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error DescribeProcess_central");
					e.printStackTrace();
				}	
				
				RegistrationResponse rrc = new RegistrationResponse();
			    String RegistrationResponseXml = rrc.asXml();
			     try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/RegistrationResponse.xml"));
			         out.write( RegistrationResponseXml );
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error RegistrationResponse");
					e.printStackTrace();
				}	
				
				RegistrationRequestXml rrx = new RegistrationRequestXml();
			    String RegistrationRequestXml = rrx.asXml();
			     try {
			     	BufferedWriter out = new BufferedWriter(new FileWriter("Examples/RegistrationRequest.xml"));
			         out.write( RegistrationRequestXml );
			         out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error RegistrationResponse");
					e.printStackTrace();
				}	
	System.out.println("done");
       
	}

}
