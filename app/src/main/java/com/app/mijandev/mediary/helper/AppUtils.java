package com.app.mijandev.mediary.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {

    public static final int ACTIVITY_REQUEST_CODE = 001;
    public static final int REQUEST_IMAGE = 002;
    public static final String INTENT_DATE = "intent_date";
    public static final String INTENT_TASK = "intent_task";
    public static final String INTENT_NOTE = "intent_note";
    public static final String INTENT_MOOD = "intent_mood";
    public static final String INTENT_PHOTO = "intent_photo";
    public static final String INTENT_POSITION = "intent_position";

    public static String getPercentString(int ori,int total)
    {

        Double percent = Double.parseDouble(String.valueOf(ori)) /Double.parseDouble(String.valueOf(total)) * 100;

        if(percent.isNaN())
        {
            percent = 0.0;
        }

        return String.format("%.2f", percent) + "%";
    }

    public static String getMonthYearString(){
        Date currentDate =  Calendar.getInstance().getTime();
        try {

            SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM");
            return spf.format(currentDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormattedMonthYearString(String date)
    {

        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM");

        Date dateType = null;
        try {
            dateType = currentFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat inputFormat = new SimpleDateFormat("MMM, yyyy");

        String currentDateTime = inputFormat.format(dateType);

        return currentDateTime;
    }


    public static String getCurrentTimeString(){
        Date currentDate =  Calendar.getInstance().getTime();
        try {

            SimpleDateFormat spf= new SimpleDateFormat("HH:mm:ss");
            return spf.format(currentDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDateString(){
        Date currentDate =  Calendar.getInstance().getTime();
        try {

            SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
            return spf.format(currentDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getFormattedDate(String date){
        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dateType = null;
        try {
            dateType = currentFormat.parse(date);
            return  dateType;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getFormattedMonthYear(String date){
        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM");

        Date dateType = null;
        try {
            dateType = currentFormat.parse(date);
            return  dateType;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getFormattedDateTime(String date){
        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dateType = null;
        try {
            dateType = currentFormat.parse(date);
            return  dateType;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFormattedTimeString(Date date) {

        try {

            SimpleDateFormat spf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            String dateString = spf.format(date);

            Date newDate = spf.parse(dateString);
            spf= new SimpleDateFormat("h:mm a");
            return spf.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormmattedDateString(String date)
    {

        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dateType = null;
        try {
            dateType = currentFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

        String currentDateTime = inputFormat.format(dateType);

        return currentDateTime;
    }

    public static void openKeyboard(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 500);
    }

    public static File getOutputMediaFile(Activity activity){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + activity.getPackageName()
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="mediary_photo_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static String storeImage(Bitmap image,Activity activity) {
        File pictureFile = getOutputMediaFile(activity);
        if (pictureFile == null) {
            Log.d("AppUtils",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("AppUtils", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("AppUtils", "Error accessing file: " + e.getMessage());
        }

        return pictureFile.getAbsolutePath();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
