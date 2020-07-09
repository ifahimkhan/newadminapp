package com.fahim.newapp.ui.favourite;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.Interface.FragmentClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.BookListViewAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.utils.Preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FavNovelFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private PageViewModel pageViewModel;
    private Preferences preferences = new Preferences();
    private RecyclerView recyclerView;
    private BookListViewAdapter bookAdapter;
    private ArrayList<BookHolder> bookHolderArrayList = new ArrayList<>();
    private List<Integer> favbookList = new ArrayList<>();
    private File sdcard = Environment.getExternalStorageDirectory();

    public static FavNovelFragment newInstance() {
        return new FavNovelFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pageViewModel =
                ViewModelProviders.of(this).get(PageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        pageViewModel.setContext(getActivity());


        recyclerView = root.findViewById(R.id.recyclerview_book);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        bookAdapter = new BookListViewAdapter(getActivity(), bookHolderArrayList, favbookList, this);
        recyclerView.setAdapter(bookAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pageViewModel.favBookList().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                favbookList.clear();
                favbookList.addAll(integers);
                bookAdapter.notifyDataSetChanged();
            }
        });

        pageViewModel.getNovelHolderFavList().observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> bookHolders) {
                Log.e(TAG, "onChanged: " + bookHolders.size());
                bookHolderArrayList.clear();
                bookHolderArrayList.addAll(bookHolders);
                bookAdapter = new BookListViewAdapter(getActivity(), bookHolderArrayList, favbookList, FavNovelFragment.this);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onClick(int id) {

        switch (id) {
            case R.id.download:
                openWithoutDownload();
                break;
            case R.id.add_to_fav:
                addToFav();
                break;
            case R.id.book_name:
                openWithoutDownload();
                break;
        }

    }

    private void openWithoutDownload() {
        File file = new File(sdcard, File.separator + getString(R.string.app_name) + File.separator + File.separator + preferences.getSelectedBookName(getActivity()) + ".pdf");
        if (file.exists()) {
            Uri mpath = Uri.fromFile(file);
            Log.e("create pdf uri path==>", "" + mpath);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(mpath, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intent.createChooser(intent, "open with"));

            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(),
                        "There is no any PDF Viewer",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (preferences.getSelectedBookLink(getActivity()).startsWith("https://drive")) {
            Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(preferences.getSelectedBookLink(getActivity())));
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "Please install PDF app", Toast.LENGTH_SHORT).show();
            }

        } else if (preferences.getSelectedBookLink(getActivity()).endsWith("pdf")) {
            Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(preferences.getSelectedBookLink(getActivity())));
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "Please install PDF app", Toast.LENGTH_SHORT).show();
            }

        } else if (preferences.getSelectedBookLink(getActivity()).endsWith("zip")) {
            Toast.makeText(getActivity(), R.string.cannot_open_zip_file, Toast.LENGTH_LONG).show();
        }

    }

    private void addToFav() {
        if (favbookList.contains(preferences.getSelectedBookId(getActivity()))) {

            pageViewModel.deleteFavBook().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
                @Override
                public void onChanged(List<Integer> integers) {
                    favbookList.clear();
                    favbookList.addAll(integers);
                    bookAdapter.setFavList(favbookList);
                    bookHolderArrayList.remove(preferences.getPosition(getActivity()));
                    bookAdapter.notifyDataSetChanged();

                }
            });
        }
    }


}
