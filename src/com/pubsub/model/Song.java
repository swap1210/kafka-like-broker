package com.pubsub.model;

public class Song implements java.io.Serializable {
    public String name;
    public double price;
    public int copiesSold;

    public Song(String name, double price) {
        this.name = name;
        this.price = price;
        this.copiesSold = 0;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " - " + copiesSold + " copies sold";
    }
}