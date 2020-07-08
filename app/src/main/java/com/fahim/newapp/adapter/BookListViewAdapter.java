package com.fahim.newapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.utils.FileUtils;
import com.fahim.newapp.utils.Preferences;

import java.io.File;
import java.util.List;

public class BookListViewAdapter extends RecyclerView.Adapter<BookListViewAdapter.ViewHolder> {
    private Context context;
    private List<BookHolder> holdersList;
    String swipedList = "";
    private Preferences preferences = new Preferences();
    private AdapterClickListener listener;
    private String Dir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String folder = "";
    private List<Integer> favList;

    public void setFavList(List<Integer> favList) {
        this.favList = favList;
    }

    public BookListViewAdapter(Context context, List<BookHolder> userList, List<Integer> favList, AdapterClickListener listener) {
        this.context = context;
        this.holdersList = userList;
        this.listener = listener;
        this.favList = favList;
        folder = this.context.getString(R.string.app_name);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_listview_adapter_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final BookHolder model = holdersList.get(position);

        holder.txtname.setText(model.getBookname());

        if (!FileUtils.isDownladable(model.booklink) || model.booklink.startsWith("https://drive") || new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + folder + File.separator + model.getBookname() + ".pdf").exists()) {
            holder.download.setVisibility(View.VISIBLE);
            holder.download.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
        } else {
            holder.download.setVisibility(View.VISIBLE);
            holder.download.setImageResource(R.drawable.ic_baseline_cloud_download_24);

        }
        if (favList.contains(model.getId())) {
            holder.addtofav.setImageResource(R.drawable.staradded);
        } else {
            holder.addtofav.setImageResource(R.drawable.star);
        }

        holder.addtofav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedBookId(context, model.getId());
                preferences.putPosition(context, position);
                listener.onClick(R.id.add_to_fav);
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedBookId(context, model.getId());
                preferences.putSelectedBookName(context, model.getBookname());
                preferences.putSelectedBookLink(context, model.getBooklink());
                listener.onClick(R.id.download);
            }
        });
        holder.txtname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.putSelectedBookId(context, model.getId());
                preferences.putSelectedBookName(context, model.getBookname());
                preferences.putSelectedBookLink(context, model.getBooklink());
                listener.onClick(R.id.book_name);
            }
        });
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
    }

    @Override
    public int getItemCount() {
        return holdersList == null ? 0 : holdersList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txtname;
        private ImageView addtofav, download, downloadDelete, option;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.book_name);
            addtofav = itemView.findViewById(R.id.add_to_fav);
            download = itemView.findViewById(R.id.download);
            downloadDelete = itemView.findViewById(R.id.downloaddelete);
            option = itemView.findViewById(R.id.option);
            option.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Share");
            Edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case 1:
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, holdersList.get(getAdapterPosition()).booklink);
                            try {
                                context.startActivity(Intent.createChooser(whatsappIntent, "Share"));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(context, "No App Found", Toast.LENGTH_LONG).show();
                            }


                            break;
                    }
                    return true;
                }
            });
        }
    }
}
