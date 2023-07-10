package com.pubsub.node.customer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pubsub.broker.remote.CustomerBrokerInterface;
import com.pubsub.model.Song;
import com.pubsub.node.EndUser;

import java.rmi.NotBoundException;

public class Customer extends EndUser {
    List<Song> songs;

    public Customer(String connectionURL, Scanner scanner) {
        super(connectionURL, scanner);
        songs = new ArrayList<>();
    }

    @Override
    public void startExecution() {
        // Connect to the Broker via RMI
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(connectionURL.split(":")[0],
                    Integer.parseInt(connectionURL.split(":")[1]));
            CustomerBrokerInterface broker = (CustomerBrokerInterface) registry.lookup("Broker");
            while (self == null) {
                String[] userPasswordSplit = inputCreds();
                if (userPasswordSplit.length != 2) {
                    System.out.println("Invalid credentials2");
                    continue;
                }
                self = broker.getUser(userPasswordSplit[0], userPasswordSplit[1]);
                if (self == null) {
                    System.out.println("Invalid credentials");
                }
            }
            int choice = -1;
            while (choice < 4) {
                System.out.print(
                        "Subscriber Menu:\n1 To List Purchased Songs\n2 To Select tuple\n3 For Balance\n4 To Exit\n" +
                                "Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (choice) {
                    case 1:
                        printSongs(songs);
                        break;
                    case 2:
                        printTuples(broker.getTuples());
                        System.out.print("Enter the tuple id: ");
                        int tupleId = scanner.nextInt();
                        String selectedTupleName = broker.getTuples().get(tupleId).name;
                        int songChoice = -1;
                        while (songChoice < 3) {
                            System.out.print("\n\n" + selectedTupleName
                                    + " tuple Selected\n" + //
                                    "1 To List Songs\n" + //
                                    "2 To Buy Song\n" + //
                                    "3 To Exit\n" + //
                                    "Enter your choice: ");
                            songChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character
                            switch (songChoice) {
                                case 1:
                                    System.out.println("Songs:");
                                    printSongs(broker.getTuple(selectedTupleName).songs);
                                    break;
                                case 2:
                                    System.out.println("Select from Songs:");
                                    printSongs(broker.getTuple(selectedTupleName).songs);
                                    System.out.print("Enter the song id: ");
                                    int songId = scanner.nextInt();
                                    Song newSong = broker.purchaseSong(self.username, selectedTupleName, songId);
                                    if (newSong == null) {
                                        System.out.println("Insufficient balance");
                                        break;
                                    } else {
                                        // refresh walet with new balance
                                        self = broker.getUser(self.username, self.password);
                                        songs.add(newSong);
                                        System.out.println("Song " + newSong + " bought successfully");
                                    }
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Balance: $" + self.walletBalance);
                        break;
                }
            }
        } catch (NumberFormatException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
