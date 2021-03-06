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
    public LiveData<List<SubjectHolder>> getAllSubjectHolder(boolean callapi) {
        bookRepository.setLoadingDialog();
        return bookRepository.callAllReadSubject(callapi);
    }
    public LiveData<List<BookHolder>> getBookHolder(boolean callapi) {
        bookRepository.setLoadingDialog();
        return bookRepository.callReadBook(callapi);
    }

    public LiveData<List<BookHolder>> editBook() {
        bookRepository.setLoadingDialog();
        return  bookRepository.callEditBook();
    }
    public LiveData<List<BookHolder>> createBook(BookHolder bookHolder) {
        bookRepository.setLoadingDialog();
        return  bookRepository.callCreateBook(bookHolder);
    }

    public LiveData<List<BookHolder>> deleteBook() {
        bookRepository.setLoadingDialog();
        return bookRepository.callDeleteBook();
    }
    public LiveData<List<Integer>> favBookList(){
        return bookRepository.callFavBookList();
    }
    public LiveData<List<Integer>> deleteFavBook(){
        bookRepository.deleteFavBook();
        return bookRepository.callFavBookList();
    }
    public LiveData<List<Integer>> addToFavBook(){
        bookRepository.addFavBook();
        return bookRepository.callFavBookList();
    }

}