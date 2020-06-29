package com.fahim.newapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.List;

public class StandardAdapter extends RecyclerView.Adapter<StandardAdapter.ViewHolder> {
    private Context context;
    private List<StandardHolder> standardholderList;
    String swipedList = "";
    private Preferences preferences=new Preferences();
    private AdapterClickListener listener;

    public StandardAdapter(Context context, List<StandardHolder> userList,AdapterClickListener listener) {
        this.context = context;
        this.standardholderList = userList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standard_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StandardHolder model = standardholderList.get(position);
        holder.txtstd.setText(model.getStandardName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedStandardId(context,model.getId());
                listener.onClick(R.id.delete_standard);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedStandardId(context,model.getId());
                preferences.putSelectedStandardName(context,model.getStandardName());
                listener.onClick(R.id.edit_standard);
            }
        });
    }

    @Override
    public int getItemCount() {
        return standardholderList == null ? 0 : standardholderList.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtstd;
        private ImageView delete,edit;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtstd = itemView.findViewById(R.id.standard_name);
            delete=itemView.findViewById(R.id.delete_standard);
            edit=itemView.findViewById(R.id.edit_standard);



        }
    }
}
