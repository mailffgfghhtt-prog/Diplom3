package com.education_services.stellarburgers.stellarburgers.pom;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AccountProfilePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By hrefProfile = By.xpath(".//a[text()='Профиль']");
    private final By hrefConstructor = By.xpath(".//p[text()='Конструктор']/parent::a");
    private final By hrefSiteLogo = By.xpath(".//div[starts-with(@class, 'AppHeader_header__logo')]/a");
    private final By buttonExit = By.xpath(".//button[text()='Выход']");

    public AccountProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Step("Клик по ссылке 'Профиль'")
    public void clickHrefProfile() {
        wait.until(d -> driver.findElement(hrefProfile).isDisplayed());
        driver.findElement(hrefProfile).click();
    }

    @Step("Клик по ссылке 'Конструктор'")
    public void clickHrefConstructor() {
        wait.until(d -> driver.findElement(hrefConstructor).isDisplayed());
        driver.findElement(hrefConstructor).click();
    }

    @Step("Клик по логотипу сайта")
    public void clickHrefSiteLogo() {
        wait.until(d -> driver.findElement(hrefSiteLogo).isDisplayed());
        driver.findElement(hrefSiteLogo).click();
    }

    @Step("Клик по кнопке 'Выход'")
    public void clickButtonExit() {
        wait.until(d -> driver.findElement(buttonExit).isDisplayed());
        driver.findElement(buttonExit).click();
    }

    @Step("Проверка видимости кнопки 'Выход'")
    public boolean isButtonExitVisible() {
        return !driver.findElements(buttonExit).isEmpty();
    }
}
