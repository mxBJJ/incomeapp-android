package com.example.incomeapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference;

    public static FirebaseAuth getFirebaseAuth(){
            if(firebaseAuth == null){

                firebaseAuth = FirebaseAuth.getInstance();

            }

            return firebaseAuth;
    }


    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){

            databaseReference = FirebaseDatabase.getInstance().getReference();

        }

        return  databaseReference;

    }
}
