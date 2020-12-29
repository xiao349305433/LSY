package com.wu.loushanyun.base.print;

public class PrintToolBean3 {
    String id;
    String timeYear;
    String timeMonth;
    String timeDay;
    String factoryName;
    String chuanganxinhao;

    public PrintToolBean3(String id, String timeYear, String timeMonth, String timeDay, String factoryName, String chuanganxinhao) {
        this.id = id;
        this.timeYear = timeYear;
        this.timeMonth = timeMonth;
        this.timeDay = timeDay;
        this.factoryName = factoryName;
        this.chuanganxinhao = chuanganxinhao;
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

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getChuanganxinhao() {
        return chuanganxinhao;
    }

    public void setChuanganxinhao(String chuanganxinhao) {
        this.chuanganxinhao = chuanganxinhao;
    }
}
