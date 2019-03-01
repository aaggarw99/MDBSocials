package com.example.mp3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SocialDetail extends AppCompatActivity {

    TextView eventNameField;
    TextView dateField;
    TextView posterField;
    TextView descField;
    TextView numInterestedField;
    CheckBox interestCheckBox;
    ImageView imgView;
    int numberInterested;
    Button back;

    ArrayList<String> uids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_detail);


        // set all UI elements
        eventNameField = findViewById(R.id.eventName);
        dateField = findViewById(R.id.date);
        posterField = findViewById(R.id.poster);
        descField = findViewById(R.id.desc);
        imgView = findViewById(R.id.img);
        interestCheckBox = findViewById(R.id.interestCheckBox);
        numInterestedField = findViewById(R.id.interested);
        back = findViewById(R.id.back);
        numberInterested = 1;

        final FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();


        // gets entry id from intent
        final Intent i = getIntent();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("/socials/"+i.getStringExtra("id"));

        final Context context = this;

        // add listener for database changes
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // fetch data
                eventNameField.setText(dataSnapshot.child("eventName").getValue(String.class));

                String userEmailSentence = "created by " + dataSnapshot.child("userEmail").getValue(String.class);
                posterField.setText(userEmailSentence);
                dateField.setText(dataSnapshot.child("Date").getValue(String.class));
                descField.setText(dataSnapshot.child("Description").getValue(String.class));

                // fetches the number of interested people string from database
                String numberInterestedString = dataSnapshot.child("numInterested").getValue(String.class);
                // converts to integer
                numberInterested = Integer.parseInt(numberInterestedString);
                // combines string with words to form a sentence
                String interestedSentence = numberInterestedString + " are interested in this event right now!";
                numInterestedField.setText(interestedSentence);

                //image shit
                StorageReference sRef = FirebaseStorage.getInstance().getReference().child(i.getStringExtra("id") + ".png");
                Glide.with(context).using(new FirebaseImageLoader()).load(sRef).into(imgView);

                // gets all the uids that are interested in this event
                uids = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.child("peopleInterested").getChildren()) {
                    if (ds.getValue(Boolean.class) != null && ds.getValue(Boolean.class) == true) {
                        String uid = ds.getKey();
                        uids.add(uid);
                    }
                }

                // checks if current user is already interested in the event, in which we change
                // checkbox
                if (uids.contains(currUser.getUid())) {
                    interestCheckBox.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FAILURE", "Failed to read value.", databaseError.toException());
            }
        });

        interestCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!interestCheckBox.isChecked()) {
                    numberInterested -= 1;
                    uids.remove(currUser.getUid());
                    // when removing a uid, set that person in the db to false
                    databaseRef.child("peopleInterested").child(currUser.getUid()).setValue(false);

                } else {
                    numberInterested += 1;
                    uids.add(currUser.getUid());
                }
                // converts updated numberInterested to string
                String newInterestedNumberString = Integer.toString(numberInterested);
                // creates sentence with it
                String newInterestedSentence = newInterestedNumberString + " are interested in this event right now!";
                numInterestedField.setText(newInterestedSentence);

                // updates database entry
                databaseRef.child("numInterested").setValue(newInterestedNumberString);

                // updates people list in database
                for (String uid : uids) {
                    databaseRef.child("peopleInterested").child(uid).setValue(true);
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });
    }

    public void goToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
