package com.fahim.newapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fahim.newapp.R;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;

import java.util.ArrayList;

public class CustomSpinnerSubjectAdapter extends ArrayAdapter<String> {

    private ArrayList<SubjectHolder> tags_list;
    private Activity activity;
    private Holder holder;


    public CustomSpinnerSubjectAdapter(Activity activity, int textViewResourceId,
                                       ArrayList<SubjectHolder> tags_list) {
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


        holder.stdName.setText(tags_list.get(position).getSubject_name());

        return view;

    }


    @Override
    public int getCount() {
        return tags_list.size();
    }

    @Override
    public String getItem(int position) {
        return tags_list.get(position).getSubject_name();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class Holder {

        TextView stdName;


    }


}