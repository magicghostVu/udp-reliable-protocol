package mypack.testwithnetty.servertest.handlers;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import mypack.log.LoggingService;
import mypack.testwithnetty.servertest.actors.SocketServerActor;
import mypack.testwithnetty.servertest.actors.msgs.ActiveChannelServer;


//bắt tất cả các gói tin đến từ bất cứ client nào
public class IncomingPackageHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    //tất cả các package từ tất cả các client
    // sẽ chỉ được xử lý bởi 1 instance handler duy nhất


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LoggingService.getInstance().getLogger().info("channel upd active");
        SocketServerActor.getActorSelf().tell(new ActiveChannelServer(ctx), ActorRef.noSender());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        LoggingService.getInstance().getLogger().info("handler added");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        //với Udp, ctx ở đây là kênh của server,
        // vì tất cả các client không có kênh truyền, mà chỉ có địa chỉ gửi
        var content = msg.content();
        content.retain();
        LoggingService.getInstance().getLogger().info("content is {} from {}", content, msg.sender());
        var b = ctx.alloc().buffer();
        b.writeBytes("phuvh".getBytes());
        // hàm này sẽ tự giải phóng byteBuf truyền vào
        ctx.write(new DatagramPacket(content, msg.sender()));
    }

    // được gọi khi đọc xong 1 package
    // đẩy tất cả các data từ server đang đợi ra bên ngoài
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
