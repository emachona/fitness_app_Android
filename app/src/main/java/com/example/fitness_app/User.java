package com.example.fitness_app;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String email;
    public String password;
    public String name;
    public String gender;
    public String phone;
    public String bday;
    public String location;
    public String kategorija;
    public int achivedPoints,level;
    public ArrayList<String> rezerviraniTr=new ArrayList<>();

    public User(){}

    public User(String name, String email, String password, String gender, String phone, String bday, String location, String kategorija, ArrayList<String> rezervirani,int achivedPoints,int level){
        this.name=name;
        this.email=email;
        this.password=password;
        this.gender=gender;
        this.phone=phone;
        this.bday=bday;
        this.location=location;
        this.kategorija=kategorija;
        this.rezerviraniTr=rezervirani;
        this.achivedPoints=achivedPoints;
        this.level=level;
    }

    public String getKategorija(){return kategorija;}

    public String getName() {
        return name;
    }

    public String getBday() {
        return bday;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getRezerviraniTr() {
        return rezerviraniTr;
    }

    public void setRezerviraniTr(ArrayList<String> rezerviraniTr) {
        this.rezerviraniTr = rezerviraniTr;
    }
    public void addRezervacija(String rez){
        this.rezerviraniTr.add(rez);
    }

    public int getAchivedPoints() {
        return achivedPoints;
    }

    public void setAchivedPoints(int achivedPoints) {
        this.achivedPoints = achivedPoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
