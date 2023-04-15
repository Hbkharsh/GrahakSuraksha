package com.example.grahaksuraksha.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "Signup Activivty";
    private TextView loginBtn;
    private EditText name_ET, email_ET, password_ET;
    private Button registerBtn;
    ProgressBar progressBar;
    private String name, email, password;
    UtilService utilService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginBtn = (TextView) findViewById(R.id.textView6);
        registerBtn= (Button) findViewById(R.id.button4);
        progressBar = findViewById(R.id.progress_bar);
        name_ET = findViewById(R.id.inputUsername);
        email_ET = findViewById(R.id.inputEmail);
        password_ET = findViewById(R.id.inputPassword);
        utilService = new UtilService();

        //Go to login page
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        //Register user ,validate our user then using the rest API completes the registration.
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, SignupActivity.this);
                name = name_ET.getText().toString();
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();

                if (validate(view)){
                    registerUser(view);
                }
            }
        });


    }

    //Api Call for register user
    private void registerUser(View view){
        progressBar.setVisibility(View.VISIBLE);
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        User user = new User(name,email,password,password);
        Call<User> userCall = retrofitApi.register(user);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.code()==200 && response.body() != null) {
                    User user = response.body();
                    Log.i(TAG, "onResponse: after logon" + new Gson().toJson(response.body()));
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
                    // Show success message and navigate to next screen
                    Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Show error message
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Show error message
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

//    private void registerUser(View view) {
//        progressBar.setVisibility(View.VISIBLE);
//
//        final HashMap<String, String> params = new HashMap<>();
//        params.put("username", name);
//        params.put("email", email);
//        params.put("password", password);
//
//
////set retry policy
//        int socketTime = 3000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTime, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//
////request add
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjectRequest);
//    }

    public boolean validate(View view){
        boolean isValid =true;
        if (TextUtils.isEmpty(name)) {
            utilService.showSnackbar(view, "Please enter name");
            isValid = false;
            return isValid;
        }
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
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }
    }
}