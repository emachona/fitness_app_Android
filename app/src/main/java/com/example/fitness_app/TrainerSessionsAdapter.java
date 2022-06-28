package com.example.fitness_app;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.ui.Trening;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrainerSessionsAdapter extends RecyclerView.Adapter<TrainerSessionsAdapter.TrainingSessionsHolder> {
    private Context context;
    private ArrayList<Trening> trainings;

    public TrainerSessionsAdapter(Context context, ArrayList<Trening> trainings){
        this.context=context;
        this.trainings=trainings;
    }

    @NonNull
    @Override
    public TrainingSessionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.trsessions_layout_item,
                parent,false);
        return new TrainingSessionsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingSessionsHolder holder, int position) {
        Trening training=trainings.get(position);
        holder.setDetails(training);
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    class TrainingSessionsHolder extends RecyclerView.ViewHolder{
        private TextView trener,saat,data,lokacija,poeni,mesta;
        private Button otkazi,potvrdi;
        private DatabaseReference mDatabaseReference;
        private DatabaseReference tDbR;
        private FirebaseAuth mAuth;
        private String trenerIme;

        public TrainingSessionsHolder(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String uID = user.getUid();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User value = snapshot.getValue(User.class);
                    trenerIme = value.getName();
                    Log.d(TAG, "TrenerIme vo onDataChange: " + trenerIme);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            trener = itemView.findViewById(R.id.myName);
            saat = itemView.findViewById(R.id.terminTime);
            data = itemView.findViewById(R.id.terminDate);
            lokacija = itemView.findViewById(R.id.terminLocation);
            poeni = itemView.findViewById(R.id.pointsShow);
            mesta = itemView.findViewById(R.id.placesShow);
            otkazi = itemView.findViewById(R.id.cancelSession);
            potvrdi = itemView.findViewById(R.id.confirmSession);
            otkazi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //status=2;
                    Log.d(TAG, "kliknato CANCEL");
                    int currPos = getAdapterPosition();
                    Trening currItem=trainings.get(currPos);
                    String trID=currItem.getSessionId();
                    Log.d(TAG, "Current item's ID is: "+trID);
                    tDbR = FirebaseDatabase.getInstance().getReference("trainings");
                    tDbR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Trening trening = dataSnapshot.getValue(Trening.class);
                                Log.d(TAG, "DataSnapshot item's ID is: "+trening.getSessionId());
                                if (trening.getTrName().equals(trenerIme) && trening.getSessionId().equals(trID)) {
                                    tDbR.child(dataSnapshot.getKey()).child("status").setValue(2);
                                    trening.setStatus(2);
                                    Log.d(TAG, "smenet status");
                                    Toast.makeText(itemView.getContext(), "Successfully cancelled", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), "Cancelling unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                    context.startActivity(new Intent(itemView.getContext(),TrainerProfile.class));
                }
            });
            potvrdi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //status=0;
                    Log.d(TAG, "kliknato CONFIRM");
                    int currPos = getAdapterPosition();
                    Trening currItem=trainings.get(currPos);
                    String trID=currItem.getSessionId();
                    Log.d(TAG, "Current item's ID is: "+trID);
                    tDbR = FirebaseDatabase.getInstance().getReference("trainings");
                    tDbR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Trening trening = dataSnapshot.getValue(Trening.class);
                                Log.d(TAG, "DataSnapshot item's ID is: "+trening.getSessionId());
                                if (trening.getTrName().equals(trenerIme) && trening.getSessionId().equals(trID)) {
                                    tDbR.child(dataSnapshot.getKey()).child("status").setValue(0);
                                    trening.setStatus(0);
                                    Log.d(TAG, "smenet status");
                                    Toast.makeText(itemView.getContext(), "Successfully confirmed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), "Confirm unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                    context.startActivity(new Intent(itemView.getContext(),TrainerProfile.class));
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
}
