package com.education_services.stellarburgers;

import io.restassured.response.Response;
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
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static com.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static com.education_services.stellarburgers.stellarburgers.generators.UserGenerator.randomUser;

public class MainPageTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String MAIN_PAGE_URL = appConfig.baseUrl();
    private static final String LOGIN_URL = appConfig.baseUrl() + "login";
    private WebDriver driver;
    private String token;
    private User user;
    private StepsAPI stepsAPI;
    private LoginPage loginPage;
    private MainPage mainPage;
    private AccountProfilePage accountProfilePage;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        stepsAPI = new StepsAPI();
        user = randomUser();
        Response responseRegister = stepsAPI.sendPostRequestAuthRegister(user);
        responseRegister.then().statusCode(200);
        token = responseRegister.jsonPath().getString("accessToken");

        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        accountProfilePage = new AccountProfilePage(driver);
    }

    @ParameterizedTest
    @MethodSource("placeForLoginData")
    @DisplayName("Открыта главная страница, Можно авторизоваться через Личный Кабинет или нажав на 'Войти в аккаунт'")
    public void successLoginFromMainPageTest(By placeForLogin) {
        driver.get(MAIN_PAGE_URL);

        if (placeForLogin.equals(mainPage.buttonLogin)) {
            mainPage.clickButtonLogin();
        } else {
            mainPage.clickHrefPersonalCabinet();
        }

        loginPage.isHeaderLoginVisible();
        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();

        mainPage.isButtonCreateOrderVisible();
        assertEquals(appConfig.baseUrl(), driver.getCurrentUrl());
    }

    static Stream<Arguments> placeForLoginData() {
        return Stream.of(
                Arguments.of(By.xpath(".//button[text()='Войти в аккаунт']")),  // Локатор передаем напрямую
                Arguments.of(By.xpath(".//p[text()='Личный Кабинет']"))
        );
    }


    @Test
    @DisplayName("Открыта главная страница, авторизованный пользователь может зайти в личный кабинет")
    public void openProfileFromMainPageTest() {
        driver.get(LOGIN_URL);
        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();
        mainPage.isButtonCreateOrderVisible();

        mainPage.clickHrefPersonalCabinet();
        accountProfilePage.isButtonExitVisible();

        assertEquals(appConfig.baseUrl() + "account/profile", driver.getCurrentUrl());
    }

    @ParameterizedTest
    @MethodSource("placeForBunTransitionData")
    @DisplayName("Открыта главная страница, Можно перейти в раздел 'Булки' из раздела 'Начинки' или 'Соусы'")
    public void transitToBunListTest(By placeForBunTransition) {
        driver.get(MAIN_PAGE_URL);

        if (placeForBunTransition.equals(By.xpath(".//span[text()='Соусы']/parent::div"))) {
            mainPage.clickButtonSauce();
        } else {
            mainPage.clickButtonMainIngredient();
        }

        mainPage.clickButtonBun();
        assertTrue(mainPage.isButtonBunCurrentVisible());
        assertTrue(mainPage.isChildInParentCoordinates(mainPage.menuContainer, mainPage.firstBun));
    }

    static Stream<Arguments> placeForBunTransitionData() {
        return Stream.of(
                Arguments.of(By.xpath(".//span[text()='Соусы']/parent::div")),
                Arguments.of(By.xpath(".//span[text()='Начинки']/parent::div"))
        );
    }


    // Аналогично для других параметризованных тестов

    @AfterEach
    public void tearDown() {
        driver.quit();
        if (token != null) {
            stepsAPI.sendDeleteRequestAuthUser(token);
        }
    }
}
