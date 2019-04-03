package mypack.testwithnetty.servertest.network;

public class RawDataPackage {
    private short sequenceId;
    private short[] akcs;
    private short cmdId;
    private byte[] payload;

    public RawDataPackage(short sequenId, short[] akcs, short cmdId, byte[] payload) {
        this.sequenceId = sequenId;
        this.akcs = akcs;
        this.cmdId = cmdId;
        this.payload = payload;
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
}
