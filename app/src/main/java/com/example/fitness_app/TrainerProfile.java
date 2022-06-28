package com.example.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainerProfile extends AppCompatActivity {
    FloatingActionButton fab;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String displayName,displayBio;
    TextView name,bio;
    Button seeTrainings,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        name=findViewById(R.id.dispTrName);
        bio=findViewById(R.id.dispBio);
        logout=findViewById(R.id.logoutTrButton);
        seeTrainings=findViewById(R.id.svoiTreninzi);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrainerProfile.this,CreateTraining.class));
            }
        });

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value=snapshot.getValue(User.class);
                displayName=value.getName();
                name.setText(displayName);
                if(!(value.getGender().isEmpty())){
                    displayBio=value.getGender();
                    bio.setText(displayBio);
                    bio.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seeTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(TrainerProfile.this, TrainerSessions.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TrainerProfile.this, MainActivity.class));
            }
        });
    }
}