package com.app.mijandev.mediary.data.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.app.mijandev.mediary.base.BaseViewModel;
import com.app.mijandev.mediary.data.dao.NoteDao;
import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.repository.NoteRepository;
import com.app.mijandev.mediary.data.repository.Resource;

import java.util.List;

import javax.inject.Inject;

public class NoteListViewModel extends BaseViewModel {

    private NoteRepository noteRepository;
    private MutableLiveData<Resource<List<NoteEntity>>> notesLiveData = new MutableLiveData<>();


    /*
     * We are injecting the NoteDao class
     * to the ViewModel.
     * */
    @Inject
    public NoteListViewModel(NoteDao noteDao) {
        noteRepository = new NoteRepository(noteDao);
    }

    /*
     * Method called by UI to fetch notes list
     * */
    public void loadNotes(String date) {
        noteRepository.loadNotes(date)
                .doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(resource -> getNotesLiveData().postValue(resource));
    }

    public void addNote(NoteEntity noteEntity){
        noteRepository.addNewNote(noteEntity).doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(resource -> getNotesLiveData().postValue(resource));
    }

    public void deleteNote(NoteEntity noteEntity){
        noteRepository.deleteNote(noteEntity).doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(resource -> getNotesLiveData().postValue(resource));
    }

    public void updateNote(NoteEntity noteEntity,int position){
        noteRepository.updateNote(noteEntity).doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(listResource -> {
                    listResource.setPositionUpdated(position);
                    getNotesLiveData().postValue(listResource);
                });
    }



    /*
     * LiveData observed by the UI
     * */
    public MutableLiveData<Resource<List<NoteEntity>>> getNotesLiveData() {
        return notesLiveData;
    }
}
