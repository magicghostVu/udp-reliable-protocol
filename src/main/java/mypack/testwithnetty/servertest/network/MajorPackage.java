package mypack.testwithnetty.servertest.network;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

//class này là một package chính được gửi đi/nhận về mỗi khi send
// nó sẽ giúp track các package khác có nhận được hay chưa
public class MajorPackage implements Serializable, WritableToByteBuf {

    private HeaderPackage header;
    private short cmdId;
    private byte[] payload;

    //private SocketAddress sender;

    public MajorPackage(HeaderPackage header, short cmdId, byte[] payload) {
        this.header = header;
        this.cmdId = cmdId;
        this.payload = payload;
        //this.sender = sender;
    }


    public HeaderPackage getHeader() {
        return header;
    }

    public short getCmdId() {
        return cmdId;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public void writeDataToByteBuf(ByteBuf target) {
        header.writeDataToByteBuf(target);
        target.writeShort(cmdId);
        target.writeBytes(payload);
    }
}
