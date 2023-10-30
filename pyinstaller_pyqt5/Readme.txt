1、安装PyQt5和PyQt5-tools(包含Qt designer)
pip install PyQt5
pip install PyQt5-tools

2、用Qt designer设计UI（Qt designer位于%PYTHON_HOME%\Lib\site-packages\qt5_applications\Qt\bin），保存为Ui_Dialog.ui

3、执行 pyuic5 -o Ui_Dialog.py Ui_Dialog.ui 生成python代码

4、增加一个main.py完成功能

--- 以下是用pyinstaller打包的步骤

5、安装pyinstaller： pip install pyinstaller

6、执行 pyinstaller -w main.py 在dist目录下生成目录，压缩这个目录即可发布。
   如果有问题可以分步执行以下命令：
   a、执行 pyi-makespec -w main.py 生成打包的main.spec文件
   b、修改main.spec文件，在包含"main.py"那个数组加入"Ui_Dialog.py"
   c、打包： pyinstaller main.spec 在dist目录下生成目录，压缩这个目录即可发布
