package com.example.rnice01.pitected2.objects;

/**
 * Created by rnice01 on 3/16/2016.
 */
public class User {
    String username, password, userID, userPin, userPriveleges;

    public User (String username, String userpass, String userID, String userPin, String userPriveleges){
        this.username = username;
        this.password = userpass;
        this.userID = userID;
        this.userPin = userPin;
        this.userPriveleges = userPriveleges;

    }

    public User(){
        this.username = "user";
        this.password = "pass";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID){this.userID = userID;}

    public String getUserID(){return userID;}

    public void setUserPin(String userPin){this.userPin = userPin;}

    public String getUserPin(){return userPin;}

    public void setUserPriveleges(String userPriveleges){this.userPriveleges = userPriveleges;}

    public String getUserPriveleges(){return userPriveleges;}

}
