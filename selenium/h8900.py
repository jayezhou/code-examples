# pip install selenium
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import time

options = webdriver.ChromeOptions()
options.add_argument("--disable-proxy-certificate-handler")
options.add_argument("--allow-running-insecure-content")
options.add_argument("--ignore-certificate-errors")

# driver = webdriver.Chrome()
driver = webdriver.Chrome(options=options)
driver.implicitly_wait(10)
driver.maximize_window()

driver.get("https://111.222.333.444:5555/WPMS/")
driver.find_element(By.ID, "user-name").send_keys("user")
driver.find_element(By.ID, "password").send_keys("pass")
driver.find_element(By.ID, "login-submit").click()
driver.find_element(By.XPATH, '//*[@value="确定"]').click()
driver.find_element(By.XPATH, '//*[@id="home-page"]/a').click()
driver.find_element(By.XPATH, '//*[@name="人脸应用"]').click()
time.sleep(10)
driver.switch_to.frame('iframe-FACE')
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]').send_keys("东湖花园白名单")
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]/../div/button[1]').click()
driver.find_element(By.XPATH, '//button[contains(text(), "人员信息")]').click()

driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]').clear()
time.sleep(2)
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]').send_keys("张飞")
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]/../div/button[1]').click()
time.sleep(2)
driver.find_element(By.XPATH, '//*[@title="删除"]').click()
time.sleep(2)
driver.switch_to.default_content()
driver.find_element(By.XPATH, '//*[@value="确定删除"]').click()
driver.switch_to.frame('iframe-FACE')
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]').clear()
time.sleep(2)
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]').send_keys("张飞")
driver.find_element(By.XPATH, '//*[@placeholder="模糊查询"]/../div/button[1]').click()

time.sleep(5)
driver.close()