package mypack.testwithnetty.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class MyChannelInit extends ChannelInitializer<NioDatagramChannel> {
    @Override
    protected void initChannel(NioDatagramChannel ch) {
        var pipeline = ch.pipeline();
        pipeline.addLast(new IncomingPackageHandler());
        //LoggingService.getInstance().getLogger().info("client connect {}", ch.id());
    }

}
