from appium import webdriver
from appium.options.android import UiAutomator2Options
from automation.config.appium_config import AppiumConfig
from automation.utils.logger_util import get_logger

logger = get_logger("DriverFactory")

class DriverFactory:
    _driver = None

    @classmethod
    def get_driver(cls, apk_path: str = None):
        if cls._driver is None:
            try:
                caps = AppiumConfig.get_desired_capabilities(apk_path)
                options = UiAutomator2Options()
                options.load_capabilities(caps)
                
                logger.info(f"Connecting to Appium Server at {AppiumConfig.APPIUM_SERVER_URL}...")
                cls._driver = webdriver.Remote(AppiumConfig.APPIUM_SERVER_URL, options=options)
                cls._driver.implicitly_wait(10)
                logger.info("Appium Driver initialized successfully!")
            except Exception as e:
                logger.warning(f"Appium Driver initialization skipped or failed: {e}")
                cls._driver = None
        return cls._driver

    @classmethod
    def quit_driver(cls):
        if cls._driver is not None:
            try:
                cls._driver.quit()
                logger.info("Appium Driver terminated cleanly.")
            except Exception as e:
                logger.error(f"Error terminating Appium driver: {e}")
            finally:
                cls._driver = None
