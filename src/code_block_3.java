package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    private WebDriver driver;

    private By firstItemAddToCartButton = By.xpath("//button[contains(@id, 'add-to-cart')]");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addItemToCart() {
        driver.findElement(firstItemAddToCartButton).click();
    }
}