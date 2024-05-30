package com.qa.Utils;
import DataHandlerUtils.JsonOperations;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.JsonModellers.TestDataDriver;
import dataUtils.MasterDataUtils;
import org.testng.annotations.DataProvider;

import java.io.IOException;

public class EnvDataProvider extends BaseClass {
    private ThreadLocal<Object[][]> tl = new ThreadLocal();
    private ThreadLocal<Object[]> tl1 = new ThreadLocal();
    MasterDataUtils masterDataUtils;
    public int findRowCount(TestDataDriver.Configurations[] test, String testCaseNumber) {
        int count = 0;
        for(int x = 0; x < test.length; ++x) {
            if(test[x].getGroupName().equalsIgnoreCase("")) {
                if (test[x].getTestCaseNumber().equalsIgnoreCase(testCaseNumber)) {
                    ++count;
                }
            }else {
                ++count;
            }
        }
        return count;
    }
    public synchronized int getColumnCount(TestDataDriver.Configurations[] test, String testCaseId) {
        int colCount = 0;
        TestDataDriver.Configurations[] elements = test;
        for(int i = 0; i < elements.length; i++) {
            TestDataDriver.Configurations entry = elements[i];
            if(entry.getGroupName().equalsIgnoreCase("")) {
                if (entry.getTestCaseNumber().equals(testCaseId)) {
                    if (!entry.getTestCaseNumber().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getEnvironment().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getClient().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getBrowserName().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getBrowserVersion().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getOSName().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getOSVersion().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getDeviceName().equalsIgnoreCase("")) {
                        colCount++;
                    }
                    if (!entry.getAppVersion().equalsIgnoreCase("")) {
                        colCount++;
                    }
/*if (!entry.getGroupName().equalsIgnoreCase("")) {
colCount++;
}*/
                }
            }else {
                if (!entry.getTestCaseNumber().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getEnvironment().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getClient().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getBrowserName().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getBrowserVersion().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getOSName().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getOSVersion().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getDeviceName().equalsIgnoreCase("")) {
                    colCount++;
                }
                if (!entry.getAppVersion().equalsIgnoreCase("")) {
                    colCount++;
                }
/*if (!entry.getGroupName().equalsIgnoreCase("")) {
colCount++;
}*/
            }
        }
        return colCount;
    }
    public synchronized Object[][] setData(TestDataDriver.Configurations[] test, String testCaseId) {
        int index = 0;
        TestDataDriver.Configurations[] var4 = test;
        int numberOfRows = test.length;
        for(int i = 0; i < numberOfRows; i++) {
            TestDataDriver.Configurations entry = var4[i];
            if(!entry.getTestCaseNumber().equalsIgnoreCase("N.A.")) {
                if (entry.getTestCaseNumber().equals(testCaseId)) {
                    String testCaseNumber = entry.getTestCaseNumber();
                    String env = entry.getEnvironment();
                    String client = entry.getClient();
                    String BrowserName = entry.getBrowserName();
                    String BrowserVersion = entry.getBrowserVersion();
                    String OSName = entry.getOSName();
                    String OSVersion = entry.getOSVersion();
                    String DeviceName = entry.getDeviceName();
                    String appVersion = entry.getAppVersion();
                    String groupName = entry.getGroupName();
                    for (int k = 0; k < 1; ++k) {
                        int x=0;
                        tl.get()[index][x] = testCaseNumber;
                        x++;
                        tl.get()[index][x] = env;
                        x++;
                        tl.get()[index][x] = client;
                        x++;
                        if (!BrowserName.equalsIgnoreCase("")) {
                            tl.get()[index][x] = BrowserName;
                            x++;
                        }
                        if (!BrowserVersion.equalsIgnoreCase("")) {
                            tl.get()[index][x] = BrowserVersion;
                            x++;
                        }
                        if (!OSName.equalsIgnoreCase("")) {
                            tl.get()[index][x] = OSName;
                            x++;
                        }
                        if (!OSVersion.equalsIgnoreCase("")) {
                            tl.get()[index][x] = OSVersion;
                            x++;
                        }
                        if (!DeviceName.equalsIgnoreCase("")) {
                            tl.get()[index][x] = DeviceName;
                            x++;
                        }
                        if (!appVersion.equalsIgnoreCase("")) {
                            tl.get()[index][x] = appVersion;
                            x++;
                        }
                        if (!groupName.equalsIgnoreCase("")) {
                            tl.get()[index][x] = groupName;
                        }
                        index++;
                    }
                }
            }else{
                String testCaseNumber = entry.getTestCaseNumber();
                String env = entry.getEnvironment();
                String client = entry.getClient();
                String BrowserName = entry.getBrowserName();
                String BrowserVersion = entry.getBrowserVersion();
                String OSName = entry.getOSName();
                String OSVersion = entry.getOSVersion();
                String DeviceName = entry.getDeviceName();
                String appVersion = entry.getAppVersion();
                String groupName = entry.getGroupName();
                for (int k = 0; k < 1; ++k) {
                    int x=0;
                    tl.get()[index][x] = testCaseNumber;
                    x++;
                    tl.get()[index][x] = env;
                    x++;
                    tl.get()[index][x] = client;
                    x++;
                    if (!BrowserName.equalsIgnoreCase("")) {
                        tl.get()[index][x] = BrowserName;
                        x++;
                    }
                    if (!BrowserVersion.equalsIgnoreCase("")) {
                        tl.get()[index][x] = BrowserVersion;
                        x++;
                    }
                    if (!OSName.equalsIgnoreCase("")) {
                        tl.get()[index][x] = OSName;
                        x++;
                    }
                    if (!OSVersion.equalsIgnoreCase("")) {
                        tl.get()[index][x] = OSVersion;
                        x++;
                    }
                    if (!DeviceName.equalsIgnoreCase("")) {
                        tl.get()[index][x] = DeviceName;
                        x++;
                    }
                    if (!appVersion.equalsIgnoreCase("")) {
                        tl.get()[index][x] = appVersion;
                    }
/*if (!groupName.equalsIgnoreCase("")) {
tl.get()[index][x] = groupName;
}*/
                    index++;
                }
            }
        }
        return tl.get();
    }
    public synchronized Object[] setData(JsonArray array) {
        for(int i=0;i<array.size();i++)
        {
            tl1.get()[i]=array.get(i);
        }
        return tl1.get();
    }

    // Data provider to bring in test case metadata
    @DataProvider(name = "getRunDetails_TC0001")
    public synchronized Object[][] ConfigureTestCaseDataTC0001() {
        String testCaseId = "TC0001";
        TestDataDriver.Configurations[] test = getRunConfigurations().getTestCaseRunConfiguration();
        int rowCount = this.findRowCount(test, testCaseId);
        Object[][] data = new Object[rowCount][getColumnCount(test,testCaseId)];
        this.tl.set(data);
        return setData(test, testCaseId);
    }

    // Data provider to bring in test data for parameterization
   @DataProvider(name = "getRunDetails_TC0002")
    public synchronized Object[] ConfigureTestCaseDataTC0002() throws IOException {
        JsonOperations jsonOperations = new JsonOperations();
        MasterDataUtils masterDataUtils = new MasterDataUtils();
        String testCaseId = "TC0002";
        String clientId = getTestCaseMetaDataBlock(testCaseId).get("Client").getAsString();
        JsonObject completeMasterData = jsonOperations.readJsonFileAndStoreInJsonObject(getMasterDataJsonPath(clientId));
        JsonArray iterationDetailsArray = masterDataUtils.getIterationDataSetsFromMasterData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData);
        Object[] data = new Object[iterationDetailsArray.size()];
        this.tl1.set(data);
        return setData(iterationDetailsArray);

}
    @DataProvider(name = "getRunDetails_TC0003")
    public synchronized Object[] ConfigureTestCaseDataTC0003() throws IOException {
        JsonOperations jsonOperations = new JsonOperations();
        MasterDataUtils masterDataUtils = new MasterDataUtils();
        String testCaseId = "TC0003";
        String clientId = getTestCaseMetaDataBlock(testCaseId).get("Client").getAsString();
        JsonObject completeMasterData = jsonOperations.readJsonFileAndStoreInJsonObject(getMasterDataJsonPath(clientId));
        JsonArray iterationDetailsArray = masterDataUtils.getIterationDataSetsFromMasterData(testCaseId, FrameworkComponentKeys.WEB.getComponentAsString(), completeMasterData);
        Object[] data = new Object[iterationDetailsArray.size()];
        this.tl1.set(data);
        return setData(iterationDetailsArray);

    }
}
