package com.wu.loushanyun.basemvp.m;

public class PrintBean {
    private int logoId;
    private String textName;

    public PrintBean(int logoId, String textName) {
        this.logoId = logoId;
        this.textName = textName;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }
}