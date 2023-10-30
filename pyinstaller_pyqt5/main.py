from PyQt5 import QtWidgets
import sys
from Ui_Dialog import Ui_Dialog
import serial.tools.list_ports

class myApp(QtWidgets.QDialog):
    ser = None

    def __init__(self):
        super(myApp, self).__init__()
        self.ui = Ui_Dialog()
        self.ui.setupUi(self)

        self.ui.btn_conn.clicked.connect(self.conn)
        self.ui.btn_send.clicked.connect(self.send)

        port_list = list(serial.tools.list_ports.comports())
        if len(port_list) <= 0:
            print("没有发现可用串口！")
        else:
            for i in port_list:
                self.ui.comboBox_ports.addItem(i.description, i.device)

    def conn(self):
        # sender = self.sender().text()
        if self.ser is not None:
            if self.ser.isOpen(): self.ser.close()
            self.ui.textEdit_content.setText('closed')
        else:
            self.ser = serial.Serial(port=self.ui.comboBox_ports.currentData(), baudrate=115200, timeout=0.5, \
                bytesize=serial.EIGHTBITS, stopbits=serial.STOPBITS_ONE, parity=serial.PARITY_NONE)
            # self.ser.bytesize = serial.EIGHTBITS
            # self.ser.stopbits = serial.STOPBITS_ONE
            # self.ser.parity = serial.PARITY_NONE
            if not self.ser.isOpen(): self.ser.open()
            self.ui.textEdit_content.setText('opened')
    
    def send(self):       
        self.ui.textEdit_content.setText('sent: ' + self.ui.textEdit_input.toPlainText())


def app():
    app = QtWidgets.QApplication(sys.argv) 
    win = myApp()
    win.show()
    sys.exit(app.exec_())

app()

