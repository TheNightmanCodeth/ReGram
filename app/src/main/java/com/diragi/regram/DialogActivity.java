package com.diragi.regram;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by joe on 7/1/16.
 */

public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog();
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ReGramDialogFragment reGramDialogFragment = new ReGramDialogFragment();
        reGramDialogFragment.show(fm, "dialog");
    }
}
