package com.example.fitness_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fitness_app.ui.Trening;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class CreateTraining extends AppCompatActivity {

    DatePickerDialog picker;
    EditText eWhere,eTime,eDate,ePoints,ePlaces;
    private int hour,min;
    String trenerIme="";
    String idNaTrening="";
    String where,time,date;
    int points,places,status;
    Button create;
    Trening trainingSession;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference trDbReference;
    private FirebaseAuth mAuth;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);

        mAuth=FirebaseAuth.getInstance();

        eWhere=findViewById(R.id.enterWhere);
        ePoints=findViewById(R.id.enterPoints);
        ePlaces=findViewById(R.id.enterPlaces);
        eDate=(EditText) findViewById(R.id.chooseDate);
        eDate.setInputType(InputType.TYPE_NULL);
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CreateTraining.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        eTime=(EditText) findViewById(R.id.chooseTime);
        eTime.setInputType(InputType.TYPE_NULL);
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        hour=selectedHour;
                        min=selectedMinute;
                        eTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,min));
                    }
                };
                TimePickerDialog timePickerDialog=new TimePickerDialog(CreateTraining.this,onTimeSetListener,hour,min,true);
                timePickerDialog.show();
            }
        });

        create=findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kreirajTrening();
            }
        });

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
    }

    private void kreirajTrening(){
        status=1;

        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(12);
        for(int i=0;i<12;++i){
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        idNaTrening=sb.toString();

        where=eWhere.getText().toString().trim();
        time=eTime.getText().toString().trim();
        date=eDate.getText().toString().trim();
        points= Integer.parseInt(ePoints.getText().toString());
        places= Integer.parseInt(ePlaces.getText().toString());

        Log.d(TAG, "TrenerIme pred da kreira objekt: " +trenerIme);
        trainingSession=new Trening(trenerIme,time,date,where,points,places,status,idNaTrening);
        Log.d(TAG, "Pravi objekt od Trening");
        trDbReference=FirebaseDatabase.getInstance().getReference("trainings");
        trDbReference.child((UUID.randomUUID().toString())).setValue(trainingSession)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Vleguva vo task successful");
                            Toast.makeText(CreateTraining.this, "Successfully created training session", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateTraining.this,TrainerProfile.class));
                        }
                        else{
                            Log.d(TAG, "Task UNsuccessful");
                            Toast.makeText(CreateTraining.this, "Creating training session UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}