package ru.wildberries.tests;

import io.qameta.allure.Allure;
import net.lightbody.bmp.core.har.*;
import net.lightbody.bmp.proxy.ProxyServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.wildberries.configuration.*;

import ru.wildberries.pageobjects.BasketPage;
import ru.wildberries.pageobjects.ItemPage;
import ru.wildberries.pageobjects.blocks.SearchPanelBlock;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

public class BasketItemsIncreasingTest extends ru.wildberries.configuration.Proxy {
    private final static String WB_URL = "https://www.wildberries.ru";
    private WebDriver driver;
    //private static final Logger logger = LogManager.getLogger(BasketItemsIncreasingTest.class);
    private ProxyServer server = new ProxyServer(4567);

    @Before
    public void setUp() throws Exception {
        Allure.step("Запуск прокси-сервера");
        server.start();
        Proxy proxy = server.seleniumProxy();
        MutableCapabilities opt = new MutableCapabilities();
        opt.setCapability(CapabilityType.PROXY, proxy);
        driver = WebDriverFactory.createNewDriver(Browser.CHROME,opt);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    @After
    public void tearDown() throws Exception {
        WebDriverFactory.closeBrowser(driver);
        server.stop();
        Allure.step("Прокси-сервер остановлен");
    }
    @DisplayName("Test Of Increasing Items Qty In The Basket")
    @Description("Тест проверяет отображение стоимости заказа " +
            "при увеличении количества одного и того же товара в корзине. " +
            "Также проверяется POST-запрос recalc, уходящий при этом на сервер.")
    @Test
    public void test() throws InterruptedException {
        driver.get(WB_URL);
        assertEquals("Страница успешно запущена","WildBerries.ru – Интернет-магазин модной одежды и обуви",driver.getTitle());
        SearchPanelBlock searchPanel = new SearchPanelBlock(driver);
        searchPanel.search("6094105");
        ItemPage itemPage = new ItemPage(driver);
        itemPage.doPositiveStepsFromChooseToCartOpening();
        BasketPage basketPage = new BasketPage(driver);
        server.newHar("recalc");
        Allure.step("Собираем данные до нажатия для проверки");
        int firstItemsQty = basketPage.getFirstItemsCount();
        int firstItemsPrice = basketPage.getFirstItemsTotalPrice();
        int totalOrderPrice = basketPage.getTotalPrice();
        Allure.step("Жмём на плюс");
        basketPage.increaseFirstItemsQtyByPlus();
        Allure.step("Проверяем, что Товаров стало: "+(1+firstItemsQty)+"; " +
                "Стоимость позиции: "+firstItemsPrice*2+"; "+
                "Итого по заказу: "+totalOrderPrice*2+" р.");
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait
                .pollingEvery(Duration.ofMillis(500))
                .withTimeout(Duration.ofSeconds(10))
                .until(dr->basketPage.getTotalItemsQty()==firstItemsQty+1);
        Allure.step("Проверка, что изменились параметры после клика на +");
        assertEquals(firstItemsQty+1,basketPage.getFirstItemsCount());
        assertEquals(firstItemsPrice*2,basketPage.getFirstItemsTotalPrice());
        assertEquals(totalOrderPrice*2,basketPage.getTotalPrice());
        Allure.step("Проверка наличия и успешности POST-запроса calc");
        Har har = server.getHar();
        String url = "https://lk.wildberries.ru/basket/recalc";
        assertTrue(isUrlExistsInHar(har,url));
        HarRequest req = getHarRequestByUrl(har,url);
        HarResponse res = getHarResponseByUrl(har,url);
        assertEquals(req.getMethod(),"POST");
        assertEquals(res.getStatus(),200);
        Allure.step("Запись лога в файл");
        recordHarToFile(har);
    }
}
