package com.qa.component.API.RequestTypes;

import com.qa.Utils.TestListener;
import io.restassured.RestAssured;
import io.restassured.http.*;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class RestUtils {

    public synchronized Response requestAsync(Method method , String contentType , String completeUrl , Map<String,String> defaultHeaders, Map<String ,String> formParams, String requestPayload, Map<String ,String> queryParams, Map<String ,String> pathParams, SSLConfig sslConfig) {
        try { RequestSpecification requestSpecification = RestAssured.given();
            requestSpecification.filter(new TestListener());
            if(contentType!=null)
            {
                requestSpecification.contentType(contentType);
            }
            if(requestPayload!=null)
            {
                requestSpecification.body(requestPayload);
            }
            if(defaultHeaders!=null)
            {
                requestSpecification.headers(defaultHeaders);
            }
            if(formParams!=null)
            {
                requestSpecification.formParams(formParams);
            }
            if(queryParams!=null)
            {
                requestSpecification.queryParams(queryParams);
            }
            if(pathParams!=null)
            {
                requestSpecification.pathParams(pathParams);
            }
            if(sslConfig!=null)
            {
                requestSpecification.config(RestAssured.config().sslConfig(sslConfig));
            }
            Thread.sleep(1000);
            Response response =requestSpecification.request(method,completeUrl);
            Thread.sleep(2000);
            if(response!=null) {
                return response;
            }
            else
            {
                return null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
