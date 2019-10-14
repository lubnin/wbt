package ru.wildberries.pageobjects.blocks;

import com.google.sitebricks.client.Web;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.wildberries.pageobjects.Page;

public class SearchPanelBlock extends Page {
    private final static By searchInput = By.cssSelector("input[id='tbSrch']");
    private final static By searchButton = By.cssSelector("span[id='btnSrch']");

    public SearchPanelBlock(WebDriver driver) {
        super(driver);
    }

    public void search(String text){
        WebElement searchInput = getSearchInput();
        searchInput.sendKeys(text);
        WebElement searchButton = getSearchButton();
        searchButton.click();
    }

    public WebElement getSearchInput(){
        return driver.findElement(searchInput);
    }
    public WebElement getSearchButton(){
        return driver.findElement(searchButton);
    }

    public void clearSearchInput(){
        getSearchInput().clear();
    }

}
