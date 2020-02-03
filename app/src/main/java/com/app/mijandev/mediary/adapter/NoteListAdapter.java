package com.app.mijandev.mediary.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;


import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.viewmodel.NoteListViewModel;
import com.app.mijandev.mediary.databinding.NoteBinding;
import com.app.mijandev.mediary.databinding.ViewNoteBinding;
import com.app.mijandev.mediary.helper.AppUtils;
import com.app.mijandev.mediary.helper.NavigatorUtils;
import com.app.mijandev.mediary.page.activity.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.CustomViewHolder> {

    private Activity activity;
    private List<NoteEntity> notes;
    private NoteListViewModel noteListViewModel;
    public NoteListAdapter(Activity activity) {
        this.activity = activity;
        this.notes = new ArrayList<>();
        this.noteListViewModel = ViewModelProviders.of((MainActivity) activity).get(NoteListViewModel.class);
    }

    @NonNull
    @Override
    public NoteListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NoteBinding itemBinding = NoteBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    public void setItems(List<NoteEntity> notes) {
        int startPosition = this.notes.size();
        this.notes.addAll(notes);
        notifyItemRangeInserted(startPosition, notes.size());
    }

    public void updateItem(NoteEntity noteEntity,int position) {
        this.notes.remove(position);
        this.notes.add(position,noteEntity);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public NoteEntity getItem(int position) {
        return notes.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position),position);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private NoteBinding binding;
        public CustomViewHolder(NoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(NoteEntity noteEntity,int position) {

            setViewNote(binding,noteEntity);

            binding.noteCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewNote(noteEntity,position);
                }
            });
        }
    }

    private void setViewNote(NoteBinding binding, NoteEntity noteEntity) {

        binding.noteText.setText(noteEntity.note);
        binding.noteDate.setText(AppUtils.getFormattedTimeString(noteEntity.getCreatedAt()));

        switch (noteEntity.getMood())
        {
            case "angry" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_red));
                break;
            case "sad" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_light_blue));
                break;
            case "happy" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_green));
                break;
            case "blissful" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_pink));
                break;
            default:
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_blue));
                break;
        }

        binding.noteMood.setText("I am " + noteEntity.getMood());

        if(noteEntity.getPhoto() != null && !TextUtils.isEmpty(noteEntity.getPhoto()))
        {
            binding.notePhoto.setImageURI(Uri.fromFile(new File(noteEntity.getPhoto())));
            binding.notePhoto.setVisibility(View.VISIBLE);
        }else{
            binding.notePhoto.setVisibility(View.GONE);
        }
    }

    private void setViewNote2(ViewNoteBinding binding, NoteEntity noteEntity) {

        binding.noteText.setText(noteEntity.note);
        binding.noteDate.setText(AppUtils.getFormattedTimeString(noteEntity.getCreatedAt()));

        switch (noteEntity.getMood())
        {
            case "angry" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_red));
                break;
            case "sad" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_light_blue));
                break;
            case "happy" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_green));
                break;
            case "blissful" :
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_pink));
                break;
            default:
                binding.noteMood.setBackground(activity.getDrawable(R.drawable.rect_bg_blue));
                break;
        }

        binding.noteMood.setText("I am " + noteEntity.getMood());

        if(noteEntity.getPhoto() != null && !TextUtils.isEmpty(noteEntity.getPhoto()))
        {
            binding.notePhoto.setImageURI(Uri.fromFile(new File(noteEntity.getPhoto())));
            binding.notePhoto.setVisibility(View.VISIBLE);
        }else{
            binding.notePhoto.setVisibility(View.GONE);
        }
    }

    public void viewNote(NoteEntity noteEntity,int position)
    {
        final AlertDialog viewDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        ViewNoteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.view_note_layout, null, false);
        builder.setView(binding.getRoot());
        viewDialog = builder.create();
        viewDialog.show();
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setViewNote2(binding,noteEntity);

        binding.deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote(noteEntity,position,viewDialog);
            }
        });

        binding.editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.dismiss();
                NavigatorUtils.redirectToAddNewNote(activity,null,noteEntity,position);
            }
        });

    }

    public void deleteNote(NoteEntity noteEntity,int position,AlertDialog viewDialog){
        new AlertDialog.Builder(activity)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        noteListViewModel.deleteNote(noteEntity);
                        notes.remove(noteEntity);
                        new File(noteEntity.getPhoto()).delete();
                        notifyItemRemoved(position);
                        viewDialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void clearItems()
    {
        this.notes.clear();
    }
}
