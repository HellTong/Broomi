package com.MKE.broomi;

public class UserAccount
{
    public UserAccount() { }


    public String emailId;
    public String password;
    public String idToken;
    public String userNickname;
    public String phoneNumber;
    public String profileImageUrl;
    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }



    public UserAccount(String userNickname, String phoneNumber){
        this.userNickname = userNickname;
        this.phoneNumber = phoneNumber;
    }

    public String getIdToken() {return idToken;}
    public void setIdToken(String idToken) {this.idToken = idToken;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password; }

    public String getEmailId() {return emailId;}
    public void setEmailId(String emailId) {this.emailId = emailId;}

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getUserNickname() { return userNickname; }
    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }
}
