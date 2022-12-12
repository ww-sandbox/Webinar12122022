package Pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.time.Duration;

public class ProductPage extends BasePage{
    public ProductPage(WebDriver driver){
        super(driver);
    }
    @FindBy(xpath = "//input[@title=\"Biały\"]")
    WebElement colorWhite;
    @FindBy(xpath = "//input[@title=\"czarny\"]" )
    WebElement colorBlack;
    @FindBy(xpath = "//input[@name=\"qty\"]")
    WebElement numberOfPieces;
    @FindBy(css=".add button")
    WebElement addToBasketButtonElement;
//    deklaracja elementów zgodnie ze stylem PageFactory
    @FindBy(id = "group_1")
    WebElement selectSizeElement;
    @FindBy(id="blockcart-modal")
    WebElement cartSummary;

    public void openProductPage(){
        driver.get("http://sampleshop.inqa.pl/men/1-1-hummingbird-printed-t-shirt.html#/1-rozmiar-s/8-kolor-bialy");
    }

    public void selectColor(String color){
        LOGGER.debug("Wybieram kolor " + color);
        if(color.equals("white")){
            colorWhite.click();
        } else if (color.equals("black")) {
            colorBlack.click();
        }
    }

    public void selectSize(String size){
        LOGGER.debug("wybieram rozmiar " + size);
        int attempts = 0;
        while( attempts < 2) {
            try {
                Select selectSize = new Select(selectSizeElement);
                selectSize.selectByVisibleText(size);
                break;
            } catch(StaleElementReferenceException e) {
            }
            attempts++;
        }
//        Tak jak wspominałem czasem przy okazji nowoczesnych bibliotek do tworzenia UI trzeba się nakombinować.
//        W tym wypadku błąd nie jest taki prosty do załatnia i trzeba to robić trochę na około. Problem z selectem
//        rozwiązuję inline w klasie danego PageObjectu, problem buttona przeniosłem do BasePage
    }

    public void setNumberOfPieces(String number){
        LOGGER.debug("ustawiam ilość sztuk " + number);
//        Logi mogą pojawiać się w różnych miejscach. Także w definicjach page objectów. Nie należy przesadzać z ilością
//        logów, oraz warto dobrze zaplanować poziom logowania poszczególnych informacji
        numberOfPieces.clear();
        numberOfPieces.sendKeys(number);
    }

    @Step("Klikam przycisk dodawania do koszyka")
    public void clickAddToBasketButton(){
        stableButtonClick(addToBasketButtonElement);
    }

    public boolean checkAddToBasketSummary(String size, String quantity, String color){
        waitForElementVisibility(cartSummary);
        String actualColor = cartSummary.findElement(By.cssSelector(".kolor strong")).getText();
        String actualSize = cartSummary.findElement(By.cssSelector(".rozmiar strong")).getText();
        String actualQuantity = cartSummary.findElement(By.cssSelector(".product-quantity strong")).getText();
        Assert.assertEquals(actualColor, color);
        Assert.assertEquals(actualSize, size);
        Assert.assertEquals(actualQuantity, quantity);
        return true;
    }
}
