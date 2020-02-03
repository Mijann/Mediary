package com.app.mijandev.mediary.data.entity;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class NoteStatEntity {

    public NoteStatEntity() {
    }

    @SerializedName("mood")
    @Expose
    public String mood="";

    @SerializedName("total")
    @Expose
    public int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
