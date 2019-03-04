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

public class RegisterFormActivity extends AppCompatActivity implements View.OnClickListener {
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

        register.setOnClickListener(this);

    }

    public void goToLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.register):
                FirebaseUtils.registerUser(email.getText().toString(), password.getText().toString(), first.getText().toString(), last.getText().toString(), mAuth, this);
                break;
        }
    }
}
