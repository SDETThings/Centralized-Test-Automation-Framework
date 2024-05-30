package com.qa.JsonModellers;

public class TestDataDriver {
    private Configurations[] TestCaseRunConfiguration;
    public Configurations[] getTestCaseRunConfiguration() {
        return this.TestCaseRunConfiguration;
    }
    public void setTestCaseRunConfiguration(Configurations[] testCaseRunConfiguration) {
        this.TestCaseRunConfiguration = testCaseRunConfiguration;
    }
    public static class Configurations {
        private String testCaseId;
        private String Client;
        private String Environment;
        private String BrowserName;
        private String BrowserVersion;
        private String OSName;
        private String OSVersion;
        private String DeviceName;
        private String AppVersion;
        private String groupName;
        public String getAppVersion() {
            return AppVersion;
        }
        public void setAppVersion(String appVersion) {
            this.AppVersion = appVersion;
        }
        public String getBrowserName() {
            return BrowserName;
        }
        public void setBrowserName(String browserName) {
            BrowserName = browserName;
        }
        public String getBrowserVersion() {
            return BrowserVersion;
        }
        public void setBrowserVersion(String browserVersion) {
            BrowserVersion = browserVersion;
        }
        public String getOSName() {
            return OSName;
        }
        public void setOSName(String OSName) {
            this.OSName = OSName;
        }
        public String getOSVersion() {
            return OSVersion;
        }
        public void setOSVersion(String OSVersion) {
            this.OSVersion = OSVersion;
        }
        public String getDeviceName() {
            return DeviceName;
        }
        public void setDeviceName(String deviceName) {
            DeviceName = deviceName;
        }
        public Configurations() {
        }
        public String getClient() {
            return this.Client;
        }
        public void setClient(String client) {
            this.Client = client;
        }
        public String getTestCaseNumber() {
            return this.testCaseId;
        }
        public void setTestCaseNumber(String testCaseNumber) {
            this.testCaseId = testCaseNumber;
        }
        public String getEnvironment() {
            return this.Environment;
        }
        public void setEnvironment(String environment) {
            this.Environment = environment;
        }
        public String getGroupName() {
            return groupName;
        }
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }
}