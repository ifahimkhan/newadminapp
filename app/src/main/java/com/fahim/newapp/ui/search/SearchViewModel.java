package com.fahim.newapp.ui.search;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;
import com.fahim.newapp.ui.books.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {


    private Context context;
    private SearchRepository searchRepository;

    public SearchViewModel() {


    }

    public void setContext(Context context) {
        this.context = context;
        searchRepository = new SearchRepository(this.context);
        searchRepository.setLoadingDialog();
    }

    public LiveData<List<BookHolder>> getListValues(String text) {

        return searchRepository.getSearchListValues(text);
    }


    public LiveData<ArrayList<String>> getsearchList() {
        return searchRepository.getsearchList();
    }

    public LiveData<List<BookHolder>> getSearchData(String text) {
        searchRepository.setLoadingDialog();
        return searchRepository.callSearchData(text);
    }

    public LiveData<List<Integer>> favBookList() {
        return searchRepository.callFavBookList();
    }

    public LiveData<List<Integer>> deleteFavBook() {
        searchRepository.deleteFavBook();
        return searchRepository.callFavBookList();
    }


    public LiveData<List<Integer>> addToFavBook(){
        searchRepository.addFavBook();
        return searchRepository.callFavBookList();
    }
}