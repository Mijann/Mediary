package com.app.mijandev.mediary.di.module;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;


import com.app.mijandev.mediary.data.AppDatabase;
import com.app.mijandev.mediary.data.dao.NoteDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    /*
     * The method returns the Database object
     * */
    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return AppDatabase.getInstance(application.getApplicationContext());
    }

    /*
     * We need the NoteDao module.
     * For this, We need the AppDatabase object
     * So we will define the providers for this here in this module.
     * */

    @Provides
    @Singleton
    NoteDao provideNoteDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.noteDao();
    }
}
