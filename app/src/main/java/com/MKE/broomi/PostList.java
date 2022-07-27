package com.MKE.broomi;

public class PostList {
    private String Title;
    private String Pay;
    private String UserID;
    private String Write;
    private String destUid;
    private String Profile;

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    //성철
    public PostList(String Title, String Pay, String UserID, String Write, String destUid){
        this.Title = Title;
        this.Pay = Pay;
        this.UserID = UserID;
        this.Write = Write;
        this.destUid = destUid;
    }

    public String getDestUid() {
        return destUid;
    }

    public void setDestUid(String destUid) {
        this.destUid = destUid;
    }





    public String getWrite() {
        return Write;
    }

    public void setWrite(String write) {
        Write = write;
    }

    public PostList(){}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPay() {
        return Pay;
    }

    public void setPay(String pay) {
        Pay = pay;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
