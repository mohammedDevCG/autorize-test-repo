package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoanApplicationPage {
    WebDriver driver;

    @FindBy(id = "userId")
    WebElement userIdField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "loginButton")
    WebElement loginButton;

    @FindBy(xpath = "//div[@id='docChecklist']//li[contains(@class, 'addressProof')]")
    WebElement addressProof;

    @FindBy(xpath = "//div[@id='docChecklist']//li[contains(@class, 'propertyProof')]")
    WebElement propertyProof;

    @FindBy(xpath = "//div[@id='docChecklist']//li[contains(@class, 'incomeProof')]")
    WebElement incomeProof;

    @FindBy(xpath = "//div[@id='docChecklist']//li[contains(@class, 'creditScoreDoc')]")
    WebElement creditScoreDoc;

    @FindBy(id = "verificationCheckbox")
    WebElement verificationCheckbox;

    public LoanApplicationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String userId, String password) {
        userIdField.sendKeys(userId);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    public void openDocument(WebElement document) {
        document.click();
    }

    public void verifyDocument(WebElement document) {
        document.click();
        if (!document.getAttribute("class").contains("verified")) {
            throw new AssertionError("Document verification failed");
        }
    }

    public void completeVerification() {
        verificationCheckbox.click();
        if (!verificationCheckbox.getAttribute("class").contains("verified")) {
            throw new AssertionError("Verification process completion failed");
        }
    }
}

package com.example.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.example.pages.LoanApplicationPage;

public class LoanApplicationTest {
    WebDriver driver;
    LoanApplicationPage loanApplicationPage;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loanApplicationPage = new LoanApplicationPage(driver);
    }

    @Test
    public void testDocumentVerification() {
        loanApplicationPage.login("user123", "abcabd");
        loanApplicationPage.openDocument(loanApplicationPage.addressProof);
        loanApplicationPage.verifyDocument(loanApplicationPage.addressProof);
        loanApplicationPage.openDocument(loanApplicationPage.propertyProof);
        loanApplicationPage.verifyDocument(loanApplicationPage.propertyProof);
        loanApplicationPage.openDocument(loanApplicationPage.incomeProof);
        loanApplicationPage.verifyDocument(loanApplicationPage.incomeProof);
        loanApplicationPage.openDocument(loanApplicationPage.creditScoreDoc);
        loanApplicationPage.verifyDocument(loanApplicationPage.creditScoreDoc);
        loanApplicationPage.completeVerification();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}