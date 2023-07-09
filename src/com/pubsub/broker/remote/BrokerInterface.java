package com.pubsub.broker.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import com.pubsub.model.Tuple;
import com.pubsub.model.User;

public interface BrokerInterface extends Remote {

    List<Tuple> getTuples() throws RemoteException;

    Tuple getTuple(String tupleName) throws RemoteException;

    User getUser(String userName, String password) throws RemoteException;
}
