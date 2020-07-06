package com.fahim.newapp.ui.favourite;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.utils.Preferences;

import java.util.List;

public class PageViewModel extends ViewModel {

    private Context context;
    private FavTabRepository favTabRepository;
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private MutableLiveData<List<BookHolder>> bookList = new MutableLiveData<>();
    private MutableLiveData<List<BookHolder>> novelList = new MutableLiveData<>();
    private Preferences preferences = new Preferences();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {

            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public void setContext(Context context) {
        this.context = context;
        favTabRepository = new FavTabRepository(this.context);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<BookHolder>> getBookHolderFavList() {
        bookList = (MutableLiveData<List<BookHolder>>) favTabRepository.favBooks();
        return bookList;
    }

    public LiveData<List<BookHolder>> getNovelHolderFavList() {
        novelList = (MutableLiveData<List<BookHolder>>) favTabRepository.favNovels();
        return novelList;

    }


    public LiveData<List<Integer>> favBookList() {
        return favTabRepository.callFavBookList();
    }

    public LiveData<List<Integer>> deleteFavBook(){
        favTabRepository.deleteFavBook();
        return favTabRepository.callFavBookList();
    }
}