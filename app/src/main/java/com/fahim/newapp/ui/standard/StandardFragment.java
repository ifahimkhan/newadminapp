package com.fahim.newapp.ui.standard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
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
import com.fahim.newapp.adapter.StandardAdapter;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.utils.Preferences;
import com.fahim.newapp.utils.QustomDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class StandardFragment extends Fragment implements AdapterClickListener, FragmentClickListener {

    private StandardViewModel standardViewModel;
    private RecyclerView recyclerView;
    private StandardAdapter standardAdapter;
    private ArrayList<StandardHolder> standardHolders = new ArrayList<StandardHolder>();
    private Preferences preferences=new Preferences();
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("TAG", "onAttach: ");
        ((MainActivity) getActivity()).setFragmentClickListener(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        standardViewModel =
                ViewModelProviders.of(this).get(StandardViewModel.class);
        standardViewModel.setContext(getActivity());
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        swipeRefreshLayout=root.findViewById(R.id.swipe_to_refresh);
        recyclerView = root.findViewById(R.id.recyclerview_standard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        standardAdapter = new StandardAdapter(getActivity(), standardHolders, this);

        recyclerView.setAdapter(standardAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                readStandardData(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        readStandardData(false);


        return root;
    }

    private void readStandardData(boolean callApi){

        standardViewModel.getStandardHolder(callApi).observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
            @Override
            public void onChanged(List<StandardHolder> standardHolders) {
                Log.e("onChanged", "getStandardHolder");
                standardAdapter = new StandardAdapter(getActivity(), standardHolders, StandardFragment.this);
                recyclerView.setAdapter(standardAdapter);
                standardAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onClick(int id) {

        switch (id) {

            case R.id.floatingbuttonstandard:
                Log.e("TAG", "onClick: floatingbuttonstandard ");
                displayCreateDialog();

                break;
            case R.id.floatingbuttonsubject:
                Log.e("TAG", "onClick: floatingbuttonsubject ");


                break;
            case R.id.floatingbuttonbooks:
                Log.e("TAG", "onClick: floatingbuttonbooks ");


                break;

            case R.id.delete_standard:

                standardViewModel.deleteStandard().observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
                    @Override
                    public void onChanged(List<StandardHolder> standardHolders) {
                        Log.e("onChanged", "delete_standard");
                        standardAdapter = new StandardAdapter(getActivity(), standardHolders, StandardFragment.this);
                        recyclerView.setAdapter(standardAdapter);
                        standardAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.edit_standard:
                displayEditDialog();
                /*standardViewModel.editStandard().observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
                    @Override
                    public void onChanged(List<StandardHolder> standardHolders) {
                        Log.e("onChanged", "editStandard");
                        standardAdapter = new StandardAdapter(getActivity(), standardHolders, StandardFragment.this);
                        recyclerView.setAdapter(standardAdapter);
                        standardAdapter.notifyDataSetChanged();
                    }
                });*/
                break;
            default:
                Log.e("TAG", "onClick: defautl ");
                break;

        }
    }



    public void displayEditDialog() {
        QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(getActivity());


        View alertLayout = getActivity().getLayoutInflater().inflate(R.layout.save_custom_dialog, null);
        qustomDialogBuilder.setCustomView(alertLayout, getActivity());
        qustomDialogBuilder.setTitle(getString(R.string.t_edit_standard));
        qustomDialogBuilder.setTitleColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setDividerColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setCancelable(true);
        final EditText editText=alertLayout.findViewById(R.id.enter_std_name_edittext);
        editText.setText(preferences.getSelectedStandardName(getActivity()));

        qustomDialogBuilder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        qustomDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!TextUtils.isEmpty(editText.getText().toString())){

                    preferences.putSelectedStandardName(getActivity(),editText.getText().toString());
                    editStandard();
                }

                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = qustomDialogBuilder.create();
        alertDialog.show();
    }
    public void displayCreateDialog() {
        QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(getActivity());


        View alertLayout = getActivity().getLayoutInflater().inflate(R.layout.save_custom_dialog, null);
        qustomDialogBuilder.setCustomView(alertLayout, getActivity());
        qustomDialogBuilder.setTitle(getString(R.string.t_create_standard));
        qustomDialogBuilder.setTitleColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setDividerColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setCancelable(true);
        final EditText editText=alertLayout.findViewById(R.id.enter_std_name_edittext);


        qustomDialogBuilder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        qustomDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!TextUtils.isEmpty(editText.getText().toString())){
                    preferences.putSelectedStandardName(getActivity(),editText.getText().toString());
                    createStandard();
                }

                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = qustomDialogBuilder.create();
        alertDialog.show();
    }

    private void editStandard(){
        standardViewModel.editStandard().observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
            @Override
            public void onChanged(List<StandardHolder> standardHolders) {
                Log.e("onChanged", "editStandard");
                standardAdapter = new StandardAdapter(getActivity(), standardHolders, StandardFragment.this);
                recyclerView.setAdapter(standardAdapter);
                standardAdapter.notifyDataSetChanged();
            }
        });
    }
    private void createStandard(){
        standardViewModel.createStandard().observe(getViewLifecycleOwner(), new Observer<List<StandardHolder>>() {
            @Override
            public void onChanged(List<StandardHolder> standardHolders) {
                Log.e("onChanged", "createStandard");
                standardAdapter = new StandardAdapter(getActivity(), standardHolders, StandardFragment.this);
                recyclerView.setAdapter(standardAdapter);
                standardAdapter.notifyDataSetChanged();
            }
        });
    }

}

