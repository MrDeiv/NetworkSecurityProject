package com.example.pos_demo;

import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Bundle;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ReaderCallback{

    private NfcAdapter nfcAdapter;
    private TextView textView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        textView = findViewById(R.id.fullScreenTextView);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);

        textView.append("[*] POS EMULATOR\n----------------------------\n");

    }
    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableReaderMode(this,
                this,
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null);
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableReaderMode(this);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        IsoDepTransceiver transceiver = new IsoDepTransceiver(isoDep,this);
        Thread thread = new Thread(transceiver);
        thread.start();
    }


}