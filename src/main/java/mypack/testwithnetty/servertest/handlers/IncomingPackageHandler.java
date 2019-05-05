package mypack.testwithnetty.servertest.handlers;

import akka.actor.ActorRef;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import mypack.log.LoggingService;
import mypack.mutils.MyUtils;
import mypack.testwithnetty.servertest.actors.LogicServerActor;
import mypack.testwithnetty.servertest.actors.SocketServerActor;
import mypack.testwithnetty.servertest.actors.msgs.ActiveChannelServer;
import mypack.testwithnetty.servertest.actors.msgs.OnePackageCome;
import mypack.testwithnetty.servertest.network.HeaderPackage;
import mypack.testwithnetty.servertest.network.MajorPackage;
import mypack.testwithnetty.servertest.network.RawDataPackage;
import mypack.testwithnetty.servertest.network.ShortPackageInclude;

import java.util.ArrayList;


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


    // đọc ra một raw data package và chuyển nó đến actor logic để xử lý tiếp
    //sau hàm này, Bytebuf đã được giải phóng
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        //với Udp, ctx ở đây là kênh của server,
        // vì tất cả các client không có kênh truyền, mà chỉ có địa chỉ gửi đến
        var content = msg.content();
        if (content.capacity() <= 0) {
            LoggingService.getInstance().getLogger().warn("package empty from {} so discard", msg.sender());
            return;
        }
        try {
            var majorPackage = MyUtils.readMajorPackage(content);
            if (majorPackage == null) {
                return;
            }
            var listShortPackage = new ArrayList<ShortPackageInclude>();
            var sizeListPackageInclude = content.readShort();
            if (sizeListPackageInclude > 0) {
                for (int i = 0; i < sizeListPackageInclude; i++) {
                    listShortPackage.add(readShortPackage(content));
                }
            }
            //todo: khởi tạo một RawPackage mới và chuyển nó đến cho logic actor
            var r = new RawDataPackage(majorPackage, listShortPackage);
            var onePackageCome = new OnePackageCome(r, msg.sender());
            LogicServerActor.getActorRef().tell(onePackageCome, ActorRef.noSender());
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while read package from " + msg.sender(), e);
        }
    }

    private ShortPackageInclude readShortPackage(ByteBuf byteBuf) {
        return MyUtils.readShortPackage(byteBuf);
    }

    // được gọi mỗi khi đọc xong 1 package
    // đẩy tất cả các data từ server đang đợi ra bên ngoài
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
