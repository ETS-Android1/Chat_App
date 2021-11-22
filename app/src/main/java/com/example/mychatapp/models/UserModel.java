package com.example.mychatapp.models;

public class UserModel {
    private String userName, userUID;

    public UserModel() {
    }

    public UserModel(String userName, String userUID) {
        this.userName = userName;
        this.userUID = userUID;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
