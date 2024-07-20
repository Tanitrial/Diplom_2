package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.User;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;

public class UpdateUserTest {

    private User userCreate;
    private User userUpdate;
    private User userForLoginBeforeUpdate;
    private User userForLoginAfterUpdate;
    private UserPrestep userPrestep;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userCreate = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userUpdate = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLoginBeforeUpdate = new User(userCreate.getEmail(), userCreate.getPassword());
        userForLoginAfterUpdate = new User(userUpdate.getEmail(), userUpdate.getPassword());
        userPrestep = new UserPrestep();
    }

    @Test
    @DisplayName("Update authorized user")
    public void updateAuthorizedUser() {
        userPrestep.setUser(userCreate);
        userPrestep.createUser();
        userPrestep.setUser(userUpdate);
        userPrestep.setUserLogin(userForLoginBeforeUpdate);
        String token = userPrestep.getAccessToken();
        userPrestep.updateUserWithToken(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Update unauthorized user")
    public void updateUnauthorizedUser() {
        userPrestep.setUser(userCreate);
        userPrestep.createUser();
        userPrestep.setUser(userUpdate);
        userPrestep.updateUserWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userPrestep.setUserLogin(userForLoginAfterUpdate);
        userPrestep.deleteUser();
    }
}