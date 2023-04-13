package com.example.grahaksuraksha.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.grahaksuraksha.R;

public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStateReceiver";
    WindowManager wm;
    ViewGroup viewgroup;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
            return;

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.i(TAG, "onReceive: "+intent.getStringExtra(TelephonyManager.EXTRA_STATE)+","+
                intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)+","
                +intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));

//        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephony.listen(new PhoneStateListener(){
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                super.onCallStateChanged(state, incomingNumber);
//                Log.i("TAG", "onCallStateChanged: "+incomingNumber);
//                 number = incomingNumber;
//
//            }
//        },PhoneStateListener.LISTEN_CALL_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Toast t= Toast.makeText(context,"Rohit call from" + number,Toast.LENGTH_LONG);
            t.show();
//
//            Intent i = new Intent(context, MainActivity2.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//            context.startActivity(i);
            showCustomPopupMenu1(context,intent);
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            Toast t= Toast.makeText(context,"My call ended ",Toast.LENGTH_SHORT);
            t.show();
            if (viewgroup != null) {
                wm.removeView(viewgroup);
                viewgroup = null;
                Log.v("Call", " wm.removeView(viewgroup);.................");
            }
            if (viewgroup != null)
                viewgroup.removeAllViewsInLayout();
        }
    }

    private void showCustomPopupMenu1(Context context, Intent intent) {

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        viewgroup = (ViewGroup) View.inflate(context, R.layout.fraud_card, null);

        TextView title = (TextView) viewgroup.findViewById(R.id.name);
        TextView phone = (TextView) viewgroup.findViewById(R.id.no);
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        title.setText("" + "Potential Fraudster");
        phone.setText("" + number);
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
        }, 8000);

//        // To remove the view  once the dialer app is closed.
//        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
//            String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            if (state1.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                if (viewgroup != null) {
//                    wm.removeView(viewgroup);
//                    viewgroup = null;
//                    Log.v("Call", " wm.removeView(viewgroup);.................");
//                }
//                if (viewgroup != null)
//                    viewgroup.removeAllViewsInLayout();
//            }
//        }

    }
}
