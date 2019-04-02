package mypack.testwithnetty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import mypack.log.LoggingService;


//bắt tất cả các gói tin đến từ bất cứ client nào
public class IncomingPackageHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    //tất cả các package từ tất cả các client
    // sẽ chỉ được xử lý bởi 1 instance handler duy nhất
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        //với Udp, ctx ở đây là kênh của server,
        // vì tất cả các client không có kênh truyền, mà chỉ có địa chỉ gửi
        LoggingService.getInstance().getLogger().error("cal from ", new Exception());
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
