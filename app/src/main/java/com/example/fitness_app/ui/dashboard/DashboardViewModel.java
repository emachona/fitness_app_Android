package com.example.fitness_app.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
import java.util.Iterator;

public class DashboardViewModel extends ViewModel {

    //private final MutableLiveData<String> mText;
    int toAdd = 0;
    private DatabaseReference dbR;
    private DatabaseReference tDbR;
    private FirebaseAuth mAuth;
    private ArrayList<String> proveriTr = new ArrayList<>();
    int achived, goal, beforeSync;

    public DashboardViewModel() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        dbR = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User korisnik = snapshot.getValue(User.class);
                proveriTr = korisnik.getRezerviraniTr();
                beforeSync = korisnik.getAchivedPoints();
                //achivedTV.setText(Integer.toString(beforeSync));
                Log.d(TAG, "Treninzi na korisnikot se: " + proveriTr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getBeforeSync() {
        return beforeSync;
    }

}