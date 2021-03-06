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
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private Context context;
    private List<SubjectHolder> standardholderList;
    String swipedList = "";
    private Preferences preferences=new Preferences();
    private AdapterClickListener listener;

    public SubjectAdapter(Context context, List<SubjectHolder> userList, AdapterClickListener listener) {
        this.context = context;
        this.standardholderList = userList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubjectHolder model = standardholderList.get(position);

        holder.txtstd.setText(model.getSubject_name());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedSubjectId(context,model.getId());
                listener.onClick(R.id.delete_subject);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedSubjectId(context,model.getId());
                preferences.putSelectedSubjectName(context,model.getSubject_name());
                listener.onClick(R.id.edit_subject);
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
            txtstd = itemView.findViewById(R.id.subject_name);
            delete=itemView.findViewById(R.id.delete_subject);
            edit=itemView.findViewById(R.id.edit_subject);



        }
    }
}
