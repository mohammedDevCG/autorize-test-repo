package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.LoanApplicationPage;
import com.example.pages.CreditScorePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class LoanApplicationTest {

    private WebDriver driver;
    private Properties prop;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private LoanApplicationPage loanApplicationPage;
    private CreditScorePage creditScorePage;

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage = new LoginPage(driver);
        loanApplicationPage = new LoanApplicationPage(driver);
        creditScorePage = new CreditScorePage(driver);
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
        loginPage.login(username, password);
        loanApplicationPage.navigateToLoanApplication();
        loanApplicationPage.verifyDocuments();
        loanApplicationPage.doubleClickToOpenDocuments();
        loanApplicationPage.checkDocumentCheckbox();
        loanApplicationPage.completeVerificationProcess();
    }

    @Test(dependsOnMethods = "verifyChecklistFunctionality")
    public void confirmCreditScoreAbove500() {
        creditScorePage.openCreditScoreReport("www.creditscore.com");
        creditScorePage.login("user123", "abcabd");
        creditScorePage.navigateToCreditScorePage();
        creditScorePage.searchCustomer("acb1243");
        creditScorePage.copyCreditScore();
        creditScorePage.updateVerificationStatus();
        creditScorePage.logout();
    }
}