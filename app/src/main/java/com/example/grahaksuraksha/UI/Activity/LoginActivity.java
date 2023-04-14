package com.example.grahaksuraksha.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grahaksuraksha.Models.User;
import com.example.grahaksuraksha.R;
import com.example.grahaksuraksha.UI.Activity.Main.MainActivity;
import com.example.grahaksuraksha.Utility.UtilService;
import com.example.grahaksuraksha.WebService.RetrofitApi;
import com.example.grahaksuraksha.WebService.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerBtn;
    private EditText email_ET, password_ET;
    private Button loginBtn;
    ProgressBar progressBar;
    private String email, password;
    UtilService utilService;
  //  SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);

        registerBtn = findViewById(R.id.textView6);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });

        loginBtn = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progress_bar);
        email_ET = findViewById(R.id.inputEmail);
        password_ET = findViewById(R.id.inputPassword);
        utilService = new UtilService();
//        sharedPreferenceClass = new SharedPreferenceClass(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, LoginActivity.this);
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();

                if (validate(view)) {
                    loginUser(view);
                }
            }
        });
    }

    private void loginUser(View view) {

        progressBar.setVisibility(View.VISIBLE);

        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        User user = new User("",email,password,"");

        Call<User> userCall = retrofitApi.login(user);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    User user = response.body();

                    // Save user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Convert user object to JSON string using Gson library
                    Gson gson = new Gson();
                    String userJson = gson.toJson(user);
                    // Save user JSON string in SharedPreferences
                    editor.putString("user", userJson);
                    editor.apply();

                    progressBar.setVisibility(View.GONE);
                    // Navigate user to main screen
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    public boolean validate(View view) {
        boolean isValid=true;

        if (TextUtils.isEmpty(email)){
            utilService.showSnackbar(view, "Please enter email");
            isValid = false;
            return isValid;
        }
        if (TextUtils.isEmpty(password)){
            utilService.showSnackbar(view, "Please enter password");
            isValid = false;
            return isValid;
        }
        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences snapshot_pref = getSharedPreferences("userSnapshot", MODE_PRIVATE);
        String userJson = snapshot_pref.getString("user", null);
        if (userJson != null) {
            // User already exists, redirect to main page
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}