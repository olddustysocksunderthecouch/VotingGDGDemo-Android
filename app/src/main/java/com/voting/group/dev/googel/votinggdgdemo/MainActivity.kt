package com.voting.group.dev.googel.votinggdgdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Adrian Bunge on 2018/09/08.
 */

class MainActivity : AppCompatActivity() {
    lateinit var mRef: DatabaseReference
    var mAuth: FirebaseAuth? = null

    lateinit var uid: String
    private var questionIndex: String? = null

    private var kindaTotalTextView: TextView? = null
    private var noTotalTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRef = FirebaseUtil.getDatabase().reference

        // Get Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser

        if (user != null) {
            uid = user.uid
            Log.d("MainActivity", "User ID: " + uid!!)
        } else {
            signOut()
        }

        question_textview

        kindaTotalTextView = findViewById(R.id.kinda_total_textview)
        noTotalTextView = findViewById(R.id.no_total_textview)


        // Buttons and ClickListeners
        val yesButton = findViewById<Button>(R.id.yes_button)
        yesButton.setOnClickListener { checkIfAnsweredAndAnswer("yes", questionIndex) }

        val kindaButton = findViewById<Button>(R.id.kinda_button)
        kindaButton.setOnClickListener { checkIfAnsweredAndAnswer("kinda", questionIndex) }

        val noButton = findViewById<Button>(R.id.no_button)
        noButton.setOnClickListener { checkIfAnsweredAndAnswer("no", questionIndex) }

        val signOutButton = findViewById<Button>(R.id.signout_button)
        signOutButton.setOnClickListener { signOut() }


        // Add listener for question_index
        val mQuestionIndexRef = mRef?.child("question_index")
        mQuestionIndexRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val questionIndexLong = dataSnapshot.getValue(Long::class.java)
                    questionIndex = questionIndexLong.toString()

                    // Start listeners based on the question_index
                    // These will fire every time the MainActivity is created
                    // and when ever the question_index value is changed in Firebase
                    fetchQuestion(questionIndex)

                    listenForAnswerTotals("yes", questionIndex)
                    listenForAnswerTotals("kinda", questionIndex)
                    listenForAnswerTotals("no", questionIndex)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun fetchQuestion(questionIndex: String?) {
        val mQuestionIndex = mRef!!.child("questions").child(questionIndex!!)
        mQuestionIndex.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val question = dataSnapshot.getValue(String::class.java)
                    question_textview.text = question
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun checkIfAnsweredAndAnswer(answer: String, questionIndex: String?) {
        val mFlatAnswers = mRef!!.child("flat_answers").child(questionIndex!!).child(uid!!)
        mFlatAnswers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val yourAnswer = dataSnapshot.getValue(String::class.java)
                    Toast.makeText(this@MainActivity, "You've already answered $yourAnswer to this question", Toast.LENGTH_LONG).show()

                } else {
                    val mFlatAnswers = mRef!!.child("flat_answers").child(questionIndex).child(uid!!)
                    mFlatAnswers.setValue(answer)

                    val mNestedAnswers = mRef!!.child("nested_answers").child(questionIndex).child(answer).child(uid!!)
                    mNestedAnswers.setValue(true)

                    Toast.makeText(this@MainActivity, "You've just answered: $answer", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun listenForAnswerTotals(answer: String, questionIndex: String?) {
        val mFlatAnswers = mRef!!.child("nested_answers").child(questionIndex!!).child(answer)
        mFlatAnswers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var total = "0"
                if (dataSnapshot.exists()) {
                    total = dataSnapshot.childrenCount.toString()
                }
                if (answer == "yes") {
                    yes_total_textview.text = total
                } else if (answer == "kinda") {
                    kindaTotalTextView!!.text = total
                } else {
                    noTotalTextView!!.text = total
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun signOut() {
        // Sign out of Firebase
        mAuth!!.signOut()
        // Define an intent to AuthenticationActivity and start it (go to the other activity)
        val startAuthenticationActivity = Intent(this@MainActivity, AuthenticationActivity::class.java)
        this.startActivity(startAuthenticationActivity)
        // Destroy the current activity to prevent users from being able to click back and opening the MainActivity again
        finish()
    }
}
