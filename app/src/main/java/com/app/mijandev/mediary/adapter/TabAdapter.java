package com.app.mijandev.mediary.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.mijandev.mediary.page.fragment.DashboardFragment;
import com.app.mijandev.mediary.page.fragment.DiaryFragment;
import com.app.mijandev.mediary.page.fragment.SettingFragment;

/**
 * Created by Mijann on 2/21/2018.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private DiaryFragment diaryFragment = new DiaryFragment();
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private SettingFragment settingFragment = new SettingFragment();

    public TabAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return dashboardFragment;
            case 1:
                return diaryFragment;
            case 2:
                return settingFragment;
            default:
                return null;
        }
    }

    
    @Override
    public int getCount() {
        return 3;
    }
}