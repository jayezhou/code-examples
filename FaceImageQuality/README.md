人脸图像质量（FaceImageQuality, FIR）检测
参考了https://github.com/Tencent/TFace和https://github.com/RuoyuChen10/FaceTechnologyTool
用TFace中的quality进行人脸质量检测，用FaceTechnologyTool中的FaceRecognition/tutorial_face_alignment.ipynb进行人脸对齐。
下载TFace预训练的模型 https://drive.google.com/file/d/1AM0iWVfSVWRjCriwZZ3FXiUGbcDzkF25/view?usp=sharing，放到model目录中。
执行 python eval.py 进行人脸图像质量评分（python版本3.11.4测试通过，各包版本参见requirements.txt）

关于TFace的提示：
1、eval.py的align函数中，获取到了人脸矩形区域和五点landmarks(5p landmarks)，其中5p landmarks在TFace的Recognition中准备训练数据的时候会用到，即 img2tfrecord.py [-h] --img_list IMG_LIST --pts_list PTS_LIST --tfrecords_name TFRECORDS_NAME 命令的pts_list
可以透過例如RetinaFace等方法，對資料集Inference產生人臉五個關鍵點，寫成txt檔格式(img_path 5p)，格式如下:
/home/duetFace/TFace/dataset/ms1m_align_112/imgs/0/2.jpg 42.536884 54.11936 73.35145 54.520832 50.009483 69.264175 42.136364 89.04865 68.6423 89.41219
/home/duetFace/TFace/dataset/ms1m_align_112/imgs/0/3.jpg 38.085724 50.03808 70.68913 49.658703 58.34093 68.98646 42.67452 88.34386 67.93545 87.85018
/home/duetFace/TFace/dataset/ms1m_align_112/imgs/0/5.jpg 37.62496 52.889153 72.1297 51.866932 54.9144 70.712814 40.97876 89.58896 69.68892 88.4794
/home/duetFace/TFace/dataset/ms1m_align_112/imgs/0/6.jpg 42.026955 53.526344 70.632645 53.77695 47.327274 70.86512 42.44559 89.870766 65.69877 90.06203
/home/duetFace/TFace/dataset/ms1m_align_112/imgs/0/10.jpg 41.27249 55.610893 72.84977 54.190445 51.748768 73.98092 43.707455 88.623856 73.385 87.48047
2、