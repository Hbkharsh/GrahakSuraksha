package com.example.grahaksuraksha.UI.Activity.Main.Others;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grahaksuraksha.Models.User;
import com.example.grahaksuraksha.R;
import com.example.grahaksuraksha.UI.Activity.LoginActivity;
import com.example.grahaksuraksha.UI.Activity.Main.Report.ReportActivity;
import com.example.grahaksuraksha.Utility.UtilService;
import com.example.grahaksuraksha.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private static final String TAG ="ProfileFragment" ;
    private FragmentProfileBinding binding;
   UtilService utilService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilService = new UtilService();

        User user = utilService.getUserFromSharedPref(this.getContext());
        if(user==null){
            utilService.showSnackbar(view,"User validation error");
            return;
        }
        Log.d(TAG, "onViewCreated() returned: " + user.getUserId()+","
                + user.getName()+","
                + user.getEmail()+","
                + user.getConfirmPassword()+"," +user.getPassword()+",");

        binding.userName.setText(user.getName());
        binding.fraudComplainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FraudComplainActivity.class));
            }
        });
        binding.complainStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ComplainStatusActivity.class));
            }
        });



        binding.profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear user data from SharedPreferences
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }
}