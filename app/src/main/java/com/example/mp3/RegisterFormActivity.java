package com.example.mp3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFormActivity extends AppCompatActivity {
    //registerUser(email.getText().toString(), password.getText().toString());
    private FirebaseAuth mAuth;


    private EditText email;
    private EditText password;
    private EditText first;
    private EditText last;
    private Button register;
    // private EditText hometown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(email.getText().toString(), password.getText().toString(), first.getText().toString(), last.getText().toString());
            }
        });

    }
    /* Registering a user */
    public void registerUser(String email, String password, String first, String last) {
        if (email == null || password == null) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please make sure your email and password are not empty", Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Please make sure your password is at least 6 characters", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Register success, update UI with the signed-in user's information
                                Log.d("SUCCESS", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                goToLogin();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FAILURE", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterFormActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    public void goToLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
