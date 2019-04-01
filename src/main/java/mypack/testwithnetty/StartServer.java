package mypack.testwithnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import mypack.log.LoggingService;
import mypack.testwithnetty.handlers.MyChannelInit;

public class StartServer {

    private static int portNetty = 9098;

    public static void main(String[] args) {
        //Bootstrap
        //var pgr = new NioEventLoopGroup();

        //var t = null;
        var wgr = new NioEventLoopGroup();
        try {

            var serverBootstrap = new Bootstrap();
            serverBootstrap.group(wgr);
            //serverBootstrap.channel(NioDatagramChannel.class);
            serverBootstrap.option(ChannelOption.SO_BROADCAST, true);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1);

            //serverBootstrap.channel(DatagramChannel.class);
            serverBootstrap.handler(new MyChannelInit());
            serverBootstrap.channel(NioDatagramChannel.class);
            //serverBootstrap.

            serverBootstrap.bind("localhost", portNetty).sync();

        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while start netty", e);
        }
    }
}
