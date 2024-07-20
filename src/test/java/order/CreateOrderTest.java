package order;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.User;
import step.Order;
import user.UserPrestep;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {

    private Order order;
    private Order emptyOrder;
    private Order invalidOrder;
    private OrderPrestep orderPrestep;
    private User user;
    private User userForLogin;
    private UserPrestep userPrestep;

    @Before
    public void setUp() {

        orderPrestep = new OrderPrestep();

        List<String> anyIngredients = orderPrestep.getIngredients().subList(0, 2);
        order = new Order(anyIngredients);

        emptyOrder = new Order();

        List<String> invalidIngredients = List.of("11c0c5a71d1f82001bd1aa6d");
        invalidOrder = new Order(invalidIngredients);


        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLogin = new User(user.getEmail(), user.getPassword());
        userPrestep = new UserPrestep();
    }

    @Test
    @DisplayName("Create order with ingredients and token")
    public void createOrder() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();
        String token = userPrestep.getAccessToken();

        orderPrestep.setOrder(order);
        orderPrestep.createOrderWithToken(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order with ingredients and without token")
    public void createOrderWithoutToken() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();

        orderPrestep.setOrder(order);
        orderPrestep.createOrderWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order without ingredients and with token")
    public void createEmptyOrder() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();
        String token = userPrestep.getAccessToken();

        orderPrestep.setOrder(emptyOrder);
        orderPrestep.createOrderWithToken(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create order without ingredients and token")
    public void createEmptyOrderAndWithoutToken() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();

        orderPrestep.setOrder(emptyOrder);
        orderPrestep.createOrderWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create order with incorrect ingredient")
    public void createInvalidOrder() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();

        orderPrestep.setOrder(invalidOrder);
        orderPrestep.createOrderWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userPrestep.deleteUser();
    }
}