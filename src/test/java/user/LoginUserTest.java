package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.User;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;

public class LoginUserTest {

    private User userCreate;
    private User userLogin;
    private UserPrestep userPrestep;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userCreate = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userLogin = new User(userCreate.getEmail(), userCreate.getPassword());
        userPrestep = new UserPrestep();
    }

    @Test
    @DisplayName("Login user happy path")
    public void loginUser() {
        userPrestep.setUser(userCreate);
        userPrestep.setUserLogin(userLogin);
        userPrestep.createUser();
        userPrestep.loginUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Login user with wrong data")
    public void loginUserWithIncorrectData() {
        userPrestep.setUser(userCreate);
        userPrestep.setUserLogin(new User("Login", "pass"));
        userPrestep.createUser();
        userPrestep.loginUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userPrestep.deleteUser();
    }
}