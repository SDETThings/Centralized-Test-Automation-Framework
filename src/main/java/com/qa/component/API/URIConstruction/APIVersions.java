package com.qa.component.API.URIConstruction;

import com.qa.Base.BaseClass;

public class APIVersions extends BaseClass {
    /* =========================================================== DEV =========================================================== */
    public static final String PETSTORE_DEV_VERSION_V1 = "v2/";
    /* =========================================================== QA =========================================================== */
    public static final String PETSTORE_QA_VERSION_V2 = "v2/";
    /* =========================================================== STAGE =========================================================== */
    public static final String PETSTORE_QA_VERSION_V3 = "v2/";
    public synchronized String getApiVersion(String client,String env , String baseUrlType,String endpointConstant) {
        String apiVersion = "";
        if (endpointConstant.contains(Constants.GET_RESTFULL_BOOKER_AUTH_TOKEN)) {
            apiVersion = "";
        } else {
            if (env.equalsIgnoreCase("ENV001")) {
                if ((getClientName(client).equalsIgnoreCase("CLIENT001"))) {
                    apiVersion = PETSTORE_DEV_VERSION_V1;
                } else if ((getClientName(client).equalsIgnoreCase("CLIENT002"))) {
                    apiVersion = PETSTORE_DEV_VERSION_V1;
                } else if ((getClientName(client).equalsIgnoreCase("CLIENT003"))) {
                    apiVersion = PETSTORE_DEV_VERSION_V1;
                }
            } else if (env.equalsIgnoreCase("ENV002") ) {
                if ((getClientName(client).equalsIgnoreCase("CLIENT001"))) {
                    apiVersion = PETSTORE_QA_VERSION_V2;
                } else if ((getClientName(client).equalsIgnoreCase("CLIENT002"))) {
                    apiVersion = PETSTORE_QA_VERSION_V2;
                } else if ((getClientName(client).equalsIgnoreCase("CLIENT003"))) {
                    apiVersion = PETSTORE_QA_VERSION_V2;
                }
            } else if (env.equalsIgnoreCase("ENV003") ) {
                if ((getClientName(client).equalsIgnoreCase("CLIENT001"))) {
                    apiVersion = PETSTORE_QA_VERSION_V3;
                } else if ((getClientName(client).equalsIgnoreCase("CLIENT002"))) {
                    apiVersion = PETSTORE_QA_VERSION_V3;
                } else if ((getClientName(client).equalsIgnoreCase("CLIENT003"))) {
                    apiVersion = PETSTORE_QA_VERSION_V3;
                }
            } else {
                apiVersion = "";
            }
        }
        return apiVersion;
    }

}
