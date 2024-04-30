package com.example.pos_demo;


import java.io.IOException;
import java.util.Arrays;

import android.nfc.tech.IsoDep;
import android.util.Log;

public class IsoDepTransceiver implements Runnable {


    private IsoDep isoDep;

    public IsoDepTransceiver(IsoDep isoDep) {
        this.isoDep = isoDep;
    }

    private static final byte[] CLA_INS_P1_P2 = { 0x00, (byte)0xA4, 0x04, 0x00 };
    private static final byte[] AID_ANDROID = { (byte)0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };

    private byte[] createSelectAidApdu(byte[] aid) {
        byte[] result = new byte[6 + aid.length];
        System.arraycopy(CLA_INS_P1_P2, 0, result, 0, CLA_INS_P1_P2.length);
        result[4] = (byte)aid.length;
        System.arraycopy(aid, 0, result, 5, aid.length);
        result[result.length - 1] = 0;
        return result;
    }

    @Override
    public void run() {
        int messageCounter = 0;
        try {
            isoDep.connect();
            byte[] response = isoDep.transceive(createSelectAidApdu(AID_ANDROID));
            while (isoDep.isConnected() && !Thread.interrupted()) {
                String message = "CI CODDIAMO PURE A MARTALO " + messageCounter++;
                response = isoDep.transceive(message.getBytes());
                Log.d("POS", new String(response));
            }
            isoDep.close();
        }
        catch (IOException e) {
            Log.d("POS", e.toString());
        }
    }
}