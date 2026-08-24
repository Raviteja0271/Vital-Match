import os
import datetime
from automation.utils.logger_util import get_logger

logger = get_logger("ScreenshotUtil")

class ScreenshotUtil:
    @staticmethod
    def capture_screenshot(driver, test_name: str) -> str:
        try:
            base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
            screenshot_dir = os.path.join(base_dir, "screenshots")
            os.makedirs(screenshot_dir, exist_ok=True)

            timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
            filename = f"{test_name}_{timestamp}.png"
            filepath = os.path.join(screenshot_dir, filename)

            if driver:
                driver.save_screenshot(filepath)
                logger.info(f"Captured screenshot: {filepath}")
                return filepath
        except Exception as e:
            logger.error(f"Failed to capture screenshot for {test_name}: {e}")
        return ""
