package com.diragi.regram.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diragi.regram.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by joe on 7/6/16.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    ArrayList<String> preflist;
    OnItemClickedListener itemClickedListener;

    public static interface OnItemClickedListener {
        public void onItemClicked(View caller, int position);
    }

    public MainRecyclerAdapter(ArrayList<String> preflist, OnItemClickedListener listener) {
        this.preflist = preflist;
        this.itemClickedListener = listener;
    }

    @Override
    public MainRecyclerAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        MainViewHolder mainViewHolder = new MainViewHolder(v);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder vh, final int position) {
        switch (position) {
            case 0:
                vh.image.setImageResource(R.drawable.ic_color_lens_black_24dp);
                vh.title.setText("Dialog Theme");
                vh.sub.setText("Change the look of the RePost dialog");
                break;
            case 1:
                vh.image.setImageResource(R.drawable.ic_present_to_all_black_24dp);
                vh.title.setText("Go Pro");
                vh.sub.setText("Remove all ads, hide the status bar icon, and unlock new themes");
        }
        vh.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickedListener.onItemClicked(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView title;
        protected TextView sub;
        protected View bg;

        public MainViewHolder(View v) {
            super(v);
            image = (ImageView)v.findViewById(R.id.list_icon);
            title = (TextView)v.findViewById(R.id.list_title);
            sub = (TextView)v.findViewById(R.id.list_sub);
            bg = v;
        }
    }
}
