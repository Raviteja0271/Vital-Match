from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from automation.utils.logger_util import get_logger

logger = get_logger("BasePage")

class BasePage:
    def __init__(self, driver):
        self.driver = driver
        self.timeout = 15

    def find_element(self, locator):
        if not self.driver:
            logger.info(f"[Mock Driver] Simulating finding element {locator}")
            return None
        wait = WebDriverWait(self.driver, self.timeout)
        return wait.until(EC.presence_of_element_located(locator))

    def click(self, locator):
        if not self.driver:
            logger.info(f"[Mock Driver] Simulating click on element {locator}")
            return True
        element = self.find_element(locator)
        element.click()
        return True

    def send_keys(self, locator, text):
        if not self.driver:
            logger.info(f"[Mock Driver] Simulating sending keys '{text}' to {locator}")
            return True
        element = self.find_element(locator)
        element.clear()
        element.send_keys(text)
        return True

    def get_text(self, locator):
        if not self.driver:
            logger.info(f"[Mock Driver] Simulating get_text for {locator}")
            return "Sample Text"
        element = self.find_element(locator)
        return element.text
