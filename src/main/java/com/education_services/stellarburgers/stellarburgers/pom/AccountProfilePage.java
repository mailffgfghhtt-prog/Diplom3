package com.education_services.stellarburgers.stellarburgers.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AccountProfilePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By hrefProfile = By.xpath(".//a[text()='Профиль']");
    public final By hrefConstructor = By.xpath(".//p[text()='Конструктор']/parent::a");
    private final By hrefSiteLogo = By.xpath(".//div[starts-with(@class, 'AppHeader_header__logo')]/a");
    private final By buttonExit = By.xpath(".//button[text()='Выход']");

    public AccountProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void clickHrefProfile() {
        wait.until(d -> driver.findElement(hrefProfile).isDisplayed());
        driver.findElement(hrefProfile).click();
    }

    public void clickHrefConstructor() {
        wait.until(d -> driver.findElement(hrefConstructor).isDisplayed());
        driver.findElement(hrefConstructor).click();
    }

    public void clickHrefSiteLogo() {
        wait.until(d -> driver.findElement(hrefSiteLogo).isDisplayed());
        driver.findElement(hrefSiteLogo).click();
    }

    public void clickButtonExit() {
        wait.until(d -> driver.findElement(buttonExit).isDisplayed());
        driver.findElement(buttonExit).click();
    }

    public boolean isButtonExitVisible() {
        return !driver.findElements(buttonExit).isEmpty();
    }
}
