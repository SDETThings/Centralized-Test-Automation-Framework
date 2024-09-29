package com.qa.component.API.URIConstruction;

import com.qa.Base.BaseClass;

public class Constants extends BaseClass {

    // ============================================== CONTENT TYPES ==============================================================================
    public static final String TESTCASE_FIELD = "testCaseId";
    public static final String ENVIRONMENT_FIELD = "Environment";
    public static final String CLIENT_FIELD = "Client";
    // ============================================== CONTENT TYPES ==============================================================================

    public static final String CONTENTTYPE_APPLICATIONJSON = "application/json";

    // ============================================== BASE URL TYPES ( APIM/FUNCTION APPS) =======================================================
    public static final String APIM = "APIM";
    // ============================================== ENDPOINT URL CONSTANTS =======================================================
    /*
        This is used to do below activities
        1. Pick out the request payload json files ( Assuming the file names contain the substring of the constant values )
        2. Pick out the actual endpoint urls
        3. Pick out the request payload fields to modify and expected fields to validate from Master test case data
        4. In some cases pick out the Base urls ( depending on application requirement )
     */
    public static final String ADD_NEW_PET = "AddPet";
    public static final String FIND_PET = "FindPet";
}
