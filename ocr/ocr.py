from fastapi import FastAPI, APIRouter, HTTPException, File, UploadFile, status
import tempfile
import os, sys
from paddleocr import PaddleOCR
from fastapi import Body, File, UploadFile
import logging
from datetime import time
import uvicorn

log_dir = os.path.join(os.path.dirname(__file__), "logs")
os.makedirs(log_dir, exist_ok=True)

logger = logging.getLogger("app")

app = FastAPI()
ocr = None

def process_image_ocr(image_path: str):
    result = ocr.ocr(image_path, cls=True)
    logger.info(f"Result content: {result}")

    return [item[1][0] for item in result]
    
@app.on_event("startup")
async def init_ocr():
    global ocr
    original_argv = sys.argv
    sys.argv = [sys.argv[0]]
    try:
        ocr = PaddleOCR(use_angle_cls=True, use_gpu=False, lang="ch")
    finally:
        sys.argv = original_argv
    
@app.post("/ocr")
async def ocr_image(
    file: UploadFile = File(None, description="图片文件，支持任意字段名")
):
    if not file:
        logger.error("未传入文件")
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail={
                "code": 422,
                "message": "未传入文件",
                "data": None
            }
        )
        
    if not file.content_type.startswith('image/'):
        logger.error(f"无效的图片格式: {file.content_type}")
        raise HTTPException(400, detail={
            "code": 400,
            "message": "无效的图片格式",
            "data": None
        })

    try:
        with tempfile.NamedTemporaryFile(delete=False, suffix=os.path.splitext(file.filename)[1]) as temp_file:
            content = await file.read()
            temp_file.write(content)
            temp_path = temp_file.name
        
            texts = process_image_ocr(temp_path)
    except Exception as e:
        logger.error("OCR处理失败", exc_info=True)
        raise HTTPException(500, detail={
            "code": 500,
            "message": f"OCR处理失败: {str(e)}",
            "data": None
        })
    finally:
        if os.path.exists(temp_path):
            os.remove(temp_path)
    
    return {
        "code": 200,
        "message": "识别文字成功。",
        "data": {
            "texts": texts
        }
    }

if __name__ == "__main__":
    uvicorn.run("ocr:app", host="0.0.0.0", port=8000)