package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;

public class DocumentVerificationTests {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        // Setup code to initialize WebDriver, WebDriverWait
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "documentChecklistData")
    public Object[][] documentChecklistData() {
        return new Object[][]{
            {"user123", "abcabd", new String[]{"Address proof", "property completion state proof", "income proof", "credit score document"}}
        };
    }

    @Test(dataProvider = "documentChecklistData")
    public void testDocumentVerificationProcess(String username, String password, String[] documents) {
        login(username, password);
        navigateToLoanApplication();
        verifyDocumentsPresent(documents);
        openAndVerifyDocuments(documents);
        completeVerification();
    }

    private void login(String username, String password) {
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
    }

    private void navigateToLoanApplication() {
        // Code to navigate to the loan application page
    }

    private void verifyDocumentsPresent(String[] documents) {
        List<WebElement> documentElements = driver.findElements(By.xpath("//div[@class='document-list']/div[@class='document']"));
        for (String document : documents) {
            boolean isPresent = documentElements.stream().anyMatch(el -> el.getText().equals(document));
            assert isPresent : "Document " + document + " is not present";
        }
    }

    private void openAndVerifyDocuments(String[] documents) {
        for (String document : documents) {
            WebElement docElement = driver.findElement(By.xpath("//div[text()='" + document + "']"));
            docElement.click(); // Simulating double click

            WebElement checkbox = driver.findElement(By.xpath("//div[@class='checkbox'][@data-doc='" + document + "']"));
            checkbox.click();
            String color = checkbox.getCssValue("background-color");
            assert color.equals("green") : "Checkbox for " + document + " did not turn green";
        }
    }

    private void completeVerification() {
        WebElement completeCheckbox = driver.findElement(By.id("completeVerification"));
        completeCheckbox.click();
        String color = completeCheckbox.getCssValue("background-color");
        assert color.equals("green") : "Complete verification checkbox did not turn green";
    }
}