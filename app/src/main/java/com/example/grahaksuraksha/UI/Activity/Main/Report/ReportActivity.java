package com.example.grahaksuraksha.UI.Activity.Main.Report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.grahaksuraksha.Models.ReportModel;
import com.example.grahaksuraksha.Models.User;
import com.example.grahaksuraksha.UI.Activity.SignupActivity;
import com.example.grahaksuraksha.Utility.UtilService;
import com.example.grahaksuraksha.WebService.RetrofitApi;
import com.example.grahaksuraksha.WebService.RetrofitClient;
import com.example.grahaksuraksha.databinding.ActivityPayementBinding;
import com.example.grahaksuraksha.databinding.ActivityReportBinding;
import com.google.gson.Gson;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private static final String TAG = "ReportActivity";
    ActivityReportBinding binding;
     private String fraudType , toReport, image , description;
     Base64 imageB64;
    UtilService utilService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utilService = new UtilService();


        binding.reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: "+view.getContext());
                utilService.hideKeyboard(view, ReportActivity.this);
                //todo inialize view
                if(validate(view)){
                    submitReport(view);
                }
            }
        });


    }
    private boolean validate(View view) {
        boolean isValid =true;
        if (TextUtils.isEmpty(fraudType)) {
            utilService.showSnackbar(view, "Please specify fraud type");
            isValid = false;
            return isValid;
        }
        if (TextUtils.isEmpty(toReport)){
            utilService.showSnackbar(view, "Please specify what to report");
            isValid = false;
            return isValid;
        }
        if (TextUtils.isEmpty(description)){
            utilService.showSnackbar(view, "Write a short desc of proof");
            isValid = false;
            return isValid;
        }
        return isValid;
    }
    private void submitReport(View view) {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

         User user = utilService.getUserFromSharedPref(ReportActivity.this);
         if(user==null){
             utilService.showSnackbar(view,"User validation error");
             return;
         }
        ReportModel reportModel = new ReportModel(user,fraudType,toReport,description);
         if(imageB64!=null){
             reportModel.setSupporting_document(imageB64);
         }

        Call<Void> userCall = retrofitApi.submitReport(reportModel);

         userCall.enqueue(new Callback<Void>() {
             @Override
             public void onResponse(Call<Void> call, Response<Void> response) {
                 if(response.isSuccessful() && response.code()==200 ) {
                     Toast.makeText(ReportActivity.this,"Thanks for reporting this Fraud",Toast.LENGTH_LONG).show();
                 }

             }

             @Override
             public void onFailure(Call<Void> call, Throwable t) {
                 Toast.makeText(ReportActivity.this,  t.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });



        }


}