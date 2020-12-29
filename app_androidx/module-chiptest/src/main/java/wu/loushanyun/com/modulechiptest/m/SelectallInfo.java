package wu.loushanyun.com.modulechiptest.m;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SelectallInfo implements Parcelable {


    /**
     * code : 0
     * msg : success
     * data : [{"id":77,"orderTime":1571713027000,"manufacturersIdentification":25,"supplier":"贵州黔物云通智能科技有限公司","orderNumber":"LSY1571713027271","productNumber":2,"tradingStatus":1,"addressId":19,"trackingNumber":"null","expirationTime":1572317827000,"trackingCompany":"null","acceptPhone":"null","deliveryTime":1571713027000,"paymentPicture":"null","pictureRemarks":"null","bussnessState":0,"inspectiontype":0,"remarks":"null","page":0,"size":0,"mt":null,"mt1":1,"mt2":0,"mt3":0,"mt4":1,"mloginSupplier":"fff","mloginFactoryNum":6}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    protected SelectallInfo(Parcel in) {
        code = in.readInt();
        msg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(msg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelectallInfo> CREATOR = new Creator<SelectallInfo>() {
        @Override
        public SelectallInfo createFromParcel(Parcel in) {
            return new SelectallInfo(in);
        }

        @Override
        public SelectallInfo[] newArray(int size) {
            return new SelectallInfo[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 77
         * orderTime : 1571713027000
         * manufacturersIdentification : 25
         * supplier : 贵州黔物云通智能科技有限公司
         * orderNumber : LSY1571713027271
         * productNumber : 2
         * tradingStatus : 1
         * addressId : 19
         * trackingNumber : null
         * expirationTime : 1572317827000
         * trackingCompany : null
         * acceptPhone : null
         * deliveryTime : 1571713027000
         * paymentPicture : null
         * pictureRemarks : null
         * bussnessState : 0
         * inspectiontype : 0
         * remarks : null
         * page : 0
         * size : 0
         * mt : null
         * mt1 : 1
         * mt2 : 0
         * mt3 : 0
         * mt4 : 1
         * mloginSupplier : fff
         * mloginFactoryNum : 6
         */

        private int id;
        private long orderTime;
        private int manufacturersIdentification;
        private String supplier;
        private String orderNumber;
        private int productNumber;
        private int tradingStatus;
        private int addressId;
        private String trackingNumber;
        private long expirationTime;
        private String trackingCompany;
        private String acceptPhone;
        private long deliveryTime;
        private String paymentPicture;
        private String pictureRemarks;
        private int bussnessState;
        private int inspectiontype;
        private String remarks;
        private int page;
        private int size;
        private Object mt;
        private int mt1;
        private int mt2;
        private int mt3;
        private int mt4;
        private String mloginSupplier;
        private int mloginFactoryNum;

        protected DataBean(Parcel in) {
            id = in.readInt();
            orderTime = in.readLong();
            manufacturersIdentification = in.readInt();
            supplier = in.readString();
            orderNumber = in.readString();
            productNumber = in.readInt();
            tradingStatus = in.readInt();
            addressId = in.readInt();
            trackingNumber = in.readString();
            expirationTime = in.readLong();
            trackingCompany = in.readString();
            acceptPhone = in.readString();
            deliveryTime = in.readLong();
            paymentPicture = in.readString();
            pictureRemarks = in.readString();
            bussnessState = in.readInt();
            inspectiontype = in.readInt();
            remarks = in.readString();
            page = in.readInt();
            size = in.readInt();
            mt1 = in.readInt();
            mt2 = in.readInt();
            mt3 = in.readInt();
            mt4 = in.readInt();
            mloginSupplier = in.readString();
            mloginFactoryNum = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeLong(orderTime);
            dest.writeInt(manufacturersIdentification);
            dest.writeString(supplier);
            dest.writeString(orderNumber);
            dest.writeInt(productNumber);
            dest.writeInt(tradingStatus);
            dest.writeInt(addressId);
            dest.writeString(trackingNumber);
            dest.writeLong(expirationTime);
            dest.writeString(trackingCompany);
            dest.writeString(acceptPhone);
            dest.writeLong(deliveryTime);
            dest.writeString(paymentPicture);
            dest.writeString(pictureRemarks);
            dest.writeInt(bussnessState);
            dest.writeInt(inspectiontype);
            dest.writeString(remarks);
            dest.writeInt(page);
            dest.writeInt(size);
            dest.writeInt(mt1);
            dest.writeInt(mt2);
            dest.writeInt(mt3);
            dest.writeInt(mt4);
            dest.writeString(mloginSupplier);
            dest.writeInt(mloginFactoryNum);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        public int getManufacturersIdentification() {
            return manufacturersIdentification;
        }

        public void setManufacturersIdentification(int manufacturersIdentification) {
            this.manufacturersIdentification = manufacturersIdentification;
        }

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getProductNumber() {
            return productNumber;
        }

        public void setProductNumber(int productNumber) {
            this.productNumber = productNumber;
        }

        public int getTradingStatus() {
            return tradingStatus;
        }

        public void setTradingStatus(int tradingStatus) {
            this.tradingStatus = tradingStatus;
        }

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }

        public String getTrackingCompany() {
            return trackingCompany;
        }

        public void setTrackingCompany(String trackingCompany) {
            this.trackingCompany = trackingCompany;
        }

        public String getAcceptPhone() {
            return acceptPhone;
        }

        public void setAcceptPhone(String acceptPhone) {
            this.acceptPhone = acceptPhone;
        }

        public long getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(long deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getPaymentPicture() {
            return paymentPicture;
        }

        public void setPaymentPicture(String paymentPicture) {
            this.paymentPicture = paymentPicture;
        }

        public String getPictureRemarks() {
            return pictureRemarks;
        }

        public void setPictureRemarks(String pictureRemarks) {
            this.pictureRemarks = pictureRemarks;
        }

        public int getBussnessState() {
            return bussnessState;
        }

        public void setBussnessState(int bussnessState) {
            this.bussnessState = bussnessState;
        }

        public int getInspectiontype() {
            return inspectiontype;
        }

        public void setInspectiontype(int inspectiontype) {
            this.inspectiontype = inspectiontype;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Object getMt() {
            return mt;
        }

        public void setMt(Object mt) {
            this.mt = mt;
        }

        public int getMt1() {
            return mt1;
        }

        public void setMt1(int mt1) {
            this.mt1 = mt1;
        }

        public int getMt2() {
            return mt2;
        }

        public void setMt2(int mt2) {
            this.mt2 = mt2;
        }

        public int getMt3() {
            return mt3;
        }

        public void setMt3(int mt3) {
            this.mt3 = mt3;
        }

        public int getMt4() {
            return mt4;
        }

        public void setMt4(int mt4) {
            this.mt4 = mt4;
        }

        public String getMloginSupplier() {
            return mloginSupplier;
        }

        public void setMloginSupplier(String mloginSupplier) {
            this.mloginSupplier = mloginSupplier;
        }

        public int getMloginFactoryNum() {
            return mloginFactoryNum;
        }

        public void setMloginFactoryNum(int mloginFactoryNum) {
            this.mloginFactoryNum = mloginFactoryNum;
        }
    }
}
