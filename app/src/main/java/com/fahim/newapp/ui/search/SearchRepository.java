package com.fahim.newapp.ui.search;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fahim.newapp.R;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.ResponseBookHolder;
import com.fahim.newapp.holder.ResponseStandardHolder;
import com.fahim.newapp.holder.ResponseSubjectHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.network.APICall;
import com.fahim.newapp.network.APINames;
import com.fahim.newapp.network.RetrofitConfig;
import com.fahim.newapp.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SearchRepository {

    private APICall apiCall;
    private Preferences preferences = new Preferences();
    private SweetAlertDialog loadingDialog;
    private Context context;

    SearchRepository(Context context) {
        this.context = context;
        apiCall = RetrofitConfig.getClient(this.context).create(APICall.class);

    }


    public void setLoadingDialog() {
        loadingDialog = new SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(ContextCompat.getColor(this.context, R.color.colorAccent));
        loadingDialog.setTitleText(context.getString(R.string.t_loading));
    }

    public LiveData<List<StandardHolder>> callReadStandard(boolean callapi) {

        final MutableLiveData<List<StandardHolder>> data = new MutableLiveData<>();

        if (callapi) {
            loadingDialog.show();
            apiCall.callReadStandard(APINames.READ_STANDARD_API).enqueue(new Callback<ResponseStandardHolder>() {
                @Override
                public void onResponse(Call<ResponseStandardHolder> call, Response<ResponseStandardHolder> response) {


                    if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                        DAO.getInstance().deleteAllStandardRows();
                        DAO.getInstance().insertStandardList(response.body().getResponse());
                        data.setValue(response.body().getResponse());
                        loadingDialog
                                .setTitleText(context.getString(R.string.w_success))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    } else {
                        data.setValue(DAO.getInstance().getAllStandard());
                        loadingDialog.setTitleText(context.getString(R.string.t_no_std_found))
                                .setContentText(response.body().getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.WARNING_TYPE);


                    }
                }

                @Override
                public void onFailure(Call<ResponseStandardHolder> call, Throwable t) {
                    loadingDialog
                            .setTitleText(context.getString(R.string.failed))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    data.setValue(DAO.getInstance().getAllStandard());


                }
            });
        } else {
            data.setValue(DAO.getInstance().getAllStandard());
        }
        return data;
    }


    public LiveData<List<BookHolder>> getSearchListValues(String text) {
        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();
        data.setValue(DAO.getInstance().getSearchListByText(text));
        return data;
    }

    public LiveData<ArrayList<String>> getsearchList() {
        final MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
        data.setValue(DAO.getInstance().getSearchListHolder());
        return data;
    }

    public LiveData<List<BookHolder>> callSearchData(String text) {
        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();


        loadingDialog.show();
        apiCall.callReadBookSearch(APINames.SEARCH_BOOK_API, text).enqueue(new Callback<ResponseBookHolder>() {
            @Override
            public void onResponse(Call<ResponseBookHolder> call, Response<ResponseBookHolder> response) {


                if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                    loadingDialog
                            .setTitleText(context.getString(R.string.w_success))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    data.setValue(response.body().getResponse());

                } else {

                    data.setValue(null);
                    loadingDialog
                            .setTitleText(context.getString(R.string.t_no_book_found))
                            .setContentText(response.body().getMessage())
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            }).changeAlertType(SweetAlertDialog.WARNING_TYPE);


                }
            }

            @Override
            public void onFailure(Call<ResponseBookHolder> call, Throwable t) {
                loadingDialog
                        .setTitleText(context.getString(R.string.failed))
                        .setConfirmText(context.getString(R.string.w_ok))
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                data.setValue(null);

            }
        });

        return data;
    }

    public LiveData<List<Integer>> callFavBookList() {
        final MutableLiveData<List<Integer>> data = new MutableLiveData<>();
        data.setValue(DAO.getInstance().getAllFavouriteBooksId());
        return data;
    }

    public void deleteFavBook() {
        DAO.getInstance().deleteFavouriteBook(preferences.getSelectedBookId(context));
    }
    public void addFavBook() {
        DAO.getInstance().addToFavouriteBooks(preferences.getSelectedBookId(context));
    }
}
