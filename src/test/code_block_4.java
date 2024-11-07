package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    private WebDriver driver;

    private By cartIcon = By.id("shopping_cart_container");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCart() {
        driver.findElement(cartIcon).click();
    }

    public void verifyItemsInCart() {
        // Implement verification logic
    }
}