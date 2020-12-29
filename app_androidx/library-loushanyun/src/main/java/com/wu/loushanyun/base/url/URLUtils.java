package com.wu.loushanyun.base.url;


public class URLUtils {
    public static String getIP() {
        return UrlController.getInstance().getIP();
    }

    public static String getSocketIP() {
        return UrlController.getInstance().getSocketIP();
    }

    public static String getHost() {
        return UrlController.getInstance().getHost();
    }

    public static String getHead() {
        return UrlController.getInstance().getHead();
    }

    public final static String HttpHead = "http://";
 //   public final static String HostTEST = "39.100.145.211";
    //  public final static String HostTEST = "2y7c311280.zicp.vip:13309/LouShanCloud";

    //public final static String HostTEST = "39.100.239.225:8080/LouShanCloud";
 // public final static String HostTEST = "39.100.239.225:8080/loushancloud";
  //public final static String HostTEST = "192.168.2.155:8080/LouShanCloud";
    //public final static String HostTEST = "39.100.239.225:8080/loushan_cloud_maven_war_exploded/";
  // public final static String HostTEST = "39.100.239.225:8083/mep";
   public final static String HostTEST = "39.100.239.225:8082/mep";
  //  public final static String HostTEST = "192.168.2.155:8082/mep";
     // public final static String HostTEST ="192.168.2.155:8084/lsy";

   // public final static String HostTEST = "139.9.6.118:8082";
  //   public final static String HostOFFICIAL = "39.100.239.225:8083/mep";
   public final static String HostOFFICIAL = "39.100.239.225:8082/mep";
  //  public final static String HostOFFICIAL = "39.100.145.211";
    //  public final static String HostOFFICIAL = "192.168.2.117:8080/LouShanCloud";
    public final static String IPAPI = "http://47.92.85.139:8008/api/Gis/GetGridByCoordinate";
    public final static String IPManager = "/login.jsp";
    public final static String IPProduct = "/jzwlw/login/pages/login.html";
    public final static String TianDiTuAddress = "/appMapModel/tianditu.html";

    public final static String SocketIpTEST = "ws://39.100.145.211/websocket";
    public final static String SocketIpOFFICIAL = "ws://61.189.188.136:180/LouShanCloudParse/websocket";


    /**
     * =========================出厂配置app-start=====================
     */
    public static final String Findbody = "/modularproducts/findbody.mvc";
    public static final String FSLoginURL = "/factorySettings/Login.mvc";
    public static final String createMeterId = "/factorySettings/createMeterId.mvc";
    public static final String FSLoginURLPHONE = "/factorySettings/login.mvc";
    public static final String FSCheckNumberURL = "/frontend/Productregister/selectProductregister.mvc";
    public static final String FSFactorySetting = "/factorySettings/Upload.mvc";
    public static final String FSGetManufacturersIdentification = "/factorySettings/getManufacturersIdentification.mvc";
    public static final String FSSaveThirdModel = "/factorySettings/saveThirdModel.mvc";
    public static final String FSSendNoteCode = "/frontend/parameterLoad/getVerificationCode.mvc";
    public static final String FSCheckNoteCode = "/frontend/parameterLoad/checkVerificationCode.mvc";
    public static final String FSSaveModule4FactorySetting = "/factorySettings/saveModule4FactorySetting.mvc";
    public static final String FSUpdateLoginPassWord = "/frontend/parameterLoad/updataPassword.mvc";
    public static final String FSelectOrdernumber = "/factorySettings/selectOrdernumber.mvc";
    public static final String FSUpdate = HttpHead + HostOFFICIAL + "/static/APP_downLoad/update_factorysetting.json";
    public static final String CreateUserId = "/factorySettings/createUserId.mvc";
    /**
     * ===================出厂配置app-end===============================
     */


    /**
     * ===================五合一app-start=====================
     */

    public static final String ILUpdate = HttpHead + HostOFFICIAL + "/static/APP_downLoad/update_lsyinit_location.json";
    public static final String ILLoginURL = "/implement/initAppLogin.mvc";
    public static final String ILLoginURLPHONE = "/inside/login.mvc";
    public static final String IFiveUpdateLoginPwd = "/inside/updateUser.mvc";
    public static final String IFiveCheckUserExist = "/inside/checkUserExist.mvc";
    public static final String IFivegetStationList = "/inside/getBaseList.mvc";
    public static final String IFiveBaseStationLocation = "/baseStationInstallImplement/baseStationLocation.mvc";
    public static final String IFivegetStationInfo = "/inside/getBaseNums.mvc?";
    public static final String IFiveselectToken = "/frontend/register/selectToken.mvc";
    public static final String IFiveArrearsVerification = "/inside/arrearsVerification.mvc";


    /**
     * -------------------Tool工具--------------------
     */
    public static final String LoginCompanyURL = "/implement/appLoginInit.mvc";
    public static final String LoginProductURL = "/factorySettings/Login.mvc";
    public static final String GetDeviceDetail = "/baseStationInstallImplement/getDeviceDetail.mvc";
    public static final String GetBaseDetailsByProductQiYe = "/baseStationInstallImplement/getBaseDetailsByProductQiYe.mvc";
    public static final String GetBaseDetail = "/baseStationInstallImplement/getBaseDetail.mvc";
    public static final String GetDevicePushData = "/baseStationInstallImplement/getDevicePushData.mvc";
    public static final String GetDeviceDetailedInfo = "/baseStationInstallImplement/forcedResult.mvc";
    /**
     * -------------------Tool工具--------------------
     */


    /**
     * -------------------远传表号接入 start--------------------
     */
    public static String BaseStationSheBeiInstallScm = "/baseStationInstallImplement/baseStationInstallScm.mvc";

    //第三方表号接入
    public static String BaseStationSheBeiInstall = "/inside/saveThirdMeter.mvc";
    /**
     * -------------------远传表号接入 end--------------------
     */


    /**
     * -------------------物联网端接入 start--------------------
     */
    public static String BaseStationSaveOnetoOneMeter = "/inside/saveOnetoOneMeter.mvc";
    public static String MInitTwogetAreaList = "/inside/getAreaList.mvc";
    public static String MInitTwogetAreaInfo = "/inside/getAreaInfo.mvc";
    public static String MInitTwogetAreaDetail = "/inside/getAreaDetail.mvc";
    public static String MInitTwogetIotInfo = "/inside/getIotInfo.mvc";
    public static String MInitTwoUpdateAreaInfo = "/inside/updateAreaInfo.mvc";
    public static String MInitTwoUpdateAreaMeter = "/inside/updateAreaMeter.mvc";
    public static String MInitTwoInsetAreaMeter = "/inside/insetAreaMeter.mvc";

    public static String MInitTwo4SavePublicMeter = "/inside/savePublicMeter.mvc";
    /**
     * -------------------物联网端接入 end--------------------
     */

    /**
     * -------------------服务 start--------------------
     */
    public static String MRepairGetProducter = "/inside/getProducter.mvc";
    public static String MRepairGetOffLineEquipment = "/inside/getOffLineEquipment.mvc";
    public static String MRepairModifyOnetoOneEquipment = "/inside/modifyOnetoOneEquipment.mvc";
    public static String MRepairGetLocationInfo = "/inside/getLocationInfo.mvc";
    public static String MRepairModifyThirdEquipment = "/inside/modifyThirdEquipment.mvc";
    public static String MRepairGetOffLineThirdEquipment = "/inside/getOffLineThirdEquipment.mvc";
    public static String MRepairModifiyThirdMeter = "/inside/modifiyThirdMeter.mvc";
    /**
     * -------------------服务 end--------------------
     */


    /**
     * ===================模组测试app-start====================
     */
    public static final String MChipUpdate = HttpHead + HostOFFICIAL + "/static/APP_downLoad/update_model_test.json";
    public static final String MChipDeviceHisData = "/model_test/getDeviceHisData.mvc";
    public static final String MChipGetNewsInfo = "/model_test/getNewsInfo.mvc";
    public static final String MChipGetModule4ParseRule = "/model_test/getModule4ParseRule.mvc";
    public static final String MChipGetModule4ParseRuleDetail = "/model_test/getModule4ParseRuleDetail.mvc";
    public static final String MChipSetRxDelay = "/model_test/setRxDelay.mvc";
    public static final String MChipLogin = "/ModuleManagementLogin/login.mvc";
    public static final String MChipLoginPhone = "/ModuleManagementLogin/loginPhone.mvc";
    public static final String MChipModuleFactoryProduction = "/moduleFactoryProduction/add.mvc";
    public static final String MChipSelectall = "/OrderbackMonitoring/selectall.mvc";
    public static final String MChipSelecorder = "/orderproduction/selecorder.mvc";
    public static final String MChipGetUpdata = "/modularproducts/getupdata.mvc";
    public static final String MChipSelectMoudul = "/modularproducts/selectmoudul.mvc?";
    public static final String MChipOrderList = "/orderproduction/queryByOrderList.mvc";
    public static final String MChipCheckproduct = "/factorySettings/checkproduct.mvc?";


    /**
     * ===================模组测试app-start=====================
     */


  /**
   * ===================智造通 app-start====================
   */
  public static final String MSixUpdate = HttpHead + "39.100.145.211" + "/static/APP_downLoad/new_model_test.json";
  public static final String MSixCheckPhone = "/login/CheckPhone";
  public static final String MSixGetVerificationCode= "/login/getVerificationCode";
  public static final String MSixLand = "/login/land";
  public static final String MSixGetBatch = "/ProTest/getBatch";
  public static final String MSixProInf = "/ProTest/ProInf";
  public static final String MSixEnvironmentName = "/ProTest/EnvironmentName";
  public static final String MSixEnvironmentInf = "/ProTest/EnvironmentInf";
    public static final String MSixEnvironmentInf2 = "/newPro/EnvironmentInf";
  public static final String MSixWorkInf = "/ProTest/WorkInf";
  public static final String MSixGoodsInf = "/ProTest/GoodsInf";
  public static final String MSixSaveInf = "/ProTest/saveInf";
  public static final String MSixManuInf = "/ProTest/ManuInf";
  public static final String MSixEnvName = "/deDug/envName";
  public static final String MSixGetCom = "/deDug/getCom";
    public static final String MSixSaveFirst = "/newPro/saveFirst";
    public static final String MSixSaveSecond = "/newPro/saveSecond";
    public static final String MSixContractPro = "/newPro/contractPro";
    public static final String MSixGetModel = "/newPro/getModel";


  /**
   * ==================服务商 app-start====================
   */
    public static final String MSevenUpdate = HttpHead + "39.100.145.211" + "/static/APP_downLoad/update_active.json";
    public static final String MSevenDoesItExist = "/serviceLogin/doesItExist";
    public static final String MSevenGetVerificationCode = "/serviceLogin/getVerificationCode";
    public static final String MSevenSignIn = "/serviceLogin/signIn";
    public static final String MSevenServiceLogin = "/serviceLogin/doesItExist";
    public static final String MSevenIsItLocalActivated = "/userActivation/isItLocalActivated";
    public static final String MSevenIsItActivated = "/userActivation/isItActivated";
    public static final String MSevenQueryBaseStation = "/userActivation/queryBaseStation";
    public static final String MSevenWaterAuthority = "/userActivation/waterAuthority";
    public static final String MSevenDetailedInformation = "/userActivation/detailedInformation";
   public static final String MSevenShowName = "/ProTest/showName";






}
