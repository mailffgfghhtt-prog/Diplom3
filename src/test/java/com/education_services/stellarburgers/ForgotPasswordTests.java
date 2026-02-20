package com.education_services.stellarburgers;

import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import com.education_services.stellarburgers.model.User;
import com.education_services.stellarburgers.stellarburgers.AppConfig;
import com.education_services.stellarburgers.stellarburgers.pom.ForgotPasswordPage;
import com.education_services.stellarburgers.stellarburgers.pom.LoginPage;
import com.education_services.stellarburgers.stellarburgers.pom.MainPage;
import com.education_services.stellarburgers.steps.StepsAPI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static com.education_services.stellarburgers.stellarburgers.generators.UserGenerator.randomUser;

public class ForgotPasswordTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String FORGOT_PASSWORD_URL = appConfig.baseUrl() + "forgot-password";
    private WebDriver driver;
    private String token;
    private User user;
    private StepsAPI stepsAPI;
    private LoginPage loginPage;
    private ForgotPasswordPage forgotPasswordPage;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        stepsAPI = new StepsAPI();
        user = randomUser();
        Response responseRegister = stepsAPI.sendPostRequestAuthRegister(user);
        responseRegister.then().statusCode(200);
        token = responseRegister.jsonPath().getString("accessToken");

        loginPage = new LoginPage(driver);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        mainPage = new MainPage(driver);
    }

    @Test
    @DisplayName("Страница восстановления пароля личного кабинета, Можно авторизоваться через ссылку 'Войти'")
    public void successLoginFromRegisterPageTest() {
        driver.get(FORGOT_PASSWORD_URL);
        forgotPasswordPage.clickHrefLogin();
        loginPage.isHeaderLoginVisible();

        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();

        mainPage.isButtonCreateOrderVisible();
        assertEquals(appConfig.baseUrl(), driver.getCurrentUrl());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        if (token != null) {
            stepsAPI.sendDeleteRequestAuthUser(token);
        }
    }
}
