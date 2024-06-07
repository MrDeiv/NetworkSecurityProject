package com.example.pos_demo;

public class Utils {
    public static String ByteToHexadecimal(byte[] byteArray)
    {
        String hex = "";

        // Iterating through each byte in the array
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }

        return hex;
    }
    public static String insertSpaces(String input) {
        StringBuilder builder = new StringBuilder(input);

        // Insert a space every 4 characters, starting from index 4
        for (int i = 4; i < builder.length(); i += 5) {
            builder.insert(i, ' ');
        }

        return builder.toString();
    }
}
