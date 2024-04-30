package com.example.hce_demo;


import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class MyHostApduService extends HostApduService {

    private int messageCounter = 0;
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

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        if (Arrays.equals(CUSTOM_AID, apdu)){
            Log.i("HCE", "Application selected");
            return "ACK".getBytes();
        }
        else {
            Log.i("HCE", "Received: " + new String(apdu));
            return ("Message from HCE: CON LA SABBIA " + messageCounter++).getBytes();
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i("HCE", "Deactivated: " + reason);
    }

}
