package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.CartPage;
import com.example.pages.CheckoutPage;
import com.example.pages.InventoryPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class TestSuite {
    private WebDriver driver;
    private Properties prop;

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        }
        // Additional browser configurations

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
    }

    @Test(dataProvider = "loginData")
    public void testShoppingFunctionality(String url, String username, String password) {
        driver.get(url);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addItemToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateToCart();
        cartPage.verifyItemsInCart();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickCheckout();
        checkoutPage.enterCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutPage.verifyTotalCost();
        checkoutPage.clickFinish();
        checkoutPage.verifyThankYouMessage();
    }
}