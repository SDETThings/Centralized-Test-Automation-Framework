package com.qa.component.API.HeaderConstruction;

import com.qa.Base.BaseClass;
import APIUtils.APIAuthentication;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Headers extends BaseClass
{
    Map<String, String> defaultHeaders;
    public Map<String, String> returnHeaders(String clientId,String... token) throws IOException {
        defaultHeaders = new ConcurrentHashMap<>();
        if(token.length>0) {
            if (getClientName(clientId).equalsIgnoreCase("CLIENT001")) {
                defaultHeaders.put("api_key", token[0]);
            } else if (getClientName(clientId).equalsIgnoreCase("CLIENT002")) {
                defaultHeaders.put("api_key", token[0]);
            } else if (getClientName(clientId).equalsIgnoreCase("CLIENT003")) {
                defaultHeaders.put("api_key", token[0]);
            }
        }else{
            defaultHeaders.put("accept", "application/json");
        }
        return this.defaultHeaders;
    }
}
