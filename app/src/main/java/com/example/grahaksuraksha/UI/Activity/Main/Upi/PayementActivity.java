package com.example.grahaksuraksha.UI.Activity.Main.Upi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grahaksuraksha.Models.FraudCheckRequest;
import com.example.grahaksuraksha.Models.FraudCheckResponse;
import com.example.grahaksuraksha.Models.User;
import com.example.grahaksuraksha.R;
import com.example.grahaksuraksha.Utility.UtilService;
import com.example.grahaksuraksha.WebService.RetrofitApi;
import com.example.grahaksuraksha.WebService.RetrofitClient;
import com.example.grahaksuraksha.databinding.ActivityPayementBinding;

import java.util.Objects;
import java.util.regex.Pattern;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayementActivity extends AppCompatActivity {

    private static final String TAG = "Payement Actiivty";
    ActivityPayementBinding binding;
    String message ,upiid;
    String amount;
    WindowManager wm;
    ViewGroup viewgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPayementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.edtUpiid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ( i == EditorInfo.IME_ACTION_DONE) {
                    upiid = Objects.requireNonNull(binding.edtUpiid.getText()).toString();
                    new UtilService().hideKeyboard(binding.layout,PayementActivity.this);
                     if(validateUPI(binding.layout)){
                         //TODO api call  for upi id
                         checkFraud(upiid);
                         Toast.makeText(PayementActivity.this,"Do aPi call ",Toast.LENGTH_LONG).show();
                     }
                    return true;
                }
                return false;

            }
        });

        binding.buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = Objects.requireNonNull(binding.edtMessage.getText()).toString();
                upiid = Objects.requireNonNull(binding.edtUpiid.getText()).toString();
                amount = Objects.requireNonNull(binding.edtAmount.getText()).toString();

                try {
                    if(validate(view)){
                        PayementGatewayStart();
                    }
                } catch (AppNotFoundException e) {
                    e.printStackTrace();
                }

                Log.i("TAG", "onCreate: "+amount+message+upiid);
            }
        });
    }



    private void PayementGatewayStart() throws AppNotFoundException {
        double amt =  Double.parseDouble(amount)+0.00;
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .setPayeeVpa(upiid)
                .setPayeeName("xyz")
                .setPayeeMerchantCode("12345")
                .setTransactionId("T20"+System.currentTimeMillis())
                .setTransactionRefId("T20"+System.currentTimeMillis())
                .setDescription(message)
                .setAmount(String.valueOf(amt));
        //  .setTransactionId(String.valueOf(System.currentTimeMillis()))

        EasyUpiPayment easyUpiPayment = builder.build();
        easyUpiPayment.startPayment();
    }

    public boolean validate(View view) {
        boolean isValid=true;

        if(!validateUPI(view)){
            isValid = false;
            return isValid;
        }

        if (TextUtils.isEmpty(message)){
            new UtilService().showSnackbar(view, "Please small note about payment. for e.g. For Food");
            isValid = false;
            return isValid;
        }
        if (TextUtils.isEmpty(amount)||amount.equals("0")){
            new UtilService().showSnackbar(view, "Please enter amount to pay");
            isValid = false;
            return isValid;
        }
        return isValid;
    }
    public boolean validateUPI(View view){
        boolean isValid=true;
        // Regex to check valid upi_Id Code
        String regex
                = "^[\\w-.]+@([\\w-])+";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        if (TextUtils.isEmpty(upiid)){
            new UtilService().showSnackbar(view, "Please enter upi id");
            isValid = false;
            return isValid;
        }
        if (!p.matcher(upiid).matches()){
            new UtilService().showSnackbar(view, "Please enter valid upi id");
            isValid = false;
            return isValid;
        }
        return isValid;
    }

    private void checkFraud(String upiid) {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        FraudCheckRequest req = new FraudCheckRequest("",upiid);
        Call<FraudCheckResponse> apiCall = retrofitApi.fraudCheck(req);

        apiCall.enqueue(new Callback<FraudCheckResponse>() {
            @Override
            public void onResponse(Call<FraudCheckResponse> call, Response<FraudCheckResponse> response) {
                if(response.isSuccessful()&& response.code()==200 && response.body() != null){
                    FraudCheckResponse res = response.body();
                    if(res.isIs_fraud()){
                        showAlertCard(PayementActivity.this, res);
                    }
                }else{
                    Log.i(TAG, "onResponse: Not Success "+response.errorBody() );
                }
            }

            @Override
            public void onFailure(Call<FraudCheckResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());

            }
        });



    }

    private void showAlertCard(Context context,FraudCheckResponse res) {

         wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
         viewgroup = (ViewGroup) View.inflate(context, R.layout.fraud_card, null);

        TextView title = viewgroup.findViewById(R.id.name);
        TextView phone = viewgroup.findViewById(R.id.no);
        if(res.isIs_gov_verified()){
            title.setText("Potential Fraudster");
            phone.setText("Goverment Verified");
        }else{
            title.setText("Potential Fraudster");
            phone.setText("Reported by "+res.getNumber_of_userReported()+ " user");
        }



        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                300,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        wm.addView(viewgroup, params);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if (viewgroup != null) {
                    wm.removeView(viewgroup);
                    viewgroup = null;
                }
                if (viewgroup != null)
                    viewgroup.removeAllViewsInLayout();
            }
        }, 5000);

    }




}