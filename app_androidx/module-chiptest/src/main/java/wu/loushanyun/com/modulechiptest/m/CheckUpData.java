package wu.loushanyun.com.modulechiptest.m;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckUpData implements Parcelable {
    private String oreder;
    private String bodynum;
    private String MLoginFactoryNum;
    private String inspectionField;
    private int moduleType;
    private String chipnum;

    public String getOreder() {
        return oreder;
    }

    public void setOreder(String oreder) {
        this.oreder = oreder;
    }

    public String getBodynum() {
        return bodynum;
    }

    public void setBodynum(String bodynum) {
        this.bodynum = bodynum;
    }

    public String getmLoginFactoryNum() {
        return MLoginFactoryNum;
    }

    public void setmLoginFactoryNum(String mLoginFactoryNum) {
        this.MLoginFactoryNum = mLoginFactoryNum;
    }

    public String getInspectionField() {
        return inspectionField;
    }

    public void setInspectionField(String inspectionField) {
        this.inspectionField = inspectionField;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public String getChipNum() {
        return chipnum;
    }

    public void setChipNum(String chipNum) {
        this.chipnum = chipNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.oreder);
        dest.writeString(this.bodynum);
        dest.writeString(this.MLoginFactoryNum);
        dest.writeString(this.inspectionField);
        dest.writeInt(this.moduleType);
        dest.writeString(this.chipnum);
    }

    public CheckUpData() {
    }

    protected CheckUpData(Parcel in) {
        this.oreder = in.readString();
        this.bodynum = in.readString();
        this.MLoginFactoryNum = in.readString();
        this.inspectionField = in.readString();
        this.moduleType = in.readInt();
        this.chipnum = in.readString();
    }

    public static final Parcelable.Creator<CheckUpData> CREATOR = new Parcelable.Creator<CheckUpData>() {
        @Override
        public CheckUpData createFromParcel(Parcel source) {
            return new CheckUpData(source);
        }

        @Override
        public CheckUpData[] newArray(int size) {
            return new CheckUpData[size];
        }
    };
}
