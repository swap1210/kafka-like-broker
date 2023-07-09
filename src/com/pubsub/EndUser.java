package com.pubsub;

import java.util.Scanner;

import com.pubsub.model.User;

public abstract class EndUser {
    public User self;
    public String connectionURL;
    public Scanner scanner;

    public EndUser(String connectionURL, Scanner scanner) {
        this.scanner = scanner;
        this.connectionURL = connectionURL;
    }

    public abstract void startExecution();
}
