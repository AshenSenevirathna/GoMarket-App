package com.avs.app.gomarket.models;

public class UserModel {

    String userFname;
    String userLname;
    String userEmail;
    String userPassword;
    String profileImg;

    public UserModel() {
    }

    public UserModel(String userFname, String userLname, String userEmail, String userPassword) {
        this.userFname = userFname;
        this.userLname = userLname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
