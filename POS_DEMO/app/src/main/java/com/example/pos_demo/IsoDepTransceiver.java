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

    private byte[] PPSE = "2PAY.SYS.DDF01".getBytes();

    private static byte[] MASTERCARD_AID = {(byte)0xA0,
                                            (byte)0x00,
                                            (byte)0x00,
                                            (byte)0x00,
                                            (byte)0x04,
                                            (byte)0x10,
                                            (byte)0x10};
    private static final byte[] CLA_INS_P1_P2 = { (byte)0x00,
                                                  (byte)0xA4,
                                                  (byte)0x04,
                                                  (byte)0x00 };
    private static final byte[] AID_ANDROID = { (byte)0xF0,
                                                (byte)0x01,
                                                (byte)0x02,
                                                (byte)0x03,
                                                (byte)0x04,
                                                (byte)0x05,
                                                (byte)0x06 };

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
            Log.d("POS","PPSE COMMAND");
            byte[] PPSE_REQUEST = createSelectAidApdu(PPSE);
            Log.d("POS","PPSE REQUEST "+ Utils.ByteToHexadecimal(PPSE_REQUEST));
            byte[] response = isoDep.transceive(PPSE_REQUEST);
            Log.d("POS","PPSE RESPONSE "+ Utils.ByteToHexadecimal(response));

            Log.d("POS","SELECT AID");
            byte[] AID_REQUEST = createSelectAidApdu(MASTERCARD_AID);
            Log.d("POS","AID REQUEST "+ Utils.ByteToHexadecimal(AID_REQUEST));
            response = isoDep.transceive(AID_REQUEST);
            Log.d("POS","AID RESPONSE "+ Utils.ByteToHexadecimal(response));

            /*
            while (isoDep.isConnected() && !Thread.interrupted()) {

            }
            */

            isoDep.close();
        }
        catch (IOException e) {
            Log.d("POS", e.toString());
        }
    }
}