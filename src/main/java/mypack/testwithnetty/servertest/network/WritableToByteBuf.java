package mypack.testwithnetty.servertest.network;

import io.netty.buffer.ByteBuf;

public interface WritableToByteBuf {
    void writeDataToByteBuf(ByteBuf target);
}
