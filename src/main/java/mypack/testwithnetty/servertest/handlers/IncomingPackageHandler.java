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

        ClassValue<String> stringClassValue = null;
        //stringClassValue.get()

        //Tup

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        //với Udp, ctx ở đây là kênh của server,
        // vì tất cả các client không có kênh truyền, mà chỉ có địa chỉ gửi
        var content = msg.content();
        if (content.capacity() <= 0) return;
        try {
            var sequenceId = content.readShort();
            var sizeAcks = content.readShort();
            if (sizeAcks > 32) {
                String msgThrow = String.format("size akcs is too big (%s), so discard now", sizeAcks);
                throw new IllegalArgumentException(msgThrow);
            }
            var acks = new short[sizeAcks];
            for (var i = 0; i < sizeAcks; i++) {
                acks[i] = content.readShort();
            }
            var cmdId = content.readShort();
            //
            var sizePayload = content.writerIndex() - content.readerIndex();
            //LoggingService.getInstance().getLogger().info("size payload is {}", sizePayload);
            var payLoad = new byte[sizePayload];
            content.readBytes(payLoad);
            //var rawDataPackage = new RawDataPackageReceived(sequenceId, acks, cmdId, payLoad, msg.sender());
            //LogicServerActor.getActorRef().tell(rawDataPackage, ActorRef.noSender());
            //ByteBuf s = PooledByteBufAllocator.DEFAULT.buffer();
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while read package from " + msg.sender(), e);
        }
    }

    // được gọi khi đọc xong 1 package
    // đẩy tất cả các data từ server đang đợi ra bên ngoài
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
