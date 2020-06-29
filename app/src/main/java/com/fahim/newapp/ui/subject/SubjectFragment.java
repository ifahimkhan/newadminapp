package com.fahim.newapp.ui.subject;

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
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.CustomSpinnerStandardAdapter;
import com.fahim.newapp.adapter.SubjectAdapter;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SubjectFragment extends Fragment implements AdapterClickListener, FragmentClickListener {

    private Preferences preferences=new Preferences();
    private SubjectViewModel subjectViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private CustomSpinnerStandardAdapter standardAdapter;
    private ViewGroup container;
    private SubjectAdapter subjectAdapter;
    private Thread thread;
    private ArrayList<StandardHolder> spinnerList=new ArrayList<>();
    private List<StandardHolder> standardHolderList=new ArrayList<StandardHolder>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subjectViewModel =
                ViewModelProviders.of(this).get(SubjectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subject, container, false);

        this.container=container;
        subjectViewModel.setContext(getActivity());



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout=container.findViewById(R.id.swipe_to_refresh_subject);
        spinner=container.findViewById(R.id.standard_spinner);
        recyclerView=container.findViewById(R.id.recyclerview_subject);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subjectAdapter=new SubjectAdapter(getActivity(),new ArrayList<SubjectHolder>(),this);
        recyclerView.setAdapter(subjectAdapter);


        standardAdapter = new CustomSpinnerStandardAdapter(getActivity(), R.layout.custom_spinner_layout, spinnerList);
        spinner.setAdapter(standardAdapter);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences.putSelectedStandardId(getActivity(),spinnerList.get(position).getId());
                Log.e(TAG, "onItemSelected: "+preferences.getSelectedStandardId(getActivity()) );
                readSubjectData(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        thread=new Thread(new Runnable() {
            @Override
            public void run() {

                spinnerList.addAll(DAO.getInstance().getAllStandard());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        standardAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();


    }

    private void readSubjectData(boolean callApi){
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!thread.isInterrupted()){
            thread.interrupt();
        }
    }
}
