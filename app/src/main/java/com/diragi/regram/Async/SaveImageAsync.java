package com.diragi.regram.Async;

import android.media.audiofx.EnvironmentalReverb;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;

/**
 * Created by joe on 7/5/16.
 */

public class SaveImageAsync extends AsyncTask<String, Integer, File> {

    public interface OnImageSaved {
        void onImageSaved(File image);
    }

    private OnImageSaved listener;

    public SaveImageAsync(OnImageSaved listener) {
        this.listener = listener;
    }

    @Override
    protected File doInBackground(String... strings) {
        String url = strings[0];
        String path = strings[1];
        try {
            URL imgUrl = new URL(url);
            InputStream input = imgUrl.openStream();
            String name = genImgName() +".jpg";
            String finalPath = path +"/" +name;
            Log.d("PATH", path.substring(1));
            OutputStream output = new FileOutputStream(new File(finalPath));
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                input.close();
                output.close();
                File file = new File(finalPath);
                Log.d("URIASYNC", Uri.fromFile(file).toString());
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String genImgName() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        String name = "";
        for (int i = 0; i < 10; i++) {
            int random = rand.nextInt(charSet.length());
            name += charSet.charAt(random);
        }
        return name;
    }

    @Override
    protected void onPostExecute(File s) {
        listener.onImageSaved(s);
    }
}
