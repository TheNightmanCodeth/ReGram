package com.diragi.regram.Activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.diragi.regram.Adapters.MainRecyclerAdapter;
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

        ArrayList<String> prefs = new ArrayList<String>();
        prefs.add("Theme");
        prefs.add("Pro");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainRecyclerAdapter recyclerAdapter = new MainRecyclerAdapter(prefs, new MainRecyclerAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(View caller, int position) {
                switch (position) {
                    case 0:
                        //Theme
                        break;
                    case 1:
                        //Pro
                        break;
                }
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
