package com.app.mijandev.mediary.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.app.mijandev.mediary.data.entity.NoteEntity;
import com.app.mijandev.mediary.page.activity.AddNewNoteActivity;

public class NavigatorUtils {

    public static void redirectToAddNewNote(Activity activity, String date, NoteEntity noteEntity, int position) {
        Intent intent = new Intent(activity, AddNewNoteActivity.class);

        if (date != null)
            intent.putExtra(AppUtils.INTENT_DATE, date);

        if(noteEntity != null)
            intent.putExtra(AppUtils.INTENT_TASK, noteEntity);

        if(position > -1)
            intent.putExtra(AppUtils.INTENT_POSITION, position);

        activity.startActivityForResult(intent,AppUtils.ACTIVITY_REQUEST_CODE );
    }

}
