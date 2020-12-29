package wu.loushanyun.com.sixapp.m;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkInfo implements Parcelable {


    /**
     * responseTime : 1590631287316
     * message : success
     * code : 0
     * data : {"productForm":"3","testNum":"7","magnification":"0.1m³/ev(10)","caliber":"15mm","distributeNum":"20","goodsModel":"型号1","rangeRatio":"80","batchNumber":"2"}
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
         * productForm : 3
         * testNum : 7
         * magnification : 0.1m³/ev(10)
         * caliber : 15mm
         * distributeNum : 20
         * goodsModel : 型号1
         * rangeRatio : 80
         * batchNumber : 2
         */

        private String productForm;
        private String testNum;
        private String magnification;
        private String caliber;
        private String distributeNum;
        private String goodsModel;



        private String rangeRatio;
        private String batchNumber;
        private String orderNumber;
        private int orderId;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }
        public String getProductForm() {
            return productForm;
        }

        public void setProductForm(String productForm) {
            this.productForm = productForm;
        }

        public String getTestNum() {
            return testNum;
        }

        public void setTestNum(String testNum) {
            this.testNum = testNum;
        }

        public String getMagnification() {
            return magnification;
        }

        public void setMagnification(String magnification) {
            this.magnification = magnification;
        }

        public String getCaliber() {
            return caliber;
        }

        public void setCaliber(String caliber) {
            this.caliber = caliber;
        }

        public String getDistributeNum() {
            return distributeNum;
        }

        public void setDistributeNum(String distributeNum) {
            this.distributeNum = distributeNum;
        }

        public String getGoodsModel() {
            return goodsModel;
        }

        public void setGoodsModel(String goodsModel) {
            this.goodsModel = goodsModel;
        }

        public String getRangeRatio() {
            return rangeRatio;
        }

        public void setRangeRatio(String rangeRatio) {
            this.rangeRatio = rangeRatio;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.productForm);
            dest.writeString(this.testNum);
            dest.writeString(this.magnification);
            dest.writeString(this.caliber);
            dest.writeString(this.distributeNum);
            dest.writeString(this.goodsModel);
            dest.writeString(this.rangeRatio);
            dest.writeString(this.batchNumber);
            dest.writeString(this.orderNumber);
            dest.writeInt(this.orderId);
        }

        protected DataBean(Parcel in) {
            this.productForm = in.readString();
            this.testNum = in.readString();
            this.magnification = in.readString();
            this.caliber = in.readString();
            this.distributeNum = in.readString();
            this.goodsModel = in.readString();
            this.rangeRatio = in.readString();
            this.batchNumber = in.readString();
            this.orderNumber = in.readString();
            this.orderId = in.readInt();
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

    public WorkInfo() {
    }

    protected WorkInfo(Parcel in) {
        this.responseTime = in.readLong();
        this.message = in.readString();
        this.code = in.readInt();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<WorkInfo> CREATOR = new Parcelable.Creator<WorkInfo>() {
        @Override
        public WorkInfo createFromParcel(Parcel source) {
            return new WorkInfo(source);
        }

        @Override
        public WorkInfo[] newArray(int size) {
            return new WorkInfo[size];
        }
    };
}
