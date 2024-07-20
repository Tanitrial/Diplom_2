package order;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.Order;
import step.User;
import user.UserPrestep;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {

    private Order order;
    private OrderPrestep orderPrestep;
    private User user;
    private User userForLogin;
    private UserPrestep userPrestep;

    @Before
    public void setUp() {

        orderPrestep = new OrderPrestep();

        List<String> anyIngredients = orderPrestep.getIngredients().subList(0, 2);
        order = new Order(anyIngredients);

        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLogin = new User(user.getEmail(), user.getPassword());
        userPrestep = new UserPrestep();
    }

    @Test
    @DisplayName("Get order for auth user")
    public void getOrderAuthUser() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();
        String token = userPrestep.getAccessToken();

        orderPrestep.setOrder(order);
        orderPrestep.createOrderWithToken(token);
        orderPrestep.getUserOrders(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Get order for unauth user")
    public void getOrderUnauthUser() {
        userPrestep.setUser(user);
        userPrestep.setUserLogin(userForLogin);
        userPrestep.createUser();

        orderPrestep.setOrder(order);
        orderPrestep.createOrderWithoutToken();
        orderPrestep.getOrdersWithoutToken()
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