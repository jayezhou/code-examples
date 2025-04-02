from distutils.core import setup  
import py2exe  
 
setup(  
    console=['chatbot_deepseek.py'],  # 如果是控制台应用  
    # windows=['your_script.py'],  # 如果是 GUI 应用  
)

# python setup.py py2exe