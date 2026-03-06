package com.education_services.stellarburgers;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.education_services.stellarburgers.model.User;
import com.education_services.stellarburgers.stellarburgers.AppConfig;
import com.education_services.stellarburgers.stellarburgers.pom.AccountProfilePage;
import com.education_services.stellarburgers.stellarburgers.pom.LoginPage;
import com.education_services.stellarburgers.stellarburgers.pom.MainPage;
import com.education_services.stellarburgers.steps.StepsAPI;
import com.education_services.stellarburgers.stellarburgers.generators.UserGenerator;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.education_services.stellarburgers.WebDriverCreator.createWebDriver;

class AccountProfileTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String LOGIN_URL = appConfig.baseUrl() + "login";
    private WebDriver driver;
    private String token;
    private User user;
    private StepsAPI stepsAPI;
    private LoginPage loginPage;
    private MainPage mainPage;
    private AccountProfilePage accountProfilePage;

    @BeforeEach
    void setUp() {
        driver = createWebDriver();
        stepsAPI = new StepsAPI();
        user = UserGenerator.randomUser();
        Response responseRegister = stepsAPI.sendPostRequestAuthRegister(user);
        responseRegister.then().statusCode(200);
        token = responseRegister.jsonPath().getString("accessToken");

        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        accountProfilePage = new AccountProfilePage(driver);
    }

    @ParameterizedTest
    @MethodSource("placeForConstructorTransitionData")
    @DisplayName("страница личного кабинета, с этой страницы можно перейти на страницу конструктора")
    void openMainPageFromPersonalCabinetTest(By placeForConstructorTransition) {
        driver.get(LOGIN_URL);
        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();
        mainPage.isButtonCreateOrderVisible();

        mainPage.clickHrefPersonalCabinet();

        if (placeForConstructorTransition.toString().contains("Конструктор")) {
            accountProfilePage.clickHrefConstructor();
        } else {
            accountProfilePage.clickHrefSiteLogo();
        }

        mainPage.isButtonCreateOrderVisible();
        assertEquals(appConfig.baseUrl(), driver.getCurrentUrl());
    }

    static Stream<Arguments> placeForConstructorTransitionData() {
        return Stream.of(
                Arguments.of(By.xpath(".//p[text()='Конструктор']/parent::a")),
                Arguments.of(By.xpath(".//div[starts-with(@class, 'AppHeader_header__logo')]/a"))
        );
    }

    @Test
    @DisplayName("Страница личного кабинета, можно из него выйти")
    void exitFromPersonalCabinetTest() {
        driver.get(LOGIN_URL);
        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();
        mainPage.isButtonCreateOrderVisible();

        mainPage.clickHrefPersonalCabinet();
        accountProfilePage.isButtonExitVisible();

        accountProfilePage.clickButtonExit();
        loginPage.isHeaderLoginVisible();
        assertEquals(LOGIN_URL, driver.getCurrentUrl());
    }

    @AfterEach
    void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии драйвера: " + e.getMessage());
        }

        try {
            if (token != null) {
                stepsAPI.sendDeleteRequestAuthUser(token);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении пользователя через API: " + e.getMessage());
        }
    }
}
