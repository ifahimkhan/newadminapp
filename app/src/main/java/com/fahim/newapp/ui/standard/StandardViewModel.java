package com.fahim.newapp.ui.standard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fahim.newapp.R;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.utils.Preferences;
import com.fahim.newapp.utils.QustomDialogBuilder;

import java.util.List;

public class StandardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<StandardHolder>> listOfStandardHolder;
    private StandardRepository standardRepository;
    private Context context;
    private Preferences preferences = new Preferences();


    public StandardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<StandardHolder>> getStandardHolder(boolean callapi) {
        standardRepository.setLoadingDialog();
        listOfStandardHolder= (MutableLiveData<List<StandardHolder>>) standardRepository.callReadStandard(callapi);

        return listOfStandardHolder;
    }

    public void setContext(Context context) {
        this.context = context;
        standardRepository = new StandardRepository(this.context);
        standardRepository.setLoadingDialog();
    }

    public LiveData<List<StandardHolder>> deleteStandard() {
        standardRepository.setLoadingDialog();
        return standardRepository.callDeleteStandard();
    }

    public LiveData<List<StandardHolder>> editStandard() {
        standardRepository.setLoadingDialog();
        return standardRepository.callEditStandard();
    }

    public LiveData<List<StandardHolder>> createStandard() {
        standardRepository.setLoadingDialog();
        return standardRepository.callCreateStandard();
    }


}