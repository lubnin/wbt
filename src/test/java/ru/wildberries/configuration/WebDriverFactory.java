package ru.wildberries.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;

public class WebDriverFactory {

    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

    public static WebDriver createNewDriver(Browser browser) {
        return createNewDriver(browser,new MutableCapabilities());
    }

    public static WebDriver createNewDriver(Browser browser,  MutableCapabilities options) {
        String driverClassName;
        WebDriver driver;
        MutableCapabilities externalOptions = new MutableCapabilities();
        if (options.asMap().size()!=0){
            externalOptions = options;
            logger.info("При инициализации обнаружены дополнительные настройки");
        }
        switch (browser){
            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability(ChromeOptions.CAPABILITY,"--start-maximized");
                HashMap<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.default_content_setting_values.notifications",2);
                chromeOptions.setExperimentalOption("prefs",prefs);
                chromeOptions.merge(externalOptions);
                driver = new ChromeDriver(chromeOptions);
                logger.info("Инициализирован драйвер браузера Chrome");
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability(FirefoxOptions.FIREFOX_OPTIONS,"--start-maximized");
                firefoxOptions.merge(externalOptions);
                driver = new FirefoxDriver(firefoxOptions);
                logger.info("Инициализирован драйвер браузера Firefox");
                break;
            default: logger.error("Не удалось определить браузер по наименованию Name: "+ browser.toString());
                driver = null;
        }
        if (driver!=null){
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void closeBrowser(WebDriver driver){
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toUpperCase();
        try{
            Browser browser = Browser.valueOf(browserName);
            logger.info("Закрываю браузер "+browser.toString());
            if (browser == Browser.IE) {
                driver.close();
            } else {
                driver.quit();
            }
        }catch (IllegalArgumentException e){
            logger.error("Не удалось определить браузер"+ browserName);
            e.printStackTrace();
        }
    }
}