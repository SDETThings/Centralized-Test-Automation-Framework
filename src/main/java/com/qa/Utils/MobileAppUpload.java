package com.qa.Utils;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class MobileAppUpload {
    public static String getAppCenterBuildName(String Brand){String AppCentreBuild = null;
        switch (Brand){
            case "aqua":
                AppCentreBuild = "Aqua-UAT";
                break;
            case "johnlewis":
                AppCentreBuild = "John-Lewis-UAT";
                break;
        }
        return AppCentreBuild;
    }
    public static String getAppUrl(String AppCenterBuildName){
        String completeRequestURIString = "https://api.appcenter.ms/v0.1/apps/newdaymobile/"+ AppCenterBuildName +"/releases/latest";
        Response response = given()
                .contentType("application/json")
                .header("X-Api-Token", "72f834f90a53d20ed0f11ebb109d3abf769dfbdd")
                .when()
                .get(completeRequestURIString, new Object[0]);
        return response.jsonPath().getString("download_url");
    }
    public static String getAppUpload(String AppBrandDetails){
        String downloadUrl = getAppUrl(AppBrandDetails);
        String requestBody = "{\n" +
                " \"url\": \" " + downloadUrl +"\"" +
                "\n}";
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Basic bW9iaWxlYXBwdWF0YXV0bzVta214Oks4c1pubW5iZlUzYURwa2c5RWg5")
                                .and()
                                .body(requestBody)
                                .when()
                                .post("https://api-cloud.browserstack.com/app-automate/upload")
                                .then()
                                .extract().response();
        return response.jsonPath().getString("app_url");
    }
}
