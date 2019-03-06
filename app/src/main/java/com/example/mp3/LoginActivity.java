package com.example.mp3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Social> socials;

    Button add;
    RecyclerView rv;
    FeedAdapter adapter;
    private DatabaseReference databaseRef;
    FloatingActionButton fabAdd;
    FloatingActionButton fabLogout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        socials = new ArrayList<>();

        add = findViewById(R.id.add);

        adapter = new FeedAdapter(this, socials);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // starting root
        databaseRef = FirebaseDatabase.getInstance().getReference().child("socials");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                socials.clear();

                // fetching new snapshot and convert it to a social object
                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()) {
                    socials.add(0, FirebaseUtils.getSocialFromFirebase(dataSnapshot2));
                }
                // lets adapter know to reorganize recycler view
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FAILURE", "Failed to read value.", error.toException());
            }
        });

        // adding new event handling
        fabAdd = findViewById(R.id.fab);
        fabAdd.setOnClickListener(this);

        // logging out handling
        fabLogout = findViewById(R.id.logout);
        fabLogout.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.analytics:
                startActivity(new Intent(this, Analytics.class));
                return true;
        }
        return false;
    }


    private void goToMainScreen() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.fab):
                Intent i = new Intent(this, NewSocialActivity.class);
                startActivity(i);
                break;
            case (R.id.logout):
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                goToMainScreen();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
        }
    }

}
