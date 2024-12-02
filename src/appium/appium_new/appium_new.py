# Import necessary libraries
import unittest
from appium import webdriver
from appium.webdriver.common.mobileby import MobileBy
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import logging
import time
import json

# Configure logging
logging.basicConfig(level=logging.INFO)

class Care4TodayTests(unittest.TestCase):
    def setUp(self):
        # Setup Appium driver for iOS and Android
        with open('config.json') as config_file:
            config = json.load(config_file)
        
        if config['platformName'] == 'Android':
            self.desired_caps = {
                'platformName': 'Android',
                'platformVersion': config['platformVersion'],
                'deviceName': config['deviceName'],
                'appPackage': 'com.care4today.android',
                'appActivity': 'com.care4today.android.MainActivity',
                'automationName': 'UiAutomator2',
                'noReset': True
            }
        else:  # iOS
            self.desired_caps = {
                'platformName': 'iOS',
                'platformVersion': config['platformVersion'],
                'deviceName': config['deviceName'],
                'bundleId': 'com.care4today.ios',
                'automationName': 'XCUITest',
                'noReset': True
            }

        self.driver = webdriver.Remote('http://localhost:4723/wd/hub', self.desired_caps)
        self.driver.implicitly_wait(10)

    def tearDown(self):
        # Quit the driver
        self.driver.quit()

    def test_user_authentication(self):
        logging.info("Starting user authentication test")
        try:
            # Locate and interact with login elements
            username_field = self.driver.find_element(MobileBy.XPATH, '//input[@name="username"]')
            password_field = self.driver.find_element(MobileBy.XPATH, '//input[@name="password"]')
            login_button = self.driver.find_element(MobileBy.XPATH, '//button[@name="login"]')

            # Input credentials
            username_field.send_keys('testuser')
            password_field.send_keys('password123')
            login_button.click()

            # Wait for authentication to complete and validate
            home_screen = WebDriverWait(self.driver, 20).until(
                EC.presence_of_element_located((MobileBy.XPATH, '//div[@id="homeScreen"]'))
            )
            self.assertIsNotNone(home_screen, "Login failed, home screen not displayed.")
            logging.info("User authentication test passed.")
        except Exception as e:
            logging.error(f"User authentication test failed: {e}")
            self.driver.save_screenshot('screenshots/user_authentication_failure.png')
            self.fail("User authentication test failed")

    def test_navigate_and_interact(self):
        logging.info("Starting navigation and interaction test")
        try:
            # Navigate through key features
            menu_button = self.driver.find_element(MobileBy.XPATH, '//button[@name="menu"]')
            menu_button.click()

            features_button = self.driver.find_element(MobileBy.XPATH, '//button[@name="features"]')
            features_button.click()

            # Interact with UI elements
            feature_item = self.driver.find_element(MobileBy.XPATH, '//div[@name="featureItem1"]')
            feature_item.click()

            # Validate data input and output
            input_field = self.driver.find_element(MobileBy.XPATH, '//input[@name="dataInput"]')
            input_field.send_keys('Test Data')
            submit_button = self.driver.find_element(MobileBy.XPATH, '//button[@name="submit"]')
            submit_button.click()

            result_text = self.driver.find_element(MobileBy.XPATH, '//div[@name="resultText"]')
            self.assertEqual(result_text.text, "Expected Output", "Data output validation failed.")
            logging.info("Navigation and interaction test passed.")
        except Exception as e:
            logging.error(f"Navigation and interaction test failed: {e}")
            self.driver.save_screenshot('screenshots/navigate_and_interact_failure.png')
            self.fail("Navigation and interaction test failed")

    def test_handle_popups(self):
        logging.info("Starting handle pop-ups test")
        try:
            # Trigger a pop-up
            popup_button = self.driver.find_element(MobileBy.XPATH, '//button[@name="triggerPopup"]')
            popup_button.click()

            # Handle the pop-up
            alert = WebDriverWait(self.driver, 10).until(EC.alert_is_present())
            alert_text = alert.text
            alert.accept()

            # Validate the pop-up handling
            self.assertEqual(alert_text, "Expected Alert Text", "Alert text validation failed.")
            logging.info("Handle pop-ups test passed.")
        except Exception as e:
            logging.error(f"Handle pop-ups test failed: {e}")
            self.driver.save_screenshot('screenshots/handle_popups_failure.png')
            self.fail("Handle pop-ups test failed")

    def test_offline_online_behavior(self):
        logging.info("Starting offline and online behavior test")
        try:
            # Simulate offline mode
            self.driver.set_network_connection(0)  # Airplane mode
            self.driver.find_element(MobileBy.XPATH, '//button[@name="offlineModeFeature"]').click()
            offline_message = self.driver.find_element(MobileBy.XPATH, '//div[@name="offlineMessage"]')
            self.assertIsNotNone(offline_message, "Offline mode message not displayed.")

            # Simulate online mode
            self.driver.set_network_connection(2)  # Wi-Fi only
            time.sleep(5)  # Wait for the network to stabilize
            self.driver.find_element(MobileBy.XPATH, '//button[@name="onlineModeFeature"]').click()
            online_message = self.driver.find_element(MobileBy.XPATH, '//div[@name="onlineMessage"]')
            self.assertIsNotNone(online_message, "Online mode message not displayed.")
            logging.info("Offline and online behavior test passed.")
        except Exception as e:
            logging.error(f"Offline and online behavior test failed: {e}")
            self.driver.save_screenshot('screenshots/offline_online_behavior_failure.png')
            self.fail("Offline and online behavior test failed")

if __name__ == '__main__':
    unittest.main()