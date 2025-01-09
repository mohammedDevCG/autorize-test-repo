package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    private WebDriver driver;
    private By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPageLoaded() {
        return driver.findElement(checkoutButton).isDisplayed();
    }

    public boolean isItemInCart() {
        return driver.findElement(By.xpath("//div[@class='inventory_item_name']")).isDisplayed();
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }
}