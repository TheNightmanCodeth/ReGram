package com.diragi.regram.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.diragi.regram.Adapters.ListAdapter;
import com.diragi.regram.ClipboardService;
import com.diragi.regram.R;

import java.util.ArrayList;

public class RedirectActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = prefs.edit();

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setContentTitle("RePost");
        notification.setContentText("Copy a posts share URL to RePost it!");
        notification.setOngoing(true);
        notification.setPriority(0);
        notification.setSmallIcon(R.drawable.ic_logo);

        final Notification notificationCompat = notification.build();

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Switch enabledToggle = (Switch)findViewById(R.id.toggle);
        enabledToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Show the notification and start service
                    notificationManager.notify(R.integer.notification_id, notificationCompat);
                    Intent i = new Intent(RedirectActivity.this, ClipboardService.class);
                    startService(i);
                } else {
                    //Disable the notification and stop service
                    notificationManager.cancel(R.integer.notification_id);
                    stopService(new Intent(RedirectActivity.this, ClipboardService.class));
                }
            }
        });

        list();
    }

    private void list() {
        final String[] titles = {"Go Pro", "Change Dialog Theme"};
        final String[] subs   = {"Remove ads, hide statusbar icon, and unlock premium themes", "Change the appearance of the RePost dialog"};
        final Integer[] icons = {R.drawable.ic_present_to_all_black_24dp, R.drawable.ic_color_lens_black_24dp};

        ListAdapter listAdapter = new ListAdapter(RedirectActivity.this, titles, subs, icons);
        ListView list = (ListView)findViewById(R.id.main_list);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RedirectActivity.this, "You clicked on:" +titles[i], Toast.LENGTH_LONG).show();
            }
        });
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
