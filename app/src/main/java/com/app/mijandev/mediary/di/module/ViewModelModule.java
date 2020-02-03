package com.app.mijandev.mediary.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.app.mijandev.mediary.base.ViewModelFactory;
import com.app.mijandev.mediary.data.viewmodel.NoteListViewModel;
import com.app.mijandev.mediary.data.viewmodel.NoteStateListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(NoteListViewModel.class)
    protected abstract ViewModel noteListViewModel(NoteListViewModel noteListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NoteStateListViewModel.class)
    protected abstract ViewModel noteStateListViewModel(NoteStateListViewModel noteStateListViewModel);


}
