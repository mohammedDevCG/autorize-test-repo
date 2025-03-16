package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreditScorePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By creditScoreLink = By.id("credit-score-link");
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By searchBox = By.id("search-box");
    private By customerDetails = By.id("customer-details");
    private By logoutButton = By.id("logout-button");

    public CreditScorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void openCreditScoreReport(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(creditScoreLink)).click();
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public void navigateToCreditScorePage() {
        // Navigate to credit score page logic here
    }

    public void searchCustomer(String customerId) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(customerId);
        // Search customer logic here
    }

    public void copyCreditScore() {
        // Copy credit score logic here
    }

    public void updateVerificationStatus() {
        // Update verification status logic here
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}