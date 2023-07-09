package com.pubsub.broker.remote;

import java.rmi.RemoteException;
import com.pubsub.model.Song;

public interface CustomerBrokerInterface extends BrokerInterface {

    Song purchaseSong(String customerName, String tupleName, int SongId) throws RemoteException;

}
