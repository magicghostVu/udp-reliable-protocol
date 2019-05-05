package mypack.uifxtestnetwork.servicenetwork;


import akka.actor.ActorRef;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import mypack.log.LoggingService;
import mypack.mconfig.ServerConfig;
import mypack.mutils.MyUtils;
import mypack.testwithnetty.servertest.network.MajorPackage;
import mypack.testwithnetty.servertest.network.ShortPackageInclude;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

// service này chịu trách nhiệm gửi và nhận gói tin, sau đó nó chuyển đến các actor
// bản thân nó không biết gì về việc gói tin đã gửi hay đã mất
public class ServiceNetwork {


    private DatagramChannel datagramChannel;

    private ActorRef actorNetWork;

    //private ActorRef actorRefSendPac


    private int defaultSizeBuff = 20000;


    ByteBufAllocator byteBufAllocator;


    private static ServiceNetwork ourInstance = new ServiceNetwork();

    public static ServiceNetwork getInstance() {
        return ourInstance;
    }

    private ServiceNetwork() {
        byteBufAllocator = ByteBufAllocator.DEFAULT;
    }


    private void init(String host, int port) {

        try {
            synchronized (this) {
                // chỉ khởi tạo nếu channel đang bằng null
                if (datagramChannel == null) {
                    datagramChannel = DatagramChannel.open();
                    var socketConnect = new InetSocketAddress(ServerConfig.getHost(), ServerConfig.getPort());
                    datagramChannel.configureBlocking(true);
                    datagramChannel.connect(socketConnect);
                    while (!datagramChannel.isConnected()) {

                    }

                    Runnable runReadPackage = () -> {

                        // chỉ allocate 1 lần rồi sử dụng lại sau mỗi vòng while
                        var tmp = ByteBuffer.allocate(defaultSizeBuff);
                        var buf = byteBufAllocator.buffer();
                        while (true) {
                            try {
                                //block đến khi có data
                                datagramChannel.receive(tmp);
                                // ready to read
                                tmp.flip();


                                buf.writeBytes(tmp);
                                var majorPackage = MyUtils.readMajorPackage(buf);
                                var listInClude = new ArrayList<ShortPackageInclude>();
                                var sizeInclude = buf.readShort();
                                if (sizeInclude > 0) {
                                    for (var i = 0; i < sizeInclude; i++) {
                                        var includePack = MyUtils.readShortPackage(buf);
                                        listInClude.add(includePack);
                                    }
                                }




                            } catch (Exception e) {
                                LoggingService.getInstance().getLogger().error("err in loop read package", e);
                            } finally {
                                // return to pool byte
                                buf.release();

                                // xóa hết data để đọc quay lại đọc tiếp
                                tmp.clear();
                            }
                        }
                    };


                }


            }
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while init service network", e);
        }
    }


}
