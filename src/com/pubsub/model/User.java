package com.pubsub.model;

public class User {
    public String username;
    public String password;
    public double walletBalance;
    public boolean isPublisher;

    public User(String username, String password, double walletBalance, boolean isPublisher) {
        this.username = username;
        this.password = password;
        this.walletBalance = walletBalance;
        this.isPublisher = isPublisher;
    }
}
