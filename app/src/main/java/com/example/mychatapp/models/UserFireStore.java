package com.example.mychatapp.models;

public class UserFireStore {
    private String userName;
    private String userImage;
    private String uid;
    private String status;

    public UserFireStore() {
    }

    public UserFireStore(String userName, String userImage, String uid, String status) {
        this.userName = userName;
        this.userImage = userImage;
        this.uid = uid;
        this.status = status;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
