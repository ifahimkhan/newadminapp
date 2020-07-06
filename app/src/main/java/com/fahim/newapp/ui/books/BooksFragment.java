package com.fahim.newapp.ui.books;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.fahim.newapp.AdminActivity;
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.BookAdapter;
import com.fahim.newapp.adapter.CustomSpinnerStandardAdapter;
import com.fahim.newapp.adapter.CustomSpinnerSubjectAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.utils.Preferences;
import com.fahim.newapp.utils.QustomDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class BooksFragment extends Fragment implements FragmentClickListener, AdapterClickListener {

    private BooksViewModel booksViewModel;
    private Preferences preferences = new Preferences();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Spinner spinnerstd, spinnersub;
    private CustomSpinnerStandardAdapter standardAdapter;
    private CustomSpinnerSubjectAdapter subjectAdapter;
    //    private ViewGroup container;
    private BookAdapter bookAdapter;
    private ArrayList<StandardHolder> spinnerList = new ArrayList<>();
    private ArrayList<SubjectHolder> subSpinnerList = new ArrayList<>();
    private List<BookHolder> subjectHolderList = new ArrayList<BookHolder>();
    ClipboardManager manager;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("TAG", "onAttach: ");
        ((AdminActivity) getActivity()).setFragmentClickListener(this);
        manager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
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
                Log.e("onChanged", "getsubjectmodel" + holders.size());
                bookAdapter = new BookAdapter(getActivity(), holders, BooksFragment.this);
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
                displayEditDialog();

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
                        booksViewModel.deleteBook().observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
                            @Override
                            public void onChanged(List<BookHolder> holders) {
                                Log.e("onChanged", "delete_book");
                                bookAdapter = new BookAdapter(getActivity(), holders, BooksFragment.this);
                                recyclerView.setAdapter(bookAdapter);
                                bookAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                dialog.show();
                Log.e("TAG", "onClick: delete_subject ");

                break;
            case R.id.floatingbuttonbooks:
                displayCreateDialog();
                Log.e("TAG", "onClick: floatingbuttonsubject ");


                break;
            default:
                Log.e(TAG, "onClick: " + "default");
                break;
        }

    }

    private void displayEditDialog() {
        QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(getActivity());


        View alertLayout = getActivity().getLayoutInflater().inflate(R.layout.book_save_custom_dialog, null);
        qustomDialogBuilder.setCustomView(alertLayout, getActivity());
        qustomDialogBuilder.setTitle(getString(R.string.t_edit_subject));
        qustomDialogBuilder.setTitleColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setDividerColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setCancelable(true);
        final EditText editTextname = alertLayout.findViewById(R.id.enter_book_name_edittext);
        final EditText editTextlink = alertLayout.findViewById(R.id.enter_book_link_edittext);
        final ImageView paste = alertLayout.findViewById(R.id.paste);

        editTextname.setText(preferences.getSelectedBookName(getActivity()));
        editTextlink.setText(preferences.getSelectedBookLink(getActivity()));

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData pasteData = manager.getPrimaryClip();
                ClipData.Item item = pasteData.getItemAt(0);
                String paste = item.getText().toString();
                editTextlink.setText(paste);
            }
        });

        qustomDialogBuilder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        qustomDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!TextUtils.isEmpty(editTextlink.getText().toString()) && !TextUtils.isEmpty(editTextname.getText().toString())) {

                    preferences.putSelectedBookLink(getActivity(), editTextlink.getText().toString());
                    preferences.putSelectedBookName(getActivity(), editTextname.getText().toString());
                    editBook();
                }
                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = qustomDialogBuilder.create();
        alertDialog.show();
    }

    private void editBook() {

        booksViewModel.editBook().observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> holders) {
                Log.e("onChanged", "editStandard");
                bookAdapter = new BookAdapter(getActivity(), holders, BooksFragment.this);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
            }
        });


    }

    public void displayCreateDialog() {
        QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(getActivity());


        View alertLayout = getActivity().getLayoutInflater().inflate(R.layout.book_save_custom_dialog, null);
        qustomDialogBuilder.setCustomView(alertLayout, getActivity());
        qustomDialogBuilder.setTitle(getString(R.string.t_create_standard));
        qustomDialogBuilder.setTitleColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setDividerColor(getResources().getColor(R.color.colorPrimary));
        qustomDialogBuilder.setCancelable(true);
        final EditText editTextname = alertLayout.findViewById(R.id.enter_book_name_edittext);
        final EditText editTextlink = alertLayout.findViewById(R.id.enter_book_link_edittext);
        final ImageView paste = alertLayout.findViewById(R.id.paste);
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData pasteData = manager.getPrimaryClip();
                ClipData.Item item = pasteData.getItemAt(0);
                String paste = item.getText().toString();
                editTextlink.setText(paste);
            }
        });

        qustomDialogBuilder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        qustomDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!TextUtils.isEmpty(editTextname.getText().toString()) &&
                        !TextUtils.isEmpty(editTextlink.getText().toString())
                ) {


                    BookHolder bookHolder=new BookHolder();
                    bookHolder.setBookname(editTextname.getText().toString());
                    bookHolder.setBooklink(editTextlink.getText().toString());
                    bookHolder.setStandardid(preferences.getSelectedStandardId(getActivity()));
                    bookHolder.setSubjectid(preferences.getSelectedSubjectId(getActivity()));

                    addBook(bookHolder);
                }

                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = qustomDialogBuilder.create();
        alertDialog.show();
    }

    private void addBook(BookHolder bookHolder) {
        booksViewModel.createBook(bookHolder).observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> holder) {
                Log.e("onChanged", "createStandard");
                bookAdapter = new BookAdapter(getActivity(), holder, BooksFragment.this);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
            }
        });
    }

}
