package com.example.fitness_app.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitness_app.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String displayName, displayBday, displayTel, displayGender, displayLoc;

    public HomeViewModel() {

        mText = new MutableLiveData<>();
//        FirebaseUser user=mAuth.getCurrentUser();
//        databaseReference= FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User value=snapshot.getValue(User.class);
//                displayName=value.getName();
//                mText.setValue(displayName);
//                displayBday=value.getBday();
//                mText.setValue(displayBday);
//                displayGender=value.getGender();
//                displayTel= value.getPhone();
//                displayLoc= value.getLocation();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



    }

    public LiveData<String> getText() {
        return mText;
    }
}