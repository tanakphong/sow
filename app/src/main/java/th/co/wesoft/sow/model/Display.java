package th.co.wesoft.sow.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER275 on 3/9/2018.
 */

public class Display implements Parcelable {
    @SerializedName("line1")
    String line1;
    @SerializedName("line2")
    String line2;
    @SerializedName("line3")
    String line3;
    @SerializedName("line4")
    String line4;
    @SerializedName("line5")
    String line5;
    @SerializedName("line6")
    String line6;
    @SerializedName("line71")
    String line71;
    @SerializedName("line72")
    String line72;
    @SerializedName("line81")
    String line81;
    @SerializedName("line82")
    String line82;

    public Display() {
    }

    public Display(String line1, String line2, String line4, String line5, String line6, String line71, String line72, String line81, String line82) {
        this.line1 = line1;
        this.line2 = line2;
        this.line4 = line4;
        this.line5 = line5;
        this.line6 = line6;
        this.line71 = line71;
        this.line72 = line72;
        this.line81 = line81;
        this.line82 = line82;
    }

    public Display(String line1, String line2, String line3, String line4, String line5, String line6, String line71, String line72, String line81, String line82) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.line5 = line5;
        this.line6 = line6;
        this.line71 = line71;
        this.line72 = line72;
        this.line81 = line81;
        this.line82 = line82;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLine5() {
        return line5;
    }

    public void setLine5(String line5) {
        this.line5 = line5;
    }

    public String getLine6() {
        return line6;
    }

    public void setLine6(String line6) {
        this.line6 = line6;
    }

    public String getLine71() {
        return line71;
    }

    public void setLine71(String line71) {
        this.line71 = line71;
    }

    public String getLine72() {
        return line72;
    }

    public void setLine72(String line72) {
        this.line72 = line72;
    }

    public String getLine81() {
        return line81;
    }

    public void setLine81(String line81) {
        this.line81 = line81;
    }

    public String getLine82() {
        return line82;
    }

    public void setLine82(String line82) {
        this.line82 = line82;
    }

    public Display(Parcel in) {
        line1 = in.readString();
        line2 = in.readString();
        line3 = in.readString();
        line4 = in.readString();
        line5 = in.readString();
        line6 = in.readString();
        line71 = in.readString();
        line72 = in.readString();
        line81 = in.readString();
        line82 = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeString(line3);
        dest.writeString(line4);
        dest.writeString(line5);
        dest.writeString(line6);
        dest.writeString(line71);
        dest.writeString(line72);
        dest.writeString(line81);
        dest.writeString(line82);
    }


    public static final Creator<Display> CREATOR = new Creator<Display>() {
        @Override
        public Display createFromParcel(Parcel in) {
            return new Display(in);
        }

        @Override
        public Display[] newArray(int size) {
            return new Display[size];
        }
    };
}
