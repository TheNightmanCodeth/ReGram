package com.diragi.regram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;

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
    File file = new File(Environment.getDataDirectory() + "img" + File.separator +genImgName());

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            return;
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            return;
        }
    };

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
        if (!logo && !url.equals("")) {
            Log.d("regram", url);
            new GetImageAsync(link -> saveToFile(genImgName(), url)).execute(url);
        } else {
            Log.d("regram", url);
            Toast.makeText(getContext(), "Invalid share URL. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToFile(String fileName, String url) {
        Picasso.with(getContext())
                .load(url)
                .into(target);
        sendToInsta();
    }

    private void sendToInsta() {
        Intent instaIntent = new Intent(Intent.ACTION_SEND);
        instaIntent.setType("image/*");
        Uri uri = Uri.fromFile(file);
        instaIntent.putExtra(Intent.EXTRA_STREAM, uri);
        instaIntent.setPackage("com.instagram.android");

        MainActivity ma = new MainActivity();
        Intent finalInstaIntent = ma.getInstaIntent(instaIntent);
        startActivity(finalInstaIntent);
    }

    private String genImgName() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        int random = rand.nextInt(charSet.length());
        String name = "";
        for (int i = 0; i < 10; i++) {
            name += charSet.charAt(i);
        }
        return name;
    }
}
