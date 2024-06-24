package com.example.pos_demo;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Card{
    private final String appServerAddress="http://ee15-192-167-140-72.ngrok-free.app/getcard";
    private String pan;
    private String cvv;
    private String expire;
    private String token;

    private final Boolean status = false;
    public Card(String token ){
        this.token = token;
    }

    public boolean GetCard() throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", this.token);
        String jsonInputString = jsonObject.toString();


        URL url = new URL(appServerAddress);

        // Open connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Set headers
        connection.setRequestProperty("Content-Type", "application/json; utf-8");

        // Enable input/output streams
        connection.setDoOutput(true);


        // Write JSON data to output stream
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Check the response code
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK){
            connection.disconnect();
            return false;
        }

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse the response JSON
                try {
                    JSONObject responseObject = new JSONObject(response.toString());
                    responseObject =  new JSONObject(responseObject.getString("data"));

                    this.pan = responseObject.getString("card_number");
                    this.cvv = responseObject.getString("cvv");
                    this.expire = responseObject.getString("expiry_date");
                }
                catch (Exception e){
                    return false;
                }

            }
        }
        connection.disconnect();
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Card number: "+Utils.insertSpaces(this.pan) + "\n");
        stringBuilder.append("CVV: "+this.cvv + "\n");
        stringBuilder.append("Expire: "+this.expire + "\n");
        return stringBuilder.toString();
    }



}
