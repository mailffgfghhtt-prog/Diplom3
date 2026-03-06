package com.education_services.stellarburgers.stellarburgers.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

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

    // Улучшенный локатор перекрывающего элемента — несколько вариантов на случай, если один не сработает
    private final By overlayElement1 = By.cssSelector("div[style*='flex']");
    private final By overlayElement2 = By.cssSelector("div.modal-overlay");
    private final By overlayElement3 = By.cssSelector("div.loading-spinner");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Увеличен таймаут для стабильности
        this.actions = new Actions(driver);
    }

    public boolean isButtonCreateOrderVisible() {
        return !driver.findElements(buttonCreateOrder).isEmpty();
    }

    public void clickButtonLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(buttonLogin));
        driver.findElement(buttonLogin).click();
    }

    public void clickHrefPersonalCabinet() {
        wait.until(ExpectedConditions.elementToBeClickable(hrefPersonalCabinet));
        driver.findElement(hrefPersonalCabinet).click();
    }

    public void clickButtonBun() {
        waitForPageLoad();
        waitForOverlayToDisappear();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(buttonBun));
        scrollToElementAndClick(element);
    }

    public boolean isButtonBunCurrentVisible() {
        return !driver.findElements(buttonBunCurrent).isEmpty();
    }

    public void clickButtonSauce() {
        waitForPageLoad();
        waitForOverlayToDisappear();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(buttonSauce));
        scrollToElementAndClick(element);
    }

    public boolean isButtonSauceCurrentVisible() {
        return !driver.findElements(buttonSauceCurrent).isEmpty();
    }

    public void clickButtonMainIngredient() {
        waitForPageLoad();
        waitForOverlayToDisappear();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(buttonMainIngredient));
        scrollToElementAndClick(element);
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

    /**
     * Ожидание загрузки страницы
     */
    private void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Ожидание исчезновения перекрывающих элементов — проверяем несколько возможных вариантов
     */
    private void waitForOverlayToDisappear() {
        try {
            // Пробуем дождаться исчезновения каждого из возможных перекрывающих элементов
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlayElement1));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlayElement2));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlayElement3));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Не все перекрывающие элементы исчезли, продолжаем работу...");
        }
    }

    /**
     * Прокрутка к элементу и клик через Actions
     * @param element элемент, по которому нужно кликнуть
     */
    private void scrollToElementAndClick(WebElement element) {
        actions.scrollToElement(element).perform();
        actions.moveToElement(element).click().perform();
    }
}
