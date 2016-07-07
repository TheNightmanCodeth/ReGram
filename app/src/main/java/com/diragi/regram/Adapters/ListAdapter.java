package com.diragi.regram.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diragi.regram.R;

import org.w3c.dom.Text;

/**
 * Created by joe on 7/6/16.
 */

public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] titles;
    private final String[] subs;
    private final Integer[] icons;

    public ListAdapter(Activity context, String[] titles, String[] subs, Integer[] icons) {
        super(context, R.layout.main_list_item, titles);
        this.context = context;
        this.titles = titles;
        this.subs = subs;
        this.icons = icons;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.main_list_item, null, true);

        TextView title = (TextView)item.findViewById(R.id.list_title);
        TextView sub   = (TextView)item.findViewById(R.id.list_sub);
        ImageView icon = (ImageView)item.findViewById(R.id.list_icon);

        title.setText(titles[position]);
        sub.setText(subs[position]);
        icon.setImageResource(icons[position]);

        return item;
    }
}
