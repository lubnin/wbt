package ru.wildberries.pageobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ItemPage extends Page {
    private static final Logger logger = LogManager.getLogger(ItemPage.class);
    private final static By cartAddBtn = By.xpath(".//button[@class = 'c-btn-orange j-add-to-card'][contains(text(),'В корзину')]");
    private final static By goToCartBtn = By.xpath(".//a[@class = 'c-btn-main j-go-to-basket'][contains(text(),'Перейти в корзину')]");
    private final static String colorsPanelString = ".//div[@id= 'colorpicker' or @class='colorpicker']/ul[*]/li";
    private final static By colorsPanelSelector = By.xpath(colorsPanelString);
    private final static String sizesPanelString = ".//div[@class= 'j-size-list size-list j-smart-overflow-instance']/label";
    private final static By sizesPanelSelector = By.xpath(sizesPanelString);

    public ItemPage(WebDriver driver) {
        super(driver);
    }
    public void addToCart(){
        getCartBtn().click();
    }
    public void openCartFromItem(){
        WebDriverWait wait = new WebDriverWait(driver,90);
        wait.until(ExpectedConditions.visibilityOfElementLocated(goToCartBtn));
        driver.findElement(goToCartBtn).click();
    }

    public WebElement getCartBtn(){
        return driver.findElement(cartAddBtn);
    }
    public void chooseTheFirstAvailableSize(){
        By firstAvailableSizeSelector = By.xpath("("+sizesPanelString+"[not(contains(@class,'disabled'))]"+")[1]");

        int elementsQty = driver.findElements(firstAvailableSizeSelector).size();
        if(elementsQty==1) {
            Actions action = new Actions(driver);
            WebElement el = driver.findElement(firstAvailableSizeSelector);
            action.moveToElement(el).perform();
            el.click();
        }else{
        logger.error("Элемент выбора размера не определён однозначно: ["+elementsQty+"] вместо 1.");
        }
    }
    public void chooseColorByIndex(int index){
        By colorSelector = By.xpath("("+colorsPanelString+")["+index+"]");
        int elementsQty = driver.findElements(colorSelector).size();
        if (elementsQty==1){
            driver.findElement(colorSelector).click();
        }else{
            logger.error("Элемент выбора цвета не определён однозначно: ["+elementsQty+"] вместо 1.");
        }
    }
    public int getAvailableSizesQty(){
        By availableSizesSelector = By.xpath(sizesPanelString+"[not(contains(@class,'disabled'))]");
        return driver.findElements(availableSizesSelector).size();
    }
    public int getColorsQty(){
        return driver.findElements(colorsPanelSelector).size();
    }
    public int getAllSizesQty(){
        return driver.findElements(sizesPanelSelector).size();
    }
    public void doPositiveStepsFromChooseToCartOpening(){
        int colorsQty = getColorsQty();
        for(int i=1;i<=colorsQty;i++){
            chooseColorByIndex(i);
            int availableSizesQty = getAvailableSizesQty();
            if(availableSizesQty>0){
                chooseTheFirstAvailableSize();
                break;
            }
        }
        addToCart();
        openCartFromItem();
    }
}
