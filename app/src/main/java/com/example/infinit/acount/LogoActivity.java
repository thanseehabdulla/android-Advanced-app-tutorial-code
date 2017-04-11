package com.example.infinit.acount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        getSupportActionBar().hide();


        SharedPreferences prefs = this.getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
        final String storedUsername = prefs.getString("username", "");
        final String storedPassword = prefs.getString("password", ""); //return nothing if no pass saved
        final String userID = prefs.getString("userID", "");





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

                    if (!storedUsername.equals("") || !storedPassword.equals("")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                   finish();

            }
        }, 200);

//        TextView tx = (TextView) findViewById(R.id.logotext);
//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PermanentMarker.ttf");
//        tx.setTypeface(custom_font);
    }
}
