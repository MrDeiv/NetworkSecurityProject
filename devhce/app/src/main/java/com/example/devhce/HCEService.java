package com.example.devhce;


import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import android.preference.PreferenceManager;

import java.util.Arrays;

public class HCEService extends HostApduService {

    private String token =null;


    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("CardData", MODE_PRIVATE);
        token = prefs.getString("token","token");
        int a = 0;
    }
    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.i("HCE", "Application selected1");
        String response = null;
        if (Arrays.equals(CUSTOM_AID, apdu)){
            Log.i("HCE", "Application selected");
            response = token;
        }
        return response.getBytes();
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i("HCE", "Deactivated: " + reason);
    }
    private static final byte[] CUSTOM_AID = {
            (byte)0x00,  // CLA
            (byte)0xa4,  // INS
            (byte)0x04,  // P1
            (byte)0x00,  // P2
            (byte)0x07,  // LC (data length = 7)
            //F0010203040506
            (byte)0xF0, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06,
            (byte)0x00   // LE
    };
}