package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;

import java.net.SocketAddress;


// quản lý trạng thái của user và thực hiện disconnect
// quản lý package ở đây luôn ????
public class UserActor extends AbstractActor {

    private int uid;

    // địa chỉ của user gửi đến
    private SocketAddress socketAddress;

    private long lastPackageTime;

    public UserActor() {
        this(-1, null);
    }

    public UserActor(int uid, SocketAddress socketAddress) {
        this.uid = uid;
        this.socketAddress = socketAddress;
        lastPackageTime = System.currentTimeMillis();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public int getUid() {
        return uid;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}
