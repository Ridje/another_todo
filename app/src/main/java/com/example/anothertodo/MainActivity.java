package com.example.anothertodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.anothertodo.ui.HelpFragment;
import com.example.anothertodo.ui.NoteFragment;
import com.example.anothertodo.ui.NoteListFragment;
import com.example.anothertodo.ui.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    Toolbar mHeaderToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDefaultFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_new_note:

                return true;
        }
        return false;
    }

    private void initView() {
        initHeaderToolbar();
        initNavigationBar();
    }

    private void initHeaderToolbar() {
        mHeaderToolbar = findViewById(R.id.header_toolbar);
        setSupportActionBar(mHeaderToolbar);
    }

    private void initDefaultFragment() {
        showFragment(new NoteListFragment());
    }

    private void initNavigationBar() {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mHeaderToolbar, R.string.open_navigation, R.string.close_navigation);
        drawer.addDrawerListener(toggle);

        NavigationView navigationMenu = drawer.findViewById(R.id.navigation_view);
        navigationMenu.setNavigationItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (navigateFragment(itemID)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
        toggle.syncState();
    }

    private boolean navigateFragment(int id) {

        switch (id) {
            case R.id.navigation_main:
                showFragment(new NoteListFragment());
                return true;
            case R.id.navigation_favourite:
                showFragment(new Fragment());
                return true;
            case R.id.navigation_settings:
                showFragment(new SettingsFragment());
                return true;
            case R.id.navigaton_help:
                showFragment(new HelpFragment());
                return true;
        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.note_list_container, fragment);
        transaction.commit();
    }

}