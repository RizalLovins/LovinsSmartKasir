package com.rizal.lovins.smartkasir.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.util.PreferenceUtil;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent intent;
                    SharedPreferences pref =
                            SplashScreenActivity.this.getSharedPreferences(PreferenceUtil.PREF_NAME, 0);
                    try {
                        String id = pref.getString("username", null);
                        Log.e("username: ", id);
                        intent = new Intent(SplashScreenActivity.this, MainActivity.class);

                    } catch (Exception e) {
                        intent = new Intent(SplashScreenActivity.this,
                                LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();

                } catch (Exception ignored) {
                }
            }
        };
        background.start();
    }
}

