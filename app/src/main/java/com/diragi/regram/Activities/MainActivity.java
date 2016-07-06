package com.diragi.regram.Activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diragi.regram.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    final Activity thisActivity = this;
    Context context = MainActivity.this;

    private final int EXT_STORAGE_REQ = 1;

    private ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            checkClipboardContents();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setContentTitle("RePost");
        notification.setContentText("Copy a posts share URL to RePost it!");
        notification.setOngoing(true);

        Notification notificationCompat = notification.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(01, notificationCompat);

        ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);

        final CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        //Login
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customTabsIntent.launchUrl(thisActivity, Uri.parse("https://api.instagram.com/oauth/authorize/" + "?client_id=" +getString(R.string.client_id) +"&redirect_uri=" +getString(R.string.redirect_uri) +"&response_type=token" +"&scope=public_content"));
            }
        });
        //Permissions
        Button permissions = (Button)findViewById(R.id.permissions);
        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(thisActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(thisActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXT_STORAGE_REQ);
        }
    }

    private void checkClipboardContents() {
        ClipboardManager cb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        String contents = "uey7dhk9fig93hekif";
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                contents = cd.getItemAt(0).getText().toString();
                if (contents.contains("https://www.instagram.com/p/")) {
                    startActivity(new Intent(MainActivity.this, DialogActivity.class));
                    editor.putString("url", contents);
                    editor.commit();
                } else if (contents.equals("uey7dhk9fig93hekif")) {
                    //Oh shit
                    Toast.makeText(getApplicationContext(), "There was a problem. Please re-copy the share url", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXT_STORAGE_REQ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
        }
    }
}
