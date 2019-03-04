package com.example.mp3;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NewSocialActivity extends AppCompatActivity implements View.OnClickListener{

    TextView eventNameField;
    TextView dateField;
    TextView descriptionField;
    ImageButton uploadImage;
    Button submit;
    private Uri uri = null;

    // Firebase shit
    private DatabaseReference databaseRef;

    StorageReference sref, imgref;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_social);

        // instantiating firebase bullshit

        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        // creates new object in socials and gets the key for it
        final String key = databaseRef.child("socials").push().getKey();
        this.key = key;


        //  storage url for images
        sref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocials-8e5ef.appspot.com/");
        // imgref is one root down from storage
        imgref = sref.child(key + ".png");

        // connecting xml items
        eventNameField = findViewById(R.id.ename);
        dateField = findViewById(R.id.date);
        descriptionField = findViewById(R.id.desc);
        uploadImage = findViewById(R.id.uploadimg);
        // set default uploadImage
        int resID = getResources().getIdentifier("round_add_photo_alternate_24", "drawable",  getPackageName());
        uploadImage.setImageResource(resID);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        uploadImage.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image from gallery result
        if (requestCode == 2 && resultCode == RESULT_OK){
            uri = data.getData();
            // sets the imageview to this image
            uploadImage.setImageURI(uri);

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.uploadimg):
                // creates new intent for getting images from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                // we use the Result callback to use this data (see below)
                startActivityForResult(galleryIntent, 2);
                break;

            case (R.id.submit):
                Toast.makeText(NewSocialActivity.this, "POSTING…", Toast.LENGTH_LONG).show();

                if (uri != null) {
                    // fetch values from xml
                    final String event_name = eventNameField.getText().toString().trim();
                    // DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    final String strDate = dateField.getText().toString();
                    final String desc = descriptionField.getText().toString().trim();

                    // get user information
                    final FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                    final String poster = currUser.getEmail();
                    final String interested = "1";

                    // do a check for empty fields
                    if (TextUtils.isEmpty(event_name) || TextUtils.isEmpty(strDate) || TextUtils.isEmpty(desc)) {
                        Toast.makeText(NewSocialActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // puts the image into the imgref storage database
                    imgref.putFile(uri).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewSocialActivity.this, "File Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            databaseRef.child("socials").child(key).child("userEmail").setValue(poster);
                            databaseRef.child("socials").child(key).child("eventName").setValue(event_name);
                            databaseRef.child("socials").child(key).child("Date").setValue(strDate);
                            databaseRef.child("socials").child(key).child("Description").setValue(desc);
                            // by default, each event starts with 1 interested
                            databaseRef.child("socials").child(key).child("numInterested").setValue(interested);
                            databaseRef.child("socials").child(key).child("peopleInterested").child(currUser.getUid()).setValue(true);
                            startActivity(new Intent(NewSocialActivity.this, LoginActivity.class));
                        }
                    });
                }
                break;
        }
    }
}