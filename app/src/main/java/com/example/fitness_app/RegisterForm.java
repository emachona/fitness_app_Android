package com.example.fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.sql.Ref;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterForm extends AppCompatActivity {
    DatePickerDialog picker;
    EditText ename, ephone, eText;
    public String lat_my = "0";
    public String long_my = "0";
    public String getloc;
    public String address,city;
    Button btn;
    public String ime, pol, tel, lokac, rod;
    private RadioGroup rg;
    private RadioButton rb;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        Toast.makeText(RegisterForm.this, "Choose your location first!", Toast.LENGTH_SHORT).show();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth=FirebaseAuth.getInstance();
        ename=(EditText) findViewById(R.id.enterName);
        ephone=(EditText) findViewById(R.id.enterTel);
        rg = (RadioGroup) findViewById(R.id.pol);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb= (RadioButton) findViewById(checkedId);
                pol = rb.getText().toString();
            }
        });
        eText=(EditText) findViewById(R.id.enterBday);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btn=findViewById(R.id.done);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NapraviProfil();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onResume(){
        super.onResume();
        getloc=getIntent().getStringExtra("myloc");
      //  Log.d("TAG",getloc);
    }


    private void NapraviProfil() throws IOException {
        ime=ename.getText().toString().trim();
        tel=ephone.getText().toString().trim();
        rod=eText.getText().toString().trim();
        lokac=getloc;

        FirebaseUser user = mAuth.getCurrentUser();
        String uID=user.getUid();
        mDatabaseReference.child(uID).child("name").setValue(ime);
        mDatabaseReference.child(uID).child("gender").setValue(pol);
        mDatabaseReference.child(uID).child("phone").setValue(tel);
        mDatabaseReference.child(uID).child("bday").setValue(rod);
        mDatabaseReference.child(uID).child("location").setValue(lokac);

        Intent intent=new Intent(RegisterForm.this, Login.class);
        startActivity(intent);
    }

}