package mypack.testwithnetty.clienttest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import mypack.log.LoggingService;
import mypack.testwithnetty.clienttest.actors.msgs.TestSendPackageToServer;
import mypack.testwithnetty.servertest.StartServer;
import mypack.testwithnetty.clienttest.actors.msgs.RawDataFromServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
            InetSocketAddress addConnect = new InetSocketAddress("49.213.81.42", StartServer.portNetty);
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
            new Thread(r).start();
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while start actor client", e);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawDataFromServer.class, this::handleNewPackage)
                .match(TestSendPackageToServer.class, this::testSendPackageToServer)
                .build();
    }


    private void testSendPackageToServer(TestSendPackageToServer msg) {
        LoggingService.getInstance().getLogger().info("received msg send test package to server");
        try {
            short sequenceId = 0;
            short[] acks = new short[10];
            for (var i = 0; i < acks.length; i++) {
                acks[i] = (short) i;
            }

            short cmdId = 0;



            ByteBuffer byteBuffer = ByteBuffer.allocate(defaultSizeBuf);

            byteBuffer.putShort(sequenceId);
            byteBuffer.putShort((short) acks.length);
            for (int i = 0; i < acks.length; i++) {
                byteBuffer.putShort(acks[i]);
            }
            byteBuffer.putShort(cmdId);
            var payload = new byte[5];
            byteBuffer.put(payload);


            byteBuffer.flip();

            var byteSend = new byte[byteBuffer.limit()];

            byteBuffer.get(byteSend);
            byteBuffer.clear();

            //byteBuffer = ByteBuffer.wrap(byteSend);

            SocketAddress sockAddr = new InetSocketAddress("49.213.81.42", 10017);
            DatagramPacket packSend = new DatagramPacket(byteSend, byteSend.length, sockAddr);

            datagramSocket.send(packSend);
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().info("err while send package to server", e);
        }


    }

    private void handleNewPackage(RawDataFromServer dataFromServer) {
        //datagramSocket.s
    }
}
