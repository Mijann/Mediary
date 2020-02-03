package com.app.mijandev.mediary.page.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.adapter.TabAdapter;
import com.app.mijandev.mediary.base.ViewModelFactory;
import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.viewmodel.NoteListViewModel;
import com.app.mijandev.mediary.databinding.MainActivityBinding;
import com.app.mijandev.mediary.helper.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.Arrays;


import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, BottomNavigationView.OnNavigationItemSelectedListener {

    /*
     * inject the ViewModelFactory. The ViewModelFactory class
     * has a list of ViewModels and will provide
     * the corresponding ViewModel in this activity
     * */
    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private NoteListViewModel noteListViewModel;

    private MainActivityBinding binding;
    private TabAdapter tabAdapter;
    private Integer[] menuItems = {R.id.tab_home,R.id.tab_diary,R.id.tab_setting};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * In our ActivityModule, we
         * defined MainActivity injection? So we need
         * to call this method in order to inject the
         * ViewModelFactory into our Activity
         * */


        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialiseView();

    }

    private void initialiseView() {

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(tabAdapter);
        binding.bottomBar.setOnNavigationItemSelectedListener(this);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.bottomBar.setSelectedItemId(menuItems[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        noteListViewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteListViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppUtils.ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {


            if(!data.hasExtra(AppUtils.INTENT_TASK))
            {
                NoteEntity noteEntity = new NoteEntity();
                String note = data.getStringExtra(AppUtils.INTENT_NOTE);
                String mood = data.getStringExtra(AppUtils.INTENT_MOOD);
                if(data.hasExtra(AppUtils.INTENT_PHOTO)){
                    noteEntity.setPhoto(data.getStringExtra(AppUtils.INTENT_PHOTO));
                }
                noteEntity.setNote(note);
                noteEntity.setMood(mood);
                String dateTime = data.getStringExtra(AppUtils.INTENT_DATE) + " " + AppUtils.getCurrentTimeString();
                noteEntity.setCreatedAt(AppUtils.getFormattedDateTime(dateTime));
                noteListViewModel.addNote(noteEntity);
            }
            else{
                NoteEntity noteEntity = data.getParcelableExtra(AppUtils.INTENT_TASK);
                int position = data.getIntExtra(AppUtils.INTENT_POSITION,0);
                noteListViewModel.updateNote(noteEntity,position);
            }

        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        binding.viewPager.setCurrentItem(Arrays.asList(menuItems).indexOf(menuItem.getItemId()));
        return true;
    }
}
