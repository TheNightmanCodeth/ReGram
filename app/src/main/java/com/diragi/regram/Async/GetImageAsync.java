package com.diragi.regram.Async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;

/**
 * Created by joe on 6/29/16.
 */

public class GetImageAsync extends AsyncTask<String, Integer, String> {
    public interface OnImageReceived {
        void onImageReceived(String link);
    }

    private OnImageReceived listener;
    String imgSrc;

    public GetImageAsync(OnImageReceived listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String postID = strings[0];

        String ACCESS_TOKEN = strings[1];
        String link = "https://api.instagram.com/v1/media/shortcode/" +postID +"?access_token=" +ACCESS_TOKEN;
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject object = (JSONObject) new JSONTokener(s).nextValue();
            JSONObject data = object.getJSONObject("data");
            JSONObject images = data.getJSONObject("images");
            JSONObject image = images.getJSONObject("standard_resolution");
            String url = image.getString("url");
            listener.onImageReceived(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
