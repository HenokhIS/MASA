package com.example.cekcuaca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cekcuaca.fragment.AccountFragment;
import com.example.cekcuaca.fragment.MainFragment;
import com.example.cekcuaca.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment mainFragment = new MainFragment();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, mainFragment)
                    .commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.navmenu);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.cuaca_btn) {
                selectedFragment = new MainFragment();
            } else if (item.getItemId() == R.id.account_btn) {
                selectedFragment = new AccountFragment();
            } else if (item.getItemId() == R.id.settings_btn) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .addToBackStack(null)
                        .commit();

                return true;
            } else {
                return false;
            }
        });
    }

}