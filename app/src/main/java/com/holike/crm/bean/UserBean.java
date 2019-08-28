package com.holike.crm.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserBean implements Parcelable {
    private String userId;
    private String userName;
    private String age;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.age);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.userId = in.readString();
        this.userName = in.readString();
        this.age = in.readString();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
