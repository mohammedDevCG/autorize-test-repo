package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.ShoppingPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class SaucedemoTests {
    private WebDriver driver;
    private Properties prop;

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

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"https://www.saucedemo.com/", "standard_user", "secret_sauce"}
        };
    }

    @Test(dataProvider = "loginData")
    public void validateLoginFunctionality(String url, String username, String password) {
        driver.get(url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isLoggedIn(), "User should be successfully logged in as standard user");
    }

    @Test(dataProvider = "loginData")
    public void validateShoppingFunctionality(String url, String username, String password) {
        driver.get(url);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isLoggedIn(), "User should be successfully logged in as standard user");

        ShoppingPage shoppingPage = new ShoppingPage(driver);
        shoppingPage.addItemToCart();
        shoppingPage.proceedToCart();
        Assert.assertTrue(shoppingPage.isItemInCart(), "The item should be added in the cart and shown in the cart page");

        shoppingPage.checkout("John", "Doe", "12345");
        Assert.assertTrue(shoppingPage.isCheckoutComplete(), "Thank you for your order! message should be shown");
    }
}