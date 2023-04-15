package com.example.grahaksuraksha.UI.Activity.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grahaksuraksha.R;
import com.example.grahaksuraksha.UI.Activity.LoginActivity;
import com.example.grahaksuraksha.UI.Activity.SignupActivity;
import com.example.grahaksuraksha.databinding.ActivityMainBinding;

public class  MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_upi, R.id.navigation_report,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }
    @Override
    protected void onStart() {
        super.onStart();

        //Todo uncomment
        SharedPreferences snapshot_pref = getSharedPreferences("userSnapshot", MODE_PRIVATE);

        String userJson = snapshot_pref.getString("user", null);
        if (userJson == null) {
            // User not exists, redirect to main page
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

}