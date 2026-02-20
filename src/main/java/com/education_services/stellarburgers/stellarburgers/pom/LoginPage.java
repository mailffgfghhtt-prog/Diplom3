package com.education_services.stellarburgers.stellarburgers.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By headerLogin = By.xpath(".//h2[text()='Вход']");
    private final By inputEmail = By.xpath(".//label[text()='Email']/following-sibling::input");
    private final By inputPassword = By.xpath(".//label[text()='Пароль']/following-sibling::input");
    private final By buttonLogin = By.xpath(".//button[text()='Войти']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isHeaderLoginVisible() {
        return !driver.findElements(headerLogin).isEmpty();
    }

    public void inputEmail(String email) {
        wait.until(d -> driver.findElement(inputEmail).isDisplayed());
        driver.findElement(inputEmail).sendKeys(email);
    }

    public void inputPassword(String password) {
        wait.until(d -> driver.findElement(inputPassword).isDisplayed());
        driver.findElement(inputPassword).sendKeys(password);
    }

    public void clickButtonLogin() {
        wait.until(d -> driver.findElement(buttonLogin).isDisplayed());
        driver.findElement(buttonLogin).click();
    }
}
