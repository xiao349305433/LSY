package wu.loushanyun.com.sixapp.m;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GetModelInfo implements Parcelable {


    /**
     * responseTime : 1600402883395
     * message : success
     * code : 0
     * datas : [{"id":157,"goodsName":"二号模组水表","goodsModel":"M02","rangeRatio":"R=80&R=100","caliber":"15mm&20mm&25mm","magnification":"0.01m³/ev(100)"},{"id":158,"goodsName":"三号模组水表","goodsModel":"M03","rangeRatio":"R=100","caliber":"15mm&20mm&25mm","magnification":"0.01m³/ev(100)"},{"id":159,"goodsName":"物联网集中器","goodsModel":"M01","rangeRatio":"undefined","caliber":"undefined","magnification":"undefined"}]
     */

    private long responseTime;
    private String message;
    private int code;
    private List<DatasBean> datas;

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

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Parcelable {
        /**
         * id : 157
         * goodsName : 二号模组水表
         * goodsModel : M02
         * rangeRatio : R=80&R=100
         * caliber : 15mm&20mm&25mm
         * magnification : 0.01m³/ev(100)
         */

        private int id;
        private String goodsName;
        private String goodsModel;
        private String rangeRatio;
        private String caliber;
        private String magnification;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
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

        public String getCaliber() {
            return caliber;
        }

        public void setCaliber(String caliber) {
            this.caliber = caliber;
        }

        public String getMagnification() {
            return magnification;
        }

        public void setMagnification(String magnification) {
            this.magnification = magnification;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.goodsName);
            dest.writeString(this.goodsModel);
            dest.writeString(this.rangeRatio);
            dest.writeString(this.caliber);
            dest.writeString(this.magnification);
        }

        public DatasBean() {
        }

        protected DatasBean(Parcel in) {
            this.id = in.readInt();
            this.goodsName = in.readString();
            this.goodsModel = in.readString();
            this.rangeRatio = in.readString();
            this.caliber = in.readString();
            this.magnification = in.readString();
        }

        public static final Creator<DatasBean> CREATOR = new Creator<DatasBean>() {
            @Override
            public DatasBean createFromParcel(Parcel source) {
                return new DatasBean(source);
            }

            @Override
            public DatasBean[] newArray(int size) {
                return new DatasBean[size];
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
        dest.writeList(this.datas);
    }

    public GetModelInfo() {
    }

    protected GetModelInfo(Parcel in) {
        this.responseTime = in.readLong();
        this.message = in.readString();
        this.code = in.readInt();
        this.datas = new ArrayList<DatasBean>();
        in.readList(this.datas, DatasBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetModelInfo> CREATOR = new Parcelable.Creator<GetModelInfo>() {
        @Override
        public GetModelInfo createFromParcel(Parcel source) {
            return new GetModelInfo(source);
        }

        @Override
        public GetModelInfo[] newArray(int size) {
            return new GetModelInfo[size];
        }
    };
}
