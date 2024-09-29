package com.qa.Tests.API;

import com.qa.CompositeFunctions.API.PetStoreCompositeFunctions;
import com.qa.DataProviders.APIDataProvider;
import com.google.gson.JsonElement;
import com.qa.component.API.URIConstruction.Constants;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PetStoreTestCases extends PetStoreCompositeFunctions {
    Map<String, JsonElement> payloadList;
    private static final ThreadLocal<String> testCaseId = new ThreadLocal<>();
    private static final ThreadLocal<String> environment = new ThreadLocal<>();
    private static final ThreadLocal<String> client = new ThreadLocal<>();
    PetStoreCompositeFunctions compositeFunctions;
    @BeforeClass
    public void PreRequisites() throws IOException {
        setProperties();
    }
    @BeforeMethod
    public void BeforeExecutionStart(ITestResult iTestResult) {
        testCaseId.set(iTestResult.getMethod().getMethodName());
        try {
            environment.set(getTestCaseMetaDataBlock(testCaseId.get()).get("Environment").getAsString());
            client.set(getTestCaseMetaDataBlock(testCaseId.get()).get("Client").getAsString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Test(description = "Pets Sanity ", dataProvider = "getDataIterations", dataProviderClass = APIDataProvider.class, enabled = false)
    public synchronized void PET0001(JsonElement testDataSet) throws IOException, IllegalAccessException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        compositeFunctions = new PetStoreCompositeFunctions();
        payloadList = new ConcurrentHashMap<>();
        setResults(testCaseId.get(), environment.get(), client.get(), testDataSet);
        if (testDataSet.getAsJsonObject().get("ACTIVE").getAsString().equalsIgnoreCase("Y")) {
            Response addPetResponse = compositeFunctions.AddNewPet(Method.POST, testCaseId.get(), client.get(), environment.get(), testDataSet.getAsJsonObject(), Constants.ADD_NEW_PET, payloadList);
            Response findPetResponse = compositeFunctions.findPet(Method.GET, testCaseId.get(), client.get(), environment.get(), testDataSet.getAsJsonObject(), Constants.FIND_PET, payloadList, addPetResponse);
        }
    }

    //@AfterMethod()
    public void afterTest(ITestResult iTestResult) throws ParseException, IOException, InterruptedException {
        //afterTestApiActivities(iTestResult);
    }
}


