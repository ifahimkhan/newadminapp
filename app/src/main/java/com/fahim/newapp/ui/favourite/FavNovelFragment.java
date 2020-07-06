package com.fahim.newapp.ui.favourite;

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

public class FavNovelFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private PageViewModel pageViewModel;
    private Preferences preferences = new Preferences();
    private RecyclerView recyclerView;
    private BookListViewAdapter bookAdapter;
    private ArrayList<BookHolder> bookHolderArrayList = new ArrayList<>();
    private List<Integer> favbookList = new ArrayList<>();

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
            case R.id.edit_book:
                Log.e("TAG", "onClick: edit_subject ");

                break;
            case R.id.delete_book:
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText(getString(R.string.t_are_you_sure));
                dialog.setContentText(getString(R.string.s_wont_be_able_to_recover));
                dialog.setCancelText(getString(R.string.dialog_cancel));
                dialog.showCancelButton(true);
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();


                    }
                });
                dialog.show();
                Log.e("TAG", "onClick: delete_subject ");

                break;
            case R.id.floatingbuttonbooks:
                Log.e("TAG", "onClick: floatingbuttonsubject ");


                break;
            default:
                Log.e(TAG, "onClick: " + "default");
                break;
        }

    }


}
