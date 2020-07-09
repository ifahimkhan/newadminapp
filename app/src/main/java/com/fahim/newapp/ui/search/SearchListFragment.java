package com.fahim.newapp.ui.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.Interface.FragmentClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.BookListViewAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.utils.Preferences;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SearchListFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private Preferences preferences = new Preferences();
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private BookListViewAdapter bookListViewAdapter;
    private List<BookHolder> bookHolderList = new ArrayList<>();
    private List<Integer> favBookList = new ArrayList<>();
    private TextView searchfor;
    private ImageView empty;
    private File sdcard = Environment.getExternalStorageDirectory();


    public static SearchListFragment newInstance() {
        return new SearchListFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_list, container, false);
        searchViewModel.setContext(getActivity());


        recyclerView = root.findViewById(R.id.recyclerview);
        searchfor = root.findViewById(R.id.search_for);
        empty = root.findViewById(R.id.empty);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        bookListViewAdapter = new BookListViewAdapter(getContext(), bookHolderList, favBookList, this);
        recyclerView.setAdapter(bookListViewAdapter);
        recyclerView.setHasFixedSize(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchfor.setText("searched For '" + preferences.getSearchBook(getActivity()) + "'");

        searchViewModel.favBookList().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                favBookList.clear();
                favBookList.addAll(integers);
                bookListViewAdapter.notifyDataSetChanged();
            }
        });
        searchViewModel.getSearchData(preferences.getSearchBook(getActivity())).observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> bookHolders) {

                if (bookHolders == null || bookHolders.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    RequestOptions options = new RequestOptions()
                            .centerCrop();

                    Log.e(TAG, "empty: ");
                    Glide.with(getActivity()).load("http://tarabeiser.com/ux/imgux/searchEmptyState.png").apply(options).into(empty);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    bookHolderList.clear();
                    bookHolderList.addAll(bookHolders);
                    bookListViewAdapter.notifyDataSetChanged();
                }
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
        if (!favBookList.contains(preferences.getSelectedBookId(getActivity()))) {

            searchViewModel.addToFavBook().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
                @Override
                public void onChanged(List<Integer> integers) {
                    Log.e("TAG", "onChanged: " + integers.toString());
                    favBookList.clear();
                    favBookList.addAll(integers);
                    bookListViewAdapter.setFavList(favBookList);
                    bookListViewAdapter.notifyDataSetChanged();
                }
            });
        }else{
            Toast.makeText(getActivity(), R.string.toast_alreadyadded,Toast.LENGTH_LONG).show();
        }
    }


}
