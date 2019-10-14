package ru.wildberries.pageobjects;


import org.openqa.selenium.WebDriver;

public abstract class Page {
    protected final WebDriver driver;

    protected Page(WebDriver driver) {
        this.driver = driver;
    }
}
