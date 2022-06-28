package com.example.fitness_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
//import java.util.UUID;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button registerBtn;
    private EditText emailTxt, passTxt1, passTxt2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private DatabaseReference databaseReference;
    private RadioGroup rg;
    private RadioButton rb;
    private String currID, tip;
    User new_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mStore = FirebaseFirestore.getInstance();
        emailTxt = findViewById(R.id.emailTxt);
        passTxt1 = findViewById(R.id.passwordTxt);
        passTxt2 = findViewById(R.id.confirmPassTxt);
        rg = findViewById(R.id.type);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) findViewById(checkedId);
                tip = rb.getText().toString();
                Log.d(TAG, "tip" + tip);
            }
        });

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerBtn) {
            registerUser();
        }
    }

    private void registerUser() {
        String email = emailTxt.getText().toString().trim();
        String password1 = passTxt1.getText().toString().trim();
        String password2 = passTxt2.getText().toString().trim();
        String userType = tip.trim();
        currID = "";

        if (email.isEmpty()) {
            emailTxt.setError("Input email address");
            emailTxt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Please enter a valid email address");
            emailTxt.requestFocus();
            return;
        }

        if (password1.isEmpty()) {
            passTxt1.setError("Input password");
            passTxt1.requestFocus();
            return;
        }
        if (password1.length() < 6) {
            passTxt1.setError("Password must contain 6 or more characters");
            passTxt1.requestFocus();
            return;
        }
        if (!password1.equals(password2)) {
            passTxt2.setError("Passwords don't match");
            passTxt2.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Successful registration", Toast.LENGTH_SHORT).show();
                    new_user = new User(null, email, password1, null, null, null, null, userType,null,0,1);
                    FirebaseUser user = mAuth.getCurrentUser();
                    databaseReference.child(user.getUid()).setValue(new_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Zapishan objekt");
                                if (userType.equals("Client")) {
                                    Intent intent = new Intent(Register.this, RegisterForm.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Register.this, TrainerRegisterForm.class);
                                    startActivity(intent);
                                }
                            }
                            else{
                                Toast.makeText(Register.this, "Didn't make an object in Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "neuspeshna registracija");
                    Toast.makeText(Register.this, "Unsuccessful registration", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}