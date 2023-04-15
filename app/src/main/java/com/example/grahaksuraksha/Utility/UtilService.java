package com.example.grahaksuraksha.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.grahaksuraksha.Models.User;
import com.example.grahaksuraksha.UI.Activity.SignupActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class UtilService {

       public static final String BASE_URL = "https://grahaksuraksha-api.onrender.com/";
  //  public static final String BASE_URL = "https://10b3-2405-201-3006-5bcb-5574-cc2f-ce5e-7a1.ngrok-free.app/";

    public void hideKeyboard(View view ,Context context)
    {
        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public User getUserFromSharedPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null);
        if (userJson != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            return  user;
        }
        return null;
    }

    public void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view,text,Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
