package com.example.devhce;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Token {

    private final String appServerAddress="http://ee15-192-167-140-72.ngrok-free.app/addcard";
    private final String pan;
    private final String cvv;
    private final String expire;
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
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        catch (Exception e){
            return  "false";
        }

        // Check the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
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
