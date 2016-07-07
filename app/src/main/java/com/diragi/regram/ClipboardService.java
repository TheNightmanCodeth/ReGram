package com.diragi.regram;

import android.app.IntentService;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.diragi.regram.Activities.DialogActivity;
import com.diragi.regram.Activities.MainActivity;

/**
 * Created by joe on 7/7/16.
 */

public class ClipboardService extends Service {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            checkClipboardContents();
        }
    };

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("Handler message:", msg.toString());
            Log.d("Service", "running");

            ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
        }
    }

    @Override
    public void onCreate() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        HandlerThread thread = new HandlerThread("RePostClipboardService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "Started");

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkClipboardContents() {
        ClipboardManager cb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        String contents = "uey7dhk9fig93hekif";
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                contents = cd.getItemAt(0).getText().toString();
                if (contents.contains("https://www.instagram.com/p/")) {
                    Intent dialog = new Intent(getApplicationContext(), DialogActivity.class);
                    dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialog);
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
    public void onDestroy() {
        Log.d("Service", "Stopped");
    }
}
