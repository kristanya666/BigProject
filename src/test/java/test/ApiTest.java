package test;

import data.ApiDataHelper;
import data.Data;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    RequestSpecification requestSpec = ApiDataHelper.getRequestSpec();
    String paymentPath = "/api/v1/pay";
    String creditPath = "/api/v1/credit";

    @Test
    public void paymentTestAPIWithApprovedCard() {
        var requestBody = ApiDataHelper.getRequestBody(Data.getApprovedCardNumber(),Data.getValidYear(),Data.getValidMonth(),Data.getValidCardholder(),Data.getValidCVV());

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(paymentPath)
                .then()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));
    }

    @Test
    public void paymentTestAPIWithDeclinedCard() {
        var requestBody = ApiDataHelper.getRequestBody(Data.getDeclinedCardNumber(),Data.getValidYear(),Data.getValidMonth(),Data.getValidCardholder(),Data.getValidCVV());

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(paymentPath)
                .then()
                .statusCode(200)
                .body("status", equalTo("DECLINED"));
    }

    @Test
    public void creditTestAPIWithApprovedCard() {
        var requestBody = ApiDataHelper.getRequestBody(Data.getApprovedCardNumber(),Data.getValidYear(),Data.getValidMonth(),Data.getValidCardholder(),Data.getValidCVV());

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(creditPath)
                .then()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));
    }

    @Test
    public void creditTestAPIWithDeclinedCard() {
        var requestBody = ApiDataHelper.getRequestBody(Data.getDeclinedCardNumber(),Data.getValidYear(),Data.getValidMonth(),Data.getValidCardholder(),Data.getValidCVV());

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(creditPath)
                .then()
                .statusCode(200)
                .body("status", equalTo("DECLINED"));
    }
}

