package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.HomePage;
import com.example.pages.CartPage;
import com.example.pages.CheckoutPage;
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

        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (Boolean.parseBoolean(prop.getProperty("chromeHeadless"))) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        }
        // Implement for other browsers similarly

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
    public void testLoginFunctionality(String url, String username, String password) {
        driver.get(url);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        Assert.assertTrue(new HomePage(driver).isPageLoaded(), "Login failed!");
    }

    @Test(dataProvider = "loginData")
    public void testShoppingFunctionality(String url, String username, String password) {
        driver.get(url);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageLoaded(), "Login failed!");

        homePage.addItemToCart();
        Assert.assertTrue(homePage.isItemAddedToCart(), "Item not added to cart!");

        homePage.goToCart();
        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isPageLoaded(), "Navigation to cart failed!");
        Assert.assertTrue(cartPage.isItemInCart(), "Item not found in cart!");

        cartPage.clickCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Navigation to checkout failed!");

        checkoutPage.enterFirstName("Test");
        checkoutPage.enterLastName("User");
        checkoutPage.enterZipCode("12345");
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isTotalCorrect(), "Total cost mismatch!");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderPlaced(), "Order placement failed!");

        checkoutPage.captureScreenshot("OrderConfirmation");
    }
}