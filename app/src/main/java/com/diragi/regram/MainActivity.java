package com.diragi.regram;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private ClipboardManager.OnPrimaryClipChangedListener listener = () -> checkClipboardContents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    public Intent getInstaIntent(Intent instaIntent) {
        Intent toRet = instaIntent;
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(toRet, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo: resolveInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.instagram.android")) {
                toRet.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                break;
            }
        }
        return toRet;
    }

    private void checkClipboardContents() {
        ClipboardManager cb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        String contents = "uey7dhk9fig93hekif";
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                contents = cd.getItemAt(0).getText().toString();
                if (contents.contains("https://www.instagram.com/p/")) {
                    startActivity(new Intent(MainActivity.this, DialogActivity.class));
                    editor.putString("url", contents);
                    editor.commit();
                } else if (contents.equals("uey7dhk9fig93hekif")) {
                    //Oh shit
                    Toast.makeText(getApplicationContext(), "There was a problem. Please re-copy the share url", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
