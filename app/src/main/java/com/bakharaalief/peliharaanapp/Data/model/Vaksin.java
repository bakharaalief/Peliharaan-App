package com.bakharaalief.peliharaanapp.Data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Vaksin implements Parcelable {
    private final String uid;
    private final Timestamp aktifitasDate;
    private final String name;

    public Vaksin(String uid, Timestamp aktifitasDate, String name) {
        this.uid = uid;
        this.aktifitasDate = aktifitasDate;
        this.name = name;
    }

    protected Vaksin(Parcel in) {
        uid = in.readString();
        aktifitasDate = in.readParcelable(Timestamp.class.getClassLoader());
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeParcelable(aktifitasDate, flags);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vaksin> CREATOR = new Creator<Vaksin>() {
        @Override
        public Vaksin createFromParcel(Parcel in) {
            return new Vaksin(in);
        }

        @Override
        public Vaksin[] newArray(int size) {
            return new Vaksin[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public Timestamp getAktifitasDate() {
        return aktifitasDate;
    }

    public String getName() {
        return name;
    }
}
