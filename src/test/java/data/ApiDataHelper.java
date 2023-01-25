package data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

public class ApiDataHelper {
    public static String getRequestBody(String card, String year, String month, String cardholder, String cvc) {
        JSONObject requestBody = new JSONObject()
                .put("number", card)
                .put("year", year)
                .put("month", month)
                .put("holder", cardholder)
                .put("cvc", cvc);

        return requestBody.toString();
    }

    public static RequestSpecification getRequestSpec() {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8080)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        return requestSpec;
    }

}
