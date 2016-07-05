package com.diragi.regram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diragi.regram.Async.GetImageAsync;
import com.diragi.regram.Async.SaveImageAsync;

import java.io.File;
import java.util.List;

/**
 * Created by joe on 6/26/16.
 */

public class ReGramDialogFragment extends DialogFragment {
    //Declare views
    private Button reGram;
    private Button withLogo;
    private TextView title;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    File pic = Environment.getExternalStorageDirectory();

    private String url;

    public ReGramDialogFragment() {
        //Empty constructor required by super
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();

        View view = inflater.inflate(R.layout.regram_dialog_fragment, container);
        reGram = (Button) view.findViewById(R.id.reGram);
        reGram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reGram(false);
            }
        });
        withLogo = (Button) view.findViewById(R.id.reGram_logo);
        withLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reGram(true);
            }
        });
        title = (TextView) view.findViewById(R.id.title);
        return view;
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
            Toast.makeText(getContext(), "Invalid share URL. Please try again", Toast.LENGTH_LONG).show();
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
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.instagram.android");
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
    }
}
