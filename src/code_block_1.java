package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.ReminderPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReminderTest {

    protected WebDriver driver;
    protected Properties prop;
    protected Logger log = LogManager.getLogger(ReminderTest.class);

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(prop.getProperty("chromeHeadless"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "reminderData")
    public Object[][] reminderData() {
        // Load data from external source (e.g., CSV, Excel)
        return new Object[][]{
            {"https://www.example.com/", "example_user", "example_password"}
        };
    }

    @Test(dataProvider = "reminderData")
    public void testReminderFunctionality(String url, String username, String password) {
        driver.get(url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        ReminderPage reminderPage = new ReminderPage(driver);
        reminderPage.navigateToReminders();
        reminderPage.verifyReminderGeneration();
        reminderPage.verifyReminderOptions();
        reminderPage.verifyReminderDetails();
        reminderPage.verifyDashboardSchedule();

        log.info("Reminder functionality test completed successfully");
    }
}