import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Care4TodayTest {
    private WebDriver driver;
    private DesiredCapabilities capabilities;

    @BeforeClass
    @Parameters({"platformName", "deviceName", "appPackage", "appActivity", "appPath"})
    public void setUp(String platformName, String deviceName, String appPackage, String appActivity, String appPath) throws Exception {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.APP, new File(appPath).getAbsolutePath());

        if (platformName.equalsIgnoreCase("Android")) {
            capabilities.setCapability("appPackage", appPackage);
            capabilities.setCapability("appActivity", appActivity);
            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } else if (platformName.equalsIgnoreCase("iOS")) {
            driver = new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testLoginAndNavigate() throws Exception {
        // Load test data from JSON file
        JSONParser parser = new JSONParser();
        JSONObject testData = (JSONObject) parser.parse(new FileReader("src/test/resources/testData.json"));

        // Test data variables
        String username = (String) testData.get("username");
        String password = (String) testData.get("password");

        // Open the application and perform authentication
        MobileElement loginButton = (MobileElement) driver.findElement(By.xpath("//android.widget.Button[@content-desc='Login']"));
        loginButton.click();

        MobileElement usernameField = (MobileElement) driver.findElement(By.xpath("//android.widget.EditText[@content-desc='Username']"));
        usernameField.sendKeys(username);

        MobileElement passwordField = (MobileElement) driver.findElement(By.xpath("//android.widget.EditText[@content-desc='Password']"));
        passwordField.sendKeys(password);

        MobileElement submitButton = (MobileElement) driver.findElement(By.xpath("//android.widget.Button[@content-desc='Submit']"));
        submitButton.click();

        // Validate successful login
        MobileElement homeScreen = (MobileElement) driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Home']"));
        Assert.assertTrue(homeScreen.isDisplayed(), "Login failed!");

        // Navigate to key features
        MobileElement featureButton = (MobileElement) driver.findElement(By.xpath("//android.widget.Button[@content-desc='Features']"));
        featureButton.click();

        // Interact with UI elements
        MobileElement dropdown = (MobileElement) driver.findElement(By.xpath("//android.widget.Spinner[@content-desc='Options']"));
        dropdown.click();
        MobileElement dropdownOption = (MobileElement) driver.findElement(By.xpath("//android.widget.CheckedTextView[@text='Option 1']"));
        dropdownOption.click();

        MobileElement textField = (MobileElement) driver.findElement(By.xpath("//android.widget.EditText[@content-desc='InputField']"));
        textField.sendKeys("Test input");

        MobileElement saveButton = (MobileElement) driver.findElement(By.xpath("//android.widget.Button[@content-desc='Save']"));
        saveButton.click();

        // Validate input data
        MobileElement successMessage = (MobileElement) driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Success']"));
        Assert.assertTrue(successMessage.isDisplayed(), "Input data validation failed!");

        // Handle pop-ups
        MobileElement popup = (MobileElement) driver.findElement(By.xpath("//android.widget.TextView[@content-desc='PopupMessage']"));
        if (popup.isDisplayed()) {
            MobileElement popupCloseButton = (MobileElement) driver.findElement(By.xpath("//android.widget.Button[@content-desc='Close']"));
            popupCloseButton.click();
        }

        // Capture screenshot on failure
        // (Implementation of screenshot capture logic here)

        // Clean up: reset environment
        // (Implementation of environment reset logic here)
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}