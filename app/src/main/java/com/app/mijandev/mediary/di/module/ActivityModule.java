package com.app.mijandev.mediary.di.module;


import com.app.mijandev.mediary.page.activity.AddNewNoteActivity;
import com.app.mijandev.mediary.page.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract AddNewNoteActivity contributeAddNewNoteActivity();

}
