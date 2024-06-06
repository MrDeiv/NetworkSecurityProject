package com.example.devhce;

import android.content.SharedPreferences;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class HCEService extends HostApduService {

    private String token =null;
    private SharedPreferences prefs;

    private byte[] response = ISO7816_COMMAND.UNKNOWN;
    public void onCreate() {
        super.onCreate();
        prefs = getApplicationContext().getSharedPreferences("CardData", MODE_PRIVATE);
        token = prefs.getString("token","token");
    }
    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {

        if (Arrays.equals(ISO7816_COMMAND.SELECT_AID, apdu)){
            Log.i("HCE", "COMMAND_RECEIVED: SELECT AID");
            response = ISO7816_COMMAND.SUCCESS;
        }
        else if(Arrays.equals(ISO7816_COMMAND.READ_RECORD, apdu)){
            Log.i("HCE", "COMMAND_RECEIVED: READ RECORD");
            response = token.getBytes();
        }
        return response;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i("HCE", "Deactivated: " + reason);
    }

}