package mypack.testwithnetty.clienttest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import mypack.log.LoggingService;
import mypack.testwithnetty.servertest.StartServer;
import mypack.testwithnetty.clienttest.actors.msgs.RawDataFromServer;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


// đóng vai trò để gửi và nhận gói tin, xác định package mất
// việc có gửi lại gói tin đã mất như thế nào phụ thuộc vào actor logic
public class ClientNetWorkActor extends AbstractActor {
    public static Props props() {
        return Props.create(ClientNetWorkActor.class);
    }
    // giữ socket này để gửi data lại cho server
    private DatagramSocket datagramSocket;
    private int defaultSizeBuf = 1500;
    //private TreeSet
    public ClientNetWorkActor() {
        try {
            var datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(true);
            this.datagramSocket = datagramChannel.socket();
            InetSocketAddress addConnect = new InetSocketAddress("localhost", StartServer.portNetty);
            datagramSocket.connect(addConnect);
            // đợi connect
            while (!datagramSocket.isConnected()) {

            }
            LoggingService.getInstance().getLogger().info("client connected");
            Runnable r = () -> {
                var buffer = ByteBuffer.allocate(defaultSizeBuf);
                while (true) {
                    try {
                        //block util data available
                        datagramChannel.receive(buffer);
                        //flip ready to read
                        buffer.flip();
                        if (buffer.limit() <= 0) continue;
                        //var tmpBuffToSend = ByteBuffer.allocate(buffer.limit());
                        var bytes = new byte[buffer.limit()];
                        buffer.get(bytes);
                        RawDataFromServer rData = new RawDataFromServer(bytes);
                        getSelf().tell(rData, ActorRef.noSender());
                        buffer.clear();
                    } catch (Exception e) {
                        LoggingService.getInstance().getLogger().error("err in loop listen package, so break now", e);
                        break;
                    }
                }
            };


        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while start actor client", e);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawDataFromServer.class, this::handleNewPackage)
                .build();
    }

    private void handleNewPackage(RawDataFromServer dataFromServer) {
        //datagramSocket.s
    }
}
