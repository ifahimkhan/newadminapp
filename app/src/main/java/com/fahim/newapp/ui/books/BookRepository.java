package com.fahim.newapp.ui.books;

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

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BookRepository {

    private APICall apiCall;
    private Preferences preferences = new Preferences();
    private SweetAlertDialog loadingDialog;
    private Context context;

    BookRepository(Context context) {
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

                    loadingDialog
                            .setTitleText(context.getString(R.string.w_success))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                        DAO.getInstance().deleteAllStandardRows();
                        DAO.getInstance().insertStandardList(response.body().getResponse());
                        data.setValue(response.body().getResponse());
                    } else {
                        data.setValue(DAO.getInstance().getAllStandard());
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.t_no_std_found))
                                .setContentText(response.body().getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();


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

    public LiveData<List<SubjectHolder>> callReadSubject(boolean callapi) {

        final MutableLiveData<List<SubjectHolder>> data = new MutableLiveData<>();

        if (callapi) {
            loadingDialog.show();
            apiCall.callReadSubject(APINames.READ_SUBJECT_API).enqueue(new Callback<ResponseSubjectHolder>() {
                @Override
                public void onResponse(Call<ResponseSubjectHolder> call, Response<ResponseSubjectHolder> response) {

                    loadingDialog
                            .setTitleText(context.getString(R.string.w_success))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                        DAO.getInstance().deleteAllSubjectRows();
                        DAO.getInstance().insertSubjectList(response.body().getResponse());

                        data.setValue(DAO.getInstance().getAllSubjectWithId(preferences.getSelectedStandardId(context)));

                    } else {
                        data.setValue(null);
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.t_no_sub_found))
                                .setContentText(response.body().getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();


                    }
                }

                @Override
                public void onFailure(Call<ResponseSubjectHolder> call, Throwable t) {
                    loadingDialog
                            .setTitleText(context.getString(R.string.failed))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Log.e(TAG, "onFailure: " + call.request().url() + call.request().body());
                    data.setValue(null);


                }
            });
        } else {
            data.setValue(DAO.getInstance().getAllSubjectWithId(preferences.getSelectedStandardId(context)));
        }
        return data;
    }

    public LiveData<List<SubjectHolder>> callAllReadSubject(boolean callapi) {

        final MutableLiveData<List<SubjectHolder>> data = new MutableLiveData<>();

        if (callapi) {
            loadingDialog.show();
            apiCall.callReadSubject(APINames.READ_SUBJECT_API).enqueue(new Callback<ResponseSubjectHolder>() {
                @Override
                public void onResponse(Call<ResponseSubjectHolder> call, Response<ResponseSubjectHolder> response) {

                    loadingDialog
                            .setTitleText(context.getString(R.string.w_success))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                        DAO.getInstance().deleteAllSubjectRows();
                        DAO.getInstance().insertSubjectList(response.body().getResponse());

                        data.setValue(DAO.getInstance().getAllSubject());

                    } else {
                        data.setValue(DAO.getInstance().getAllSubject());
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.t_no_sub_found))
                                .setContentText(response.body().getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();


                    }
                }

                @Override
                public void onFailure(Call<ResponseSubjectHolder> call, Throwable t) {
                    loadingDialog
                            .setTitleText(context.getString(R.string.failed))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Log.e(TAG, "onFailure: " + call.request().url() + call.request().body());
                    data.setValue(DAO.getInstance().getAllSubject());


                }
            });
        } else {
            data.setValue(DAO.getInstance().getAllSubject());
        }
        return data;
    }

    public LiveData<List<BookHolder>> callReadBook(boolean callapi) {

        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();

        if (callapi) {
            loadingDialog.show();
            apiCall.callReadBook(APINames.READ_BOOK_API).enqueue(new Callback<ResponseBookHolder>() {
                @Override
                public void onResponse(Call<ResponseBookHolder> call, Response<ResponseBookHolder> response) {

                    loadingDialog
                            .setTitleText(context.getString(R.string.w_success))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
//                        Log.e(TAG, "onResponse: "+call.request().url() );
//                        Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()) );
                        DAO.getInstance().deleteAllBooksRows();
                        DAO.getInstance().insertBookList(response.body().getResponse());

                        data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));

//                        data.setValue(response.body().getResponse());
                    } else {
                        data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.t_no_book_found))
                                .setContentText(response.body().getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();


                    }
                }

                @Override
                public void onFailure(Call<ResponseBookHolder> call, Throwable t) {
                    loadingDialog
                            .setTitleText(context.getString(R.string.failed))
                            .setConfirmText(context.getString(R.string.w_ok))
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Log.e(TAG, "onFailure: " + call.request().url() + call.request().body());
                    data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));


                }
            });
        } else {
            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));
        }
        return data;
    }


    public LiveData<List<BookHolder>> callDeleteBook() {
        loadingDialog.show();
        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();
        apiCall.callDeleteBook(APINames.Delete_BOOK_API, preferences.getSelectedBookId(context))
                .enqueue(new Callback<ResponseSubjectHolder>() {
                    @Override
                    public void onResponse(Call<ResponseSubjectHolder> call, Response<ResponseSubjectHolder> response) {
                        if (response.body().isStatus()) {

                            loadingDialog
                                    .setTitleText(context.getString(R.string.w_success))
                                    .setContentText(response.body().getMessage())
                                    .setConfirmText(context.getString(R.string.w_ok))
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            DAO.getInstance().deleteBookIDRows(preferences.getSelectedBookId(context));
                            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context),preferences.getSelectedSubjectId(context)));


                        } else {
                            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context),preferences.getSelectedSubjectId(context)));
                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(context.getString(R.string.t_no_book_found))
                                    .setContentText(response.body().getMessage())
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseSubjectHolder> call, Throwable t) {
                        loadingDialog
                                .setTitleText(context.getString(R.string.failed))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context),preferences.getSelectedSubjectId(context)));


                    }
                });

        return data;
    }


    public LiveData<List<BookHolder>> callEditBook() {
        loadingDialog.show();
        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();
        BookHolder mBookHolder = new BookHolder();
        mBookHolder.setBooklink(preferences.getSelectedBookLink(context));
        mBookHolder.setBookname(preferences.getSelectedBookName(context));
        mBookHolder.setId(preferences.getSelectedBookId(context));

        apiCall.callEditBook(APINames.UPDATE_BOOK_API, mBookHolder)
                .enqueue(new Callback<ResponseBookHolder>() {
                    @Override
                    public void onResponse(Call<ResponseBookHolder> call, Response<ResponseBookHolder> response) {

                        loadingDialog
                                .setTitleText(context.getString(R.string.w_success))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {

                            DAO.getInstance().deleteAllBooksRows();
                            DAO.getInstance().insertBookList(response.body().getResponse());

                            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));


                        } else {
                            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));

                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(context.getString(R.string.t_no_sub_found))
                                    .setContentText(response.body().getMessage())
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBookHolder> call, Throwable t) {
                        loadingDialog
                                .setTitleText(context.getString(R.string.failed))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context), preferences.getSelectedSubjectId(context)));


                    }
                });

        return data;
    }

    public LiveData<List<BookHolder>> callCreateBook(BookHolder bookHolder) {
        loadingDialog.show();
        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();
        apiCall.callCreateBook(APINames.CREATE_BOOK_API,bookHolder)
                .enqueue(new Callback<ResponseBookHolder>() {
                    @Override
                    public void onResponse(Call<ResponseBookHolder> call, Response<ResponseBookHolder> response) {

                        loadingDialog
                                .setTitleText(context.getString(R.string.w_success))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        if (response.body().isStatus() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {

                            DAO.getInstance().deleteAllBooksRows();
                            DAO.getInstance().insertBookList(response.body().getResponse());
                            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context),preferences.getSelectedSubjectId(context)));


                        } else {
                            data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context),preferences.getSelectedSubjectId(context)));
                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(context.getString(R.string.t_no_std_found))
                                    .setContentText(response.body().getMessage())
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBookHolder> call, Throwable t) {
                        loadingDialog
                                .setTitleText(context.getString(R.string.failed))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        data.setValue(DAO.getInstance().getAllBooks(preferences.getSelectedStandardId(context),preferences.getSelectedSubjectId(context)));


                    }
                });

        return data;
    }


}
