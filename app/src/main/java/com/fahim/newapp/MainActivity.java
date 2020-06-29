package com.fahim.newapp;

import android.os.Bundle;
import android.view.View;

import com.fahim.newapp.Interface.FragmentClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButtonStandard, floatingActionButtonSubject, floatingActionButtonBooks;
    FragmentClickListener fragmentClickListener;


    public void setFragmentClickListener(FragmentClickListener listener) {
        this.fragmentClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_standard, R.id.navigation_subject, R.id.navigation_books)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        floatingActionButtonStandard = findViewById(R.id.floatingbuttonstandard);
        floatingActionButtonSubject = findViewById(R.id.floatingbuttonsubject);
        floatingActionButtonBooks = findViewById(R.id.floatingbuttonbooks);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull final NavDestination destination, @Nullable Bundle arguments) {

                switch (destination.getId()) {
                    case R.id.navigation_standard: {
                        floatingActionButtonStandard.show();
                        floatingActionButtonSubject.hide();
                        floatingActionButtonBooks.hide();
                    }
                    break;
                    case R.id.navigation_subject: {
                        floatingActionButtonStandard.hide();
                        floatingActionButtonSubject.show();
                        floatingActionButtonBooks.hide();
                    }
                    break;
                    case R.id.navigation_books: {
                        floatingActionButtonStandard.hide();
                        floatingActionButtonSubject.hide();
                        floatingActionButtonBooks.show();

                    }
                    break;
                    default:
                        break;

                }

            }
        });

        floatingActionButtonStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentClickListener.onClick(R.id.floatingbuttonstandard);
            }
        });
        floatingActionButtonSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentClickListener.onClick(R.id.floatingbuttonsubject);
            }
        });
        floatingActionButtonBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentClickListener.onClick(R.id.floatingbuttonbooks);
            }
        });

    }

}
