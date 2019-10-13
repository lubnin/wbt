package ru.wildberries;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import ru.wildberries.configuration.Browser;
import ru.wildberries.configuration.WebDriverFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class BasketItemsIncreasingTest {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BasketItemsIncreasingTest.class);
    private ProxyServer server = new ProxyServer(4567);

    @Before
    public void setUp() throws Exception {
        logger.info("Запуск прокси-сервера");
        server.start();
        Proxy proxy = server.seleniumProxy();
        //proxy.setSslProxy("localhost:4567");
        MutableCapabilities opt = new MutableCapabilities();
        opt.setCapability(CapabilityType.PROXY, proxy);
        opt.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
        driver = WebDriverFactory.createNewDriver(Browser.CHROME,opt);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    @After
    public void tearDown() throws Exception {
        WebDriverFactory.closeBrowser(driver);
        server.stop();
        logger.info("Прокси-сервер остановлен");
    }

    @Test
    public void test() {
        server.newHar("wildberries");
        driver.get("https://www.wildberries.ru");
        Har har = server.getHar();
        System.out.println(har);
        recordHarToFile(har);
        assertEquals("Страница успешно запущена","WildBerries.ru – Интернет-магазин модной одежды и обуви",driver.getTitle());
    }

    private void recordHarToFile(Har har) {
        try {
            File file = new File("logs\\har-logs.har");
            if (!file.exists()) {
                logger.info("Файл для har-логов не найден, создаю новый...");
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            try {
                har.writeTo(fos);
            }
            finally {
                logger.info("Лог прокси-сервера успешно записан");
                fos.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
