package pack.testwithnio;

import pack.log.LoggingService;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class MainStartServer {

    private static int buffSize = 1024;

    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                SocketAddress address = new InetSocketAddress("localhost", 9099);
                DatagramChannel channel = DatagramChannel.open();
                DatagramSocket socket = channel.socket();
                socket.bind(address);
                LoggingService.getInstance().getLogger().info("udp bound to {}", address);
                while (true) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(buffSize);
                    channel.receive(byteBuffer);
                    byteBuffer.flip();
                    if (byteBuffer.limit() > 0) {
                        LoggingService.getInstance().getLogger().info("buff limit is {}", byteBuffer.limit());
                    }
                    byteBuffer.clear();
                }
            } catch (Exception e) {

            }
        };

        new Thread(r).start();




    }
}
