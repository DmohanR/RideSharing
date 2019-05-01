package com.example.mohan.ridesharing.Pojo;

public class UpTrips {
    String tripID;
    String vin;
    String from;
    String to;
    String tripStatus;
    String userName;
    String finishedTime;

    public UpTrips(String tripID, String vin, String from, String to) {
        this.tripID = tripID;
        this.vin = vin;
        this.from = from;
        this.to = to;
    }
    public UpTrips(String tripID, String vin, String from, String to,String finishedTime) {
        this.tripID = tripID;
        this.vin = vin;
        this.from = from;
        this.to = to;
        this.finishedTime = finishedTime;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }
}
