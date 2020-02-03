package com.app.mijandev.mediary.di.module;


import com.app.mijandev.mediary.page.fragment.DashboardFragment;
import com.app.mijandev.mediary.page.fragment.DiaryFragment;
import com.app.mijandev.mediary.page.fragment.SettingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract DiaryFragment contributeDiaryFragment();

    @ContributesAndroidInjector
    abstract DashboardFragment contributeDashboardFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributeSettingFragment();

}
