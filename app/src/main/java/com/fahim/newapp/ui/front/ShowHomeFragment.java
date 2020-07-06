package com.fahim.newapp.ui.front;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fahim.newapp.R;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.utils.Preferences;

public class ShowHomeFragment extends Fragment {
    public ShowHomeFragment() {
        // Required empty public constructor
    }

    public static ShowHomeFragment newInstance() {
        return new ShowHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    Preferences preferences = new Preferences();

    @Override
    public void onResume() {
        super.onResume();
        if (preferences.getNovelsId(getActivity()) == 0) {
            preferences.putNovelsId(getActivity(), DAO.getInstance().getNovelID());
            Log.e("ShowHomeF", "onResume: " + preferences.getNovelsId(getActivity()));
        }

    }


}
