package com.example.feedforward_courier;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.feedforward_courier.databinding.ActivitySignInBinding;


public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_log_in,R.id.navigation_register)
                .build();
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment_sign_in);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);


    }

}