package mypack.testwithnetty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import mypack.log.LoggingService;


public class IncomingPackageHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        var content = msg.content();
        content.retain();
        LoggingService.getInstance().getLogger().info("content is {} from {}", content, msg.sender());
        var b = ctx.alloc().buffer();
        b.writeBytes("phuvh".getBytes());
        // hàm này sẽ tự giải phóng bytebuf truyền vào
        ctx.write(new DatagramPacket(content, msg.sender()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        LoggingService.getInstance().getLogger().error("call from ", new Exception());
    }
}
