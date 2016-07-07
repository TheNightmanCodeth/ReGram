package com.diragi.regram.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diragi.regram.Async.GetImageAsync;
import com.diragi.regram.Async.SaveImageAsync;
import com.diragi.regram.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;

/**
 * Created by joe on 7/1/16.
 */

public class DialogActivity extends AppCompatActivity {

    private String url;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    File pic = Environment.getExternalStorageDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_test_id));

        showDialog();
    }

    private void showDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.regram_dialog, null);

        Button noLogo = (Button)dialogView.findViewById(R.id.reGram);
        Button logo = (Button)dialogView.findViewById(R.id.reGram_logo);

        noLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reGram(false);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reGram(true);
            }
        });

        AdView dialogBanner = (AdView)dialogView.findViewById(R.id.dialog_banner_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        dialogBanner.loadAd(adRequest);

        alertDialog.setView(dialogView);

        alertDialog.show();
    }

    private void reGram(Boolean logo) {
        url = prefs.getString("url", "");
        String imageId = url.split("/")[4];
        String accessToken = prefs.getString("ACCESS_TOKEN", null);
        if (!logo && imageId != null && accessToken != null) {
            Log.d("Image id", imageId);
            new GetImageAsync(new GetImageAsync.OnImageReceived() {
                @Override
                public void onImageReceived(String link) {
                    saveToFile(link);
                }
            }).execute(imageId, accessToken);
        } else {
            Log.d("regram", url);
            Toast.makeText(getApplicationContext(), "Invalid share URL. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToFile(String url) {
        Log.e("Image", url);
        System.out.println(url);
        new SaveImageAsync(new SaveImageAsync.OnImageSaved() {
            @Override
            public void onImageSaved(File image) {
                sendToInsta(image);
            }
        }).execute(url, Environment.getExternalStorageDirectory().toString());
    }

    private void sendToInsta(File file) {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }catch (Exception e) {
                e.printStackTrace();
            }
            shareIntent.setType("image/jpeg");
            startActivity(shareIntent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="
                    + "com.instagram.android"));
            startActivity(intent);
        }
        deleteImageFromStorage(file);
    }

    private void deleteImageFromStorage(File fileToDelete) {
        //TODO: Delete image from storage
        //fileToDelete.delete();
    }
}
