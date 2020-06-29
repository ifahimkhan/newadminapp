package com.fahim.newapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomSpinnerStandardAdapter extends ArrayAdapter<String> {

    private ArrayList<StandardHolder> tags_list;
    private Activity activity;
    private Holder holder;


    public CustomSpinnerStandardAdapter(Activity activity, int textViewResourceId,
                                        ArrayList<StandardHolder> tags_list) {
        super(activity, textViewResourceId, new ArrayList<String>());
        this.activity = activity;
        this.tags_list = tags_list;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewGroup vparent = parent;

        if (view == null) {

            view = View.inflate(activity, R.layout.custom_spinner_layout, null);

            holder = new Holder();

            holder.stdName = view
                    .findViewById(R.id.mtextview);

            view.setTag(holder);
        } else {

            holder = (Holder) view.getTag();

        }


        holder.stdName.setText(tags_list.get(position).getStandardName());

        return view;

    }


    @Override
    public int getCount() {
        return tags_list.size();
    }

    @Override
    public String getItem(int position) {
        return tags_list.get(position).getStandardName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class Holder {

        TextView stdName;


    }


}