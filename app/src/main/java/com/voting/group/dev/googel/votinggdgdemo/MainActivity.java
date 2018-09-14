package com.voting.group.dev.googel.votinggdgdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Adrian Bunge on 2018/09/08.
 */

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    // The IDE isn't happy but we don't care because we'll
    // need this as class variables later
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout for this activity - you'll then be able to find views by their ID
        setContentView(R.layout.activity_main);

        // Get Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            uid = user.getUid();
            Log.d("MainActivity", "User ID: " + uid);
        } else {
            signOut();
        }
        // View the signout button view in the xlm layout
        Button signOutButton = findViewById(R.id.signout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    // Your first method whoop'd whoop!
    private void signOut() {
        // Sign out of Firebase
        mAuth.signOut();
        // Define an intent to AuthenticationActivity and start it (go to the other activity)
        Intent startAuthenticationActivity = new Intent(MainActivity.this, AuthenticationActivity.class);
        MainActivity.this.startActivity(startAuthenticationActivity);
        // Destroy the current activity to prevent users from being able to click back and opening the MainActivity again
        finish();
    }

}
