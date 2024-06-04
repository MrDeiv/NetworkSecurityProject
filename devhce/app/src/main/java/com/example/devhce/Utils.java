package com.example.devhce;

public class Utils {
    public static String insertSpaces(String input) {
        StringBuilder builder = new StringBuilder(input);

        // Insert a space every 4 characters, starting from index 4
        for (int i = 4; i < builder.length(); i += 5) {
            builder.insert(i, ' ');
        }

        return builder.toString();
    }
}
