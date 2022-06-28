package com.example.fitness_app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitness_app.MainActivity;
import com.example.fitness_app.MapsActivity;
import com.example.fitness_app.R;
import com.example.fitness_app.User;
import com.example.fitness_app.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView name, bday, tel, gender, loc;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String displayName, displayBday, displayTel, displayGender, displayLoc;
    Button savePhChanges, logoutBtn;
    EditText changeNmbr;
    private DatabaseReference mDatabaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        name = view.findViewById(R.id.text_home);
        bday = view.findViewById(R.id.textView2);
        tel = view.findViewById(R.id.textView4);
        gender = view.findViewById(R.id.textView3);
        loc = view.findViewById(R.id.textView5);
        changeNmbr = view.findViewById(R.id.changePhone);
//        changeLctn = view.findViewById(R.id.changeLocation);
        savePhChanges = view.findViewById(R.id.savePhoneChanges);
//        saveLocChanges=view.findViewById(R.id.saveLocChanges);
        logoutBtn = view.findViewById(R.id.logoutButton);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        String uID = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                displayName = value.getName();
                name.setText(displayName);
                displayBday = value.getBday();
                bday.setText(displayBday);
                displayGender = value.getGender();
                gender.setText(displayGender);
                displayTel = value.getPhone();
                tel.setText(displayTel);
                displayLoc = value.getLocation();
                loc.setText(displayLoc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        savePhChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNumber = changeNmbr.getText().toString().trim();
                if (!newNumber.isEmpty()) {
                    mDatabaseReference.child(uID).child("phone").setValue(newNumber);
                    tel.setText(newNumber);
                }
            }
        });

//        saveLocChanges.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getloc=getActivity().getIntent().getStringExtra("myloc");
//                loc.setText(getloc);
//                mDatabaseReference.child(uID).child("location").setValue(getloc);
//            }
//        });
//
//        changeLctn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                intent.putExtra("od","change");
//                startActivity(intent);
//            }
//        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return view;
    }
}