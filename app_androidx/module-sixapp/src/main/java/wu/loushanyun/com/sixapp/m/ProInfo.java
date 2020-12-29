package wu.loushanyun.com.sixapp.m;

import android.os.Parcel;
import android.os.Parcelable;

public class ProInfo implements Parcelable {


    /**
     * responseTime : 1590566650125
     * message : success
     * code : 0
     * data : {"batchTotal":"15","productForm":"3","testNum":3,"goodsId":83,"businessName":"贵州云通曙光技术服务有限公司","collectionScene":"2","goodsModel":"CENT1","batchTime":"0","tel":"18248417986","distributeNum":15,"goodsName":"总线集中采集器","jobNumber":"L12345","batchNumber":"1"}
     */

    private long responseTime;
    private String message;
    private int code;
    private DataBean data;

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * batchTotal : 15
         * productForm : 3
         * testNum : 3
         * goodsId : 83
         * businessName : 贵州云通曙光技术服务有限公司
         * collectionScene : 2
         * goodsModel : CENT1
         * batchTime : 0
         * tel : 18248417986
         * distributeNum : 15
         * goodsName : 总线集中采集器
         * jobNumber : L12345
         * batchNumber : 1
         */

        private String batchTotal;
        private String productForm;
        private int testNum;
        private int goodsId;
        private String businessName;
        private String collectionScene;
        private String goodsModel;
        private String batchTime;
        private String tel;
        private int distributeNum;
        private String goodsName;
        private String jobNumber;
        private String batchNumber;

        public String getBatchTotal() {
            return batchTotal;
        }

        public void setBatchTotal(String batchTotal) {
            this.batchTotal = batchTotal;
        }

        public String getProductForm() {
            return productForm;
        }

        public void setProductForm(String productForm) {
            this.productForm = productForm;
        }

        public int getTestNum() {
            return testNum;
        }

        public void setTestNum(int testNum) {
            this.testNum = testNum;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getCollectionScene() {
            return collectionScene;
        }

        public void setCollectionScene(String collectionScene) {
            this.collectionScene = collectionScene;
        }

        public String getGoodsModel() {
            return goodsModel;
        }

        public void setGoodsModel(String goodsModel) {
            this.goodsModel = goodsModel;
        }

        public String getBatchTime() {
            return batchTime;
        }

        public void setBatchTime(String batchTime) {
            this.batchTime = batchTime;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public int getDistributeNum() {
            return distributeNum;
        }

        public void setDistributeNum(int distributeNum) {
            this.distributeNum = distributeNum;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.batchTotal);
            dest.writeString(this.productForm);
            dest.writeInt(this.testNum);
            dest.writeInt(this.goodsId);
            dest.writeString(this.businessName);
            dest.writeString(this.collectionScene);
            dest.writeString(this.goodsModel);
            dest.writeString(this.batchTime);
            dest.writeString(this.tel);
            dest.writeInt(this.distributeNum);
            dest.writeString(this.goodsName);
            dest.writeString(this.jobNumber);
            dest.writeString(this.batchNumber);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.batchTotal = in.readString();
            this.productForm = in.readString();
            this.testNum = in.readInt();
            this.goodsId = in.readInt();
            this.businessName = in.readString();
            this.collectionScene = in.readString();
            this.goodsModel = in.readString();
            this.batchTime = in.readString();
            this.tel = in.readString();
            this.distributeNum = in.readInt();
            this.goodsName = in.readString();
            this.jobNumber = in.readString();
            this.batchNumber = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.responseTime);
        dest.writeString(this.message);
        dest.writeInt(this.code);
        dest.writeParcelable(this.data, flags);
    }

    public ProInfo() {
    }

    protected ProInfo(Parcel in) {
        this.responseTime = in.readLong();
        this.message = in.readString();
        this.code = in.readInt();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProInfo> CREATOR = new Parcelable.Creator<ProInfo>() {
        @Override
        public ProInfo createFromParcel(Parcel source) {
            return new ProInfo(source);
        }

        @Override
        public ProInfo[] newArray(int size) {
            return new ProInfo[size];
        }
    };
}
