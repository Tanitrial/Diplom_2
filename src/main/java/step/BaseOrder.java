package step;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static step.ApiStep.*;

public class BaseOrder {


    public static RequestSpecification specForOrder() {
        return given()
                .baseUri(BASE_URI)
                .basePath(API_ORDERS)
                .contentType(ContentType.JSON);
    }

    public static RequestSpecification specForIngredients() {
        return given()
                .baseUri(BASE_URI)
                .basePath(API_INGREDIENTS)
                .contentType(ContentType.JSON);
    }
}