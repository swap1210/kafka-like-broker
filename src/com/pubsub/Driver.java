package com.pubsub;

import java.rmi.RemoteException;
import java.util.Scanner;

import com.pubsub.broker.Broker;
import com.pubsub.node.customer.Customer;
import com.pubsub.node.seller.Seller;

public class Driver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select the mode: \n1. Publisher\n2. Subscriber\n3. Broker\nEnter your choice: ");

        int mode = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (mode) {
            case 1:
                Seller sender = new Seller(args[0], scanner);
                sender.startExecution();
                break;
            case 2:
                Customer customer = new Customer(args[0], scanner);
                customer.startExecution();
                break;
            case 3:
                Broker broker;
                try {
                    broker = new Broker(scanner, Integer.parseInt(args[0]));
                    broker.startExecution();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Invalid mode selected.");
                break;
        }

        scanner.close();
    }
}
