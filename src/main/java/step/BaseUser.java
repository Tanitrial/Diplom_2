package step;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static step.ApiStep.API_AUTH;
import static step.ApiStep.BASE_URI;

public class BaseUser {
    public static RequestSpecification spec() {
        return given()
                .baseUri(BASE_URI)
                .basePath(API_AUTH)
                .contentType(ContentType.JSON);
    }
}
