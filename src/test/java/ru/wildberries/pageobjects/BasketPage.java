package ru.wildberries.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasketPage extends Page {
    private static final String itemsPanelString=".//div[@class='basket-list-items']";
    private static final By firstItemCountInput=By.cssSelector("input.in_tb");
    private static final By firstItemPlusBtn = By.cssSelector("button.plus");
    private static final By firstItemsTotalPrice = By.cssSelector("div.total-price");
    private static final By totalPrice = By.cssSelector("span.do-order-total-price");
    private static final By totalItemsQty = By.cssSelector("div.do-order-total-line.total-count");

    public BasketPage(WebDriver driver) {
        super(driver);
    }
    public WebElement getFirstItemPlusBtn(){
        WebDriverWait wait = new WebDriverWait(driver,90);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstItemPlusBtn));
        return driver.findElement(firstItemPlusBtn);
    }
    public WebElement getFirstItemsCountElement(){
        return driver.findElement(firstItemCountInput);
    }
    public WebElement getFirstItemsTotalPriceElement(){
        return driver.findElement(firstItemsTotalPrice);
    }
    public WebElement getTotalPriceElement(){
        return driver.findElement(totalPrice);
    }
    public WebElement getTotalItemsElement(){
        return driver.findElement(totalItemsQty);
    }
    public void increaseFirstItemsQtyByPlus(){
        getFirstItemPlusBtn().click();
    }
    public int getFirstItemsCount(){
        return Integer.parseInt(getFirstItemsCountElement().getAttribute("value"));
    }
    public int getFirstItemsTotalPrice(){
        return Integer.parseInt(getFirstItemsTotalPriceElement().getAttribute("textContent").replaceAll("[^0-9]", ""));
    }
    public int getTotalPrice(){
        return Integer.parseInt(getTotalPriceElement().getText().replaceAll("[^0-9]", ""));
    }
    public int getTotalItemsQty(){
        return Integer.parseInt(getTotalItemsElement().getAttribute("textContent").replaceAll("[^0-9]", ""));
    }
}
