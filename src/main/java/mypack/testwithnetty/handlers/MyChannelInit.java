package mypack.testwithnetty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import mypack.log.LoggingService;

public class MyChannelInit extends ChannelInitializer<NioDatagramChannel> {
    @Override
    protected void initChannel(NioDatagramChannel ch) {
        var pipeline = ch.pipeline();
        pipeline.addLast(new IncomingPackageHandler());
        LoggingService.getInstance().getLogger().info("client connect {}", ch.id());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx){
        LoggingService.getInstance().getLogger().info("client connected {}", ctx.channel().id());

    }
}
