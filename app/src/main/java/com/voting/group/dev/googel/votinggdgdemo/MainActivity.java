package com.voting.group.dev.googel.votinggdgdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private String uid;
    private String questionIndex;

    private TextView questionTextView;
    private TextView yourAnswerTextView;
    private TextView totalTextView;
    private TextView yesTotalTextView;
    private TextView noTotalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference to the the Firebase Realtime Database
        mRef = FirebaseUtil.getDatabase().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
             uid = user.getUid();
             Log.d("MainActivity", "User ID: " + uid);
        }
        else{
            signOut();
        }

        // Views
        questionTextView = findViewById(R.id.question_textview);
        yourAnswerTextView = findViewById(R.id.your_answer_textview);
        totalTextView = findViewById(R.id.total_total_textview);
        yesTotalTextView = findViewById(R.id.yes_total_textview);
        noTotalTextView = findViewById(R.id.no_total_textview);

        // Buttons
        Button yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAnsweredAndAnswer("yes");
            }
        });

        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAnsweredAndAnswer("no");
            }
        });

        Button signOutButton = findViewById(R.id.signout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        // Add listener for question index
        DatabaseReference mQuestionIndexRef = mRef.child("question_index");
        mQuestionIndexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long questionIndexLong = dataSnapshot.getValue(Long.class);
                    questionIndex = String.valueOf(questionIndexLong);
                    
                    fetchQuestion(String.valueOf(questionIndex));
                    listenForAnswerTotals("yes", questionIndex);
                    listenForAnswerTotals("no", questionIndex);
                    listenForTotalResults(questionIndex);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void fetchQuestion(String questionIndex){
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

    private void checkIfAnsweredAndAnswer(final String answer){
        if(questionIndex != null) {
            DatabaseReference mNormalisedAnswers = mRef.child("normalised_answers").child(questionIndex).child(uid);
            mNormalisedAnswers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String yourAnswer = dataSnapshot.getValue(String.class);
                        Toast.makeText(MainActivity.this, "You've already answered this question", Toast.LENGTH_LONG).show();
                        yourAnswerTextView.setText(yourAnswer);
                    }
                    else{
                        DatabaseReference mNormalisedAnswers = mRef.child("normalised_answers").child(questionIndex).child(uid);
                        mNormalisedAnswers.setValue(answer);

                        DatabaseReference mDenormalisedAnswers = mRef.child("de_normalised_answers").child(questionIndex).child(answer).child(uid);
                        mDenormalisedAnswers.setValue(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void listenForTotalResults(String questionIndex){
        DatabaseReference mNormalisedAnswers = mRef.child("normalised_answers").child(questionIndex);
        mNormalisedAnswers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String total = String.valueOf(dataSnapshot.getChildrenCount());
                    totalTextView.setText(total);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void listenForAnswerTotals(final String answer, String questionIndex){
        DatabaseReference mDeNormalisedAnswers = mRef.child("de_normalised_answers").child(questionIndex).child(answer);
        mDeNormalisedAnswers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String total = "0";
                if(dataSnapshot.exists()){
                    total = String.valueOf(dataSnapshot.getChildrenCount());
                }
                if (answer.equals("yes")){
                    yesTotalTextView.setText(total);
                }
                else{
                    noTotalTextView.setText(total);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void signOut(){
        mAuth.signOut();
        Intent startAuthenticationActivity = new Intent(MainActivity.this, AuthenticationActivity.class);
        MainActivity.this.startActivity(startAuthenticationActivity);
        finish();
    }
}
