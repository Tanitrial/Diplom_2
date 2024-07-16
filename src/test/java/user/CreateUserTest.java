package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.User;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest {

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
    @DisplayName("Create user happy path")
    public void createUser() {
        userPrestep.setUser(userCreate);
        userPrestep.setUserLogin(userLogin);
        userPrestep.createUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create duplicate user")
    public void createTheSameUser() {
        userPrestep.setUser(userCreate);
        userPrestep.setUserLogin(userLogin);
        userPrestep.createUser();
        userPrestep.createUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user with empty body request")
    public void createInvalidUser() {
        userPrestep.setUser(new User());
        userPrestep.setUserLogin(new User());
        userPrestep.createUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        userPrestep.deleteUser();
    }
}