package com.example.fitness_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.fitness_app.ui.Trening;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrainerSessions extends AppCompatActivity {
    RecyclerView svoiTreninzi;
    private TrainerSessionsAdapter adapter;
    private ArrayList<Trening> treninzi;
    DatabaseReference db;
    DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    String trenerIme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_sessions);

        db= FirebaseDatabase.getInstance().getReference("trainings");
        svoiTreninzi=findViewById(R.id.seeSessions);
        svoiTreninzi.setLayoutManager(new LinearLayoutManager(this));
        treninzi=new ArrayList<>();
        adapter=new TrainerSessionsAdapter(this,treninzi);
        svoiTreninzi.setAdapter(adapter);
        svoiTreninzi.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value=snapshot.getValue(User.class);
                trenerIme=value.getName();
                Log.d(TAG, "TrenerIme vo onDataChange: " +trenerIme);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        createListData();
    }

    private void createListData(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Trening trening=dataSnapshot.getValue(Trening.class);
                    if(trening.getTrName().equals(trenerIme) && trening.getStatus()==1) {
                        treninzi.add(trening);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}