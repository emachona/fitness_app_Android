package com.example.fitness_app.ui.notifications;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class TrainingsClientAdapter extends RecyclerView.Adapter<TrainingsClientAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<Trening> trainings;

    public TrainingsClientAdapter(ArrayList<Trening> trainings) {
        this.trainings = trainings;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_trainings_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Trening training = trainings.get(position);
        holder.setDetails(training);
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView trener, saat, data, lokacija, poeni, mesta;
        private Button zakazi;
        private DatabaseReference mDatabaseReference;
        private DatabaseReference tDbR;
        private FirebaseAuth mAuth;
        private ArrayList<String> cuvaj;
        private int sega,namaleni;
        private String najdenKey;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User korisnik = snapshot.getValue(User.class);
                    cuvaj = korisnik.getRezerviraniTr();
                    Log.d(TAG, "Treninzi na korisnikot se: "+cuvaj);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


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
                    int currPos = getAdapterPosition();
                    Trening currItem = trainings.get(currPos);
                    String trID = currItem.getSessionId();
                    Log.d(TAG, "Current item's ID is: " + trID);
                    //do tuka ja zema pozicijata na kliknatiot item->id na soodvetniot trening
                    tDbR = FirebaseDatabase.getInstance().getReference("trainings");
                    tDbR.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Trening tr = dataSnapshot.getValue(Trening.class);
                                Log.d(TAG, "DataSnapshot item's ID is: " + tr.getSessionId());
                                if (tr.getSessionId().equals(trID)) {
                                    //go najde treningot vo baza spored treningId i pravi potrebni promeni
                                    Log.d(TAG, "go najdov kliknat event");
                                    najdenKey = dataSnapshot.getKey();
                                    sega = tr.getPlaces();
                                    cuvaj.add(trID);
                                }
                            }
                            mDatabaseReference.child("rezerviraniTr").setValue(cuvaj);//go cuva treningot za update na poeni
                            namaleni = sega - 1;
                            tDbR.child(najdenKey).child("places").setValue(namaleni);
                            Toast.makeText(itemView.getContext(), "Reserved a place", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), "Reservation unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

//            mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    User logged = snapshot.getValue(User.class);
//                    logged.addRezervacija(cuvaj.get(0));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
        }

        void setDetails(Trening training) {
            trener.setText(training.getTrName());
            saat.setText(training.getTime());
            data.setText(training.getDate());
            lokacija.setText(training.getLocation());
            poeni.setText(Integer.toString(training.getPoints()));
            mesta.setText(Integer.toString(training.getPlaces()));
        }

    }
}
