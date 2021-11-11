package com.bakharaalief.peliharaanapp.Data.model;

import com.google.firebase.Timestamp;

public class Aktifitas {
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
}
