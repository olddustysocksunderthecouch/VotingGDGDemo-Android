package com.voting.group.dev.googel.votinggdgdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Adrian Bunge on 2018/09/08.
 */

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    // The IDE isn't happy but we don't care because we'll
    // need this as class variables later
    private String uid;

    private String questionIndex;

    private TextView questionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout for this activity - you'll then be able to find views by their ID
        setContentView(R.layout.activity_main);

        // Reference to the the Firebase Realtime Database
        // The Firebase Utils class enables the super smart persistence of data in the app
        // and will also cache writes to the database if there is no internet connectivity
        // It'll be hard for you to fully appreciate how truly amazing and awesome this is!
        mRef = FirebaseUtil.getDatabase().getReference();

        // Get Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            uid = user.getUid();
            Log.d("MainActivity", "User ID: " + uid);
        } else {
            signOut();
        }

        questionTextView = findViewById(R.id.question_textview);

        // Buttons and ClickListeners
        Button yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAnsweredAndAnswer("yes", questionIndex);
            }
        });

        Button kindaButton = findViewById(R.id.kinda_button);
        kindaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAnsweredAndAnswer("kinda", questionIndex);
            }
        });

        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAnsweredAndAnswer("no", questionIndex);
            }
        });

        Button signOutButton = findViewById(R.id.signout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        // Add listener for question_index
        DatabaseReference mQuestionIndexRef = mRef.child("question_index");
        mQuestionIndexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long questionIndexLong = dataSnapshot.getValue(Long.class);
                    questionIndex = String.valueOf(questionIndexLong);

                    // Start listeners based on the question_index
                    // These will fire every time the MainActivity is created
                    // and when ever the question_index value is changed in Firebase
                    fetchQuestion(questionIndex);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void fetchQuestion(String questionIndex) {
        DatabaseReference mQuestionIndex = mRef.child("questions").child(questionIndex);
        mQuestionIndex.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String question = dataSnapshot.getValue(String.class);
                    questionTextView.setText(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void checkIfAnsweredAndAnswer(final String answer, final String questionIndex) {
        DatabaseReference mFlatAnswers = mRef.child("flat_answers").child(questionIndex).child(uid);
        mFlatAnswers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String yourAnswer = dataSnapshot.getValue(String.class);
                    // If and answer exists in the flatAnswers node
                    // show a toast at the bottom of the screen to tell the user
                    Toast.makeText(MainActivity.this, "You've already answered " + yourAnswer + " to this question", Toast.LENGTH_LONG).show();

                } else {
                    // Otherwise set the answer the user has selected to both the flat_answers node and the nested_answers
                    // Note that you use the user's Unique ID (UID)
                    DatabaseReference mFlatAnswers = mRef.child("flat_answers").child(questionIndex).child(uid);
                    mFlatAnswers.setValue(answer);

                    DatabaseReference mNestedAnswers = mRef.child("nested_answers").child(questionIndex).child(answer).child(uid);
                    mNestedAnswers.setValue(true);

                    Toast.makeText(MainActivity.this, "You've just answered: " + answer, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

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
