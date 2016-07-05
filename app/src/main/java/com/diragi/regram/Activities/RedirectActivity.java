package com.diragi.regram.Activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.diragi.regram.R;

public class RedirectActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = prefs.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null) {
            String uriString = uri.toString();
            String access_token = uriString.substring(uriString.lastIndexOf("=") + 1);
            if (access_token != null) {
                Log.d("ACCESS_TOKEN", access_token);
                edit.putString("ACCESS_TOKEN", access_token);
                edit.commit();
            }
        }
    }
}
