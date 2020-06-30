package com.fahim.newapp.ui.books;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.ui.subject.SubjectRepository;

import java.util.List;

public class BooksViewModel extends ViewModel {


    private Context context;
    private BookRepository bookRepository;

    public BooksViewModel() {


    }

    public void setContext(Context context) {
        this.context = context;
        bookRepository = new BookRepository(this.context);
        bookRepository.setLoadingDialog();
    }

    public LiveData<List<StandardHolder>> getStandardHolder(boolean callapi) {
        bookRepository.setLoadingDialog();


        return bookRepository.callReadStandard(callapi);
    }

    public LiveData<List<SubjectHolder>> getSubjectHolder(boolean callapi) {
        bookRepository.setLoadingDialog();
        return bookRepository.callReadSubject(callapi);
    }
    public LiveData<List<BookHolder>> getBookHolder(boolean callapi) {
        bookRepository.setLoadingDialog();
        return bookRepository.callReadBook(callapi);
    }

}