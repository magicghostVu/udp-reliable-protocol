package mypack.testwithnetty.servertest.network;

import java.io.Serializable;

public class ShortPackageInclude implements Serializable {

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
}
