package com.voting.group.dev.googel.votinggdgdemo;

import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Adrian Bunge on 2018/09/08.
 */

public class FirebaseUtil {

    private static FirebaseDatabase mDatabase;


    public static FirebaseDatabase getDatabase() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);
            }

            return mDatabase;
    }
}