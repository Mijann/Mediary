package com.app.mijandev.mediary.page.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.app.mijandev.mediary.R;
import com.app.mijandev.mediary.helper.SharedPrefManager;

import me.aflak.libraries.callback.FingerprintDialogSecureCallback;
import me.aflak.libraries.callback.FingerprintSecureCallback;
import me.aflak.libraries.dialog.FingerprintDialog;
import me.aflak.libraries.utils.FingerprintToken;


/**
 * Created by Mijan on 16/07/2018.
 */

public class Splash extends Activity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        /* New Handler to start the Main-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPrefManager.getInstance(Splash.this) != null)
                {
                    if(SharedPrefManager.getInstance(Splash.this).getFingerPrintAccess())
                    {
                        /* Create an Intent that will start the Main-Activity. */
                        FingerprintDialog.initialize(Splash.this)
                                .title("Your personal diary is locked")
                                .message("Your fingerprint is required")
                                .callback(new FingerprintDialogSecureCallback() {
                                    @Override
                                    public void onAuthenticationSucceeded() {

                                            goToMainActivity();

                                    }

                                    @Override
                                    public void onAuthenticationCancel() {

                                        finish();

                                    }

                                    @Override
                                    public void onNewFingerprintEnrolled(FingerprintToken token) {

                                    }
                                },"KEY")
                                .show();
                    }else{
                        goToMainActivity();
                    }
                }
                else{
                    goToMainActivity();
                }



            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void goToMainActivity() {

        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

}
