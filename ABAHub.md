# ABA Hub #

We have the ABA Hub up serving limited WPS functions on incf-dev-local. So far we only have a couple functions and they return incomplete data, but the important thing is that the server is in being. The code is maintained in our source code repository.

## Working WPS Functions ##

  * GetCapabilities -- returns a validated WPS XML response

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&request=GetCapabilities

  * DescribeProcess -- returns a validated WPS XML response

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=DescribeProcess

  * Execute : Get2DImagesByPOI

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=ABAvoxel;x=6272;y=3678;z=4874;gene=C1ql2;zoom=25


  * Execute : TransformPOI

> http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_ABAvoxel_1.0;outputSrsName=Mouse_AGEA_1.0;x=1;y=112;z=162;filter=cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_ABAvoxel_1.0;outputSrsName=Mouse_AGEA_1.0;x=1;y=112;z=162;filter=cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_ABAvoxel_1.0;outputSrsName=Mouse_AGEA_1.0;x=1;y=112;z=162;filter=cerebellum


  * Execute : GetTransformationChain

> http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum


  * Execute : GetStructureNamesByPOI

> http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine

> http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine


  * Execute : ListTransformations

> http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum


  * Execute : GetCorrelationMapByPOI

> http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal

> http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetCorrelationMapByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:coronal

  * Execute : ListSRS

http://132.239.131.188:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs

http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs

http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs


UCSD HUB

  * Execute : TransformPOI

> http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_WHS_0.9;x=1;y=4.3;z=1.78;filter=cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_WHS_0.9;x=1;y=4.3;z=1.78;filter=cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_WHS_0.9;x=1;y=4.3;z=1.78;filter=cerebellum


  * Execute : Get2DImagesByPOI

> http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3

> http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3


  * Execute : Retrieve2DImage

> http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Retrieve2DImage&DataInputs=sourceType=WMS;sourceURL=http%3A%2F%2Fimage.wholebraincatalog.org%2Fcgi-bin%2Fmapserv%3Fmap%3Dcrbsatlas%2Fmapfiles%2Fgensat_3363_modified_sm_transformed-ms.map%26LAYERS%3Dgensat_penk1_09%26FORMAT%3Dpng24%26VERSION%3D1.1.1%26REQUEST%3DGetMap;srsName=Mouse_ABAvoxel_1.0;xmin=-1.9298;xmax=8.73376;ymin=-9.92461;ymax=1.14128;filter=maptype:Sagittal

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Retrieve2DImage&DataInputs=sourceType=WMS;sourceURL=http%3A%2F%2Fimage.wholebraincatalog.org%2Fcgi-bin%2Fmapserv%3Fmap%3Dcrbsatlas%2Fmapfiles%2Fgensat_3363_modified_sm_transformed-ms.map%26LAYERS%3Dgensat_penk1_09%26FORMAT%3Dpng24%26VERSION%3D1.1.1%26REQUEST%3DGetMap;srsName=Mouse_ABAvoxel_1.0;xmin=-1.9298;xmax=8.73376;ymin=-9.92461;ymax=1.14128;filter=maptype:Sagittal

> http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Retrieve2DImage&DataInputs=sourceType=WMS;sourceURL=http%3A%2F%2Fimage.wholebraincatalog.org%2Fcgi-bin%2Fmapserv%3Fmap%3Dcrbsatlas%2Fmapfiles%2Fgensat_3363_modified_sm_transformed-ms.map%26LAYERS%3Dgensat_penk1_09%26FORMAT%3Dpng24%26VERSION%3D1.1.1%26REQUEST%3DGetMap;srsName=Mouse_ABAvoxel_1.0;xmin=-1.9298;xmax=8.73376;ymin=-9.92461;ymax=1.14128;filter=maptype:Sagittal


  * Execute : GetStructureNamesByPOI

> http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=-2;y=-5.3;z=6;vocabulary=Mouse_paxinos_1.0;filter=structureset:Fine

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=-2;y=-5.3;z=6;vocabulary=Mouse_paxinos_1.0;filter=structureset:Fine

> http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=-2;y=-5.3;z=6;vocabulary=Mouse_paxinos_1.0;filter=structureset:Fine


  * Execute : GetTransformationChain

> http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum


  * Execute : ListTransformations

> http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

  * Execute : ListSRS

http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs

http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs

http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs


WHS Hub

  * Execute : GetTransformationChain

> http://132.239.131.188:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

  * Execute : ListTransformations

> http://132.239.131.188:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum


  * Execute : TransformPOI

> http://132.239.131.188:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_WHS_1.0;outputSrsName=Mouse_WHS_0.9;x=-1.5;y=0;z=0;filter=cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_WHS_1.0;outputSrsName=Mouse_WHS_0.9;x=-1.5;y=0;z=0;filter=cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_WHS_1.0;outputSrsName=Mouse_WHS_0.9;x=-1.5;y=0;z=0;filter=cerebellum


  * Execute : GetStructureNamesByPOI

> http://132.239.131.188:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=Mouse_paxinos_1.0;filter=structureset:Fine

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=Mouse_paxinos_1.0;filter=structureset:Fine

> http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=Mouse_paxinos_1.0;filter=structureset:Fine

  * Execute : ListSRS

http://132.239.131.188:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs

http://incf-dev-local.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs

http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=ListSRSs


EMAP Hub

  * Execute : TransformPOI

> http://132.239.131.188:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_WHS_1.0;outputSrsName=Mouse_EMAP-T26_1.0;x=12;y=-29;z=-73;filter=cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_WHS_1.0;outputSrsName=Mouse_EMAP-T26_1.0;x=12;y=-29;z=-73;filter=cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI&DataInputs=inputSrsName=Mouse_WHS_1.0;outputSrsName=Mouse_EMAP-T26_1.0;x=12;y=-29;z=-73;filter=cerebellum


  * Execute : GetTransformationChain

> http://132.239.131.188:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum


  * Execute : ListTransformations

> http://132.239.131.188:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev-local.crbs.ucsd.edu:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

> http://incf-dev.crbs.ucsd.edu:8080/atlas-emap?service=WPS&version=1.0.0&request=Execute&Identifier=ListTransformations&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum


## WPS Functions under Development ##

  * Execute : ListSRSs
  * Execute : DescribeSRS
  * Execute : DescribeTransformation
  * Execute : GetGenesByPOI

## Ancillary URLs ##

  * http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba/favicon.ico

> Returns the INCF favicon. In some cases the favicon is not displaying properly. I need to check this out.

  * http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba/ping/server

> Returns a string acknowledging server connection.