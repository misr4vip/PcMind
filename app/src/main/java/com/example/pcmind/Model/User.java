package com.example.pcmind.Model;

public  class User {
    private static String id;
    private static String name ;
    private static String email;
    private static String mobile;
    private static boolean isAdmin;
    private static boolean isConnected;


    public User(String id, String name, String email,String mobile, boolean isAdmin, boolean isConnected) {
        User.id = id;
        User.name = name;
        User.email = email;
        User.mobile = mobile;
        User.isAdmin = isAdmin;
        User.isConnected = isConnected;
    }
    public static String getMobile() {
        return User.mobile;
    }

    public static void setMobile(String mobile) {
        User.mobile = mobile;
    }
    public static  String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static  boolean isAdmin() {
        return isAdmin;
    }

    public static void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static void setConnected(boolean connected) {
        isConnected = connected;
    }


}
