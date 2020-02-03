package com.app.mijandev.mediary.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mijan on 5/14/2018.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mediarySharedpreferencedata";
    private static final String KEY_USER_FINGERPRINT = "keyuserfingerprint";
    private static final String KEY_USER_ATTEMPT = "keyansweringattempt";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void switchFingerPrint(boolean fingerprint)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_FINGERPRINT, fingerprint);
        editor.apply();

    }


    public boolean getFingerPrintAccess()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_USER_FINGERPRINT,false);
    }

}
