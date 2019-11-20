package com.holike.crm.bean.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


/**
 * Created by pony on 2019/8/19.
 * Copyright holike possess 2019.
 */
public class Installer implements Parcelable {
    public static Installer newAdded() {
        return new Installer(null, null);
    }

    private String id;
    private String installUserId;
    private String installUserName;
    private String feedbackFlag;

    public Installer() {
    }

    public Installer(String installUserId, String installUserName) {
        this.installUserId = installUserId;
        this.installUserName = installUserName;
    }

    public Installer(String id, String installUserId, String installUserName) {
        this.id = id;
        this.installUserId = installUserId;
        this.installUserName = installUserName;
    }

    public Installer(String id, String installUserId, String installUserName, String feedbackFlag) {
        this.id = id;
        this.installUserId = installUserId;
        this.installUserName = installUserName;
        this.feedbackFlag = feedbackFlag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setInstallUserId(String installUserId) {
        this.installUserId = installUserId;
    }

    public String getInstallUserId() {
        return installUserId == null ? "" : installUserId;
    }

    public void setInstallUserName(String installUserName) {
        this.installUserName = installUserName;
    }

    public String getInstallUserName() {
        return installUserName == null ? "" : installUserName;
    }

    public void setFeedback(String feedbackFlag) {
        this.feedbackFlag = feedbackFlag;
    }

    public boolean isFeedback() {
        return TextUtils.equals(feedbackFlag, "Y") || TextUtils.equals(feedbackFlag, "y");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Installer) {
            return TextUtils.equals(installUserId, ((Installer) obj).installUserId);
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(installUserId);
        parcel.writeString(installUserName);
        parcel.writeString(feedbackFlag);
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Installer createFromParcel(Parcel source) {
            Installer i = new Installer();
            i.setId(source.readString());
            i.setInstallUserId(source.readString());
            i.setInstallUserName(source.readString());
            i.setFeedback(source.readString());
            return i;
        }

        @Override
        public Installer[] newArray(int size) {
            return new Installer[size];
        }
    };
}
