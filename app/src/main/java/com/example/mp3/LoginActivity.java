package com.example.mp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    ArrayList<Social> socials;

    Button add;
    private DatabaseReference databaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        add = findViewById(R.id.add);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("socials");

        socials = new ArrayList<>();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Long timestamp = (Long) dataSnapshot.getValue();

                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()) {
                    String email = dataSnapshot2.child("userEmail").getValue(String.class);
                    String uid = dataSnapshot2.child("uid").getKey();
                    String event_name = dataSnapshot2.child("eventName").getValue(String.class);
                    String date = dataSnapshot2.child("Date").getValue(String.class);
                    String desc = dataSnapshot2.child("Description").getValue(String.class);
                    String numInterested = dataSnapshot2.child("numInterested").getValue(String.class);
                    Social newSocial = new Social(event_name, email, desc, date, uid, numInterested);
                    socials.add(newSocial);
                }
                if (!socials.isEmpty()) {
                    Log.d("FETCH RESULTS:", socials.get(0).getEventName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FAILURE", "Failed to read value.", error.toException());
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewSocialActivity();
            }
        });

    }

    public void goToNewSocialActivity() {
        Intent i = new Intent(this, NewSocialActivity.class);
        startActivity(i);
    }


}
