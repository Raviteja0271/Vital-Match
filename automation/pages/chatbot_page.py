from appium.webdriver.common.appiumby import AppiumBy
from automation.pages.base_page import BasePage

class ChatbotPage(BasePage):
    CHAT_INPUT = (AppiumBy.ACCESSIBILITY_ID, "ChatbotInput")
    SEND_BUTTON = (AppiumBy.ACCESSIBILITY_ID, "ChatbotSendBtn")
    CHIP_SHOW_ALL = (AppiumBy.ACCESSIBILITY_ID, "ChipShowAllDonors")
    CHIP_PRAKASAM = (AppiumBy.ACCESSIBILITY_ID, "ChipDonorsInPrakasam")
    CHIP_A_PLUS = (AppiumBy.ACCESSIBILITY_ID, "ChipAPlusDonors")

    def ask_query(self, query: str):
        self.send_keys(self.CHAT_INPUT, query)
        return self.click(self.SEND_BUTTON)

    def click_show_all(self):
        return self.click(self.CHIP_SHOW_ALL)
