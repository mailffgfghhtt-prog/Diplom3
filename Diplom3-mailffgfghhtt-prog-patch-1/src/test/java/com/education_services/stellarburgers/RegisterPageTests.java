package com.education_services.stellarburgers;

import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import com.education_services.stellarburgers.model.User;
import com.education_services.stellarburgers.stellarburgers.AppConfig;
import com.education_services.stellarburgers.stellarburgers.pom.LoginPage;
import com.education_services.stellarburgers.stellarburgers.pom.MainPage;
import com.education_services.stellarburgers.stellarburgers.pom.RegisterPage;
import com.education_services.stellarburgers.steps.StepsAPI;
import static org.junit.jupiter.api.Assertions.*;
import static com.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static com.education_services.stellarburgers.stellarburgers.generators.UserGenerator.randomUser;
import static com.education_services.stellarburgers.stellarburgers.generators.UserGenerator.randomUserWithShortPassword;

public class RegisterPageTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String REGISTER_URL = appConfig.baseUrl() + "register";
    private WebDriver driver;
    private String token;
    private StepsAPI stepsAPI;
    private RegisterPage registerPage;
    private LoginPage loginPage;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        stepsAPI = new StepsAPI();
        registerPage = new RegisterPage(driver);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
    }

    @Test
    @DisplayName("Открыта страница регистрации, можно зарегистрироваться с валидными, уникальными данными")
    public void successRegistrationWithValidCredsRedirectsToLoginPageTest() {
        User user = randomUser();

        driver.get(REGISTER_URL);
        registerPage.inputName(user.getName());
        registerPage.inputEmail(user.getEmail());
        registerPage.inputPassword(user.getPassword());
        registerPage.clickButtonRegister();

        loginPage.isHeaderLoginVisible();
        assertEquals(appConfig.baseUrl() + "login", driver.getCurrentUrl());

        Response responseLogin = stepsAPI.sendPostRequestAuthLogin(user.getEmail(), user.getPassword());
        responseLogin.then().statusCode(200);
        token = responseLogin.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Открыта страница регистрации, система выдаёт ошибку при попытке ввести пароль короче 6 символов")
    public void unsuccessRegistrationWithShortPasswordTest() {
        User user = randomUserWithShortPassword();

        driver.get(REGISTER_URL);
        registerPage.inputName(user.getName());
        registerPage.inputEmail(user.getEmail());
        registerPage.inputPassword(user.getPassword());
        registerPage.clickButtonRegister();

        assertTrue(registerPage.isErrorMessageVisible(), "Сообщение об ошибке не отображается");

        String errorMessage = registerPage.getErrorMessageText();
        assertTrue(errorMessage.contains("Некорректный пароль") ||
                        errorMessage.contains("Пароль некорректный"),
                "Неверный текст ошибки: " + errorMessage);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        if (token != null) {
            stepsAPI.sendDeleteRequestAuthUser(token);
        }
    }
}
