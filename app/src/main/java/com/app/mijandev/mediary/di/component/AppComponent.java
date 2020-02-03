package com.app.mijandev.mediary.di.component;

import android.app.Application;


import com.app.mijandev.mediary.di.module.ActivityModule;
import com.app.mijandev.mediary.di.module.DbModule;
import com.app.mijandev.mediary.di.module.FragmentModule;
import com.app.mijandev.mediary.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = { DbModule.class, ViewModelModule.class,
        AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder dbModule(DbModule dbModule);

        AppComponent build();
    }

    void inject(AppController appController);
}
