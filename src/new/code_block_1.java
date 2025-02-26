package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class LoanApplicationTest {

    protected WebDriver driver;
    protected Properties prop;

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        }
        // Add conditions for other browsers if needed

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
                {"user123", "abcabd"}
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyDocumentChecklist(String username, String password) {
        driver.get("URL_OF_APPLICATION");

        // Login
        driver.findElement(By.id("loginID")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();

        // Navigate to the loan application
        driver.findElement(By.xpath("//a[contains(text(),'Loan Application')]")).click();

        // Verify all documents are present
        String[] documents = {"Address proof", "Property completion state proof", "Income proof", "Credit score document"};
        for (String document : documents) {
            WebElement docElement = driver.findElement(By.xpath("//div[contains(text(),'" + document + "')]"));
            assert docElement.isDisplayed();
        }

        // Double click to open each document
        for (String document : documents) {
            WebElement docElement = driver.findElement(By.xpath("//div[contains(text(),'" + document + "')]"));
            docElement.click(); // Assuming click will open the document
            // Add steps to handle document view if required
        }

        // Click checkbox against each document
        for (String document : documents) {
            WebElement checkbox = driver.findElement(By.xpath("//div[contains(text(),'" + document + "')]/following-sibling::input[@type='checkbox']"));
            checkbox.click();
            assert checkbox.isSelected();
        }

        // Complete the verification process
        WebElement completeCheckbox = driver.findElement(By.id("completeVerification"));
        completeCheckbox.click();
        assert completeCheckbox.isSelected();
    }

    @Test(dependsOnMethods = {"verifyDocumentChecklist"})
    public void confirmCreditScore() {
        driver.get("URL_OF_APPLICATION");

        // Open the credit score report from the checklist
        driver.findElement(By.xpath("//div[contains(text(),'Credit score document')]")).click();
        driver.get("www.creditscore.com");

        // Login to the credit score link
        driver.findElement(By.id("loginID")).sendKeys("user123");
        driver.findElement(By.id("password")).sendKeys("abcabd");
        driver.findElement(By.id("loginButton")).click();

        // Navigate to credit score page
        driver.findElement(By.xpath("//a[contains(text(),'Credit Score')]")).click();

        // Search for customer ID
        driver.findElement(By.id("searchBox")).sendKeys("acb1243");
        driver.findElement(By.id("searchButton")).click();

        // Copy the credit score in customer details
        WebElement creditScoreElement = driver.findElement(By.id("creditScore"));
        String creditScore = creditScoreElement.getText();
        assert Integer.parseInt(creditScore) > 500;

        // Update the verification status in the system
        driver.findElement(By.id("verificationStatus")).sendKeys(creditScore);

        // Logout of the credit score system
        driver.findElement(By.id("logoutButton")).click();
    }
}