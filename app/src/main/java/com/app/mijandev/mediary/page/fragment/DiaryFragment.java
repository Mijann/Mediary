package com.app.mijandev.mediary.page.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.adapter.NoteListAdapter;
import com.app.mijandev.mediary.base.BaseFragment;
import com.app.mijandev.mediary.base.ViewModelFactory;
import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.viewmodel.NoteListViewModel;
import com.app.mijandev.mediary.databinding.NoteListFragmentBinding;
import com.app.mijandev.mediary.helper.AppUtils;
import com.app.mijandev.mediary.helper.NavigatorUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends BaseFragment {


    @Inject
    ViewModelFactory viewModelFactory;

    NoteListViewModel noteListViewModel;
    private NoteListFragmentBinding binding;
    private NoteListAdapter noteListAdapter;
    private Calendar myCalendar = null;
    private DatePickerDialog.OnDateSetListener dateDialog;
    private String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {
        noteListAdapter = new NoteListAdapter(activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        binding.notesList.setLayoutManager(linearLayoutManager);
        binding.notesList.setAdapter(noteListAdapter);

        noteListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                binding.emptyLayout.emptyContainer.setVisibility(noteListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        dateDialog();

        date = AppUtils.getCurrentDateString();

        noteListViewModel.loadNotes(date);

        binding.dateTextView.setText(AppUtils.getFormmattedDateString(date));

        binding.addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigatorUtils.redirectToAddNewNote(getActivity(), date, null,-1);
            }
        });

        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

    }


    private void initialiseViewModel() {
        noteListViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(NoteListViewModel.class);

        noteListViewModel.getNotesLiveData().observe(this, resource -> {
            if (resource.isLoading()) {

            } else if (!resource.data.isEmpty()) {

                if (resource.isSuccess())
                    updateNoteList(resource.data);
                else
                {
                    if(noteListAdapter.getItemCount() > 0)
                    {
                        int positionUpdated = resource.getPositionUpdated();

                        noteListAdapter.updateItem(resource.data.get(0),positionUpdated);
                    }

                }

            } else handleErrorResponse();
        });
    }


    private void updateNoteList(List<NoteEntity> notes) {
        hideLoader();
        binding.emptyLayout.emptyContainer.setVisibility(View.GONE);
        binding.notesList.setVisibility(View.VISIBLE);
        noteListAdapter.setItems(notes);
        binding.notesList.smoothScrollToPosition(noteListAdapter.getItemCount());
    }


    private void displayLoader() {
        binding.notesList.setVisibility(View.GONE);
        binding.loaderLayout.rootView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.notesList.setVisibility(View.VISIBLE);
        binding.loaderLayout.rootView.setVisibility(View.GONE);
    }

    private void handleErrorResponse() {
        hideLoader();
        binding.notesList.setVisibility(View.GONE);
        binding.emptyLayout.emptyContainer.setVisibility(View.VISIBLE);
    }

    private void selectDate() {

        myCalendar = Calendar.getInstance();
        myCalendar.setTime(AppUtils.getFormattedDate(date));
        new DatePickerDialog(getActivity(), dateDialog, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void dateDialog() {
        dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                date = sdf.format(myCalendar.getTime());

                binding.dateTextView.setText(AppUtils.getFormmattedDateString(date));

                noteListAdapter.clearItems();
                noteListAdapter.notifyDataSetChanged();
                displayLoader();
                noteListViewModel.loadNotes(date);


            }

        };
    }

}


