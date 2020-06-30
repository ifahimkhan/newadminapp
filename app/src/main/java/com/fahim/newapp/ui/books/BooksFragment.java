package com.fahim.newapp.ui.books;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.Interface.FragmentClickListener;
import com.fahim.newapp.MainActivity;
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.BookAdapter;
import com.fahim.newapp.adapter.CustomSpinnerStandardAdapter;
import com.fahim.newapp.adapter.CustomSpinnerSubjectAdapter;
import com.fahim.newapp.adapter.SubjectAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.ui.subject.SubjectFragment;
import com.fahim.newapp.ui.subject.SubjectViewModel;
import com.fahim.newapp.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BooksFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private BooksViewModel booksViewModel;
    private Preferences preferences = new Preferences();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Spinner spinnerstd,spinnersub;
    private CustomSpinnerStandardAdapter standardAdapter;
    private CustomSpinnerSubjectAdapter subjectAdapter;
    //    private ViewGroup container;
    private BookAdapter bookAdapter;
    private ArrayList<StandardHolder> spinnerList = new ArrayList<>();
    private ArrayList<SubjectHolder> subSpinnerList = new ArrayList<>();
    private List<BookHolder> subjectHolderList = new ArrayList<BookHolder>();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("TAG", "onAttach: ");
        ((MainActivity) getActivity()).setFragmentClickListener(this);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        booksViewModel =
                ViewModelProviders.of(this).get(BooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_book, container, false);
        booksViewModel.setContext(getActivity());

        swipeRefreshLayout = root.findViewById(R.id.swipe_to_refresh_book);
        spinnerstd = root.findViewById(R.id.standard_spinner_b);
        spinnersub = root.findViewById(R.id.subject_spinner_b);
        bookAdapter = new BookAdapter(getActivity(), subjectHolderList, this);

        recyclerView = root.findViewById(R.id.recyclerview_book);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(bookAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        booksViewModel.getStandardHolder(false).observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
            @Override
            public void onChanged(List<StandardHolder> list) {
                spinnerList.clear();
                spinnerList.addAll(list);
                standardAdapter = new CustomSpinnerStandardAdapter(getActivity(), R.layout.custom_spinner_layout, spinnerList);
                spinnerstd.setAdapter(standardAdapter);
                standardAdapter.notifyDataSetChanged();


            }
        });
        spinnerstd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences.putSelectedStandardId(getActivity(), spinnerList.get(position).getId());
                Log.e(TAG, "onItemSelected: " + preferences.getSelectedStandardId(getActivity()));

                booksViewModel.getSubjectHolder(false).observe(getViewLifecycleOwner(), new Observer<List<SubjectHolder>>() {
                    @Override
                    public void onChanged(List<SubjectHolder> holders) {
                        subSpinnerList.clear();
                        subSpinnerList.addAll(holders);
                        subjectAdapter = new CustomSpinnerSubjectAdapter(getActivity(), R.layout.custom_spinner_layout, subSpinnerList);
                        spinnersub.setAdapter(subjectAdapter);
                        subjectAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnersub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences.putSelectedSubjectId(getActivity(), subSpinnerList.get(position).getId());
                Log.e(TAG, "onItemSelected: " + preferences.getSelectedSubjectId(getActivity()));

                readBookData(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readBookData(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });




    }

    private void readBookData(boolean callApi) {
        booksViewModel.getBookHolder(callApi).observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> holders) {
                Log.e("onChanged", "getsubjectmodel");
                bookAdapter = new BookAdapter(getActivity(), holders, BooksFragment.this);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(int id) {

    }
}
