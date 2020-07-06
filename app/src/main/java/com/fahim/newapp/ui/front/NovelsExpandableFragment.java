package com.fahim.newapp.ui.front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fahim.newapp.R;
import com.fahim.newapp.adapter.CustomExpandableListAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.HomeActivity;
import com.fahim.newapp.ui.books.BooksViewModel;
import com.fahim.newapp.utils.Preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.fahim.newapp.database.DataBaseInterface.NOVELS;

public class NovelsExpandableFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    Preferences preferences = new Preferences();

    List<String> expandableListTitle = new ArrayList<>();
    List<SubjectHolder> expandableListSubject = new ArrayList<>();
    HashMap<String, List<SubjectHolder>> expandableListDetail = new HashMap<>();
    BooksViewModel booksViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    SwipeRefreshLayout.OnRefreshListener refreshListener;

    public NovelsExpandableFragment() {
        // Required empty public constructor
    }

    public static NovelsExpandableFragment newInstance() {
        return new NovelsExpandableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        booksViewModel =
                ViewModelProviders.of(this).get(BooksViewModel.class);
        View view = inflater.inflate(R.layout.books_expandable_layout, container, false);
        booksViewModel.setContext(getActivity());
        expandableListView = view.findViewById(R.id.expandableListView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh_book);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getData(false);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition).getSubject_name(), Toast.LENGTH_SHORT
                ).show();
                SubjectHolder holder = expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(childPosition);

                preferences.putSelectedStandardId(getActivity(), holder.getStandard_id());
                preferences.putSelectedSubjectId(getActivity(), holder.getId());
                ((HomeActivity) getActivity()).selectItem(1);

                return false;
            }
        });


    }

    private void getData(final boolean b) {

        booksViewModel.getAllSubjectHolder(b).observe(getViewLifecycleOwner(), new Observer<List<SubjectHolder>>() {
            @Override
            public void onChanged(List<SubjectHolder> subjectHolders) {
                if (subjectHolders == null || subjectHolders.size() == 0) {
                    if (b == false) {
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                // directly call onRefresh() method
                                refreshListener.onRefresh();
                            }
                        });
                    }
                } else {
                    expandableListSubject.clear();
                    expandableListSubject.addAll(subjectHolders);

                    booksViewModel.getStandardHolder(b).observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
                        @Override
                        public void onChanged(List<StandardHolder> standardHolders) {
                            expandableListTitle.clear();
                            expandableListDetail.clear();


                            for (StandardHolder standardHolder : standardHolders) {
                                List<SubjectHolder> subjectHolders = new ArrayList<>();

                                for (SubjectHolder holders : expandableListSubject) {
                                    if (holders.getStandard_id() == standardHolder.getId())
//                                    expandableListTitle.add(standardHolder.getStandardName());
                                        subjectHolders.add(holders);
                                }
                                if (standardHolder.getStandardName().toLowerCase().contains(NOVELS.toLowerCase())) {
                                    expandableListTitle.add(standardHolder.getStandardName());
                                    expandableListDetail.put(standardHolder.getStandardName(), subjectHolders);
                                }
                            }
                            expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
                            expandableListView.setAdapter(expandableListAdapter);
                        }
                    });
                }


            }
        });

    }

    private void getAllBooks(boolean callApi) {
        booksViewModel.getBookHolder(callApi).observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> bookHolders) {

            }
        });

    }
}
