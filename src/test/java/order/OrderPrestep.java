package order;

import step.BaseOrder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.Setter;
import step.Order;


import java.util.List;

import static io.restassured.RestAssured.given;

@Setter
public class OrderPrestep {

    private Order order;

    @Step("Create order with token")
    public Response createOrderWithToken(String token) {
        return given().log().all()
                .spec(BaseOrder.specForOrder())
                .header("Authorization", token)
                .body(order)
                .when().post();
    }

    @Step("Create order without token")
    public Response createOrderWithoutToken() {
        return given().log().all()
                .spec(BaseOrder.specForOrder())
                .body(order)
                .with().post();
    }

    @Step("Get orders for user with token")
    public Response getUserOrders(String token) {
        return given().log().all()
                .spec(BaseOrder.specForOrder())
                .header("Authorization", token)
                .when().get();
    }

    @Step("Get orders without token")
    public Response getOrdersWithoutToken() {
        return given().log().all()
                .spec(BaseOrder.specForOrder())
                .when().get();
    }

    @Step("Get ingredients")
    public List<String> getIngredients(){
        return given().log().all()
                .spec(BaseOrder.specForIngredients())
                .when().get()
                .then()
                .extract().path("data._id");
    }
}