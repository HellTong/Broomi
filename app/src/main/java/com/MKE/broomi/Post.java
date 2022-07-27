package com.MKE.broomi;

public class Post {


    public String Pay;
    public String Title;
    public String Write;
    public String userID;
    public String destUid;
    public String profile;

    public Post(String Title, String Write, String userID, String Pay,String destUid,String profile){
        this.Title = Title;
        this.Write = Write;
        this.Pay = Pay;
        this.userID = userID;
        this.destUid = destUid;
        this.profile = profile;
    }

}
