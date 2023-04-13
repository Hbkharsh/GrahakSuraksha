package com.example.grahaksuraksha.UI.Activity.Main.Upi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grahaksuraksha.R;
import com.example.grahaksuraksha.Utility.UtilService;
import com.example.grahaksuraksha.databinding.ActivityPayementBinding;

import java.util.Objects;
import java.util.regex.Pattern;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;

public class PayementActivity extends AppCompatActivity {

    ActivityPayementBinding binding;
    String message ,upiid;
    String amount;

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

}