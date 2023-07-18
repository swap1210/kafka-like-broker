package com.pubsub.model;

import java.util.LinkedList;
import java.util.List;

public class Tuple implements java.io.Serializable {
    public String name = "";
    public List<Song> songs;

    public Tuple(String name) {
        this.name = name;
        this.songs = new LinkedList<>();
    }

    @Override
    public String toString() {
        String tupleString = name + ":\n";
        for (Song song : songs) {
            tupleString += "\t" + song + "\n";
        }
        return tupleString;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple tuple = (Tuple) obj;
            return tuple.name.equals(this.name);
        }
        return false;
    }
}
