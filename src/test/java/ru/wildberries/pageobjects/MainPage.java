package ru.wildberries.pageobjects;

import org.openqa.selenium.WebDriver;
import ru.wildberries.pageobjects.blocks.SearchPanelBlock;

public class MainPage extends Page {

    public MainPage(WebDriver driver) {
        super(driver);
        SearchPanelBlock searchPanelBlock = new SearchPanelBlock(driver);
    }
}
