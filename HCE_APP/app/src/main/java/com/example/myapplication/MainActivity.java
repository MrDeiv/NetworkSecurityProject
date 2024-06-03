package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.color.MaterialColorUtilitiesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private TextView cardNumber;
    private TextView owner;
    private TextView expire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cardNumber = (TextView) findViewById(R.id.cardNumber);
        owner = (TextView) findViewById(R.id.cardHolderName);
        expire = (TextView)findViewById(R.id.cardExpiry);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);



        SharedPreferences sharedPreferences = getSharedPreferences("CardData", MODE_PRIVATE);
        cardNumber.setText(Utils.insertSpaces(sharedPreferences.getString("CardNumber","1111222233334444")));
        owner.setText(sharedPreferences.getString("owner","owner"));
        expire.setText(sharedPreferences.getString("expire","MM/YY"));

        // Only for debug purpuse
        String token = sharedPreferences.getString("token","token");
        if(token != "token"){
            Toast.makeText(this, "Token "+token,Toast.LENGTH_SHORT).show();
        }

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HCE","START NEW ACTIVITY");
                Intent intent = new Intent(MainActivity.this, RegisterCard.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("CardData", MODE_PRIVATE);
        cardNumber.setText(Utils.insertSpaces(sharedPreferences.getString("CardNumber","1111222233334444")));
        owner.setText(sharedPreferences.getString("owner","ower"));
        expire.setText(sharedPreferences.getString("expire","MM/YY"));
        super.onResume();
    }
}