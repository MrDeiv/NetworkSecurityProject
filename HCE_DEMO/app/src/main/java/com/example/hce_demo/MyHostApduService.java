package com.example.hce_demo;


import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class MyHostApduService extends HostApduService {

    private int messageCounter = 0;

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.i("HCE", "Receiving");
        if (Arrays.equals(CUSTOM_AID, apdu)){
            Log.i("HCE", "Application selected");
            return getWelcomeMessage();
        }
        else {
            Log.i("HCE", "Received: " + new String(apdu));
            return getNextMessage();
        }
    }

    private byte[] getWelcomeMessage() {
        return "Hello Desktop!".getBytes();
    }

    private byte[] getNextMessage() {
        return ("Message from HCE: CON LA SABBIA " + messageCounter++).getBytes();
    }

    private boolean selectAidApdu(byte[] apdu) {
        return apdu.length >= 2 && apdu[0] == (byte) 0 && apdu[1] == (byte) 0xa4;
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
