package com.example.grahaksuraksha.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null && pdus.length > 0) {
                    StringBuilder messageBuilder = new StringBuilder();
                    String sender = null;
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        messageBuilder.append(smsMessage.getMessageBody());
                        sender = smsMessage.getOriginatingAddress();
                    }
                    String message = messageBuilder.toString();
                    Log.d("MySmsReceiver", "Message received: " + message + ", from: " + sender);
                    // TODO: Handle the received SMS
                    Toast.makeText(context,"Message received: " + message + ", from: " + sender,Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

    }
}
