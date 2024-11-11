package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoanApplicationPage {
    private WebDriver driver;

    private By loanApplicationLink = By.id("loanApplicationLink");

    public LoanApplicationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToLoanApplication() {
        driver.findElement(loanApplicationLink).click();
    }
}