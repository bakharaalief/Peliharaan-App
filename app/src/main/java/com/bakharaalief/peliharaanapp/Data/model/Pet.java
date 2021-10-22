package com.bakharaalief.peliharaanapp.Data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Pet implements Parcelable {
    private final String uid;
    private final Timestamp birthDate;
    private final String name;
    private final String type;

    public Pet(String uid, Timestamp birthDate, String name, String type) {
        this.uid = uid;
        this.birthDate = birthDate;
        this.name = name;
        this.type = type;
    }

    protected Pet(Parcel in) {
        uid = in.readString();
        birthDate = in.readParcelable(Timestamp.class.getClassLoader());
        name = in.readString();
        type = in.readString();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeParcelable(birthDate, i);
        parcel.writeString(name);
        parcel.writeString(type);
    }
}
