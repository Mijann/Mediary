package com.app.mijandev.mediary.data.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.app.mijandev.mediary.base.BaseViewModel;
import com.app.mijandev.mediary.data.dao.NoteDao;
import com.app.mijandev.mediary.data.entity.NoteStatEntity;
import com.app.mijandev.mediary.data.repository.NoteRepository;
import com.app.mijandev.mediary.data.repository.Resource;

import java.util.List;

import javax.inject.Inject;

public class NoteStateListViewModel extends BaseViewModel {

    private NoteRepository noteRepository;
    private MutableLiveData<Resource<List<NoteStatEntity>>> noteStatsLiveData = new MutableLiveData<>();


    /*
     * We are injecting the NoteDao class
     * to the ViewModel.
     * */
    @Inject
    public NoteStateListViewModel(NoteDao noteDao) {
        noteRepository = new NoteRepository(noteDao);
    }

    /*
     * Method called by UI to fetch notes list
     * */
    public void loadNoteStats(String month) {
        noteRepository.getNoteStats(month)
                .doOnSubscribe(disposable -> addToDisposable(disposable))
                .subscribe(resource -> getNoteStatsLiveData().postValue(resource));
    }

    /*
     * LiveData observed by the UI
     * */
    public MutableLiveData<Resource<List<NoteStatEntity>>> getNoteStatsLiveData() {
        return noteStatsLiveData;
    }
}
