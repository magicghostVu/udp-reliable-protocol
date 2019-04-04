package mypack.testwithnetty.servertest.network;

import java.io.Serializable;
import java.net.SocketAddress;

public class RawDataPackage implements Serializable {
    private short sequenceId;
    private short[] akcs;
    private short cmdId;
    private byte[] payload;

    private SocketAddress sender;

    public RawDataPackage(short sequenId, short[] akcs, short cmdId, byte[] payload, SocketAddress sender) {
        this.sequenceId = sequenId;
        this.akcs = akcs;
        this.cmdId = cmdId;
        this.payload = payload;
        this.sender = sender;
    }


    public short getSequenceId() {
        return sequenceId;
    }

    public short[] getAkcs() {
        return akcs;
    }

    public short getCmdId() {
        return cmdId;
    }

    public byte[] getPayload() {
        return payload;
    }

    public SocketAddress getSender() {
        return sender;
    }
}
