package mypack.testwithnetty.servertest.network;

import java.io.Serializable;

// gửi đi và nhận đều là sử dụng class này
// éo được vì in và out có các cấu trúc khác nhau
public class MajorPackage implements Serializable {

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

}
