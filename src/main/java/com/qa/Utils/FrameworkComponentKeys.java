package com.qa.Utils;

public enum FrameworkComponentKeys {
    API("API"),
    WEB("WEB"),
    MAINFRAME("MAINFRAME"),
    MOBILE("MOBILE");
    public String label;

    FrameworkComponentKeys(String label) {
        this.label = label;
    }

    public String getComponentAsString() {
        return label;
    }
}
