package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.cardNumber);
        textView.setText("1234 5678 9012 3478"); //set text for text view

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event here
                // For example, you can open a new activity or perform some action
                // Replace the toast with your desired action
                Log.d("HCE","START NEW ACTIVITY");
                Intent intent = new Intent(MainActivity.this, RegisterCard.class);
                startActivity(intent);
            }
        });

    }
}