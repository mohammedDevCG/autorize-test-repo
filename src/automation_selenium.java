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

public class LoanApplicationTest {
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

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("baseUrl"));
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
            {"user123", "abcabd"}
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyChecklistFunctionality(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loanApplication")));

        driver.findElement(By.id("loanApplication")).click();

        WebElement addressProof = driver.findElement(By.id("addressProof"));
        WebElement propertyCompletion = driver.findElement(By.id("propertyCompletion"));
        WebElement incomeProof = driver.findElement(By.id("incomeProof"));
        WebElement creditScoreDocument = driver.findElement(By.id("creditScoreDocument"));

        Assert.assertTrue(addressProof.isDisplayed());
        Assert.assertTrue(propertyCompletion.isDisplayed());
        Assert.assertTrue(incomeProof.isDisplayed());
        Assert.assertTrue(creditScoreDocument.isDisplayed());

        addressProof.click();
        driver.findElement(By.id("addressProofCheckbox")).click();

        propertyCompletion.click();
        driver.findElement(By.id("propertyCompletionCheckbox")).click();

        incomeProof.click();
        driver.findElement(By.id("incomeProofCheckbox")).click();

        creditScoreDocument.click();
        driver.findElement(By.id("creditScoreCheckbox")).click();

        driver.findElement(By.id("verificationComplete")).click();

        WebElement verificationStatus = driver.findElement(By.id("verificationStatus"));
        Assert.assertEquals(verificationStatus.getText(), "Complete");
    }

    @Test(dependsOnMethods = "verifyChecklistFunctionality")
    public void confirmCreditScoreAbove500() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.id("creditScoreReport")).click();
        driver.findElement(By.id("username")).sendKeys("user123");
        driver.findElement(By.id("password")).sendKeys("abcabd");
        driver.findElement(By.id("loginButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("creditScorePage")));

        driver.findElement(By.id("searchCustomerId")).sendKeys("acb1243");
        driver.findElement(By.id("searchButton")).click();

        WebElement creditScore = driver.findElement(By.id("creditScore"));
        Assert.assertTrue(Integer.parseInt(creditScore.getText()) > 500);

        driver.findElement(By.id("verificationStatus")).sendKeys(creditScore.getText());
        driver.findElement(By.id("logout")).click();
    }
}