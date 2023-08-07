import os
import torch
import torchvision.transforms as T
from generate_pseudo_labels.extract_embedding.model import model
import numpy as np
from PIL import Image
import cv2
import sys
sys.path.append(os.path.dirname(os.path.abspath(__file__)))
from tools.alignment import FaceAlignment
from mtcnn.src import detect_faces


class Evaluator():


    def __init__(self):
        eval_model = './model/SDD_FIQA_checkpoints_r50.pth'   # checkpoint
        device = 'cpu'                                        # 'cpu' or 'cuda:x'
        self.net = self.network(eval_model, device)


    def read_img(self, warp_image):     # read image & data pre-process
        data = torch.randn(1, 3, 112, 112)
        transform = T.Compose([
            T.Resize((112, 112)),
            T.ToTensor(),
            T.Normalize(mean=[0.5, 0.5, 0.5], std=[0.5, 0.5, 0.5]),
        ])
        img = warp_image.convert("RGB")
        data[0, :, :, :] = transform(img)
        return data


    def network(self, eval_model, device):
        net = model.R50([112, 112], use_type="Qua").to(device)
        net_dict = net.state_dict()     
        data_dict = {
            key.replace('module.', ''): value for key, value in torch.load(eval_model, map_location=device).items()}
        net_dict.update(data_dict)
        net.load_state_dict(net_dict)
        net.eval()
        return net


    def align(self, original_image):
        bounding_boxes, landmarks = detect_faces(original_image)
        if (len(landmarks) == 0): 
            return None
        image = cv2.cvtColor(np.array(original_image), cv2.COLOR_RGB2BGR)
        # alignment
        face_align = FaceAlignment()
        lnk = landmarks.reshape((-1,5,2), order='F').astype(int)[0] # The first landmark
        # print(lnk)
        warp_image = face_align(image, lnk)
        #print(warp_image)
        return warp_image


    def eval(self, img):
        warp_image = self.align(img)
        if type(warp_image) == type(None):
            return 0
        warp_image_pil = Image.fromarray(cv2.cvtColor(warp_image,cv2.COLOR_BGR2RGB))    
        input_data = self.read_img(warp_image_pil)
        pred_score = self.net(input_data).data.cpu().numpy().squeeze()
        return pred_score