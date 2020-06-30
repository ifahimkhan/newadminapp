package com.fahim.newapp.ui.subject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.fahim.newapp.adapter.CustomSpinnerStandardAdapter;
import com.fahim.newapp.adapter.StandardAdapter;
import com.fahim.newapp.adapter.SubjectAdapter;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SubjectFragment extends Fragment implements AdapterClickListener, FragmentClickListener {

    private Preferences preferences = new Preferences();
    private SubjectViewModel subjectViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private CustomSpinnerStandardAdapter standardAdapter;
//    private ViewGroup container;
    private SubjectAdapter subjectAdapter;
    private Thread thread;
    private ArrayList<StandardHolder> spinnerList = new ArrayList<>();
    private List<SubjectHolder> subjectHolderList = new ArrayList<SubjectHolder>();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("TAG", "onAttach: ");
        ((MainActivity) getActivity()).setFragmentClickListener(this);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subjectViewModel =
                ViewModelProviders.of(this).get(SubjectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subject, container, false);
        subjectViewModel.setContext(getActivity());

        Log.e(TAG, "onCreateView: Subject");


        swipeRefreshLayout = root.findViewById(R.id.swipe_to_refresh_subject);
        spinner = root.findViewById(R.id.standard_spinner);
        subjectAdapter = new SubjectAdapter(getActivity(), subjectHolderList, this);

        recyclerView = root.findViewById(R.id.recyclerview_subject);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(subjectAdapter);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: Subject" + Thread.currentThread().getName());



        subjectViewModel.getStandardHolder(false).observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
            @Override
            public void onChanged(List<StandardHolder> list) {
                spinnerList.addAll(list);
                standardAdapter = new CustomSpinnerStandardAdapter(getActivity(), R.layout.custom_spinner_layout, spinnerList);
                spinner.setAdapter(standardAdapter);
                standardAdapter.notifyDataSetChanged();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences.putSelectedStandardId(getActivity(), spinnerList.get(position).getId());
                Log.e(TAG, "onItemSelected: " + preferences.getSelectedStandardId(getActivity()));
                readSubjectData(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readSubjectData(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void readSubjectData(boolean callApi) {
        subjectViewModel.getSubjectHolder(callApi).observe(getViewLifecycleOwner(), new Observer<List<SubjectHolder>>() {
            @Override
            public void onChanged(List<SubjectHolder> subjectHolders) {
                Log.e("onChanged", "getsubjectmodel");
                subjectAdapter = new SubjectAdapter(getActivity(), subjectHolders, SubjectFragment.this);
                recyclerView.setAdapter(subjectAdapter);
                subjectAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(int id) {

        switch (id){
            case R.id.edit_subject:
                Log.e("TAG", "onClick: edit_subject ");

                break;
            case R.id.delete_subject:
                Log.e("TAG", "onClick: delete_subject ");

                break;
            case R.id.floatingbuttonsubject:
                Log.e("TAG", "onClick: floatingbuttonsubject ");


                break;
            default:
                Log.e(TAG, "onClick: "+"default" );
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
