package wu.loushanyun.com.sixapp.m;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsM implements Parcelable {
    private String koujing;
    private String beilv;
    private String rangeRatio;

    public String getKoujing() {
        return koujing;
    }

    public void setKoujing(String koujing) {
        this.koujing = koujing;
    }

    public String getBeilv() {
        return beilv;
    }

    public void setBeilv(String beilv) {
        this.beilv = beilv;
    }

    public String getRangeRatio() {
        return rangeRatio;
    }

    public void setRangeRatio(String rangeRatio) {
        this.rangeRatio = rangeRatio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.koujing);
        dest.writeString(this.rangeRatio);
        dest.writeString(this.beilv);
    }

    public GoodsM() {
    }

    protected GoodsM(Parcel in) {
        this.koujing = in.readString();
        this.rangeRatio = in.readString();
        this.beilv = in.readString();
    }

    public static final Parcelable.Creator<GoodsM> CREATOR = new Parcelable.Creator<GoodsM>() {
        @Override
        public GoodsM createFromParcel(Parcel source) {
            return new GoodsM(source);
        }

        @Override
        public GoodsM[] newArray(int size) {
            return new GoodsM[size];
        }
    };
}
