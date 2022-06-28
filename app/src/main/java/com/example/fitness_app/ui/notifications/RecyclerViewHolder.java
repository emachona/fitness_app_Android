package com.example.fitness_app.ui.notifications;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.User;
import com.example.fitness_app.ui.Trening;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView trener,saat,data,lokacija,poeni,mesta;
    private Button zakazi;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference tDbR;
    private FirebaseAuth mAuth;
    private String treningID;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uID = user.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User value = snapshot.getValue(User.class);
//                trenerIme = value.getName();
//                Log.d(TAG, "TrenerIme vo onDataChange: " + trenerIme);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        trener = itemView.findViewById(R.id.ime_na_trenerot);
        saat = itemView.findViewById(R.id.terminSaat);
        data = itemView.findViewById(R.id.terminData);
        lokacija = itemView.findViewById(R.id.terminLokacija);
        poeni = itemView.findViewById(R.id.pointsView);
        mesta = itemView.findViewById(R.id.placesView);
        zakazi = itemView.findViewById(R.id.reserve);

        zakazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //ja staviv klasata vo adapter :))))
            }
        });
    }

    void setDetails(Trening training){
        trener.setText(training.getTrName());
        saat.setText(training.getTime());
        data.setText(training.getDate());
        lokacija.setText(training.getLocation());
        poeni.setText(Integer.toString(training.getPoints()));
        mesta.setText(Integer.toString(training.getPlaces()));
    }
}
