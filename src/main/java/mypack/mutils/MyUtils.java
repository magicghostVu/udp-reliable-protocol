package mypack.mutils;

import io.netty.buffer.ByteBuf;
import mypack.log.LoggingService;
import mypack.testwithnetty.servertest.network.HeaderPackage;
import mypack.testwithnetty.servertest.network.MajorPackage;
import mypack.testwithnetty.servertest.network.ShortPackageInclude;

public class MyUtils {
    public static long getCrTimeInSecond() {
        return System.currentTimeMillis() / 1000L;
    }



    public static ShortPackageInclude readShortPackage(ByteBuf byteBuf){
        var sequenceId = byteBuf.readShort();
        var cmdId = byteBuf.readShort();
        var sizePayload = byteBuf.readShort();
        byte[] dataPayload = new byte[sizePayload];
        byteBuf.readBytes(dataPayload);
        return new ShortPackageInclude(sequenceId, cmdId, dataPayload);
    }

    private static HeaderPackage readHeaderPackage(ByteBuf buf) {
        try {
            var sequence = buf.readShort();
            var lastReceived = buf.readShort();
            byte[] acks = new byte[4];
            buf.readBytes(acks);
            var h = new HeaderPackage(sequence, lastReceived);
            h.setAckBits(acks);
            return h;
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while read header package", e);
            return null;
        }
    }

    // sẽ biến đổi ByteBuf truyền vào
    public static MajorPackage readMajorPackage(ByteBuf buf) {
        var headerPackage = readHeaderPackage(buf);
        if (headerPackage == null) return null;
        try {
            var cmdId = buf.readShort();

            var sizePayload = buf.readShort();
            byte[] dataPayload = new byte[sizePayload];
            buf.readBytes(dataPayload);
            var m = new MajorPackage(headerPackage, cmdId, dataPayload);
            return m;
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while read major package", e);
            return null;
        }
    }
}
