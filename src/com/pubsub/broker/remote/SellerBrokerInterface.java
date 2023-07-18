package com.pubsub.broker.remote;

import java.rmi.RemoteException;
import com.pubsub.model.Song;
import com.pubsub.model.Tuple;

public interface SellerBrokerInterface extends BrokerInterface {
    String addTuple(Tuple tuple) throws RemoteException;

    void addSong(String tupleName, Song song) throws RemoteException;

    void removeSong(String tupleName, int songId) throws RemoteException;

}
