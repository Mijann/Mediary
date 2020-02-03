package com.app.mijandev.mediary.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.app.mijandev.mediary.helper.TimestampConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity
public class NoteEntity implements Parcelable {

    public NoteEntity() {
    }

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("note")
    @Expose
    public String note="";

    @SerializedName("photo")
    @Expose
    public String photo="";

    @SerializedName("mood")
    @Expose
    public String mood="";

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    private Date modifiedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected NoteEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        note = in.readString();
        photo = in.readString();
        mood = in.readString();
        createdAt = (java.util.Date) in.readSerializable();
        modifiedAt = (java.util.Date) in.readSerializable();
    }

    public static final Creator<NoteEntity> CREATOR = new Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel in) {
            return new NoteEntity(in);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(note);
        parcel.writeString(photo);
        parcel.writeString(mood);
        parcel.writeSerializable(createdAt);
        parcel.writeSerializable(modifiedAt);

    }


}
