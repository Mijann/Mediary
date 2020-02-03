package com.app.mijandev.mediary.data.repository;



import com.app.mijandev.mediary.data.dao.NoteDao;
import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.entity.NoteStatEntity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public Observable<Resource<List<NoteEntity>>> loadNotes(String date) {

        return loadFromDB(date);

    }
    private Observable<Resource<List<NoteEntity>>> loadFromDB(String date)
    {
        List<NoteEntity> noteEntities = noteDao.getNotes(date);
        return Flowable.just(noteEntities).toObservable()
                .map(Resource::success);
    }

    public Observable<Resource<List<NoteEntity>>> addNewNote(NoteEntity noteEntity){

        Long id = noteDao.insertNote(noteEntity);

        if(id != null)
        {
            noteEntity.setId(id);
        }

        List<NoteEntity> noteEntities = Arrays.asList(noteEntity);

        return Flowable.just(noteEntities).toObservable().map(Resource::success);
    }

    public Observable<Resource<List<NoteEntity>>> deleteNote(NoteEntity noteEntity)
    {
        noteDao.deleteNote(noteEntity);

        List<NoteEntity> noteEntities = Arrays.asList(noteEntity);

        return Flowable.just(noteEntities).toObservable().map(Resource::deleting);
    }

    public Observable<Resource<List<NoteEntity>>> updateNote(NoteEntity noteEntity){

        noteDao.updateNote(noteEntity);

        List<NoteEntity> noteEntities = Arrays.asList(noteEntity);

        return Flowable.just(noteEntities).toObservable().map(Resource::updating);
    }

    public Observable<Resource<List<NoteStatEntity>>> getNoteStats(String month)
    {
        List<NoteStatEntity> noteStatEntities = noteDao.getNoteStat(month);
        return Flowable.just(noteStatEntities).toObservable()
                .map(Resource::success);
    }

}
