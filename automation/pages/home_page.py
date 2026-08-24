from appium.webdriver.common.appiumby import AppiumBy
from automation.pages.base_page import BasePage

class HomePage(BasePage):
    WELCOME_HEADER = (AppiumBy.ACCESSIBILITY_ID, "WelcomeHeader")
    LOCATION_TEXT = (AppiumBy.ACCESSIBILITY_ID, "LocationText")
    TOTAL_DONORS_CARD = (AppiumBy.ACCESSIBILITY_ID, "TotalDonorsCard")
    AVAILABLE_DONORS_CARD = (AppiumBy.ACCESSIBILITY_ID, "AvailableDonorsCard")
    EMERGENCY_REQUESTS_CARD = (AppiumBy.ACCESSIBILITY_ID, "EmergencyRequestsCard")
    CONNECTIONS_CARD = (AppiumBy.ACCESSIBILITY_ID, "ConnectionsCard")
    CHATBOT_FAB = (AppiumBy.ACCESSIBILITY_ID, "ChatbotFab")

    def get_welcome_text(self):
        return self.get_text(self.WELCOME_HEADER)

    def click_chatbot_fab(self):
        return self.click(self.CHATBOT_FAB)
