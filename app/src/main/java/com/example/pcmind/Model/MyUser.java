package com.example.pcmind.Model;

public class MyUser {
    private  String id;
    private  String name ;
    private  String email;
    private  String mobile;
public MyUser(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private  boolean isAdmin;
    private  boolean isConnected;
    public MyUser(String id, String name, String email, String mobile, boolean isAdmin, boolean isConnected) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.isAdmin = isAdmin;
        this.isConnected = isConnected;
    }


}
