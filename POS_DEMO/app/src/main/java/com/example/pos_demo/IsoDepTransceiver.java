package com.example.pos_demo;


import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.nfc.tech.IsoDep;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

public class IsoDepTransceiver implements Runnable {


    private IsoDep isoDep;

    private Activity activity;
    private TextView textView;

    private ProgressBar progressBar;

    byte[] response;

    private String token;
    public IsoDepTransceiver(IsoDep isoDep, Activity activity) {

        this.isoDep = isoDep;
        this.activity = activity;
        this.textView = this.activity.findViewById(R.id.fullScreenTextView);
        this.progressBar = this.activity.findViewById(R.id.progressBar);
        activity.runOnUiThread(() -> {
            textView.setTextSize(10);
        });
    }

    public void print(String data){
        activity.runOnUiThread(() -> {
            textView.append(data+"\n");
        });
    }

    @Override
    public void run() {
        int messageCounter = 0;
        try {
            isoDep.connect();

             Log.d("hce_pos","SENDING SELECT AID");
             response = isoDep.transceive(ISO7816_COMMAND.SELECT_AID);
             if(!Arrays.equals(response,ISO7816_COMMAND.SUCCESS)){
                 print("Error reading card)");
                 isoDep.close();
             }
             response = isoDep.transceive(ISO7816_COMMAND.READ_RECORD);
             this.token = new String(response);
             print("Token: "+ this.token);

            isoDep.close();
        }
        catch (IOException e) {
            Log.d("hce_pos", e.toString());
        }

        activity.runOnUiThread(() -> {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        });

        Card card = new Card(token);

        try {
            if(card.GetCard()){
                print(card.toString());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        activity.runOnUiThread(() -> {
            progressBar.setVisibility(ProgressBar.GONE);
        });


    }
}