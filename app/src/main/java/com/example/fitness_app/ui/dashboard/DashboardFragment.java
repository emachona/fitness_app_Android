package com.example.fitness_app.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitness_app.R;
import com.example.fitness_app.User;
//import com.example.fitness_app.databinding.FragmentDashboardBinding;
import com.example.fitness_app.ui.Trening;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;

public class DashboardFragment extends Fragment {
  //  private FragmentDashboardBinding binding;
    private int progress = 0;
    Button update;
    ProgressBar progressBar;
    TextView achivedTV, lvlTV;
    int achived, userLvl, beforeSync;
    int toAdd = 0;
    private DatabaseReference dbR;
    private DatabaseReference tDbR;
    private FirebaseAuth mAuth;
    private ArrayList<String> proveriTr = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        achivedTV = view.findViewById(R.id.achivedPointsShow);
        lvlTV=view.findViewById(R.id.levelShow);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        dbR = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        dbR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User korisnik = snapshot.getValue(User.class);
                proveriTr = korisnik.getRezerviraniTr();//lista na treninzi za korisnik
                beforeSync = korisnik.getAchivedPoints();//segashni poeni
                userLvl=korisnik.getLevel();//segashen level
                if(beforeSync>=100){
                    ++userLvl;
                    beforeSync=0;
                    dbR.child("level").setValue(userLvl);
                    dbR.child("achivedPoints").setValue(0);
                }
                achivedTV.setText(Integer.toString(beforeSync));
                lvlTV.setText(Integer.toString(userLvl));
                achived=beforeSync;
                updateProgressBar(beforeSync);
                Log.d(TAG, "Treninzi na korisnikot se: " + proveriTr);
                Log.d(TAG, "Level na korisnikot e: " + userLvl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update = (Button) view.findViewById(R.id.sync);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePoints();
            }
        });

        return view;
    }

    private void updateProgressBar(int progr){
        progressBar.setProgress(progr);
    }

    private void updatePoints() {//sync
        tDbR = FirebaseDatabase.getInstance().getReference("trainings");
        tDbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Trening tr = dataSnapshot.getValue(Trening.class);
                    for(int i=0;i<proveriTr.size();i++){
                        if(tr.getSessionId().equals(proveriTr.get(i)) && tr.getStatus()==0){
                            toAdd+=tr.getPoints();
                            proveriTr.set(i,"provereno");
                        }
                    }
                    Log.d(TAG, "Provereno za: "+tr.getSessionId());
                }
                if(toAdd!=0){
                    Log.d(TAG, "Vkupno za dodavanje poeni: "+toAdd);
                    achived+=toAdd;
                    Iterator<String> itr = proveriTr.iterator();
                    // Holds true till there is single element remaining in the object
                    while (itr.hasNext()) {
                        // Remove elements "provereno"
                        String x = itr.next();
                        if (x.equals("provereno")){
                            itr.remove();
                        }
                    }
                    dbR.child("rezerviraniTr").setValue(proveriTr);
                    if(achived>=100){
                        Toast.makeText(getView().getContext(), "LEVEL UPGRADED!", Toast.LENGTH_LONG).show();
                        dbR.child("achivedPoints").setValue(achived);
                    }else{
                        achivedTV.setText(Integer.toString(achived));
                        dbR.child("achivedPoints").setValue(achived);
                    }

                }else{
                    Toast.makeText(getView().getContext(), "Your points are up to date!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

//    DashboardViewModel dashboardViewModel =
//            new ViewModelProvider(this).get(DashboardViewModel.class);
//
//    binding =FragmentDashboardBinding.inflate(inflater,container,false);
//    View root = binding.getRoot();
//
//    final TextView textView = binding.textGoal;
//        dashboardViewModel.getText().
//
//    observe(getViewLifecycleOwner(),textView::setText);
//        return root;
//}
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

