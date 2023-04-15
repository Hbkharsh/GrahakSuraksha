package com.example.grahaksuraksha.UI.Activity.Main.Report;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.grahaksuraksha.Models.ReportModel;
import com.example.grahaksuraksha.Models.User;
import com.example.grahaksuraksha.UI.Activity.LoginActivity;
import com.example.grahaksuraksha.UI.Activity.SignupActivity;
import com.example.grahaksuraksha.Utility.UtilService;
import com.example.grahaksuraksha.WebService.RetrofitApi;
import com.example.grahaksuraksha.WebService.RetrofitClient;
import com.example.grahaksuraksha.databinding.ActivityPayementBinding;
import com.example.grahaksuraksha.databinding.ActivityReportBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private static final String TAG = "ReportActivity";
    ActivityReportBinding binding;
     private String fraudType , toReport , description;
     String imageB64;
    UtilService utilService;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utilService = new UtilService();


        binding.edtUploadImg.setOnClickListener(view -> {
            binding.progressBar.setIndeterminate(true);
            ImagePicker.with(ReportActivity.this)
                    .crop(4f,3f)
                    .galleryOnly()
                    .compress(50)	//Final image size will be less than 1 MB
                    // .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)) ////  Path: /storage/sdcard0/Android/data/package/files/Pictures
                    .start(100);
        });


        binding.reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fraudType = binding.edtFraudTypeSpinner.getSelectedItem().toString();
                toReport = binding.edtToReport.getText().toString();
                description=binding.edtDescription.getText().toString();

                //Log.i(TAG, "onClick: "+view.getContext());
                utilService.hideKeyboard(view, ReportActivity.this);
                //todo inialize view
                if(validate(view)){

                    submitReport(view);
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==100){
                assert data != null;
                Uri uri = data.getData();
                if(uri!=null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] byteArray = outputStream.toByteArray();

                    //Use your Base64 String as you wish
                    String encodedString = Base64.encodeToString(byteArray,Base64.DEFAULT);
                    imageB64 = encodedString;
                    Log.i(TAG, "onActivityResult: "+ imageB64);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.progressBar.setIndeterminate(false);
                            binding.progressBar.setProgress(100,true);
                            Toast.makeText(ReportActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                        }
                    },1000);
                }

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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
             Log.i(TAG, "onClick: CHECKING INPUTS"+ imageB64);
             reportModel.setSupporting_document(imageB64);
         }

        Call<Void> userCall = retrofitApi.submitReport(reportModel);

         userCall.enqueue(new Callback<Void>() {
             @Override
             public void onResponse(Call<Void> call, Response<Void> response) {

                 if(response.isSuccessful() && response.code()==200 ) {
                     //TODO call for submiiting report
                     Log.i(TAG, "onResponse: Check"+response);
                     Toast.makeText(ReportActivity.this,"Thanks for reporting this Fraud",Toast.LENGTH_LONG).show();

                 }else{
                     Log.i(TAG, "onResponse: Not Success "+response.message()+response.errorBody() );
                 }

             }

             @Override
             public void onFailure(Call<Void> call, Throwable t) {
                 Toast.makeText(ReportActivity.this,  t.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });



        }


}