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
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context context;
    private List<BookHolder> standardholderList;
    String swipedList = "";
    private Preferences preferences=new Preferences();
    private AdapterClickListener listener;

    public BookAdapter(Context context, List<BookHolder> userList, AdapterClickListener listener) {
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
        final BookHolder model = standardholderList.get(position);

        holder.txtstd.setText(model.getBookname());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedBookId(context,model.getId());
                listener.onClick(R.id.delete_subject);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedBookId(context,model.getId());
                preferences.putSelectedSubjectName(context,model.getBookname());
                preferences.putSelectedSubjectName(context,model.getBookname());
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
            txtstd = itemView.findViewById(R.id.book_name);
            delete=itemView.findViewById(R.id.delete_book);
            edit=itemView.findViewById(R.id.edit_book);



        }
    }
}
