package com.example.automessagingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.util.HashMap;

public class IncomingSMSReceiver extends BroadcastReceiver {
    SQLiteDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {

        db = context.openOrCreateDatabase("messagedb", Context.MODE_PRIVATE, null);

        Bundle bundle = intent.getExtras();
        Object[] pdusObj = (Object[]) bundle.get("pdus");
        //"SmsMessage" take byte array object, as bundle object can't be directly converted into byte array object, we r typecasting object array mode
        //"pdus" is default key
        SmsMessage sm = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
        //PDU stands for Protocol Data Unit.
        String sendermobileno = sm.getDisplayOriginatingAddress();
        //Here we are getting the mobile number(It is a default method)
        String message = sm.getDisplayMessageBody().toLowerCase();
        //It displays the body/actual message


        HashMap hm = new HashMap();
        //"HashMap" also takes data in key value pair
        Cursor cur = db.rawQuery("select * from messages", null);
        while (cur.moveToNext()) {
            hm.put(cur.getString(0), cur.getString(1));
            //Here we are putting the data into the "HashMap"
        }
        if (hm.containsKey(message))
        //Here we are checking if the incoming message is available in the "HashMap" or not, if available then enter into the loop(Here we are giving the key)
        {
            String oMessage = hm.get(message).toString();
            //Here we are getting the value from the key
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(sendermobileno, null, oMessage, null, null);
        }


    }
}
