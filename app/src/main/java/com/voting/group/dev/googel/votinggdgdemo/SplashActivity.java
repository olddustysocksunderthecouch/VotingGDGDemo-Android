package com.voting.group.dev.googel.votinggdgdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the firebase auth instance and then get the current user object
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            //Intents are always formatted "where are you leaving from", "where are you going to"
            Intent intent = new Intent(SplashActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            Log.e("SplashActivity", "User is null"); // Log.e Will be formatted as an error in logcat
            finish(); //the activity after the intent to prevent back navigation
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            Log.d("SplashActivity", "User is signed in");
            // The only difference between Log.e and Log.d is the way the message is
            // formatted in logcat - nothing special
        }
    }
}
