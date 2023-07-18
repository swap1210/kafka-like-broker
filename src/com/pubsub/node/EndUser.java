package com.pubsub.node;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.pubsub.model.Song;
import com.pubsub.model.Tuple;
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

    public String[] inputCreds() {
        System.out.print("Enter your email/password: ");
        String userPassword = scanner.nextLine();
        String[] userPasswordSplit = userPassword.split("/");
        return userPasswordSplit;
    }

    public void printSongs(List<Song> songs) {
        int j = 0;
        System.out.println("Name - Price");
        for (Song song : songs) {
            System.out.println((j++) + ". " + song);
        }
    }

    public void printTuples(Map<String, Tuple> tuples) {
        System.out.println("Tuples:");
        int i = 0;
        System.out.println("Name");
        for (Tuple tuple : tuples.values()) {
            System.out.println((i++) + ". " + tuple.name);
        }
    }
}
