import os

class AppiumConfig:
    APPIUM_SERVER_URL = os.getenv("APPIUM_SERVER_URL", "http://127.0.0.1:4723")
    PLATFORM_NAME = "Android"
    DEVICE_NAME = os.getenv("ANDROID_DEVICE_NAME", "emulator-5554")
    AUTOMATION_NAME = "UiAutomator2"
    APP_PACKAGE = "com.simats.vitalmatch"
    APP_ACTIVITY = ".MainActivity"
    NO_RESET = False
    FULL_RESET = False
    NEW_COMMAND_TIMEOUT = 300
    EXPLICIT_WAIT_TIMEOUT = 15

    @classmethod
    def get_desired_capabilities(cls, apk_path: str = None):
        caps = {
            "platformName": cls.PLATFORM_NAME,
            "appium:deviceName": cls.DEVICE_NAME,
            "appium:automationName": cls.AUTOMATION_NAME,
            "appium:appPackage": cls.APP_PACKAGE,
            "appium:appActivity": cls.APP_ACTIVITY,
            "appium:noReset": cls.NO_RESET,
            "appium:fullReset": cls.FULL_RESET,
            "appium:newCommandTimeout": cls.NEW_COMMAND_TIMEOUT,
            "appium:autoGrantPermissions": True,
        }
        if apk_path and os.path.exists(apk_path):
            caps["appium:app"] = apk_path
        return caps
