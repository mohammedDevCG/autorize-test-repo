package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.LoanApplicationPage;
import com.example.pages.DocumentChecklistPage;
import com.example.pages.CreditScorePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

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

    @Test(priority = 1)
    public void verifyDocumentChecklist() {
        LoginPage loginPage = new LoginPage(driver);
        LoanApplicationPage loanApplicationPage = new LoanApplicationPage(driver);
        DocumentChecklistPage documentChecklistPage = new DocumentChecklistPage(driver);

        driver.get(prop.getProperty("url"));
        loginPage.enterUsername(prop.getProperty("username"));
        loginPage.enterPassword(prop.getProperty("password"));
        loginPage.clickLogin();

        loanApplicationPage.navigateToLoanApplication();
        Assert.assertTrue(documentChecklistPage.areAllDocumentsPresent(), "All documents are not present in the checklist");

        documentChecklistPage.doubleClickToOpenDocuments();
        documentChecklistPage.verifyDocuments();
        documentChecklistPage.completeVerification();
    }

    @Test(priority = 2, dependsOnMethods = "verifyDocumentChecklist")
    public void confirmCreditScoreAbove500() {
        LoginPage loginPage = new LoginPage(driver);
        CreditScorePage creditScorePage = new CreditScorePage(driver);

        driver.get(prop.getProperty("creditScoreUrl"));
        loginPage.enterUsername(prop.getProperty("username"));
        loginPage.enterPassword(prop.getProperty("password"));
        loginPage.clickLogin();

        creditScorePage.navigateToCreditScorePage();
        creditScorePage.searchCustomerById(prop.getProperty("customerId"));
        Assert.assertTrue(creditScorePage.isCreditScoreAbove500(), "Credit score is not above 500");
        creditScorePage.updateVerificationStatus();
        creditScorePage.logout();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}