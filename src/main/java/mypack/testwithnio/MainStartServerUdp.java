package mypack.testwithnio;

import mypack.log.LoggingService;
import org.apache.commons.lang3.mutable.MutableInt;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Scanner;

public class MainStartServerUdp {
    private static int buffSize = 1024;

    public static void main(String[] args) {
        final var address = new InetSocketAddress("localhost", 9099);
        Runnable r = () -> {
            try {
                var channelUdp = DatagramChannel.open();
                // none blocking
                channelUdp.configureBlocking(false);
                var socketUdp = channelUdp.socket();
                socketUdp.bind(address);
                LoggingService.getInstance().getLogger().info("udp bound to {}", address);
                while (true) {
                    var byteBuffer = ByteBuffer.allocate(buffSize);

                    //addSend is address of sender
                    var addSend = channelUdp.receive(byteBuffer);
                    // flip to ready read
                    byteBuffer.flip();
                    var sizeData = -1;
                    if (byteBuffer.limit() > 0) {
                        sizeData = byteBuffer.limit();
                        LoggingService.getInstance().getLogger().info("buff limit is {}, received from {}", byteBuffer.limit(), addSend);
                        var bytes = new byte[sizeData];
                        byteBuffer.get(bytes);
                        String msg = new String(bytes);
                        LoggingService.getInstance().getLogger().info("msg from client {} is {}", addSend, msg);

                        bytes = "this is response".getBytes();
                        //note: if you want use DatagramPacket, channel blocking must be configured to true
                        /*var packetResponse = new DatagramPacket(bytes, bytes.length, addSend);
                        socket.send(packetResponse);*/
                        var byteBufferResponse = ByteBuffer.allocate(buffSize);
                        byteBufferResponse.put(bytes);
                        //make it ready to read
                        byteBufferResponse.flip();
                        channelUdp.send(byteBufferResponse, addSend);

                        var otherBuf = ByteBuffer.allocate(buffSize);
                        otherBuf.put("this is new data".getBytes());
                        otherBuf.flip();
                        channelUdp.send(otherBuf, addSend);
                    }
                    byteBuffer.clear();
                }
            } catch (Exception e) {
                LoggingService.getInstance().getLogger().error("err while start server", e);
            }
        };


        new Thread(r).start();
        var scanner = new Scanner(System.in);
        //waite server start
        scanner.nextInt();
        var list = Arrays.asList("phuvh", "vint", "haonc", "linhntm2");
        try {
            var datagramChannelClient = DatagramChannel.open();
            var socket = datagramChannelClient.socket();
            socket.connect(address);
            // wait for connected
            while (!socket.isConnected()) {

            }
            LoggingService.getInstance().getLogger().info("client connected to {}", address);
            var sendData = list.get(0).getBytes();
            var bf = ByteBuffer.allocate(buffSize);
            bf.put(sendData);
            bf.flip();
            datagramChannelClient.write(bf);
            // wait server response
            while (true) {
                var byteBuffer = ByteBuffer.allocate(buffSize);
                var addServer = datagramChannelClient.receive(byteBuffer);
                byteBuffer.flip();
                if (byteBuffer.limit() <= 0) continue;
                var dataFromServer = new byte[byteBuffer.limit()];
                byteBuffer.get(dataFromServer);
                LoggingService.getInstance().getLogger().info("data: {} from {}", new String(dataFromServer), addServer);
                byteBuffer.clear();
            }
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while connect to server", e);
        }
    }
}
