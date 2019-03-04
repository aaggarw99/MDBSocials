package com.example.mp3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {

    /* Logging in functionality */
    public static void loginUser(String email, String password, final FirebaseAuth mAuth, final Activity activity) {
        if (email == null || password == null) {
            Toast.makeText(activity, "Please enter your email and password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please make sure your email and password are not empty", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SUCCESS", "signInWithEmailAndPassword:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(activity.getApplicationContext(), LoginActivity.class);
                                activity.startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FAILURE", "signInWithEmailAndPassword:failure", task.getException());
                                Toast.makeText(activity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public static void registerUser(String email, String password, String first, String last, final FirebaseAuth mAuth, final Activity activity) {
        if (email == null || password == null) {
            Toast.makeText(activity, "Please enter your email and password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please make sure your email and password are not empty", Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(activity, "Please make sure your password is at least 6 characters", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Register success, update UI with the signed-in user's information
                                Log.d("SUCCESS", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                                activity.startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FAILURE", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(activity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    public static Social getSocialFromFirebase(DataSnapshot snapshot) {
        String email = snapshot.child("userEmail").getValue(String.class);
        String key = snapshot.getKey();
        String event_name = snapshot.child("eventName").getValue(String.class);
        String date = snapshot.child("Date").getValue(String.class);
        String desc = snapshot.child("Description").getValue(String.class);
        String numInterested = snapshot.child("numInterested").getValue(String.class);
        return new Social(event_name, email, desc, date, key, numInterested);
    }

    public static StorageReference getImageStorageRef(String key) {
        return FirebaseStorage.getInstance().getReference().child(key + ".png");
    }


}
