package com.fahim.newapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.fahim.newapp.AdminActivity;
import com.fahim.newapp.FavouriteActivity;
import com.fahim.newapp.R;
import com.fahim.newapp.database.DAO;
import com.fahim.newapp.ui.front.BookListViewFragment;
import com.fahim.newapp.ui.front.BooksExpandableFragment;
import com.fahim.newapp.ui.front.NovelsExpandableFragment;
import com.fahim.newapp.ui.front.ShowHomeFragment;
import com.fahim.newapp.ui.search.SearchFragment;
import com.fahim.newapp.utils.PermissionUtil;
import com.fahim.newapp.utils.Preferences;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";

    DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private PermissionUtil permissionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestStoragePermission();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        selectItem(0);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_novels:
                selectItem(R.id.nav_novels);
                break;
            case R.id.nav_books:
                selectItem(R.id.nav_books);
                break;
            case R.id.nav_search:
                selectItem(R.id.nav_search);
                break;
            case R.id.nav_admin:
                startActivity(new Intent(this, AdminActivity.class));
                break;
            case R.id.nav_fav:
                startActivity(new Intent(this, FavouriteActivity.class));
                break;
            case R.id.nav_settings:
//                startActivity(new Intent(this, MainActivity.class));
                break;


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectItem(int position) {
        // update the no_items content by replacing fragments
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = ShowHomeFragment.newInstance();
                break;
            case 1:
                fragment = BookListViewFragment.newInstance();
                break;

            case R.id.nav_novels:
                fragment = NovelsExpandableFragment.newInstance();
                break;

            case R.id.nav_books:
                fragment = BooksExpandableFragment.newInstance();
                break;
            case R.id.nav_settings:
                fragment = ShowHomeFragment.newInstance();
                break;
            case R.id.nav_search:
                fragment = SearchFragment.newInstance();
                break;


        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0) {
            //Pop the back stack since we want to maintain only one level of the back stack
            //Don't add the transaction to back stack since we are navigating to the first fragment
            //being displayed and adding the same to the backstack will result in redundancy
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, TAG_CONTENT_FRAGMENT).commit();
        } else if (position == 1) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, TAG_CONTENT_FRAGMENT).addToBackStack("expandable").commit();

        } else {
            //Pop the back stack since we want to maintain only one level of the back stack
            //Add the transaction to the back stack since we want the state to be preserved in the back stack
            //if (position != 9)
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
        }

        // update selected item and title, then close the drawer
        //mDrawerList.setItemChecked(position, true);
//        setTitle(mOptionTitles[position]);
        //mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void selectNavigationMenuItem(int pos) {
        navigationView.getMenu().getItem(pos).setChecked(true);
    }

    public void booksClicked(View view) {
        selectItem(R.id.nav_books);

    }

    public void novelClicked(View view) {
        selectItem(R.id.nav_novels);
    }

    public void adminClicked(View view) {
        startActivity(new Intent(this, AdminActivity.class));
    }

    public void settClicked(View view) {
    }

    public void videoClicked(View view) {
    }

    public void favClicked(View view) {
        startActivity(new Intent(this, FavouriteActivity.class));
    }

    public void searchClicked(View view) {
        selectItem(R.id.nav_search);
    }
}
