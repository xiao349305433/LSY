package wu.loushanyun.com.moduletwoinit.m;

public class WuLianSaveData {
    private String sn;
    private String factoryCode;
    private String chuanganxinhao;
    private String anzhuangshijian;
    private String beizhu;
    private String areaZuoBiao;//坐标

    public WuLianSaveData(String sn, String factoryCode, String chuanganxinhao, String anzhuangshijian, String beizhu, String areaZuoBiao) {
        this.sn = sn;
        this.factoryCode = factoryCode;
        this.chuanganxinhao = chuanganxinhao;
        this.anzhuangshijian = anzhuangshijian;
        this.beizhu = beizhu;
        this.areaZuoBiao = areaZuoBiao;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getChuanganxinhao() {
        return chuanganxinhao;
    }

    public void setChuanganxinhao(String chuanganxinhao) {
        this.chuanganxinhao = chuanganxinhao;
    }

    public String getAnzhuangshijian() {
        return anzhuangshijian;
    }

    public void setAnzhuangshijian(String anzhuangshijian) {
        this.anzhuangshijian = anzhuangshijian;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getAreaZuoBiao() {
        return areaZuoBiao;
    }

    public void setAreaZuoBiao(String areaZuoBiao) {
        this.areaZuoBiao = areaZuoBiao;
    }
}
