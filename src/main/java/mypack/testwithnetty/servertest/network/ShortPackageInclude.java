package mypack.testwithnetty.servertest.network;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class ShortPackageInclude implements Serializable, WritableToByteBuf {

    private short sequenceId;
    private short cmdId;
    private byte[] payload;

    public ShortPackageInclude(short sequenceId, short cmdId, byte[] payload) {
        this.sequenceId = sequenceId;
        this.cmdId = cmdId;
        this.payload = payload;
    }

    public short getSequenceId() {
        return sequenceId;
    }

    public short getCmdId() {
        return cmdId;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public void writeDataToByteBuf(ByteBuf target) {
        target.writeShort(sequenceId);
        target.writeShort(cmdId);
        // thêm size
        target.writeShort((short) payload.length);
        target.writeBytes(payload);
    }
}
