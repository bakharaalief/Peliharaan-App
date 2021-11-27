package com.bakharaalief.peliharaanapp.Data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Aktifitas implements Parcelable {
    private final String uid;
    private final Timestamp aktifitasDate;
    private final String type;
    private final String note;

    public Aktifitas(String uid, Timestamp aktifitasDate, String type, String note) {
        this.uid = uid;
        this.aktifitasDate = aktifitasDate;
        this.type = type;
        this.note = note;
    }

    protected Aktifitas(Parcel in) {
        uid = in.readString();
        aktifitasDate = in.readParcelable(Timestamp.class.getClassLoader());
        type = in.readString();
        note = in.readString();
    }

    public static final Creator<Aktifitas> CREATOR = new Creator<Aktifitas>() {
        @Override
        public Aktifitas createFromParcel(Parcel in) {
            return new Aktifitas(in);
        }

        @Override
        public Aktifitas[] newArray(int size) {
            return new Aktifitas[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public Timestamp getAktifitasDate() {
        return aktifitasDate;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeParcelable(aktifitasDate, i);
        parcel.writeString(type);
        parcel.writeString(note);
    }
}
