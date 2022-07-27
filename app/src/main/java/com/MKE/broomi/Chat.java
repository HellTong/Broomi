package com.MKE.broomi;

public class Chat {
    private String userID;
    private String oppoID;
    private String chatWrite;


    public Chat(String userID,String chatWrite, String oppoID){
        this.userID = userID;
        this.chatWrite = chatWrite;
        this.oppoID = oppoID;
    }
    public String getOppoID() {return oppoID;}

    public void setOppoID(String oppoID) {this.oppoID = oppoID;}
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChatWrite() {
        return chatWrite;
    }

    public void setChatWrite(String chatWrite) {
        this.chatWrite = chatWrite;
    }


}
