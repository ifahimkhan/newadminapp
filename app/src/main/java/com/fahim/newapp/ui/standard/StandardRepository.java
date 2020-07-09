package com.fahim.newapp.ui.standard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fahim.newapp.R;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.holder.ResponseStandardHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.network.APICall;
import com.fahim.newapp.network.APINames;
import com.fahim.newapp.network.RetrofitConfig;
import com.fahim.newapp.utils.Preferences;
import com.fahim.newapp.utils.QustomDialogBuilder;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StandardRepository {

    private APICall apiCall;
    private Preferences preferences = new Preferences();
    private SweetAlertDialog loadingDialog;
    private Context context;

    StandardRepository(Context context) {
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
                        loadingDialog
                                .setTitleText(context.getString(R.string.t_no_std_found))
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
                    data.setValue(null);


                }
            });
        } else {
            data.setValue(DAO.getInstance().getAllStandard());
        }
        return data;
    }

    public LiveData<List<StandardHolder>> callDeleteStandard() {
        loadingDialog.show();
        final MutableLiveData<List<StandardHolder>> data = new MutableLiveData<>();
        apiCall.callDeleteStandard(APINames.Delete_STANDARD_API, preferences.getSelectedStandardId(context)).enqueue(new Callback<ResponseStandardHolder>() {
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
                    loadingDialog
                            .setTitleText(context.getString(R.string.t_no_std_found))
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
                data.setValue(null);


            }
        });

        return data;
    }

    public LiveData<List<StandardHolder>> callEditStandard() {
        loadingDialog.show();
        final MutableLiveData<List<StandardHolder>> data = new MutableLiveData<>();
        apiCall.callEditStandard(APINames.UPDATE_STANDARD_API, preferences.getSelectedStandardId(context), preferences.getSelectedStandardName(context)).enqueue(new Callback<ResponseStandardHolder>() {
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

        return data;
    }

    public LiveData<List<StandardHolder>> callCreateStandard() {
        loadingDialog.show();
        final MutableLiveData<List<StandardHolder>> data = new MutableLiveData<>();
        apiCall.callCreateStandard(APINames.CREATE_STANDARD_API,
                preferences.getSelectedStandardName(context)).enqueue(new Callback<ResponseStandardHolder>() {
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

        return data;
    }


}
