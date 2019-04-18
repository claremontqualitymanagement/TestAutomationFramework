package se.pricer.tests;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMainDialog {

    WebDriver driver;

    public ApplicationMainDialog(WebDriver driver){
        this.driver = driver;
    }

    public void buy(String itemType, int amount){
        WebElement productElement = productOption(itemType);
        if(productElement==null) {
            System.out.println("Cannot choose '" + itemType + "'.");
            Assert.fail("Cannot choose '" + itemType + "' among products '" + String.join("', '", productOptions.toString()));
        }
        productElement.click();
        amountToBuyField.sendKeys(String.valueOf(amount));

    }

    public void sell(String itemType, int amount) {
        sellButton.click();
    }

    public int currentMoney(){
        return Integer.valueOf(moneyField.getText());
    }

    public List<String> availableProducts(){
        List<String> products = new ArrayList<>();
        for (WebElement prod : productOptions){
            products.add(prod.getText());
        }
        return products;
    }

    private WebElement sellButton = driver.findElement(By.xpath("//button[@onclick='sellThis(0)']"));

    private WebElement amountToBuyField = driver.findElement(By.id("buyAmount"));

    private WebElement productOption(String option){
        return  driver.findElement(By.xpath("////select[@onchange='setProduct(this.value)']/option[text()= '" + option + "']"));
    }

    private WebElement buyButton = driver.findElement(By.xpath("//button[@onclick='buyStuff()']"));

    private WebElement moneyField = driver.findElement(By.id("money"));

    private WebElement productSelector = driver.findElement(By.xpath("//select[@onchange='setProduct(this.value)']"));

    private List<WebElement> productOptions = driver.findElements(By.xpath("////select[@onchange='setProduct(this.value)']/option"));

    public int buyingPrice(String product) {
        return Integer.valueOf(driver.findElement(By.xpath("////select[@onchange='setProduct(this.value)']//*[text()= '" + product + "']/../td[1]")).getText());
    }

    public int sellingPrice(String product) {
        return 0;
    }
}
