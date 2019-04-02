package mypack.testwithnetty.clienttest.actors.msgs;

public class RawDataFromServer {
    private byte[] arRaw;
    public RawDataFromServer(byte[] arRaw) {
        this.arRaw = arRaw;
    }
    public byte[] getArRaw() {
        return arRaw;
    }
}
