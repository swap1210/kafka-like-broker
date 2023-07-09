package com.pubsub.broker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pubsub.broker.remote.CustomerBrokerInterface;
import com.pubsub.broker.remote.SellerBrokerInterface;
import com.pubsub.model.Song;
import com.pubsub.model.Tuple;
import com.pubsub.model.User;

public class Broker extends UnicastRemoteObject implements SellerBrokerInterface, CustomerBrokerInterface {
    private static final String DATA_FILENAME = "./resources/userData.txt";
    Scanner scanner;
    List<Tuple> tuples;
    List<User> publishers;
    List<User> subscribers;
    public int startingPort;

    public Broker(Scanner scanner, int startingPort) throws RemoteException {
        this.scanner = scanner;
        this.startingPort = startingPort;
        this.tuples = new ArrayList<>();
        this.publishers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        loadUserData();
        startRegistry();
    }

    private void startRegistry() {
        try {
            // register broker in RMI registry
            Registry registry = LocateRegistry.createRegistry(startingPort);
            registry.rebind("Broker", this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // read userData.txt pipe delimited file and load into publisher and subscriber
    // list
    private void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split("\\|");

                // Extract data from the line and create a new User object
                String username = userData[0];
                String password = userData[1];
                double walletBalance = Double.parseDouble(userData[2]);
                boolean isPublisher = Boolean.parseBoolean(userData[3]);

                User user = new User(username, password, walletBalance, isPublisher);
                if (isPublisher)
                    publishers.add(user);
                else
                    subscribers.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTuple(Tuple item) throws RemoteException {
        tuples.add(item);
    }

    public Tuple getTuple(String itemName) throws RemoteException {
        for (Tuple item : tuples) {
            if (item.name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    public List<Tuple> getTuples() throws RemoteException {
        return tuples;
    }

    public void startExecution() {
        try {
            int choice = -1;
            while (choice != 0) {
                System.out.println("\n\nBroker started [" + InetAddress.getLocalHost().getHostAddress()
                        + ":" + startingPort
                        + "]");
                System.out.print("0 Exit\n" +
                        "1 List all Tuples\n" +
                        "2 List all Publishers\n" +
                        "3 List all Subscribers\n" +
                        "Enter your choice:");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("List of all topics:");
                        for (Tuple item : tuples) {
                            System.out.println(item.name);
                            for (Song song : item.songs) {
                                System.out.println("\t" + song.name);
                            }
                        }
                        break;
                    case 2:
                        System.out.println("List of all publishers:");
                        System.out.println("Username\t|");
                        for (User user : publishers) {
                            System.out.println(user.username + "\t\t|");
                        }
                        break;
                    case 3:
                        System.out.println("List of all subscribers:");
                        // print column names
                        System.out.println("Username\t|\tWalletBalance\t");
                        for (User user : subscribers) {
                            System.out.println(user.username + "\t\t|\t\t" + user.walletBalance);
                        }
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }
        } catch (

        UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSong(String tupleName, Song song) throws RemoteException {
        for (Tuple tuple : tuples) {
            if (tuple.name.equals(tupleName)) {
                tuple.songs.add(song);
                break;
            }
        }
    }

    private void saveUserData(List<User> userList, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILENAME, append))) {
            for (User user : userList) {
                writer.write(user.username + "|" + user.password + "|" + user.walletBalance + "|" + user.isPublisher);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Song purchaseSong(String customerName, String tupleName, int SongId) throws RemoteException {
        Song song = null;
        for (Tuple tuple : tuples) {
            if (tuple.name.equals(tupleName)) {
                song = tuple.songs.get(SongId);
                break;
            }
        }

        if (song != null) {
            for (User user : subscribers) {
                if (user.username.equals(customerName)) {
                    // check balance
                    if (user.walletBalance < song.price) {
                        return null;
                    } else {
                        song.copiesSold++;
                        user.walletBalance -= song.price;
                        saveUserData(publishers, false);
                        saveUserData(subscribers, true);
                        break;
                    }
                }
            }
        }
        return song;
    }

    @Override
    public User getUser(String userName, String password) throws RemoteException {
        for (User user : publishers) {
            if (user.username.equals(userName) && user.password.equals(password)) {
                return user;
            }
        }
        for (User user : subscribers) {
            if (user.username.equals(userName) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }
}
