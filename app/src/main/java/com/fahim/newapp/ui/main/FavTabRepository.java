package com.fahim.newapp.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fahim.newapp.R;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.ResponseBookHolder;
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

class FavTabRepository {
    private APICall apiCall;
    private Preferences preferences = new Preferences();
    private SweetAlertDialog loadingDialog;
    private Context context;

    FavTabRepository(Context context) {
        this.context = context;
        apiCall = RetrofitConfig.getClient(this.context).create(APICall.class);

    }

    public LiveData<List<BookHolder>> favBooks() {

        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();
        data.setValue(DAO.getInstance().getAllFavouriteBooks(preferences.getNovelsId(context)));
        return data;
    }

    public LiveData<List<BookHolder>> favNovels() {

        final MutableLiveData<List<BookHolder>> data = new MutableLiveData<>();
        data.setValue(DAO.getInstance().getAllFavouriteNovels(preferences.getNovelsId(context)));
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


}
