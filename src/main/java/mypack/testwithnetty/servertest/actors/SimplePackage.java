package mypack.testwithnetty.servertest.actors;

import java.io.Serializable;

public class SimplePackage implements Serializable {
    private short cmdId;
    private byte[] payloadData;

    public SimplePackage(short cmdId, byte[] payloadData) {
        this.cmdId = cmdId;
        this.payloadData = payloadData;
    }

    public short getCmdId() {
        return cmdId;
    }

    public byte[] getPayloadData() {
        return payloadData;
    }
}
