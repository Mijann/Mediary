package com.app.mijandev.mediary.page.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.base.ViewModelFactory;
import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.data.viewmodel.NoteListViewModel;
import com.app.mijandev.mediary.databinding.AddNewNoteActivityBinding;
import com.app.mijandev.mediary.helper.AppUtils;
import com.hsalf.smilerating.BaseRating;

import java.io.File;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class AddNewNoteActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private AddNewNoteActivityBinding binding;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int PERMISSION_ALL = 101;

    private Bitmap imageBitmap = null;
    private String date;
    private NoteEntity noteEntity;
    private int position;
    private boolean isDeletedPhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_note);
        initialiseView();
    }

    private void initialiseView() {

        if (getIntent() != null) {
            if (getIntent().hasExtra(AppUtils.INTENT_DATE)) {
                date = getIntent().getStringExtra(AppUtils.INTENT_DATE);
            }

            if (getIntent().hasExtra(AppUtils.INTENT_TASK)) {
                noteEntity = getIntent().getParcelableExtra(AppUtils.INTENT_TASK);
            }

            if (getIntent().hasExtra(AppUtils.INTENT_POSITION)) {
                position = getIntent().getIntExtra(AppUtils.INTENT_POSITION, -1);
            }
        }

        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();
            }
        });

        binding.deleteNotePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBitmap = null;
                binding.linearPhoto.setVisibility(View.GONE);
                isDeletedPhoto = true;

            }
        });

        if (noteEntity != null) {
            binding.noteEditText.setText(noteEntity.getNote());
            if (noteEntity.getPhoto() != null && !TextUtils.isEmpty(noteEntity.getPhoto())) {
                binding.notePhoto.setImageURI(Uri.fromFile(new File(noteEntity.getPhoto())));
                binding.linearPhoto.setVisibility(View.VISIBLE);
            } else {
                binding.linearPhoto.setVisibility(View.GONE);
            }
        }

        initialiseSmileyRating();

    }

    private void initialiseSmileyRating() {

        binding.smileyRating.setSelectedSmile(BaseRating.OKAY);
        binding.smileyRating.setNameForSmile(BaseRating.TERRIBLE, "Angry");
        binding.smileyRating.setNameForSmile(BaseRating.BAD, "Sad");
        binding.smileyRating.setNameForSmile(BaseRating.GOOD, "Happy");
        binding.smileyRating.setNameForSmile(BaseRating.GREAT, "Blissful");

        if (noteEntity != null) {
            switch (noteEntity.getMood()) {
                case "angry":
                    binding.smileyRating.setSelectedSmile(BaseRating.TERRIBLE);
                    break;
                case "sad":
                    binding.smileyRating.setSelectedSmile(BaseRating.BAD);
                    break;
                case "happy":
                    binding.smileyRating.setSelectedSmile(BaseRating.GOOD);
                    break;
                case "blissful":
                    binding.smileyRating.setSelectedSmile(BaseRating.GREAT);
                    break;
                default:
                    binding.smileyRating.setSelectedSmile(BaseRating.OKAY);
                    break;

            }
        }

    }

    private void addNewNote() {

        if (TextUtils.isEmpty(binding.noteEditText.getText().toString().trim())) {
            binding.noteEditText.setError("Field Cannot Empty");
            binding.noteEditText.requestFocus();
            AppUtils.openKeyboard(getApplicationContext());
            return;
        }

        Intent intent = new Intent();

        if (imageBitmap != null) {
            if (AppUtils.storeImage(imageBitmap, AddNewNoteActivity.this) != null) {
                String imagePath = AppUtils.storeImage(imageBitmap, AddNewNoteActivity.this);

                if (noteEntity == null)
                    intent.putExtra(AppUtils.INTENT_PHOTO, imagePath);
                else
                    noteEntity.setPhoto(imagePath);
            }
        }

        String note = binding.noteEditText.getText().toString().trim();
        String mood = getMoodString(binding.smileyRating.getRating());

        if (noteEntity == null) {
            intent.putExtra(AppUtils.INTENT_NOTE, note);
            intent.putExtra(AppUtils.INTENT_MOOD, mood);
            intent.putExtra(AppUtils.INTENT_DATE, date);
        } else {

            if(isDeletedPhoto){
                new File(noteEntity.getPhoto()).delete();
                noteEntity.setPhoto(null);
            }

            noteEntity.setNote(note);
            noteEntity.setMood(mood);
            intent.putExtra(AppUtils.INTENT_TASK, noteEntity);
            intent.putExtra(AppUtils.INTENT_POSITION, position);
        }

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String getMoodString(int mood) {
        switch (mood) {
            case 1:
                return "angry";
            case 2:
                return "sad";
            case 3:
                return "okay";
            case 4:
                return "happy";
            case 5:
                return "blissful";
            default:
                return "";
        }

    }

    private void openPicker() {

        if (Build.VERSION.SDK_INT < 23) {
            takePhoto();
        } else {

            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(AddNewNoteActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                takePhoto();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                return;
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, AppUtils.REQUEST_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQUEST_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            binding.notePhoto.setImageBitmap(imageBitmap);
            binding.linearPhoto.setVisibility(View.VISIBLE);
            binding.addNewNestedScrollview.smoothScrollTo(0, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.camera_menu:
                openPicker();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
