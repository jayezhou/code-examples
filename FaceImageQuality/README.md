人脸图像质量（FaceImageQuality, FIR）检测
参考了https://github.com/Tencent/TFace和https://github.com/RuoyuChen10/FaceTechnologyTool
用TFace中的quality进行人脸质量检测，用FaceTechnologyTool中的FaceRecognition/tutorial_face_alignment.ipynb进行人脸对齐。
下载TFace预训练的模型 https://drive.google.com/file/d/1AM0iWVfSVWRjCriwZZ3FXiUGbcDzkF25/view?usp=sharing，放到model目录中。
执行 python eval.py 进行人脸图像质量评分（python版本3.11.4测试通过，各包版本参见requirements.txt）