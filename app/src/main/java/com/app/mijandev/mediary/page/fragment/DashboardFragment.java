package com.app.mijandev.mediary.page.fragment;


import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.base.BaseFragment;
import com.app.mijandev.mediary.base.ViewModelFactory;
import com.app.mijandev.mediary.data.entity.NoteStatEntity;
import com.app.mijandev.mediary.data.viewmodel.NoteListViewModel;
import com.app.mijandev.mediary.data.viewmodel.NoteStateListViewModel;
import com.app.mijandev.mediary.databinding.DashboardBinding;
import com.app.mijandev.mediary.helper.AppUtils;
import com.app.mijandev.mediary.page.activity.MainActivity;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private DashboardBinding binding;
    NoteStateListViewModel noteStateListViewModel;
    NoteListViewModel noteListViewModel;
    private PieChartData data;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = true;
    private boolean hasLabelForSelected = true;
    private String monthYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
    }

    private void initialiseViewModel() {
        noteStateListViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(NoteStateListViewModel.class);

        noteStateListViewModel.getNoteStatsLiveData().observe(this, resource -> {

            if (resource.isLoading()) {

            } else {
                generateData(resource.data);
            }

        });

        noteListViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(NoteListViewModel.class);

        noteListViewModel.getNotesLiveData().observe(this, resource -> {

            if (resource.isLoading()) {

            } else {
                noteStateListViewModel.loadNoteStats(monthYear);
            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {
        monthYear = AppUtils.getMonthYearString();
        binding.monthYearTextview.setText(AppUtils.getFormattedMonthYearString(monthYear));
        noteStateListViewModel.loadNoteStats(monthYear);

        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker();
            }
        });

    }

    private void showPicker() {
        final Calendar today = Calendar.getInstance();
        today.setTime(AppUtils.getFormattedMonthYear(monthYear));
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getActivity(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {

                today.set(Calendar.YEAR, selectedYear);
                today.set(Calendar.MONTH, selectedMonth);

                String myFormat = "yyyy-MM"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                monthYear = sdf.format(today.getTime());

                binding.monthYearTextview.setText(AppUtils.getFormattedMonthYearString(monthYear));

                noteStateListViewModel.loadNoteStats(monthYear);

            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setTitle("Select monthly statistics")
                .build()
                .show();
    }

    private void generateData(List<NoteStatEntity> data) {

        int angryCount = 0;
        int sadCount = 0;
        int okayCount = 0;
        int happyCount = 0;
        int blissfulCount = 0;

        for (NoteStatEntity noteStatEntity : data) {
            switch (noteStatEntity.getMood()) {
                case "angry":
                    angryCount = noteStatEntity.getTotal();
                    break;
                case "sad":
                    sadCount = noteStatEntity.getTotal();
                    break;
                case "okay":
                    okayCount = noteStatEntity.getTotal();
                    break;
                case "happy":
                    happyCount = noteStatEntity.getTotal();
                    break;
                case "blissful":
                    blissfulCount = noteStatEntity.getTotal();
                    break;
                default:


            }
        }


        int total = angryCount + sadCount + okayCount + happyCount + blissfulCount;

        binding.angryCount.setText(AppUtils.getPercentString(angryCount, total));
        binding.sadCount.setText(AppUtils.getPercentString(sadCount, total));
        binding.okayCount.setText(AppUtils.getPercentString(okayCount, total));
        binding.happyCount.setText(AppUtils.getPercentString(happyCount, total));
        binding.blissfulCount.setText(AppUtils.getPercentString(blissfulCount, total));


        List<SliceValue> values = new ArrayList<SliceValue>();

        SliceValue angrySliceValue = new SliceValue(angryCount, getResources().getColor(R.color.angryColor));
        SliceValue sadSliceValue = new SliceValue(sadCount, getResources().getColor(R.color.sadColor));
        SliceValue okaySliceValue = new SliceValue(okayCount, getResources().getColor(R.color.okayColor));
        SliceValue happySliceValue = new SliceValue(happyCount, getResources().getColor(R.color.happyColor));
        SliceValue blissfulSliceValue = new SliceValue(blissfulCount, getResources().getColor(R.color.blissfulColor));


        if (angryCount > 0) {
            values.add(angrySliceValue);
        }

        if (sadCount > 0) {
            values.add(sadSliceValue);
        }

        if (okayCount > 0) {
            values.add(okaySliceValue);
        }

        if (happyCount > 0) {
            values.add(happySliceValue);
        }

        if (blissfulCount > 0) {
            values.add(blissfulSliceValue);
        }


        this.data = new PieChartData(values);
        this.data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        this.data.setHasCenterCircle(hasCenterCircle);
        this.data.setCenterCircleScale((float) 0.8);


        if (hasCenterText1) {
            this.data.setCenterText1(String.valueOf(total));

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SF-Pro-Display-Bold.otf");
            this.data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            this.data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size))).setCenterText1Color(R.color.colorDarkerGreyishBlue);
        }

        if (hasCenterText2) {
            if (total > 0)
                this.data.setCenterText2("Total Entry");
            else
                this.data.setCenterText2("No entry");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SF-Pro-Display-Bold.otf");

            this.data.setCenterText2Typeface(tf);
            this.data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size))).setCenterText2Color(R.color.colorGreyishBlue);
        }


        binding.pieChartView.setPieChartData(this.data);
    }


}
