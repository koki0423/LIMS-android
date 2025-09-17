package com.example.lims_android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lims_android.R;
import com.example.lims_android.ui.lend.NfcLendDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lims_android.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_scan, R.id.navigation_search, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // ★ステップ1：そもそも onNewIntent が呼ばれているか？
        Log.d("NFC_DEBUG", "--- MainActivity onNewIntent TRIGGERED! ---");

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            // ★ステップ2：NavHostFragment は見つかっているか？
            Log.d("NFC_DEBUG", "NavHostFragment FOUND.");

            Fragment dialogFragment = navHostFragment.getChildFragmentManager().findFragmentByTag("NfcLendDialog");

            if (dialogFragment instanceof NfcLendDialogFragment) {
                // ★ステップ3：NfcLendDialogFragment は見つかっているか？
                Log.d("NFC_DEBUG", "NfcLendDialogFragment FOUND! Forwarding intent...");
                ((NfcLendDialogFragment) dialogFragment).processNfcIntent(intent);
            } else {
                Log.e("NFC_DEBUG", "NfcLendDialogFragment NOT FOUND! This is the problem.");
                if (dialogFragment != null) {
                    Log.e("NFC_DEBUG", "Found fragment is: " + dialogFragment.getClass().getSimpleName());
                } else {
                    Log.e("NFC_DEBUG", "findFragmentByTag returned null.");
                }
            }
        } else {
            Log.e("NFC_DEBUG", "NavHostFragment NOT FOUND! This is the problem.");
        }
    }
}