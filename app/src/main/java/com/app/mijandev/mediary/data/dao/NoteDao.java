package com.app.mijandev.mediary.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.entity.NoteStatEntity;

import java.util.List;


@Dao
public interface NoteDao {


    @Query("SELECT * FROM `NoteEntity` WHERE date(created_at) = :date ORDER BY created_at ASC")
    List<NoteEntity> getNotes(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNote(NoteEntity noteEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateNote(NoteEntity noteEntity);

    @Delete
    void deleteNote(NoteEntity NoteEntity);

    @Query("SELECT COUNT(*) as total, mood FROM `NoteEntity` WHERE strftime('%Y-%m', created_at) = :month GROUP BY mood ")
    List<NoteStatEntity> getNoteStat(String month);


}
