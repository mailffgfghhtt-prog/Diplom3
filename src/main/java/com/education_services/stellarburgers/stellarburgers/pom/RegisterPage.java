package com.education_services.stellarburgers.stellarburgers.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inputName = By.xpath(".//label[text()='Имя']/following-sibling::input");
    private final By inputEmail = By.xpath(".//label[text()='Email']/following-sibling::input");
    private final By inputPassword = By.xpath(".//label[text()='Пароль']/following-sibling::input");
    private final By buttonRegister = By.xpath(".//button[text()='Зарегистрироваться']");
    private final By errorMessageUnderPassword = By.xpath(".//p[contains(@class, 'input__error')]");
    private final By hrefLogin = By.xpath(".//a[@href='/login']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void inputName(String name) {
        wait.until(d -> driver.findElement(inputName).isDisplayed());
        driver.findElement(inputName).sendKeys(name);
    }

    public void inputEmail(String email) {
        wait.until(d -> driver.findElement(inputEmail).isDisplayed());
        driver.findElement(inputEmail).sendKeys(email);
    }

    public void inputPassword(String password) {
        wait.until(d -> driver.findElement(inputPassword).isDisplayed());
        driver.findElement(inputPassword).sendKeys(password);
    }

    public void clickButtonRegister() {
        wait.until(d -> driver.findElement(buttonRegister).isDisplayed());
        driver.findElement(buttonRegister).click();
    }

    public String getErrorMessageText() {
        wait.until(d -> driver.findElement(errorMessageUnderPassword).isDisplayed());
        return driver.findElement(errorMessageUnderPassword).getText();
    }

    public boolean isErrorMessageVisible() {
        return !driver.findElements(errorMessageUnderPassword).isEmpty();
    }

    public void clickHrefLogin() {
        wait.until(d -> driver.findElement(hrefLogin).isDisplayed());
        driver.findElement(hrefLogin).click();
    }
}
