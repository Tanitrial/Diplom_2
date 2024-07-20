package user;

import step.BaseUser;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.Setter;
import step.User;


import static io.restassured.RestAssured.given;

@Setter
public class UserPrestep {
    private User user;
    private User userLogin;


    @Step("Create user")
    public Response createUser() {
        return given().log().all()
                .spec(BaseUser.spec())
                .body(user)
                .when()
                .post("/register");
    }

    @Step("Login user")
    public Response loginUser() {
        return given().log().all()
                .spec(BaseUser.spec())
                .body(userLogin)
                .when().post("/login");
    }

    @Step("Get accessToken")
    public String getAccessToken() {
        return given().log().all()
                .spec(BaseUser.spec())
                .body(userLogin)
                .when().post("/login")
                .then()
                .extract().path("accessToken");
    }

    @Step("Update user with token")
    public Response updateUserWithToken(String token) {
        return given().log().all()
                .spec(BaseUser.spec())
                .header("Authorization", token)
                .body(user)
                .when().patch("/user");
    }

    @Step("Update user without token")
    public Response updateUserWithoutToken() {
        return given().log().all()
                .spec(BaseUser.spec())
                .body(user)
                .when().patch("/user");
    }

    @Step("Delete user")
    public void deleteUser() {
        String accessToken = getAccessToken();
        if (accessToken != null)
            given().log().all()
                    .spec(BaseUser.spec())
                    .header("Authorization", accessToken)
                    .delete("/user");
    }
}