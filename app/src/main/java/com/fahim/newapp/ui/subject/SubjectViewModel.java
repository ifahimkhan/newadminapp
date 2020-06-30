package com.fahim.newapp.ui.subject;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.ui.standard.StandardRepository;

import java.util.List;

public class SubjectViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<SubjectHolder>> listMutableLiveData;
    private Context context;
    private SubjectRepository subjectRepository;

    public void setContext(Context context) {
        this.context = context;
        subjectRepository = new SubjectRepository(this.context);
        subjectRepository.setLoadingDialog();
    }

    public SubjectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<SubjectHolder>> getSubjectHolder(boolean callapi) {
        subjectRepository.setLoadingDialog();
        listMutableLiveData = (MutableLiveData<List<SubjectHolder>>) subjectRepository.callReadSubject(callapi);
        return listMutableLiveData;
    }
    public LiveData<List<StandardHolder>> getStandardHolder(boolean callapi) {
        subjectRepository.setLoadingDialog();


        return  subjectRepository.callReadStandard(callapi);
    }


    public LiveData<List<SubjectHolder>> editSubject() {
        subjectRepository.setLoadingDialog();
        return  subjectRepository.callEditStandard();
    }
    public LiveData<List<SubjectHolder>> createSubject() {
        subjectRepository.setLoadingDialog();
        return subjectRepository.callCreateSubject();
    }

    public LiveData<List<SubjectHolder>> deleteSubject() {
        subjectRepository.setLoadingDialog();
        return subjectRepository.callDeleteSubject();
    }

}