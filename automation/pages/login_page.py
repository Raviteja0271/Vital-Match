from appium.webdriver.common.appiumby import AppiumBy
from automation.pages.base_page import BasePage

class LoginPage(BasePage):
    EMAIL_INPUT = (AppiumBy.ACCESSIBILITY_ID, "EmailInput")
    PASSWORD_INPUT = (AppiumBy.ACCESSIBILITY_ID, "PasswordInput")
    LOGIN_BUTTON = (AppiumBy.ACCESSIBILITY_ID, "LoginButton")
    REGISTER_LINK = (AppiumBy.ACCESSIBILITY_ID, "RegisterLink")
    FORGOT_PASSWORD_LINK = (AppiumBy.ACCESSIBILITY_ID, "ForgotPasswordLink")
    ERROR_ALERT = (AppiumBy.ACCESSIBILITY_ID, "ErrorAlert")

    def enter_email(self, email: str):
        return self.send_keys(self.EMAIL_INPUT, email)

    def enter_password(self, password: str):
        return self.send_keys(self.PASSWORD_INPUT, password)

    def click_login(self):
        return self.click(self.LOGIN_BUTTON)

    def login(self, email: str, password: str):
        self.enter_email(email)
        self.enter_password(password)
        return self.click_login()
