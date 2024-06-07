package com.example.devhce;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class RegisterCard extends AppCompatActivity {

    private EditText TextCreditNumber;
    private EditText TextOwner;
    private EditText TextExpiration;
    private EditText TextCVV;
    private Button buttonSubmit;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registercard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextCreditNumber = findViewById(R.id.editTextCreditNumber);
        TextExpiration = findViewById(R.id.editTextExpiration);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        TextOwner= findViewById(R.id.editTextOwner);
        TextCVV = findViewById(R.id.editTextCVV);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(ProgressBar.GONE);
        TextCreditNumber.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                String unformatted = s.toString().replace(String.valueOf(SPACE_CHAR), "");
                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < unformatted.length(); i++) {
                    formatted.append(unformatted.charAt(i));
                    if ((i + 1) % 4 == 0 && i + 1 != unformatted.length()) {
                        formatted.append(SPACE_CHAR);
                    }
                }

                TextCreditNumber.setText(formatted.toString());
                TextCreditNumber.setSelection(formatted.length());

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text change
            }
        });

        TextExpiration.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                String unformatted = s.toString().replace("/", "");
                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < unformatted.length(); i++) {
                    formatted.append(unformatted.charAt(i));
                    if (i == 1 && i + 1 != unformatted.length()) {
                        formatted.append('/');
                    }
                }

                TextExpiration.setText(formatted.toString());
                TextExpiration.setSelection(formatted.length());

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text change
            }
        });


        buttonSubmit.setOnClickListener(v -> submitForm());

    }

    private void submitForm() {
        String creditNumber = TextCreditNumber.getText().toString().trim().replace(" ", "");
        String owner = TextOwner.getText().toString().trim();
        String cvv = TextCVV.getText().toString().trim();
        String expiration = TextExpiration.getText().toString().trim().replace("/", "");

        if (!CheckForm(Arrays.asList(creditNumber, owner, cvv, expiration))) {

            Toast.makeText(this, "Fill all field", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(ProgressBar.VISIBLE);

        SubmitTask sb = new SubmitTask(this,this,progressBar,buttonSubmit);
        sb.execute(creditNumber, owner, cvv, expiration);
        Log.d("HCE", "submiting data");
    }

    private boolean CheckForm(List<String> Data){

        for(int i = 0; i< Data.size(); i++){
            if (TextUtils.isEmpty(Data.get(i))){
                return false;
            }
        }
        return true;
    }
}