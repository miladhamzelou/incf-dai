# Atlas Function-Hub Applicability Table #

This table indicates which INCF Atlas processes are applicable to the various hubs.

  * An **[X](.md)** is an implemented process and is a link to an example.
  * An **O** is a planned process but is not yet implemented.
  * A blank cell means the process is not applicable to the hub.

<table cellpadding='5' cellspacing='0' border='1'>
<blockquote><tr>
<blockquote><td></td>
<th align='center'>Central</th>
<th align='center'>ABA Hub</th>
<th align='center'>EMAP Hub</th>
<th align='center'>UCSD Hub</th>
<th align='center'>WHS Hub</th>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetCapabilities'>GetCapabilities</a><sup>1</sup></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&request=GetCapabilities'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&request=GetCapabilities'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&request=GetCapabilities'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&request=GetCapabilities'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&request=GetCapabilities'>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#DescribeProcess'>DescribeProcess</a><sup>1</sup></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=DescribeProcess&Identifier=ALL'>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#DescribeSRS'>DescribeSRS</a></th>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=Mouse_ABAreference_1.0'>X</a></b></td>
<td align='center'><b>O</b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=Mouse_Paxinos_1.0'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=DescribeSRS&DataInputs=srsName=Mouse_WHS_0.9'>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#DescribeTransformation'>DescribeTransformation</a></th>
<td align='center'></td>
<td align='center'><b>O</b></td>
<td align='center'><b>O</b></td>
<td align='center'><b>O</b></td>
<td align='center'><b>O</b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#Get2DImagesByPOI'>Get2DImagesByPOI</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3'>X</a></b><sup>2</sup></td>
<td align='center'><b>O</b></td>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3'>X</a></b></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetAnnotationsByPOI'>GetAnnotationsByPOI</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0;tolerance=3'>X</a></b><sup>2</sup></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0;tolerance=3'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=-2;y=-1;z=0;tolerance=3;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=-2;y=-1;z=0;tolerance=3;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=image;x=-2;y=-1;z=0;tolerance=3;filter=filePath:http://ccdb-portal.crbs.ucsd.edu:8081/ZoomifyDataServer/data/MP_1001_rec'>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetCorrelationMapByPOI'>GetCorrelationMapByPOI</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal'>X</a></b><sup>2</sup></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal'>X</a></b></td>
<td align='center'></td>
<td align='center'></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetGenesByPOI'>GetGenesByPOI</a></th>
<td align='center'></td>
<td align='center'><b>O</b></td>
<td align='center'><b>O</b></td>
<td align='center'></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetGeneExpressionByGeneId'>GetGeneExpressionByGeneId</a></th>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetGeneExpressionByGeneId&DataInputs=geneIdentifier=Coch&RawDataOutput=SparseValueVolumeXML'>X</a></b></td>
<td align='center'></td>
<td align='center'></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetObjectsByPOI'>GetObjectsByPOI</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetObjectsByPOI&DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0'>X</a></b><sup>2</sup></td>
<td align='center'></td>
<td align='center'></td>
<td align='center'></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetStructureNamesByPOI'>GetStructureNamesByPOI</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=structureset:anatomic'>X</a></b><sup>2</sup></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:fine'>X</a></b></td>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=-4;y=-2.3;z=2;vocabulary=Mouse_Paxinos_1.0;filter=NONE'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter='>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#GetTransformationChain'>GetTransformationChain</a><sup>3</sup></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_ABAreference_1.0;outputSrsName=Mouse_WHS_1.0;filter=cerebellum'>X</a></b></td>
<td align='center'></td>
<td align='center'></td>
<td align='center'></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#ListSRSs'>ListSRSs</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>X</a></b><sup>2</sup></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>X</a></b></td>
<td align='center'><b>O</b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs'>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#ListTransformations'>ListTransformations</a></th>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>X</a></b><sup>2</sup></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations'>X</a></b></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#Retrieve2DImage'>Retrieve2DImage</a></th>
<td align='center'></td>
<td align='center'><b>O</b></td>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Retrieve2DImage&DataInputs=sourceType=WMS;sourceURL=http%3A%2F%2Fimage.wholebraincatalog.org%2Fcgi-bin%2Fmapserv%3Fmap%3Dcrbsatlas%2Fmapfiles%2Fgensat_3363_modified_sm_transformed-ms.map%26LAYERS%3Dgensat_penk1_09%26FORMAT%3Dpng24%26VERSION%3D1.1.1%26REQUEST%3DGetMap;srsName=Mouse_ABAreference_1.0;xmin=-1.9298;xmax=8.73376;ymin=-9.92461;ymax=1.14128;filter=NONE'>X</a></b></td>
<td align='center'></td>
</blockquote></tr>
<tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#SetAnnotations'>SetAnnotations</a></th>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev-local.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev-local.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev-local.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=SetAnnotation&DataInputs=filePath=http://132.239.131.188/incf-common/Annotation1.xml'>X</a></b></td>
</blockquote></tr>
</blockquote><blockquote><tr>
<blockquote><th align='left'><a href='http://code.google.com/p/incf-dai/wiki/AtlasRequestInterfaceSpec#TransformPOI'>TransformPOI</a></th>
<td align='center'></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_ABAvoxel_1.0_To_Mouse_AGEA_1.0_v1.0;x=1;y=112;z=162'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/emap/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_WHS_1.0_To_Mouse_EMAP-T26_1.0_v1.0;x=12;y=-29;z=-73'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_Paxinos_1.0_To_Mouse_WHS_0.9_v1.0;x=1;y=4.3;z=1.78'>X</a></b></td>
<td align='center'><b><a href='http://incf-dev.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=transformationCode=Mouse_WHS_1.0_To_Mouse_WHS_0.9_v1.0;x=1.5;y=1;z=0.6'>X</a></b></td>
</blockquote><blockquote></tr>
</table></blockquote></blockquote>

  * <sup>1</sup>  The WPS **GetCapabilities** and **DescribeProcess** requests are applicable to central and all hubs.
  * <sup>2</sup>  Union operation across hubs.
  * <sup>3</sup>  Central only processes.