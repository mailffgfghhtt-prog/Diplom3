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
import com.education_services.stellarburgers.stellarburgers.pom.LoginPage;
import com.education_services.stellarburgers.stellarburgers.pom.MainPage;
import com.education_services.stellarburgers.steps.StepsAPI;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static com.education_services.stellarburgers.stellarburgers.generators.UserGenerator.randomUser;

/**
 * Тесты для главной страницы приложения Stellar Burgers.
 * Все локаторы элементов вынесены в классы Page Object.
 * Проверяет:
 * - авторизацию через разные элементы интерфейса;
 * - переходы между вкладками меню (Булки, Начинки, Соусы).
 */
public class MainPageTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String MAIN_PAGE_URL = appConfig.baseUrl();

    private WebDriver driver;
    private String token;
    private User user;
    private StepsAPI stepsAPI;
    private LoginPage loginPage;
    private MainPage mainPage;

    @BeforeEach
    void setUp() {
        driver = createWebDriver();
        stepsAPI = new StepsAPI();
        user = randomUser();

        // Регистрация пользователя через API
        Response responseRegister = stepsAPI.sendPostRequestAuthRegister(user);
        responseRegister.then().statusCode(200);
        token = responseRegister.jsonPath().getString("accessToken");

        // Инициализация объектов страниц
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
    }

    @Test
    @DisplayName("Открыта главная страница, можно авторизоваться через кнопку 'Войти в аккаунт'")
    void successLoginFromMainPageViaLoginButton() {
        driver.get(MAIN_PAGE_URL);

        mainPage.clickButtonLogin();
        loginPage.isHeaderLoginVisible();
        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();

        mainPage.isButtonCreateOrderVisible();
    }

    @Test
    @DisplayName("Открыта главная страница, можно авторизоваться через ссылку 'Личный Кабинет'")
    void successLoginFromMainPageViaPersonalCabinet() {
        driver.get(MAIN_PAGE_URL);

        mainPage.clickHrefPersonalCabinet();
        loginPage.isHeaderLoginVisible();
        loginPage.inputEmail(user.getEmail());
        loginPage.inputPassword(user.getPassword());
        loginPage.clickButtonLogin();

        mainPage.isButtonCreateOrderVisible();
    }

    @Test
    @DisplayName("Проверка переходов между вкладками: Булки, Начинки, Соусы")
    void checkTransitionsBetweenTabs() {
        driver.get(MAIN_PAGE_URL);

        // Переход на вкладку «Булки»
        mainPage.clickButtonBun();
        assertTrue(mainPage.isButtonBunCurrentVisible(),
                "Вкладка 'Булки' не стала активной после клика");
        assertTrue(mainPage.isChildInParentCoordinates(mainPage.menuContainer, mainPage.firstBun),
                "Первый элемент раздела 'Булки' не отображается в контейнере меню");

        // Переход на вкладку «Начинки»
        mainPage.clickButtonMainIngredient();
        assertTrue(mainPage.isButtonMainIngredientCurrentVisible(),
                "Вкладка 'Начинки' не стала активной после клика");
        assertTrue(mainPage.isChildInParentCoordinates(mainPage.menuContainer, mainPage.firstMainIngredient),
                "Первый элемент раздела 'Начинки' не отображается в контейнере меню");

        // Переход на вкладку «Соусы»
        mainPage.clickButtonSauce();
        assertTrue(mainPage.isButtonSauceCurrentVisible(),
                "Вкладка 'Соусы' не стала активной после клика");
        assertTrue(mainPage.isChildInParentCoordinates(mainPage.menuContainer, mainPage.firstSauce),
                "Первый элемент раздела 'Соусы' не отображается в контейнере меню");
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
