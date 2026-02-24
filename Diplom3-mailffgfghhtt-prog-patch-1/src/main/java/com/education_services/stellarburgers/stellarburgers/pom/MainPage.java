package com.education_services.stellarburgers.stellarburgers.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By buttonCreateOrder = By.xpath(".//button[text()='Оформить заказ']");
    public final By buttonLogin = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By hrefPersonalCabinet = By.xpath(".//p[text()='Личный Кабинет']");
    private final By buttonBun = By.xpath(".//span[text()='Булки']/parent::div");
    private final By buttonBunCurrent = By.xpath(".//span[text()='Булки']/parent::div[contains(@class, 'current')]");
    public final By firstBun = By.xpath(".//h2[text()='Булки']/following-sibling::ul[1]/a");
    public final By buttonSauce = By.xpath(".//span[text()='Соусы']/parent::div");
    private final By buttonSauceCurrent = By.xpath(".//span[text()='Соусы']/parent::div[contains(@class, 'current')]");
    public final By firstSauce = By.xpath(".//h2[text()='Соусы']/following-sibling::ul[1]/a");
    private final By buttonMainIngredient = By.xpath(".//span[text()='Начинки']/parent::div");
    private final By buttonMainIngredientCurrent = By.xpath(".//span[text()='Начинки']/parent::div[contains(@class, 'current')]");
    public final By firstMainIngredient = By.xpath(".//h2[text()='Начинки']/following-sibling::ul[1]/a");
    public final By menuContainer = By.xpath(".//div[contains(@class, 'menuContainer')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isButtonCreateOrderVisible() {
        return !driver.findElements(buttonCreateOrder).isEmpty();
    }

    public void clickButtonLogin() {
        wait.until(d -> driver.findElement(buttonLogin).isDisplayed());
        driver.findElement(buttonLogin).click();
    }

    public void clickHrefPersonalCabinet() {
        wait.until(d -> driver.findElement(hrefPersonalCabinet).isDisplayed());
        driver.findElement(hrefPersonalCabinet).click();
    }

    public void clickButtonBun() {
        wait.until(d -> driver.findElement(buttonBun).isDisplayed());
        driver.findElement(buttonBun).click();
    }

    public boolean isButtonBunCurrentVisible() {
        return !driver.findElements(buttonBunCurrent).isEmpty();
    }

    public void clickButtonSauce() {
        wait.until(d -> driver.findElement(buttonSauce).isDisplayed());
        driver.findElement(buttonSauce).click();
    }

    public boolean isButtonSauceCurrentVisible() {
        return !driver.findElements(buttonSauceCurrent).isEmpty();
    }

    public void clickButtonMainIngredient() {
        wait.until(d -> driver.findElement(buttonMainIngredient).isDisplayed());
        driver.findElement(buttonMainIngredient).click();
    }

    public boolean isButtonMainIngredientCurrentVisible() {
        return !driver.findElements(buttonMainIngredientCurrent).isEmpty();
    }

    public boolean isChildInParentCoordinates(By parent, By child) {
        try {
            wait.until(driver -> {
                var parentRect = driver.findElement(parent).getRect();
                var childRect = driver.findElement(child).getRect();
                return childRect.getX() >= parentRect.getX() &&
                        childRect.getY() >= parentRect.getY() &&
                        childRect.getX() + childRect.getWidth() <= parentRect.getX() + parentRect.getWidth() &&
                        childRect.getY() + childRect.getHeight() <= parentRect.getY() + parentRect.getHeight();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
