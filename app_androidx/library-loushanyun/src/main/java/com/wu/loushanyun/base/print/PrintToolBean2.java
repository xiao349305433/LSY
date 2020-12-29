package com.wu.loushanyun.base.print;

public class PrintToolBean2 {
    String sn;
    String chuangan;
    String factoryName;
    String id;
    String timeYear;
    String timeMonth;
    String timeDay;

    int type=0;

    public PrintToolBean2(int type,String sn, String chuangan, String factoryName, String id, String timeYear, String timeMonth, String timeDay) {
        this.sn = sn;
        this.type = type;
        this.chuangan = chuangan;
        this.factoryName = factoryName;
        this.id = id;
        this.timeYear = timeYear;
        this.timeMonth = timeMonth;
        this.timeDay = timeDay;
    }

    public PrintToolBean2(String sn, int type) {
        this.sn = sn;
        this.type = type;
    }

    public String getSn() {
        return sn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getChuangan() {
        return chuangan;
    }

    public void setChuangan(String chuangan) {
        this.chuangan = chuangan;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeYear() {
        return timeYear;
    }

    public void setTimeYear(String timeYear) {
        this.timeYear = timeYear;
    }

    public String getTimeMonth() {
        return timeMonth;
    }

    public void setTimeMonth(String timeMonth) {
        this.timeMonth = timeMonth;
    }

    public String getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(String timeDay) {
        this.timeDay = timeDay;
    }
}
