package com.app.mijandev.mediary.di.component;

import android.app.Activity;
import android.app.Application;


import com.app.mijandev.mediary.di.module.DbModule;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class AppController extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        DaggerAppComponent.builder()
                .application(this)
                .dbModule(new DbModule())
                .build()
                .inject(this);
    }
}
