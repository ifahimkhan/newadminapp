package com.fahim.newapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FavBookFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private PageViewModel pageViewModel;
    private Preferences preferences = new Preferences();
    private RecyclerView recyclerView;
    private BookListViewAdapter bookAdapter;
    private ArrayList<BookHolder> bookHolderArrayList = new ArrayList<>();
    private List<Integer> favbookList = new ArrayList<>();
    private String TAG = FavBookFragment.class.getSimpleName();

    public static FavBookFragment newInstance() {
        return new FavBookFragment();
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

        getBooksData();


    }

    private void getBooksData() {
        pageViewModel.getBookHolderFavList().observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> bookHolders) {
                Log.e(TAG, "onChanged: " + bookHolders.size());
                bookHolderArrayList.clear();
                bookHolderArrayList.addAll(bookHolders);
                bookAdapter = new BookListViewAdapter(getActivity(), bookHolderArrayList, favbookList, FavBookFragment.this);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onClick(int id) {

        switch (id) {
            case R.id.download:
//                fileDownload(preferences.getSelectedBookLink(getActivity()));
                break;
            case R.id.add_to_fav:
                addToFav();
                break;
            case R.id.book_name:
//                openWithoutDownload();
                break;
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
//                    getBooksData();

                }
            });
        }
    }


}
