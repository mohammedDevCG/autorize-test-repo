package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CheckoutPage {
    private WebDriver driver;
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By zipCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By totalCost = By.className("summary_total_label");
    private By thankYouMessage = By.xpath("//h2[text()='Thank you for your order!']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPageLoaded() {
        return driver.findElement(continueButton).isDisplayed();
    }

    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    public void enterZipCode(String zipCode) {
        driver.findElement(zipCodeField).sendKeys(zipCode);
    }

    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    public boolean isTotalCorrect() {
        // Implement the logic to check the total cost
        return true;
    }

    public void clickFinish() {
        driver.findElement(finishButton).click();
    }

    public boolean isOrderPlaced() {
        return driver.findElement(thankYouMessage).isDisplayed();
    }

    public void captureScreenshot(String testName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/Screenshots/" + testName + ".png";
        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}