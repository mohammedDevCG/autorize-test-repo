package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreditScorePage {
    private WebDriver driver;

    private By creditScorePageLink = By.id("creditScorePageLink");
    private By searchBox = By.id("searchBox");
    private By customerDetails = By.id("customerDetails");
    private By logoutButton = By.id("logoutButton");

    public CreditScorePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCreditScorePage() {
        driver.findElement(creditScorePageLink).click();
    }

    public void searchCustomerById(String customerId) {
        driver.findElement(searchBox).sendKeys(customerId);
        driver.findElement(searchBox).submit();
    }

    public boolean isCreditScoreAbove500() {
        // Implement logic to check if credit score is above 500
        return Integer.parseInt(driver.findElement(customerDetails).getText()) > 500;
    }

    public void updateVerificationStatus() {
        // Implement logic to update verification status
    }

    public void logout() {
        driver.findElement(logoutButton).click();
    }
}