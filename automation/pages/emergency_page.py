from appium.webdriver.common.appiumby import AppiumBy
from automation.pages.base_page import BasePage

class EmergencyPage(BasePage):
    BLOOD_GROUP_DROPDOWN = (AppiumBy.ACCESSIBILITY_ID, "BloodGroupSelect")
    HOSPITAL_INPUT = (AppiumBy.ACCESSIBILITY_ID, "HospitalInput")
    CONTACT_INPUT = (AppiumBy.ACCESSIBILITY_ID, "ContactInput")
    STATE_DROPDOWN = (AppiumBy.ACCESSIBILITY_ID, "StateSelect")
    DISTRICT_DROPDOWN = (AppiumBy.ACCESSIBILITY_ID, "DistrictSelect")
    CITY_DROPDOWN = (AppiumBy.ACCESSIBILITY_ID, "CitySelect")
    SUBMIT_EMERGENCY_BUTTON = (AppiumBy.ACCESSIBILITY_ID, "SubmitEmergencyBtn")
    BLOOD_RECEIVED_BUTTON = (AppiumBy.ACCESSIBILITY_ID, "BloodReceivedBtn")

    def post_emergency(self, blood_group: str, hospital: str, contact: str):
        self.send_keys(self.HOSPITAL_INPUT, hospital)
        self.send_keys(self.CONTACT_INPUT, contact)
        return self.click(self.SUBMIT_EMERGENCY_BUTTON)
