package com.example.fitness_app.ui;

public class Trening {
    public String trName, time,date, location,sessionId;
    public int points,places,status;

    public Trening(){}

    public Trening(String trName,String time,String date, String location, int points,int places,int status,String sessionId){
        this.trName=trName;
        this.time=time;
        this.date=date;
        this.location=location;
        this.points=points;
        this.places=places;
        this.status=status;
        this.sessionId=sessionId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getTrName() { return trName; }
    public String getTime(){return time;}
    public String getDate() { return date; }
    public String getLocation(){return location;}
    public int getPoints(){return points;}
    public int getPlaces(){return  places;}
    public int getStatus() { return status; }
}
