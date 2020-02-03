package com.app.mijandev.mediary.page.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.base.BaseFragment;
import com.app.mijandev.mediary.base.ViewModelFactory;
import com.app.mijandev.mediary.databinding.SettingFragmentBinding;
import com.app.mijandev.mediary.helper.SharedPrefManager;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SettingFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private SettingFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {

        if(SharedPrefManager.getInstance(getActivity()).getFingerPrintAccess())
        {
            binding.fingerPrintSwitch.setChecked(true);
        }else{
            binding.fingerPrintSwitch.setChecked(false);
        }

        binding.fingerPrintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPrefManager.getInstance(getActivity()).switchFingerPrint(b);
            }
        });

    }
}
