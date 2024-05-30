package com.qa.component.API.URIConstruction;

import com.qa.Base.BaseClass;

public class ApplicationEndpoints extends BaseClass {
    public String getBearerTokenEndpoint = "auth";
    public String placePetOrderEndpoint = "store/order";
    public String getPetOrderEndpoint = "store/order/{{orderId}}";

}
