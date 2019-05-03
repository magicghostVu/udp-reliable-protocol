package mypack.testwithnetty.servertest.actors.msgs;

import mypack.testwithnetty.servertest.network.RawDataPackage;

import java.io.Serializable;
import java.net.SocketAddress;

public class OnePackageCome implements Serializable {

    private RawDataPackage data;
    private SocketAddress addSender;

    public OnePackageCome(RawDataPackage data, SocketAddress addSender) {
        this.data = data;
        this.addSender = addSender;
    }

    public RawDataPackage getData() {
        return data;
    }

    public SocketAddress getAddSender() {
        return addSender;
    }
}
