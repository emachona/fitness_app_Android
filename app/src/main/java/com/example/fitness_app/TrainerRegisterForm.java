package com.example.fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class TrainerRegisterForm extends AppCompatActivity {
    public String ime,bio;
    EditText eName, eBio;
    Button btn;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_register_form);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth=FirebaseAuth.getInstance();
        eName=findViewById(R.id.enterTrName);
        eBio=findViewById(R.id.enterBio);

        btn=findViewById(R.id.finish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    NapraviProfilTrener();
            }
        });
    }

    private void NapraviProfilTrener(){
        ime=eName.getText().toString().trim();
        bio=eBio.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        String uID=user.getUid();
        mDatabaseReference.child(uID).child("name").setValue(ime);
        if(!bio.isEmpty()){
            mDatabaseReference.child(uID).child("gender").setValue(bio);
            //bio za trener ke se cuva vo gender
        }

        Intent intent=new Intent(TrainerRegisterForm.this, Login.class);
        startActivity(intent);
    }
}