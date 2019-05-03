package mypack.testwithnetty.servertest.actors.msgs;

import io.netty.channel.ChannelHandlerContext;

public class ActiveChannelServer {
    private ChannelHandlerContext ctx;
    public ActiveChannelServer(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
    public ChannelHandlerContext getCtx() {
        return ctx;
    }
}
