package com.fahim.newapp.ui.search;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fahim.newapp.AdminActivity;
import com.fahim.newapp.HomeActivity;
import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.Interface.FragmentClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.BookAdapter;
import com.fahim.newapp.adapter.BookListViewAdapter;
import com.fahim.newapp.adapter.CustomSpinnerStandardAdapter;
import com.fahim.newapp.adapter.CustomSpinnerSubjectAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.ui.books.BooksViewModel;
import com.fahim.newapp.ui.front.BookListViewFragment;
import com.fahim.newapp.utils.Preferences;
import com.fahim.newapp.utils.QustomDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SearchFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private Preferences preferences = new Preferences();
    private ListView listView;
    private TextInputLayout textInputLayout;
    private SearchViewModel searchViewModel;
    private ArrayList<String> searchList = new ArrayList<>();
    private ArrayList<String> holderList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private BookListViewAdapter bookListViewAdapter;
    private List<BookHolder> bookHolderList = new ArrayList<>();
    private List<Integer> favBookList = new ArrayList<>();


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        searchViewModel.setContext(getActivity());


        listView = root.findViewById(R.id.search_list);
        textInputLayout = root.findViewById(R.id.textField);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchViewModel.getsearchList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                searchList.clear();
                holderList.clear();
                searchList.addAll(strings);
                holderList.addAll(strings);
                Log.e(TAG, "searchList: " + searchList.size());
                arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_items, R.id.expandedListItem, holderList);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        });
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getListValues(s.toString());
            }
        });
        textInputLayout.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e(TAG, "onClick: " + textInputLayout.getEditText().getText().toString());
                    callNextFragment(textInputLayout.getEditText().getText().toString());
                    return true;
                }
                return false;
            }
        });
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "search icon: " + textInputLayout.getEditText().getText().toString());
                callNextFragment(textInputLayout.getEditText().getText().toString());

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                callNextFragment(holderList.get(position));
            }
        });


    }


    @Override
    public void onClick(int id) {


    }

    private void getListValues(String text) {
        holderList.clear();
        for (String s : searchList) {
            if (s.toLowerCase().contains(text.toLowerCase()))
                holderList.add(s);
        }
        Log.e(TAG, "getListValues: " + holderList.size());

        arrayAdapter.notifyDataSetChanged();
    }

    private void callNextFragment(String text) {
        if (!TextUtils.isEmpty(text)) {
            preferences.putSearchBook(getActivity(), text);
            ((HomeActivity) getActivity()).selectItem(R.id.search_for);

        }

    }


}
