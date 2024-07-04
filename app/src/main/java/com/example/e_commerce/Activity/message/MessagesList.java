package com.example.e_commerce.Activity.message;

public class MessagesList {

    private String name;
    private String mobile;
    private String lastMessage;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    private String profilePic;

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    private String chatKey;
    private int unssenMessages;


    public MessagesList(String name, String mobile, String lastMessage, String profilePic, int unssenMessages, String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.unssenMessages = unssenMessages;
        this.profilePic = profilePic;
        this.chatKey = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnssenMessages() {
        return unssenMessages;
    }

    public void setUnssenMessages(int unssenMessages) {
        this.unssenMessages = unssenMessages;
    }
}
