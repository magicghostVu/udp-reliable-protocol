package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import mypack.log.LoggingService;
import mypack.testwithnetty.servertest.actors.msgs.ActiveChannelServer;
import mypack.testwithnetty.servertest.handlers.MyChannelInit;

//chỉ có vai trò gửi gói tin đến một địa chỉ xác định và nhận gói tin từ mọi nơi
public class SocketServerActor extends AbstractActor {
    public static Props props() {
        return Props.create(SocketServerActor.class);
    }


    private static ActorRef _self;

    private boolean ready;

    private ChannelHandlerContext ctx;

    // khởi tạo socket ở đây??
    public SocketServerActor() {
        try {
            this.ready = false;
            _self = getSelf();
            var portNetty = 10017;
            var wgr = new NioEventLoopGroup();
            // với udp, vì không tạo kênh kết nối với client nên là không sử dụng server bootstrap,
            // chỉ sử dụng Bootstrap
            var bootstrap = new Bootstrap();
            bootstrap.group(wgr);
            bootstrap.option(ChannelOption.SO_BROADCAST, true);
            //bootstrap.option(ChannelOption.SO_BACKLOG, 1);
            bootstrap.handler(new MyChannelInit());
            bootstrap.channel(NioDatagramChannel.class);

            var host = "49.213.81.42";

            bootstrap.bind(host, portNetty).sync();

            LoggingService.getInstance().getLogger().info("ok, ready bind to {}/{}", host, portNetty);

        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while start netty", e);
        }
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActiveChannelServer.class, this::serverReady)
                .build();
    }


    private void serverReady(ActiveChannelServer msg) {
        this.ctx = msg.getCtx();
        ready = true;
        LoggingService.getInstance().getLogger().info("Server socket ready !!!!!!!!!!!");
        LogicServerActor.initSelf();
    }

    public static ActorRef getActorSelf() {
        return _self;
    }
}
