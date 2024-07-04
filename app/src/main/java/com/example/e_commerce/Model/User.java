package com.example.e_commerce.Model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private int id;
    private String fullname;
    private String email;
    private String password;
    private String phoneNumber;
    private String username;
    private String role;
    private String address;
    private String avatarUrl;
    private String gender;
    private Date createAt;
    private int status;
    private static User user = null;

    public User(int id, String fullname, String email, String password, String phoneNumber
            , String username, String role, String address, String avatarUrl, String gender
            , Date createAt, int status) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.createAt = createAt;
        this.status = status;
    }

    public User(String fullname, String email, String password, String phoneNumber, String username
            , String role, String address, String avatarUrl, String gender
            , Date createAt, int status) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.createAt = createAt;
        this.status = status;
    }

    public User(int id, String fullname, String email, String password, String phoneNumber
            , String username, String role, String address, String avatarUrl) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
        this.address = address;
        this.avatarUrl = avatarUrl;
    }

    public User(String fullname, String email, String password, String phoneNumber
            , String username, String role, String address, String avatarUrl) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
        this.address = address;
        this.avatarUrl = avatarUrl;
    }

    private User() {}

    public static User getInstance(){
        if(user == null)
            user = new User();
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phoneNumber;
    }

    public void setPhone_number(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
