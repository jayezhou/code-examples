import os
import torch
import torchvision.transforms as T
from generate_pseudo_labels.extract_embedding.model import model
import numpy as np
from PIL import Image
import cv2
#import  matplotlib.pyplot as plt
import sys
#print(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(os.path.dirname(os.path.abspath(__file__)))
from tools.alignment import FaceAlignment
from mtcnn.src import detect_faces


def read_img(warp_image):     # read image & data pre-process
    data = torch.randn(1, 3, 112, 112)
    transform = T.Compose([
        T.Resize((112, 112)),
        T.ToTensor(),
        T.Normalize(mean=[0.5, 0.5, 0.5], std=[0.5, 0.5, 0.5]),
    ])
    img = warp_image.convert("RGB")
    data[0, :, :, :] = transform(img)
    return data


def network(eval_model, device):
    net = model.R50([112, 112], use_type="Qua").to(device)
    net_dict = net.state_dict()     
    data_dict = {
        key.replace('module.', ''): value for key, value in torch.load(eval_model, map_location=device).items()}
    net_dict.update(data_dict)
    net.load_state_dict(net_dict)
    net.eval()
    return net
    

def align(original_image):
    bounding_boxes, landmarks = detect_faces(original_image)
    if (len(landmarks) == 0): 
        return None
    image = cv2.cvtColor(np.array(original_image), cv2.COLOR_RGB2BGR)
    # alignment
    face_align = FaceAlignment()
    lnk = landmarks.reshape((-1,5,2), order='F').astype(int)[0] # The first landmark
    print(lnk)
    warp_image = face_align(image, lnk)
    #print(warp_image)
    return warp_image


# def imshow(img):
#     """
#     jupyter imshow
#     """
#     plt.figure(dpi=50)
#     plt.axis('off')
#     img = img[:,:,::-1] 	# transform image to rgb
#     plt.imshow(img)
#     plt.show()
    
def eval(img):
    eval_model = './model/SDD_FIQA_checkpoints_r50.pth'   # checkpoint
    device = 'cpu'                                        # 'cpu' or 'cuda:x'
    net = network(eval_model, device)
    warp_image = align(img)
    if type(warp_image) == type(None):
        return 0
    warp_image_pil = Image.fromarray(cv2.cvtColor(warp_image,cv2.COLOR_BGR2RGB))    
    input_data = read_img(warp_image_pil)
    pred_score = net(input_data).data.cpu().numpy().squeeze()
    return pred_score


if __name__ == "__main__":    
    imgpath = './demo_imgs/a.jpg'                         # [1,2,3.jpg]
    image = Image.open(imgpath).convert('RGB')
    pred_score = eval(image)
    print(f"Quality score = {pred_score}")

# if __name__ == "__main__":
#     imgpath = './demo_imgs/112.jpg'                         # [1,2,3.jpg]
#     device = 'cpu'                                        # 'cpu' or 'cuda:x'
#     eval_model = './model/SDD_FIQA_checkpoints_r50.pth'   # checkpoint
#     net = network(eval_model, device)
    
#     #original_image = Image.open(imgpath)
#     original_image = Image.open(imgpath).convert('RGB')
#     warp_image = align(original_image)
#     if type(warp_image) == type(None):
#         print(f"Quality score = 0")
#         sys.exit(0)
#     #imshow(warp_image)
    
#     warp_image_pil = Image.fromarray(cv2.cvtColor(warp_image,cv2.COLOR_BGR2RGB))    
#     input_data = read_img(warp_image_pil)
#     pred_score = net(input_data).data.cpu().numpy().squeeze()
#     print(f"Quality score = {pred_score}")
