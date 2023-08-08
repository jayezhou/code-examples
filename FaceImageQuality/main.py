from flask import Flask, request, render_template
from PIL import Image
from evaluator import Evaluator
import io
import base64
import re
import logging
from logging.handlers import TimedRotatingFileHandler
import traceback
# import os
# import datetime

app = Flask(__name__)
evaluator = Evaluator()

# current_path = os.getcwd()
# log_filename = current_path + "/logs/" + datetime.datetime.now().strftime("%Y-%m-%d") + ".log"
# mode = ""
# if os.path.exists(log_filename):
#     mode = 'a'
# else:
#     mode = 'w'
logger = logging.getLogger()
logger.setLevel(logging.INFO)
handler = TimedRotatingFileHandler("fiq.log", when="d", interval=1, backupCount=10)
fmt = "%(levelname)s - %(asctime)-15s - %(filename)s - line %(lineno)d --> %(message)s"
date_fmt = "%Y-%m-%d %H:%M:%S"    #"%a %d %b %Y %H:%M:%S"
formatter = logging.Formatter(fmt, date_fmt)
handler.setFormatter(formatter)
logger.addHandler(handler)
# logging.basicConfig(        # 针对 basicConfig 进行配置(basicConfig 其实就是对 logging 模块进行动态的调整，之后可以直接使用)
#     level=logging.INFO,     # INFO 等级以下的日志不会被记录
#     format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',    # 日志输出格式
#     filename='fiq.log',    # 日志存放路径(存放在当前相对路径)
#     filemode=mode,          # 输入模式；如果当前我们文件已经存在，可以使用 'a' 模式替代 'w' 模式
#                             # 与文件写入的模式相似，'w' 模式为没有文件时创建文件；'a' 模式为追加内容写入日志文件
# )


@app.route('/')
def index():
    return render_template('index.html')

@app.route('/fiqaByForm', methods=['POST'])
def fiqaByForm():
    try:
        uploaded_image = request.files["img"]
        pilImg = Image.open(uploaded_image).convert('RGB')
        score = evaluator.eval(pilImg)
        return '{"score":"' + str(score) + '"}'
    except Exception as e:
        logger.error(traceback.format_exc())
        return '{"errcode":"500", "errmsg":"' + str(e) + '"}'

@app.route('/fiqaByBase64', methods=['POST'])
def fiqaByBase64():
    try:
        data = request.get_json()
        uploaded_image = data.get('img')
        uploaded_image = re.sub('^data:image/.+;base64,', '', uploaded_image)
        # print(uploaded_image)

        decoded_image = io.BytesIO(base64.b64decode(uploaded_image))
        pilImg = Image.open(decoded_image).convert('RGB')
        score = evaluator.eval(pilImg)
        return '{"score":"' + str(score) + '"}'
    except Exception as e:
        logger.error(traceback.format_exc())
        return '{"errcode":"500", "errmsg":"' + str(e) + '"}'

if __name__ == '__main__':
    logger.info("Started")
    app.run(host='0.0.0.0', port='8765', debug=False)

