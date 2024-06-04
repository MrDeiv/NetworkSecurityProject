package com.example.devhce;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;


public class SubmitTask extends AsyncTask<String, Void, Boolean> {

    private final ProgressBar progressBar;
    private final Button button;
   private final RegisterCard parentClass;

    private final Context context;

    private final Boolean status = false;
    public SubmitTask(Context context,RegisterCard parent, ProgressBar progressBar, Button Button){
        this.button = Button;
        this.progressBar = progressBar;
        this.parentClass = parent;
        this.context = context;

    }
    @Override
    protected Boolean doInBackground(String... params) {

        Token token = new Token(params[0],params[2],params[3]);

        try {
            token.Generate();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String data = token.getToken();
        if(TextUtils.isEmpty(data)){
            return false;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("CardData", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("CardNumber",params[0]);
        editor.putString("owner",params[1]);
        editor.putString("expire",params[3]);
        editor.putString("token",data);
        editor.apply();


        Log.d("HCE","Token is "+data);
        return true;
    }

    public boolean getStat(){
        return status;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // Hide the progress bar and enable the submit button
        progressBar.setVisibility(ProgressBar.GONE);

        if (result) {
            Toast.makeText(parentClass, "Card Register successfully", Toast.LENGTH_SHORT).show();
            parentClass.finish();
        } else {
            Toast.makeText(parentClass, "Error in card Registration", Toast.LENGTH_SHORT).show();
        }
    }
}