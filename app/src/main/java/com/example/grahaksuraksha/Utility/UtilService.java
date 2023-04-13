package com.example.grahaksuraksha.Utility;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.grahaksuraksha.UI.Activity.SignupActivity;
import com.google.android.material.snackbar.Snackbar;

public class UtilService {

    public static final String BASE_URL = "http://65.1.132.68:8004";

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

    public void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view,text,Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
