package com.example.controller;

public class UserEndpointConstants {
    
    private UserEndpointConstants() {
        // Private constructor to prevent instantiation
    }
    
    public static final String USER_BASE = "/user";
    public static final String USER_ID = USER_BASE + "/{id}";
    public static final String USER_DELETE = USER_ID + "/delete";
}