package com.education_services.stellarburgers.steps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import com.education_services.stellarburgers.clients.ApiClient;
import com.education_services.stellarburgers.model.User;
public class StepsAPI {
    private ApiClient apiClient = new ApiClient();
    @Step("запрос DELETE  на /api/auth/user")
    public Response sendDeleteRequestAuthUser(String token) {
        return apiClient.deleteUser(token);
    }
    @Step("запрос POST  на /api/auth/login")
    public Response sendPostRequestAuthLogin(String email, String password) {
        return apiClient.loginUser(email, password);
    }
    @Step("запрос POST  на /api/auth/register")
    public Response sendPostRequestAuthRegister(User user) {
        return apiClient.createUser(user);
    }
}