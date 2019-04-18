package se.pricer.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PricerTest {

    String currentTestEnvironmentUrl = "https://sukram-89.github.io/store/";
    ApplicationMainDialog app;
    WebDriver driver;


    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","/Users/mystrandberg/Downloads/chromedriver2");
        driver  = new ChromeDriver();
        driver.navigate().to(currentTestEnvironmentUrl);
        app = new ApplicationMainDialog(driver);
    }

    @Test
    public void noEmptyList(){
        Assert.assertTrue(app.availableProducts().size() > 0);
    }

    /**
     * Buying one of each item should sum up to 10000
     */
    @Test
    public void BuyZeroTV() {
        app.buy("TV", 0);
        Assert.assertEquals("10000", app.currentMoney());
    }

    @Test
    public void buyAmountIsStillIntact(){
        for (String product: app.availableProducts()){
            app.buy(product, 5);
        }
        Assert.assertEquals("10000", app.currentMoney());
    }

    @Test
    public void buyAndSell(){
        app.buy("banana", 1);
        app.sell("banana", 1);
        Assert.assertEquals("10000", app.currentMoney());
    }

    @Test
    public void subtractedAmount(){
        for(String product : app.availableProducts()) {
            int intitalMoney = app.currentMoney();
            app.buy(product, 1);
            Assert.assertTrue("Failed money deduction when buying product '" + product + "'.", app.currentMoney() == intitalMoney - app.buyingPrice(product));
            app.sell(product, 1);
            Assert.assertTrue("Failed money deduction when buying product '" + product + "'.", app.currentMoney() == intitalMoney + app.sellingPrice(product));
        }
    }

    @After
    public void cleaning(){
        if(driver == null) return;
        driver.close();
        driver.quit();
    }
}