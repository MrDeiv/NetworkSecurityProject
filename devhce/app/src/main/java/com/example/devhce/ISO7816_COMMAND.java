package com.example.devhce;

public class ISO7816_COMMAND {
    public static final byte[] SELECT_AID = {
            (byte)0x00,  // CLA
            (byte)0xa4,  // INS
            (byte)0x04,  // P1
            (byte)0x00,  // P2
            (byte)0x07,  // LC (data length = 7)
            //F0010203040506
            (byte)0xF0, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06,
            (byte)0x00   // LE
    };

    public static final byte[] READ_RECORD = {
            (byte) 0x00,  // CLA
            (byte) 0xB2,  // INS
            (byte) 0x01,  // P1
            (byte) 0x0C,  // P2
            (byte) 0x00   // length
    };

    public static final byte[] SUCCESS={
            (byte)0x90,  // CLA
            (byte)0x00,  // INS
    };

    public static final byte[] UNKNOWN = {
            (byte)6D,
            (byte)00,
    };
}
