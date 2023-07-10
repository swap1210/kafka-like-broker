package com.pubsub.node.seller;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.pubsub.broker.remote.SellerBrokerInterface;
import com.pubsub.model.Song;
import com.pubsub.model.Tuple;
import com.pubsub.node.EndUser;

public class Seller extends EndUser {

    public Seller(String connectionURL, Scanner scanner) {
        super(connectionURL, scanner);
    }

    public void startExecution() {
        try {
            // Connect to the Broker via RMI
            Registry registry = LocateRegistry.getRegistry(connectionURL.split(":")[0],
                    Integer.parseInt(connectionURL.split(":")[1]));
            SellerBrokerInterface broker = (SellerBrokerInterface) registry.lookup("Broker");
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
            while (choice < 3) {
                System.out.print("\n\nPublisher Menu:\n1 To Add tuple\n2 To Select tuple\n3 To Exit\n" +
                        "Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (choice) {
                    case 1:
                        System.out.print("Enter new tuple name: ");
                        String tupleName = scanner.nextLine();
                        broker.addTuple(new Tuple(tupleName));
                        break;
                    case 2:
                        System.out.println("Select from Tuples:");
                        printTuples(broker.getTuples());
                        System.out.print("Enter the tuple id: ");
                        int tupleId = scanner.nextInt();
                        String selectedTupleName = broker.getTuples().get(tupleId).name;
                        int SongChoice = -1;
                        while (SongChoice < 3) {
                            System.out.print(selectedTupleName
                                    + " tuple Selected\n" + //
                                    "1 To List Songs\n" + //
                                    "2 To Add Song\n" + //
                                    "3 To Exit\n" + //
                                    "Enter your choice: ");
                            SongChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character
                            switch (SongChoice) {
                                case 1:
                                    System.out.println("Songs:");
                                    System.out.println("Name - Price");
                                    printSongs(broker.getTuple(selectedTupleName).songs);
                                    break;
                                case 2:
                                    System.out.print("Enter new song name: ");
                                    String songName = scanner.nextLine();
                                    System.out.print("Enter new song price: ");
                                    double songPrice = scanner.nextDouble();
                                    scanner.nextLine(); // Consume newline character
                                    System.out.print("Enter new song length: ");
                                    long songLength = scanner.nextLong();
                                    scanner.nextLine(); // Consume newline character
                                    broker.addSong(selectedTupleName, new Song(songName, songPrice, songLength));
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                                    break;
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
