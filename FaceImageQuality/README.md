人脸图像质量（FaceImageQuality, FIR）检测
参考了https://github.com/Tencent/TFace和https://github.com/RuoyuChen10/FaceTechnologyTool
用TFace中的quality进行人脸质量检测，用FaceTechnologyTool中的FaceRecognition/tutorial_face_alignment.ipynb进行人脸对齐。
下载TFace预训练的模型 https://drive.google.com/file/d/1AM0iWVfSVWRjCriwZZ3FXiUGbcDzkF25/view?usp=sharing，放到model目录中。
执行 python eval.py 进行人脸图像质量评分（python版本3.11.4测试通过，各包版本参见requirements.txt）

关于TFace的提示：eval.py的align函数中，获取到了人脸矩形区域和五点landmarks(5p landmarks)，其中5p landmarks在TFace的Recognition中准备训练数据的时候会用到，即 img2tfrecord.py [-h] --img_list IMG_LIST --pts_list PTS_LIST --tfrecords_name TFRECORDS_NAME 命令的pts_list