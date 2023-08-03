from flask import Flask, request, render_template
from PIL import Image
from evaluator import Evaluator
import io
import base64
import re

app = Flask(__name__)
evaluator = Evaluator()

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/fiqaByForm', methods=['POST'])
def fiqaByForm():
    uploaded_image = request.files["img"]
    pilImg = Image.open(uploaded_image).convert('RGB')
    score = evaluator.eval(pilImg)
    return '{score:' + str(score) + '}'

@app.route('/fiqaByBase64', methods=['POST'])
def fiqaByBase64():
    data = request.get_json()
    uploaded_image = data.get('img')
    uploaded_image = re.sub('^data:image/.+;base64,', '', uploaded_image)
    print(uploaded_image)

    decoded_image = io.BytesIO(base64.b64decode(uploaded_image))
    pilImg = Image.open(decoded_image).convert('RGB')
    score = evaluator.eval(pilImg)
    return '{score:' + str(score) + '}'    

if __name__ == '__main__':
    app.run(host='127.0.0.1', port='8765', debug=True)

