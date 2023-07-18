package com.pubsub.model;

public class Song implements java.io.Serializable {
    public String name;
    public double price;
    public int copiesSold;
    public long lengthInSeconds;

    public Song(String name, double price, long lengthInSeconds) {
        this.name = name;
        this.price = price;
        this.lengthInSeconds = lengthInSeconds;
        this.copiesSold = 0;
    }

    @Override
    public String toString() {
        return "Song [" + name + "] " + price + "$, copies sold:" + copiesSold + " length "
                + lengthInSeconds + " seconds";
    }
}