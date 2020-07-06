package com.fahim.newapp.ui.subject;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fahim.newapp.R;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.holder.ResponseStandardHolder;
import com.fahim.newapp.holder.ResponseSubjectHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.network.APICall;
import com.fahim.newapp.network.APINames;
import com.fahim.newapp.network.RetrofitConfig;
import com.fahim.newapp.utils.Preferences;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SubjectRepository {

    private APICall apiCall;
    private Preferences preferences = new Preferences();
    private SweetAlertDialog loadingDialog;
    private Context context;

    SubjectRepository(Context context) {
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
                        data.setValue(null);
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
                    data.setValue(null);


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

    public LiveData<List<SubjectHolder>> callDeleteSubject() {
        loadingDialog.show();
        final MutableLiveData<List<SubjectHolder>> data = new MutableLiveData<>();
        apiCall.callDeleteSubject(APINames.Delete_SUBJECT_API, preferences.getSelectedSubjectId(context))
                .enqueue(new Callback<ResponseSubjectHolder>() {
                    @Override
                    public void onResponse(Call<ResponseSubjectHolder> call, Response<ResponseSubjectHolder> response) {

                        loadingDialog
                                .setTitleText(context.getString(R.string.w_success))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                        if (response.body().isStatus()) {

                            DAO.getInstance().deleteSubjectIDRows(preferences.getSelectedSubjectId(context));
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
                        data.setValue(null);


                    }
                });

        return data;
    }

    public LiveData<List<SubjectHolder>> callEditStandard() {
        loadingDialog.show();
        final MutableLiveData<List<SubjectHolder>> data = new MutableLiveData<>();
        apiCall.callEditSubject(APINames.UPDATE_SUBJECT_API, preferences.getSelectedSubjectName(context), preferences.getSelectedSubjectId(context))
                .enqueue(new Callback<ResponseSubjectHolder>() {
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
                            data.setValue(DAO.getInstance().getAllSubjectWithId(preferences.getSelectedStandardId(context)));

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
                        data.setValue(DAO.getInstance().getAllSubjectWithId(preferences.getSelectedStandardId(context)));


                    }
                });

        return data;
    }

    public LiveData<List<SubjectHolder>> callCreateSubject() {
        loadingDialog.show();
        final MutableLiveData<List<SubjectHolder>> data = new MutableLiveData<>();
        apiCall.callCreateSubject(APINames.CREATE_SUBJECT_API,
                preferences.getSelectedSubjectName(context), preferences.getSelectedStandardId(context))
                .enqueue(new Callback<ResponseSubjectHolder>() {
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
                            data.setValue(DAO.getInstance().getAllSubjectWithId(preferences.getSelectedStandardId(context)));
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
                    public void onFailure(Call<ResponseSubjectHolder> call, Throwable t) {
                        loadingDialog
                                .setTitleText(context.getString(R.string.failed))
                                .setConfirmText(context.getString(R.string.w_ok))
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        data.setValue(DAO.getInstance().getAllSubjectWithId(preferences.getSelectedStandardId(context)));


                    }
                });

        return data;
    }


}
