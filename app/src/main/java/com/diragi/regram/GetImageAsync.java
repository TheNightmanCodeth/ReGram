package com.diragi.regram;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by joe on 6/29/16.
 */

public class GetImageAsync extends AsyncTask<String, Integer, String> {
    public interface OnImageReceived {
        void onImageReceived(String link);
    }

    private OnImageReceived listener;
    String link;
    Document doc;
    Element img;
    String imgSrc;

    public GetImageAsync(OnImageReceived listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        link = strings[0];
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            img = doc.select("img").first();
            imgSrc = img.absUrl("src");
        }

        return imgSrc;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onImageReceived(s);
    }
}
