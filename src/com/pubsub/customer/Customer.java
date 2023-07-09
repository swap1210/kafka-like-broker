package com.pubsub.customer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import com.pubsub.EndUser;
import com.pubsub.broker.remote.CustomerBrokerInterface;
import com.pubsub.model.Song;
import com.pubsub.model.Tuple;
import java.rmi.NotBoundException;

public class Customer extends EndUser {

    public Customer(String connectionURL, Scanner scanner) {
        super(connectionURL, scanner);
    }

    @Override
    public void startExecution() {
        // Connect to the Broker via RMI
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(connectionURL.split(":")[0],
                    Integer.parseInt(connectionURL.split(":")[1]));
            CustomerBrokerInterface broker = (CustomerBrokerInterface) registry.lookup("Broker");
            int choice = -1;
            while (choice < 3) {
                System.out.print("Subscriber Menu:\n1 To List tuple\n2 To Select tuple\n3 To Exit\n" +
                        "Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (choice) {
                    case 1:
                        printTuples(broker.getTuples());
                        break;
                    case 2:
                        printTuples(broker.getTuples());
                        System.out.print("Enter the tuple id: ");
                        int tupleId = scanner.nextInt();
                        String selectedTupleName = broker.getTuples().get(tupleId).name;
                        int songChoice = -1;
                        while (songChoice < 3) {
                            System.out.print(selectedTupleName
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
                                    printSongs(broker.getTuple(selectedTupleName).songs);
                                    System.out.print("Enter the song id: ");
                                    int songId = scanner.nextInt();
                                    broker.purchaseSong(self.username, selectedTupleName, songId);
                                    String selectedSongName = broker.getTuple(selectedTupleName).songs.get(songId).name;
                                    System.out.println("Song " + selectedSongName + " bought successfully");
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }
                        }
                        break;
                }
            }
        } catch (NumberFormatException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void printSongs(List<Song> songs) {
        int j = 0;
        System.out.println("Name - Price");
        for (Song song : songs) {
            System.out.println((j++) + ". " + song);
        }
    }

    private void printTuples(List<Tuple> tuples) {
        System.out.println("Tuples:");
        int i = 0;
        System.out.println("Name");
        for (Tuple tuple : tuples) {
            System.out.println((i++) + ". " + tuple.name);
        }
    }

}
