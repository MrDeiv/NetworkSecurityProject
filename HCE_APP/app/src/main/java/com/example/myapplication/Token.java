package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Token {

    private String appServerAddress="https://66a7-192-167-140-71.ngrok-free.app/addcard";
    private String pan;
    private String cvv;
    private String expire;
    private String token;
    public Token(String pan, String cvv, String expire ){
        this.pan = pan;
        this.cvv = cvv;
        this.expire = expire;
    }

    public String Generate() throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pan", pan);
        jsonObject.put("cvv", cvv);
        jsonObject.put("expire", expire);


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
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Check the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse the response JSON
                JSONObject responseObject = new JSONObject(response.toString());
                String token = responseObject.getString("token");
                this.token = token;

            }
        }

        connection.disconnect();
        return token;
    }

    public String getToken(){
        return token;
    }
}
