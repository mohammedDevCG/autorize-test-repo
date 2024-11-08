package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class LoanApplicationTests {
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
        return new Object[][] {
            {"user123", "abcabd"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testChecklistFunctionality(String username, String password) {
        driver.get("https://loan-application-system.com");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loanApplicationLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Loan Application']")));
        loanApplicationLink.click();

        List<WebElement> documents = driver.findElements(By.xpath("//ul[@id='documentChecklist']/li"));
        Assert.assertEquals(documents.size(), 4);

        for (WebElement document : documents) {
            document.click();
            WebElement documentContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentContent")));
            Assert.assertTrue(documentContent.isDisplayed());

            WebElement documentCheckbox = document.findElement(By.xpath(".//input[@type='checkbox']"));
            documentCheckbox.click();
            Assert.assertTrue(documentCheckbox.isSelected());
        }

        WebElement completeVerificationButton = driver.findElement(By.id("completeVerification"));
        completeVerificationButton.click();
        WebElement verificationStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("verificationStatus")));
        Assert.assertEquals(verificationStatus.getText(), "Verification Complete");
    }

    @Test(dataProvider = "loginData")
    public void testCreditScoreVerification(String username, String password) {
        driver.get("https://loan-application-system.com");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loanApplicationLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Loan Application']")));
        loanApplicationLink.click();

        WebElement creditScoreLink = driver.findElement(By.xpath("//a[text()='Credit Score Report']"));
        creditScoreLink.click();

        driver.get("https://www.creditscore.com");

        WebElement creditUsernameField = driver.findElement(By.id("creditUsername"));
        WebElement creditPasswordField = driver.findElement(By.id("creditPassword"));
        WebElement creditLoginButton = driver.findElement(By.id("creditLoginButton"));

        creditUsernameField.sendKeys(username);
        creditPasswordField.sendKeys(password);
        creditLoginButton.click();

        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customerSearch")));
        searchBox.sendKeys("acb1243");
        WebElement searchButton = driver.findElement(By.id("searchButton"));
        searchButton.click();

        WebElement creditScore = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("creditScore")));
        int score = Integer.parseInt(creditScore.getText());
        Assert.assertTrue(score > 500);

        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
    }
}