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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anothertodo.ui.HelpFragment;
import com.example.anothertodo.ui.NoteListFragment;
import com.example.anothertodo.ui.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{
    Toolbar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initColorsForNotes(getResources());
        setContentView(R.layout.activity_main);
        initView();
        initDefaultFragment();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void initView() {
        initActionBar();
        initNavigationBar();
    }

    private void initActionBar() {
        mActionBar = findViewById(R.id.action_bar);
        setSupportActionBar(mActionBar);
    }

    private void initDefaultFragment() {
        showFragment(new NoteListFragment());
    }

    private void initNavigationBar() {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mActionBar, R.string.open_navigation, R.string.close_navigation);
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
                setTitle(R.string.main_window);
                return true;
            case R.id.navigation_favourite:
                showFragment(NoteListFragment.newInstance(true));
                setTitle(R.string.favourite);
                return true;
            case R.id.navigation_settings:
                showFragment(new SettingsFragment());
                setTitle(R.string.settings);
                return true;
            case R.id.navigaton_help:
                showFragment(new HelpFragment());
                setTitle(R.string.help);
                return true;
        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_workflow, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

}