package mypack.testwithnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import mypack.log.LoggingService;
import mypack.testwithnetty.handlers.MyChannelInit;

public class StartServer {

    private static int portNetty = 9098;
    public static void main(String[] args) {
        try {
            var wgr = new NioEventLoopGroup();
            // với udp, vì không tạo kênh kết nối với client nên là không sử dụng server bootstrap,
            // chỉ sử dụng Bootstrap
            var bootstrap = new Bootstrap();
            bootstrap.group(wgr);
            bootstrap.option(ChannelOption.SO_BROADCAST, true);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1);
            bootstrap.handler(new MyChannelInit());
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.bind("localhost", portNetty).sync();
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while start netty", e);
        }
    }
}
